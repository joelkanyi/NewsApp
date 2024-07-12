package io.github.joelkanyi.presentation.utils

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String
)
