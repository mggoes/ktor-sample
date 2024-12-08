package br.com.sample.exception

class NotFoundException(override val message: String) : RuntimeException(message)
