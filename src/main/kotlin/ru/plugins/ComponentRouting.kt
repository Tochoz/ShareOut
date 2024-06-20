package ru.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.pebble.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.pebbletemplates.pebble.loader.ClasspathLoader
import ru.*

val components = listOf(
    "header",
    "footer",
    "base",
    "page-title",
    "welcome-screen",
    "text-photo",
    "title-text",
    "zigzag",
    "three-tiles",
    "accordion",
    "contact-form",
    "cabinet-screen",

)


fun Application.configureComponentsWatch() {
    routing {
        get("/components/{id_component}"){
            val component_name = call.parameters["id_component"]?.lowercase()
            if (component_name != null && component_name in components)
                call.respond(PebbleContent("/components/component.peb", getPayloadFromCall(call)+mapOf("component" to component_name)))
            else
                call.respond(HttpStatusCode(200, "Component not found"))
        }

        get("/components"){
            call.respond(PebbleContent("/components/components.peb", getPayloadFromCall(call)+mapOf("components" to components)))
        }

        get("/demo"){
            call.respond(PebbleContent("demo-page.peb",
                mapOf("items" to listOf(
                    mapOf("title" to "Какой максимальный размер файла я могу передать?", "description" to "С учётом сложившейся международной обстановки, внедрение современных методик, в своём классическом представлении, допускает внедрение форм воздействия. Следует отметить, что глубокий уровень погружения способствует подготовке и реализации поэтапного и последовательного развития общества."),
                    mapOf("title" to "Какой максимальный размер файла я могу передать? Сообщениe в несколько строк", "description" to "С учётом сложившейся международной обстановки, внедрение современных методик, в своём классическом представлении, допускает внедрение форм воздействия"),
                ))+getPayloadFromCall(call)
            ))
        }
        get("/main"){
            call.respond(PebbleContent("main.peb",
                mapOf("items" to faq, "location" to "main") + getPayloadFromCall(call)
            ))
        }

        get("/"){
            val content = call.request.queryParameters["content"]
            val id = call.request.queryParameters["id"]
            print("$content")
            if (content != null) {
                call.respond(
                    PebbleContent(
                        "deliver.peb",
                        getPayloadFromCall(call) + mapOf("sendcontent" to content, "location" to "deliver")
                    )
                )
            }
            if (id != null) {
                val link = links.find { it.id == id.toInt() }
                if (link!=null) {
                    call.respond(
                        PebbleContent(
                            "deliver.peb",
                            getPayloadFromCall(call) + mapOf("sendcontent" to link.content)
                        )
                    )
                }
            }
            call.respond(
                PebbleContent(
                    "deliver.peb",
                    getPayloadFromCall(call)
            ))
        }

        post("/"){
            val formParameters = call.receiveParameters()
            val content = formParameters["content"].toString()

            if (content!="")
                call.respondRedirect("/?content="+ content.encodeURLParameter())
            else
                call.respondRedirect("/")
        }

        get("/profile"){
            val userSession = call.sessions.get<UserSession>()
            if (userSession != null){
                call.respond(PebbleContent("profile.peb",
                    mapOf("user" to userSession, "favorites" to favs, "location" to "profile")
                ))
            }
            else
                call.respondRedirect("/main")
        }

        get("/profile/texts"){
            val userSession = call.sessions.get<UserSession>()
            if (userSession != null){
                call.respond(PebbleContent("links.peb",
                    mapOf("user" to userSession, "links" to links)
                ))
            }
            else
                call.respondRedirect("/main")
        }
        get("/profile/files"){
            val userSession = call.sessions.get<UserSession>()
            if (userSession != null){
                call.respond(PebbleContent("files.peb",
                    mapOf("user" to userSession).plus(
                        mapOf("files" to files)
                    )
                ))
            }
            else
                call.respondRedirect("/main")
        }
        post("/api/organisation-contact"){
            call.respondRedirect("/contact-success")
        }
        get("/contact-success"){
            call.respond(PebbleContent("form-target.peb", getPayloadFromCall(call)))
        }

    }
}