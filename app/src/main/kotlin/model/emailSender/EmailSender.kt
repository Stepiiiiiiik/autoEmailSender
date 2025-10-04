package model.emailSender

import org.apache.commons.mail.EmailAttachment
import org.apache.commons.mail.MultiPartEmail
import kotlin.io.path.Path
import kotlin.io.path.exists

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

    fun send(recipients: List<Email>, subject: String, text: String, filePath: String?) {
        if (recipients.size > 30) {
            throw IllegalArgumentException("Too many recipients")
        }

        val email = MultiPartEmail()

        filePath?.apply {
            val attach = EmailAttachment()
            if (!Path(filePath).exists()) {
                throw IllegalArgumentException("Path does not exist")
            }
            attach.path = filePath
            attach.disposition = EmailAttachment.ATTACHMENT
            email.attach(attach)
        }

        email.setHostName(serverList[this.smtp]?.first ?: throw IllegalArgumentException("Mail server doesn't exist"))
        email.setSmtpPort(
            serverList[this.smtp]?.second ?: throw IllegalArgumentException("Mail server doesn't exist")
        )
        email.setAuthentication(
            this.email?.getEmail() ?: throw IllegalArgumentException("Email can not be null"),
            this.password
        )
        email.isSSLOnConnect = true

        email.setFrom(this.email?.getEmail() ?: throw IllegalArgumentException("Email can not be null"))
        recipients.forEach { recipient ->
            email.addTo(recipient.getEmail())
        }
        email.setSubject(subject)
        email.setMsg(text)

        email.send()
    }
}
