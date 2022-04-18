package app.snack.data.repository

import app.snack.data.sources.local.Preferences
import app.snack.data.sources.remote.SnackAPI
import app.snack.model.Error
import app.snack.model.SnackCredentials
import app.snack.model.request.*
import app.snack.model.result.*
import javax.inject.Inject


interface Repository {

    val token: String?
    var isFirstLaunch: Boolean

    var showPrivacyConfirmationScreen: Boolean

    suspend fun signIn(email: String, password: String): SignInResult
    suspend fun signInGoogle(token: String): SignInResult
    suspend fun signUp(email: String, password: String): SignUpResult
    suspend fun recoverPassword(email: String): Result
    suspend fun sendConfirmation(): ConfirmationResult

    suspend fun registerDevice(deviceId: String): Result
    suspend fun fetchProfile(profilePrice: UserRequest.Profile): ProfileResult
    suspend fun editCredentials(credentials: UserRequest.Credentials): Result
    suspend fun editEmail(email: UserRequest.Email): Result
    suspend fun checkEmail(email: UserRequest.Email): Result
    suspend fun editPassword(password: UserRequest.Password): Result

    suspend fun enableTrafficSharing(): Result
    suspend fun disableTrafficSharing(): Result

    suspend fun transactionsHistory(): TransactionsResult
    suspend fun transactionAdd(transactionRequest: TransactionAdd): Result
    suspend fun transactionAddNegative(): Result
    suspend fun checkNegativeTransactions(): Result

    suspend fun supportSendEmail(message: SupportMessage): Result

}

