package ru

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import ru.plugins.*
import java.io.File

data class UserSession(
    val id: Int,
    val is_authenticated: Boolean,
    val used: Int,
    val persent: Int,
)
var lastId = 0;

data class User(
    val email: String,
    val password: String,
    val used: Int,
    val persent: Int,
){
    fun toSession(): UserSession{
        return UserSession((lastId++) + 1, true, used, persent)
    }
}

data class Link(
    var id:Int,
    var name: String,
    var content: String,
    var size: Int,
    var date: String,
    var favourite: Boolean,

){
    var preparedContent: String
    init {
        var build = StringBuilder(content)
        for (i: Int in 10..content.length step 13){
            build.insert(i, "<wbr>")
        }
        preparedContent = build.toString();
    }
}

data class File(
    var id:Int,
    var name: String,
    var content: String,
    var size: String,
    var date: String,
    var favourite: Boolean
    ){
    init {
        var preparedContent: String

        var build = StringBuilder(content)
        for (i: Int in 10..content.length step 13){
            build.insert(i, "<wbr>")
        }
        preparedContent = build.toString();
    }
}

data class Favorites(
    var links: List<Link>? = null,
    var files: List<ru.File>? = null,
)
var links = listOf(
    Link(0, "Ключ от кошелька", "983e887a227069e9f37d15f30167fe68e52b5ec2c077c1ae4db6bbc61623f6a9", 64, "30.10.2023", true),
    Link(1, "Мой личный сайт", "my-personal-site.su", 9, "19.11.2021", true),
    Link(3, "Пароль от телефона", "iamironman2003", 14, "13.05.2023", false),
)

var favs: Favorites = Favorites(
    listOf(
        Link(0, "Ключ от кошелька", "983e887a227069e9f37d15f30167fe68e52b5ec2c077c1ae4db6bbc61623f6a9", 64, "30.10.2003", true),
        Link(1, "Мой личный сайт", "my-personal-site.su", 19, "13.05.2022", true),
    ),
    listOf(
        File(0, "Резюме", "meiscool.txt", "12 МБ", "19.11.2021", true),
        File(1, "Отчёт типо ЛР1", "Отчёт по типографике P33692.docx", "128 КБ", "04.12.2021", true),
    ),
)

val users = mutableListOf(User("test@mail.ru","12345", 13, 13))

fun main(args: Array<String>) {
     io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Sessions){
        cookie<UserSession>("user_session", directorySessionStorage(File("build/.sessions")))
    }
    configureTemplating()
    configureRouting()
    configureAccounts()
    configureComponentsWatch()
}
