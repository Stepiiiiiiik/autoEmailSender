package model.emailSender

class EmailSender {
    private val email: Email
    private val password: String
    private val smtp: mailServer

    private enum class mailServer {
        Gmail,
        MailRu,
        Yandex,
    }

    private val serverList: Map<mailServer, Pair<String, Int>> = mapOf(
        mailServer.Gmail to Pair("smtp.gmail.com", 465),
        mailServer.MailRu to Pair("smtp.mail.ru", 465),
        mailServer.Yandex to Pair("smtp.yandex.ru", 465),
    )

    private val domainToServer: Map<String, mailServer> = mapOf(
        "gmail.com" to mailServer.Gmail,
        "mailru" to mailServer.MailRu,
        "bk.ru" to mailServer.MailRu,
        "inbox.ru" to mailServer.MailRu,
        "list.ru" to mailServer.MailRu,
        "yandex.ru" to mailServer.Yandex,
        "ya.ru" to mailServer.Yandex,
    )

    public constructor(email: String, password: String) {
        this.email = Email(email)
        this.password = password
        this.smtp = domainToServer[this.email.getDomain()] ?: throw IllegalArgumentException("Invalid email format")
    }
}
