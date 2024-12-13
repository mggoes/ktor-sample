package br.com.sample.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.forms.*
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.Parameters.Companion.build
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy.Builtins.SnakeCase

private const val CLIENT_ID = ""
private const val CLIENT_SECRET = ""
private const val BASE_URL = "http://localhost:8080/realms/corporate/protocol/openid-connect"

@OptIn(ExperimentalSerializationApi::class)
fun main() {
    runBlocking {
        val client = HttpClient(Java) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    namingStrategy = SnakeCase
                })
            }
        }

        val deviceResponse = client.submitForm(
            url = "$BASE_URL/auth/device",
            formParameters = build {
                append("client_id", CLIENT_ID)
                append("client_secret", CLIENT_SECRET)
            }
        ).body<DeviceResponse>()
        println("Go to ${deviceResponse.verificationUriComplete} to complete your authentication process")

        while (true) {
            val tokenResponse = client.submitForm(
                url = "$BASE_URL/token",
                formParameters = build {
                    append("client_id", CLIENT_ID)
                    append("client_secret", CLIENT_SECRET)
                    append("device_code", deviceResponse.deviceCode)
                    append("grant_type", "urn:ietf:params:oauth:grant-type:device_code")
                }
            )

            if (OK == tokenResponse.status) {
                val token = tokenResponse.body<TokenResponse>()
                println(token)
                break
            }

            val error = tokenResponse.body<TokenErrorResponse>()
            println(error)
            delay(deviceResponse.interval.toLong() * 1500)
        }
    }
}

@Serializable
data class DeviceResponse(
    val deviceCode: String,
    val userCode: String,
    val verificationUri: String,
    val verificationUriComplete: String,
    val expiresIn: Int,
    val interval: Int,
)

@Serializable
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val sessionState: String,
    val scope: String,
    val expiresIn: Int,
    val refreshExpiresIn: Int,
)

@Serializable
data class TokenErrorResponse(
    val error: String,
    val errorDescription: String,
)
