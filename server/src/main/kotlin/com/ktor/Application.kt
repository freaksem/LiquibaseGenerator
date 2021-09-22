package com.ktor

import com.ktor.routing.configureRouting
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 9000, host = "127.0.0.1") {
        install(CORS) {
            host("localhost:8080")
        }
        install(ContentNegotiation) {
            json()
        }
        configureRouting()
    }.start(true)
}