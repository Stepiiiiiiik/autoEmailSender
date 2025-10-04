package model.emailSender

class EmailSender {
    private val email: Email
    private val password: String
    private val smtp: mailServer

    private enum class mailServer {
        gmail,
        mailru,
        yandex,
    }

    private inner class Email {
        val email: String

        public constructor(email: String) {
            if (!validateEmail(email)) {
                throw IllegalArgumentException("Invalid email format")
            }
            this.email = email
        }

        public fun getDomain(): String {
            val split: List<String> = email.split("@")
            if (split.size != 2) {
                throw IllegalArgumentException("Invalid email format")
            }
            return split[1]
        }

        private fun validateEmail(email: String): Boolean {
            val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
            return email.matches(Regex(emailRegex))
        }

    }

    private val serverList: Map<mailServer, Pair<String, Int>> = mapOf(
        mailServer.gmail to Pair("smtp.gmail.com", 465),
        mailServer.mailru to Pair("smtp.mail.ru", 465),
        mailServer.yandex to Pair("smtp.yandex.ru", 465),
    )

    private val domainToServer: Map<String, mailServer> = mapOf(
        "gmail.com" to mailServer.gmail,
        "mailru" to mailServer.mailru,
        "bk.ru" to mailServer.mailru,
        "inbox.ru" to mailServer.mailru,
        "list.ru" to mailServer.mailru,
        "yandex.ru" to mailServer.yandex,
        "ya.ru" to mailServer.yandex,
    )

    public constructor(email: String, password: String) {
        this.email = Email(email)
        this.password = password
        this.smtp = domainToServer[this.email.getDomain()] ?: throw IllegalArgumentException("Invalid email format")
    }
}