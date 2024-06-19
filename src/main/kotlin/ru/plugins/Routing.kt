package ru.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File


fun Application.configureRouting() {
    routing {
        staticFiles("/static", File("files"))
        // Маршрут для robots.txt
        get("/robots.txt") {
            call.respondText(this::class.java.classLoader.getResource("static/robots.txt")!!.readText(), ContentType.Text.Plain)
        }

        // Маршрут для sitemap.xml
        get("/sitemap.xml") {
            call.respondText(this::class.java.classLoader.getResource("static/sitemap.xml")!!.readText(), ContentType.Text.Xml)
        }

    }
}
