package br.com.sample

import br.com.sample.exception.ErrorMessage
import br.com.sample.exception.NotFoundException
import br.com.sample.model.userRepositoryModule
import br.com.sample.routes.registerUserRoutes
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::fullModule).start(true)
}

fun Application.baseModule() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    install(StatusPages) {
        exception<NotFoundException> { call, cause ->
            call.respond(NotFound, ErrorMessage(cause.message))
        }
    }

    registerUserRoutes()
}

fun Application.fullModule() {
    baseModule()

    install(Koin) {
        slf4jLogger()
        modules(userRepositoryModule)
    }
}
