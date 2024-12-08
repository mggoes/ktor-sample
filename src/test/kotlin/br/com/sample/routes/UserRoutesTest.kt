package br.com.sample.routes

import br.com.sample.baseModule
import br.com.sample.model.User
import br.com.sample.model.UserRepository
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.KoinTest
import org.koin.test.junit5.KoinTestExtension
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@ExtendWith(MockKExtension::class)
class UserRoutesTest : KoinTest {

    @SpyK
    private var userRepository = UserRepository()

    @JvmField
    @RegisterExtension
    @Suppress("unused")
    val koinTestExtension = KoinTestExtension.create {
        modules(org.koin.dsl.module {
            single<UserRepository> { userRepository }
        })
    }

    @Test
    fun `Should create a new user`() = testApplication {
        // Given
        application {
            baseModule()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // When
        val response = client.post("/users") {
            contentType(Json)
            setBody(User(name = "Test User"))
        }

        // Then
        assertEquals(Created, response.status)

        val user = response.body<User>()
        assertNotNull(user.id)
        assertEquals("Test User", user.name)
    }

    @Test
    fun `Should read all users`() = testApplication {
        // Given
        application {
            baseModule()
        }

        every { userRepository.findAll() } returns listOf(User(Uuid.random().toString(), "Test User"))

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // When
        val response = client.get("/users")

        // Then
        assertEquals(OK, response.status)

        val users = response.body<List<User>>()
        assertNotNull(users)
        assertFalse(users.isEmpty())

        val user = users.first()
        assertEquals("Test User", user.name)
    }

    @Test
    fun `Should read by id`() = testApplication {
        // Given
        application {
            baseModule()
        }

        val id = Uuid.random().toString()

        every { userRepository.findById(any()) } returns User(id, "Test User")

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // When
        val response = client.get("/users/$id")

        // Then
        assertEquals(OK, response.status)

        val user = response.body<User>()
        assertNotNull(user)
        assertEquals("Test User", user.name)
    }

    @Test
    fun `Should return not found when user does not exist`() = testApplication {
        // Given
        application {
            baseModule()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // When
        val response = client.get("/users/12345")

        // Then
        assertEquals(NotFound, response.status)
    }

    @Test
    fun `Should update a user`() = testApplication {
        // Given
        application {
            baseModule()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        var user = client.post("/users") {
            contentType(Json)
            setBody(User(name = "Test User"))
        }.body<User>()

        // When
        val response = client.put("/users/${user.id}") {
            contentType(Json)
            setBody(User(name = "Test User Updated"))
        }

        // Then
        assertEquals(OK, response.status)

        val updated = response.body<User>()
        assertEquals(user.id, updated.id)
        assertEquals("Test User Updated", updated.name)
    }

    @Test
    fun `Should delete a user`() = testApplication {
        // Given
        application {
            baseModule()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        var user = client.post("/users") {
            contentType(Json)
            setBody(User(name = "Test User"))
        }.body<User>()

        // When
        val response = client.delete("/users/${user.id}") {
            contentType(Json)
            setBody(User(name = "Test User Updated"))
        }

        // Then
        assertEquals(OK, response.status)
    }
}