class SnackRepository @Inject constructor(
    private val api: SnackAPI,
    private val preferences: Preferences,
) : Repository {

    override val token: String
        get() = preferences.token ?: ""

    override var isFirstLaunch: Boolean
        set(value) {
            preferences.isFirstLaunch = value
        }
        get() = preferences.isFirstLaunch

    override var showPrivacyConfirmationScreen: Boolean
        set(value) {
            preferences.showPrivacyConfirmationScreen = value
        }
        get() = preferences.showPrivacyConfirmationScreen

    // region Auth
    override suspend fun signIn(email: String, password: String): SignInResult = try {
        val request = AuthRequest.SignIn(email, password)
        val response = api.signInAsync(request)

        when (response.isSuccessful) {
            true -> SignInResult.Success.apply {
                preferences.isNativeAuthentication = true
                preferences.token = response.body()?.data?.token
                preferences.credentials = SnackCredentials(email, password)
            }
            else -> SignInResult.parseError(response)
        }
    } catch (exception: Exception) {
        SignInResult.fromException(exception)
    }

    override suspend fun signInGoogle(token: String) = try {
        val signInRequest = AuthRequest.SignInGoogle(token)
        val response = api.signInGoogleAsync(signInRequest)
        when (response.isSuccessful) {
            true -> SignInResult.Success.apply {
                preferences.isNativeAuthentication = false
                preferences.token = response.body()?.data?.token
            }
            else -> {
                SignInResult.parseError(response)
            }
        }
    } catch (exception: Exception) {
        SignInResult.fromException(exception)
    }

    override suspend fun signUp(email: String, password: String) = try {
        val request = AuthRequest.SignUp(email, password)
        val response = api.signUpAsync(request)

        when (response.isSuccessful) {
            true -> SignUpResult.Success.apply {
                preferences.credentials = SnackCredentials(email, password)
                preferences.token = response.body()?.data?.token
            }
            else -> SignUpResult.parseError(response)
        }
    } catch (exception: Exception) {
        SignUpResult.fromException(exception)
    }

    override suspend fun recoverPassword(email: String): Result =
        try {
            val request = AuthRequest.RecoverPassword(email)
            val response = api.recoverPasswordAsync(request)

            when (response.body()?.error) {
                null -> Result.Success
                else -> Result.Failure(Error(message = response.body()?.error))
            }
        } catch (exception: Exception) {
            Result.fromException(exception)
        }

    override suspend fun registerDevice(deviceId: String): Result = try {
        val request = DeviceRequest.Register(deviceId)
        val response = api.registerDeviceAsync(request, token)

        when (response.body()?.error) {
            null -> Result.Success
            else -> Result.Failure(Error(message = response.body()?.error))
        }
    } catch (exception: Exception) {
        Result.fromException(exception)
    }

    override suspend fun sendConfirmation(): ConfirmationResult =
        try {
            val response = api.sendConfirmation(token)
            when (response.isSuccessful) {
                true -> {
                    ConfirmationResult.Success(response.body()!!)
                }
                else -> ConfirmationResult.parseError(response)
            }
        } catch (exception: Exception) {
            ConfirmationResult.fromException(exception)
        }

    // endregion

    // region User

    override suspend fun fetchProfile(profile: UserRequest.Profile): ProfileResult =
        try {
            val response = api.fetchProfile(token, profile)
            when (response.isSuccessful) {
                true -> {
                    ProfileResult.Success(response.body()!!)
                }
                else -> ProfileResult.parseError(response)
            }
        } catch (exception: Exception) {
            ProfileResult.fromException(exception)
        }


    override suspend fun editCredentials(credentials: UserRequest.Credentials): Result =
        try {
            val response = api.editCredentials(token, credentials)
            when (response.isSuccessful) {
                true -> {
                    Result.Success
                }
                else -> Result.parseError(response)
            }
        } catch (exception: Exception) {
            Result.fromException(exception)
        }


    override suspend fun editEmail(email: UserRequest.Email): Result =
        try {
            val response = api.editEmail(token, email)
            when (response.isSuccessful) {
                true -> {
                    Result.Success
                }
                else -> Result.parseError(response)
            }
        } catch (exception: Exception) {
            Result.fromException(exception)
        }


    override suspend fun editPassword(password: UserRequest.Password): Result =
        try {
            val response = api.editPassword(token, password)
            when (response.isSuccessful) {
                true -> {
                    Result.Success
                }
                else -> Result.parseError(response)
            }
        } catch (exception: Exception) {
            Result.fromException(exception)
        }

    override suspend fun enableTrafficSharing(): Result = try {
        val response = api.enableTrafficSharing(token)
        when (response.isSuccessful) {
            true -> {
                Result.Success
            }
            else -> Result.parseError(response)
        }
    } catch (exception: Exception) {
        Result.fromException(exception)
    }

    override suspend fun disableTrafficSharing(): Result = try {
        val response = api.disableTrafficSharing(token)
        when (response.isSuccessful) {
            true -> {
                Result.Success
            }
            else -> Result.parseError(response)
        }
    } catch (exception: Exception) {
        Result.fromException(exception)
    }

    override suspend fun checkEmail(email: UserRequest.Email): Result =
        try {
            val response = api.checkEmail(token, email)
            when (response.isSuccessful) {
                true -> {
                    val result = response.body()!!
                    if (result.data.validation) {
                        Result.Success
                    } else {
                        Result.Failure()
                    }
                }
                else -> Result.parseError(response)
            }
        } catch (exception: Exception) {
            Result.fromException(exception)
        }

    // endregion

    // region Transactions

    override suspend fun transactionsHistory(): TransactionsResult =
//        TransactionsResult.Success(TransactionResponse.Transactions.mockTransactionsHistoryList())
        try {
            val response = api.transactionsHistory(token)
            when (response.isSuccessful) {
                true -> {
                    TransactionsResult.Success(response.body()!!.data.transactions)
                }
                else -> TransactionsResult.parseError(response)
            }
        } catch (exception: Exception) {
            TransactionsResult.fromException(exception)
        }

    override suspend fun transactionAdd(transaction: TransactionAdd): Result =
        try {
            val response = api.transactionsAdd(token, transaction)
            when (response.isSuccessful) {
                true -> {
                    Result.Success
                }
                else -> Result.parseError(response)
            }
        } catch (exception: Exception) {
            Result.fromException(exception)
        }


    override suspend fun transactionAddNegative(): Result =
        try {
//            val transaction = TransactionAdd("", 2f, -6.0f)
            val transaction = TransactionAddBonus("")
            val response = api.transactionsAddNegative(token, transaction)
            when (response.isSuccessful) {
                true -> {
                    Result.Success
                }
                else -> Result.parseError(response)
            }
        } catch (exception: Exception) {
            Result.fromException(exception)
        }

    override suspend fun checkNegativeTransactions(): Result =
        try {
            val response = api.checkNegativeTransactions(token)
            when (response.isSuccessful) {
                true -> {
                    val result = response.body()!!
                    if (result.data.validation) {
                        Result.Success
                    } else {
                        Result.Failure()
                    }
                }
                else -> Result.parseError(response)
            }
        } catch (exception: Exception) {
            Result.fromException(exception)
        }

    // endregion

    override suspend fun supportSendEmail(message: SupportMessage): Result =
        try {
            val response = api.supportSendEmail(token, message)
            when (response.isSuccessful) {
                true -> Result.Success
                else -> Result.parseError(response)
            }
        } catch (exception: Exception) {
            Result.fromException(exception)
        }

}