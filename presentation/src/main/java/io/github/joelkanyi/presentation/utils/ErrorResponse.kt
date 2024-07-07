package io.github.joelkanyi.presentation.utils

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("errors")
    val errors: List<Any>? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("attempts")
    val attempts: Int? = null,
    @SerializedName("code")
    val code: Int? = null,
)
