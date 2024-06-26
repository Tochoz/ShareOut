package ru.plugins

import io.ktor.http.*
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
            val msg = call.request.queryParameters["msg"]
            if (msg != null)
                call.respond(
                    PebbleContent("login.peb",
                    getPayloadFromCall(call) + mapOf("location" to "login") + mapOf("alertmessage" to msg)
                ))
            else
                call.respond(
                    PebbleContent("login.peb",
                        getPayloadFromCall(call) + mapOf("location" to "login")
                    ))
        }

        post("/accounts/login"){
            val formParameters = call.receiveParameters()
            val username = formParameters["email"].toString().trim()
            val password = formParameters["password"].toString().trim()

            var user = users.find { it.email == username }
            if (user == null)
                call.respondRedirect("/accounts/login?msg=" + "Аккаунта с такой почтой не существует.".encodeURLPath())
            else if (user.password != password)
                call.respondRedirect("/accounts/login?msg=" + "Неверный пароль от аккаунта.".encodeURLPath())
            else if (user != null){
                call.sessions.set(user.toSession())
                call.respondRedirect("/profile")
            }

        }

        get("/accounts/reg"){
            val msg = call.request.queryParameters["msg"]
            if (msg != null)
                call.respond(
                    PebbleContent("registration.peb",
                        getPayloadFromCall(call) + mapOf("alertmessage" to msg)
                ))
            else
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
            if (found != null)
                call.respondRedirect("/accounts/reg?msg=" + "Аккаунт с такой почтой уже существует.".encodeURLPath())
            else if (password != passwordrepeat)
                call.respondRedirect("/accounts/reg?msg=" + "Пароли не совпадают.".encodeURLPath())
            else{
                var user = User(username, password, 0, 0)
                users.add(user)
                call.sessions.set(user.toSession())
                call.respondRedirect("/profile")

            }
        }

        get("/accounts/logout"){
            call.sessions.clear<UserSession>()
            call.respondRedirect("/main");
        }

        get("/accounts/newpassword"){

            call.respond(
                PebbleContent("newpassword.peb",
                        getPayloadFromCall(call)
                ))
        }
        post("/accounts/newpassword"){
            call.respondRedirect("/accounts/login")
        }

        get("/accounts/password-reset"){
            val msg = call.request.queryParameters["msg"]
            if (msg!=null)
                call.respond(
                    PebbleContent("password-reset.peb",
                        getPayloadFromCall(call) + mapOf("alertmessage" to msg)
                    ))
            else
                call.respond(
                    PebbleContent("password-reset.peb",
                        getPayloadFromCall(call)
                    ))

        }
        post("/accounts/password-reset"){
            call.respondRedirect("/accounts/password-reset?msg="+"Если данный почтовый адрес зарегистрирован, на него было отправлено письмо с информацией о дальнейших действиях.".encodeURLPath())
        }
    }
}