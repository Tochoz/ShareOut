package ru

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.sessions.*
import ru.plugins.*
import java.io.File
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.routing.*

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

var files = listOf(
    File(5, "Резюме", "meiscool.txt", "12 МБ", "19.11.2021", true),
    File(1, "Отчёт типо ЛР1", "Отчёт по типографике P33692.docx", "952 КБ", "04.12.2021", true),
    File(4, "Конфиг сервера", "setup.sh", "13 КБ", "13.05.2023", false),
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

val faq = listOf(
    mapOf("title" to "Какой макси&shy;мальный размер файла я&nbsp;могу пере&shy;дать?",
    "description" to "Индиви&shy;дуаль&shy;ный размер файла не&nbsp;может превы&shy;шать 10&nbsp;МБ, в&nbsp;общей слож&shy;ности у&nbsp;заре&shy;гистри&shy;рован&shy;ного пользо&shy;вателя есть возмож&shy;ность хранить до&nbsp;100&nbsp;МБ инфор&shy;мации включая ссылки. Коли&shy;чество исполь&shy;зован&shy;ной памяти указано в&nbsp;личном кабинете."),
    mapOf("title" to "Как долго дейс&shyтвует код для&nbsp;полу&shyчения?",
        "description" to "Для незаре&shy;гистри&shy;рован&shy;ных поль&shy;зова&shy;телей время действия кода 2&nbsp;минуты. Заре&shy;гист&shy;рирован&shy;ные поль&shy;зова&shy;тели могут увели&shy;чить это время до&nbsp;5&nbsp;минут."),
    mapOf("title" to "Какая макси&shy;мальная длина переда&shy;вае&shy;мого текста?",
        "description" to "Макси&shy;мальная длина переда&shy;вае&shy;мого текста 2000 символов."),
    mapOf("title" to "Какое коли&shy;чество ссылок и&nbsp;файлов сохра&shy;няется в&nbsp;недав&shy;них?",
        "description" to "В&nbsp;списке недавних сохра&shy;няются все ссылки и&nbsp;файлы. В&nbsp;случае если выде&shy;лен&shy;ные вам 100&nbsp;МБ закон&shy;чатся, система будет очищать ссылки и&nbsp;файлы начиная с&nbsp;самых старых."),
)

val users = mutableListOf(User("test@mail.ru","12345", 13, 13))

fun main(args: Array<String>) {
     io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Compression){
        gzip()
        deflate()
    }
    install(CachingHeaders)

    install(Sessions){
        cookie<UserSession>("user_session", directorySessionStorage(File("build/.sessions")))
    }
    configureTemplating()

    configureRouting()
    configureAccounts()
    configureComponentsWatch()
    routing {
//        install(CachingHeaders) {
//            options { call, content ->
//                when (content.contentType?.withoutParameters()) {
//                    ContentType.Text.Plain -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 3600))
//                    ContentType.Text.Html -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 60))
//                    else -> null
//                }
//            }
//        }
    }
}
