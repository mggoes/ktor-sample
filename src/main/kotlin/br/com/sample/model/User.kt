package br.com.sample.model

import kotlinx.serialization.Serializable

@Serializable
data class User(val id: String? = null, val name: String)
