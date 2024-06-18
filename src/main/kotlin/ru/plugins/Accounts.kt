package ru.plugins

import io.ktor.server.application.*
import io.ktor.server.pebble.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import ru.User
import ru.UserSession
import ru.users

fun getPayloadFromCall(call: ApplicationCall) : Map<String, Any>{
    val userSession = call.sessions.get<UserSession>()
    if (userSession != null){
        return mapOf("user" to userSession)
    }
    return mapOf()
}

fun Application.configureAccounts() {
    routing {
        get("/accounts/login"){
            call.respond(
                PebbleContent("login.peb",
                getPayloadFromCall(call)
            ))
        }

        post("/accounts/login"){
            val formParameters = call.receiveParameters()
            val username = formParameters["email"].toString().trim()
            val password = formParameters["password"].toString().trim()

            var user = users.find { it.email == username && it.password == password}
            if (user != null){
                call.sessions.set(user.toSession())
            }
            call.respondRedirect("/profile")
        }

        get("/accounts/reg"){
            call.respond(
                PebbleContent("registration.peb",
                    getPayloadFromCall(call)
            ))
        }

        post("/accounts/reg"){
            val formParameters = call.receiveParameters()
            val username = formParameters["email"].toString().trim()
            val password = formParameters["password"].toString().trim()
            val passwordrepeat = formParameters["passwordrepeat"].toString().trim()
            var found = users.find { it.email == username }
            if (password == passwordrepeat && found == null) {
                var user = User(username, password, 0, 0)
                users.add(user)
                call.sessions.set(user.toSession())
                call.respondRedirect("/profile")

            }
            call.respondRedirect("/accounts/reg")
        }

        get("/accounts/logout"){
            call.sessions.clear<UserSession>()
            call.respondRedirect("/main");
        }

        get("/accounts/password-reset"){
            call.respond(
                PebbleContent("password-reset.peb",
                    getPayloadFromCall(call)
                ))
        }
        post("/accounts/password-reset"){
            call.respondRedirect("/profile")
        }
    }
}