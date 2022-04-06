package app.snack.model.result

import app.snack.model.Error

sealed class GoogleCredentialResult {
    data class Success(val token: String) : GoogleCredentialResult()
    data class Failure(val error: Error) : GoogleCredentialResult()
}
