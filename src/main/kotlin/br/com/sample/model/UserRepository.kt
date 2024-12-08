package br.com.sample.model

import br.com.sample.exception.NotFoundException
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserRepository {

    private val users = mutableMapOf<String, User>()

    fun findAll() = this.users.values.toList()

    fun findById(id: String) = this.users[id] ?: throw NotFoundException("User $id not found")

    fun save(user: User): User {
        val id = Uuid.random().toString()
        val new = user.copy(id)
        this.users[id] = new
        return new
    }

    fun update(id: String, user: User): User {
        val saved = this.users[id] ?: throw NotFoundException("User $id not found")
        val updated = saved.copy(name = user.name)
        this.users[id] = updated
        return updated
    }

    fun delete(id: String) {
        if (!this.users.containsKey(id)) throw NotFoundException("User $id not found")
        this.users.remove(id)
    }
}

val userRepositoryModule = module {
    singleOf(::UserRepository)
}
