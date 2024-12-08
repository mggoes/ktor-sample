package br.com.sample.exception

import kotlinx.serialization.Serializable

@Serializable
data class ErrorMessage(val message: String)
