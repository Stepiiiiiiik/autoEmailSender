package model.emailSender

class Email {
    private var email: String

    constructor(email: String) {
        if (!validateEmail(email)) {
            throw IllegalArgumentException("Invalid email format")
        }
        this.email = email
    }

    fun getEmail() : String {
        return email
    }

    fun getDomain(): String {
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