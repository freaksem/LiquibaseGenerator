package com.ktor.utils

class Regex {
    companion object {
        val DATE_REGEX = """^(\d{1,2}\.\d{1,2}\.\d{4})|(\d{1,2}\/\d{1,2}\/\d{4})$""".toRegex()
    }
}