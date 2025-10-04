package model.emailSender

import org.apache.commons.mail.EmailAttachment
import org.apache.commons.mail.MultiPartEmail

class EmailSender private constructor() {
    private var email: Email? = null
    private var password = ""
    private var smtp = MailServer.Default

    private enum class MailServer {
        Gmail,
        MailRu,
        Yandex,
        Default
    }

    private val serverList: Map<MailServer, Pair<String, Int>> = mapOf(
        MailServer.Gmail to Pair("smtp.gmail.com", 465),
        MailServer.MailRu to Pair("smtp.mail.ru", 465),
        MailServer.Yandex to Pair("smtp.yandex.ru", 465),
    )

    private val domainToServer: Map<String, MailServer> = mapOf(
        "gmail.com" to MailServer.Gmail,
        "mailru" to MailServer.MailRu,
        "bk.ru" to MailServer.MailRu,
        "inbox.ru" to MailServer.MailRu,
        "list.ru" to MailServer.MailRu,
        "yandex.ru" to MailServer.Yandex,
        "ya.ru" to MailServer.Yandex,
    )

    constructor(email: String, password: String) : this() {
        this.email = Email(email)
        this.password = password
        this.smtp = domainToServer[this.email?.getDomain()] ?: throw IllegalArgumentException("Invalid email format")
    }
}
