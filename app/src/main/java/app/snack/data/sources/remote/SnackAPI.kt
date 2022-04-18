package app.snack.data.sources.remote

import app.snack.model.request.*
import app.snack.model.response.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface SnackAPI {

    // region Auth

    @POST("/auth/signin")
    suspend fun signInAsync(@Body request: AuthRequest.SignIn): Response<AuthResponse.SignIn>

    @POST("/auth/googleSignIn")
    suspend fun signInGoogleAsync(@Body request: AuthRequest.SignInGoogle): Response<AuthResponse.SignIn>

    @POST("/auth/signup")
    suspend fun signUpAsync(@Body request: AuthRequest.SignUp): Response<AuthResponse.SignUp>

    @POST("/auth/recoverPassword")
    suspend fun recoverPasswordAsync(@Body request: AuthRequest.RecoverPassword): Response<SuccessResponse>

    @GET("/auth/sendConfirmation")
    suspend fun sendConfirmation(@Header("Authorization") token: String): Response<ConfirmationResponse>

    @POST("/auth/logout")
    suspend fun logoutAsync(@Header("Authorization") token: String): Response<SuccessResponse>

    // endregion

    // region Device

    @POST("/device/register")
    suspend fun registerDeviceAsync(
        @Body request: DeviceRequest.Register,
        @Header("Authorization") token: String
    ): Response<SuccessResponse>

    // endregion

    // region user

    @POST("/user/profile")
    suspend fun fetchProfile(@Header("Authorization") token: String, @Body request: UserRequest.Profile): Response<ProfileResponse>

    @POST("/user/editCredentials")
    suspend fun editCredentials(@Header("Authorization") token: String, @Body request: UserRequest.Credentials): Response<SuccessResponse>

    @POST("/user/editEmail")
    suspend fun editEmail(@Header("Authorization") token: String, @Body request: UserRequest.Email): Response<SuccessResponse>

    @POST("/user/editPassword")
    suspend fun editPassword(@Header("Authorization") token: String, @Body request: UserRequest.Password): Response<SuccessResponse>

    @POST("/user/checkEmail")
    suspend fun checkEmail(@Header("Authorization") token: String, @Body request: UserRequest.Email): Response<ValidationResponse>

    @GET("/user/checkNegativeTransactions")
    suspend fun checkNegativeTransactions(@Header("Authorization") token: String): Response<ValidationResponse>

    @GET("/user/enableTrafficSharing")
    suspend fun enableTrafficSharing(@Header("Authorization") token: String): Response<SuccessResponse>

    @GET("/user/disableTrafficSharing")
    suspend fun disableTrafficSharing(@Header("Authorization") token: String): Response<SuccessResponse>

    // endregion

    // region transactions
    @GET("/transactions/history")
    suspend fun transactionsHistory(@Header("Authorization") token: String): Response<TransactionResponse>

    @POST("/transactions/add")
    suspend fun transactionsAdd(@Header("Authorization") token: String, @Body request: TransactionAdd): Response<SuccessResponse>

    // endregion

    // region support

    @POST("/support/sendMail")
    suspend fun supportSendEmail(@Header("Authorization") token: String, @Body request: SupportMessage): Response<SuccessResponse>

    // endregion

}
