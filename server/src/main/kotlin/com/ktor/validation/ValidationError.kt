package com.ktor.validation

import kotlinx.serialization.Serializable

@Serializable
sealed class ValidationError(val msg: String) {
    @Serializable
    data class Empty(val fieldName: String) : ValidationError("$fieldName must not be empty")

    @Serializable
    data class MinLength(val fieldName: String, val length: Int) : ValidationError("Minimum length for $fieldName is $length")

    @Serializable
    data class InvalidRequest(
        val reasons: List<ValidationError>
    ) : ValidationError("Invalid request")
}

data class RequestParam(val fieldName: String, val value: String?)