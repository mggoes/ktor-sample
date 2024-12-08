package br.com.sample.routes

import br.com.sample.model.User
import br.com.sample.model.UserRepository
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.registerUserRoutes() {
    routing {
        route("/users") {
            val userRepository by inject<UserRepository>()

            post {
                call.receive<User>().let { user ->
                    call.respond(Created, userRepository.save(user))
                }
            }

            get {
                call.respond(userRepository.findAll())
            }

            get("/{id}") {
                call.parameters["id"]?.let { id ->
                    call.respond(userRepository.findById(id))
                } ?: call.respond(BadRequest)
            }

            put("/{id}") {
                call.parameters["id"]?.let { id ->
                    call.respond(OK, userRepository.update(id, call.receive<User>()))
                } ?: call.respond(BadRequest)
            }

            delete("/{id}") {
                call.parameters["id"]?.let { id ->
                    userRepository.delete(id)
                    call.respond(OK)
                } ?: call.respond(BadRequest)
            }
        }
    }
}
