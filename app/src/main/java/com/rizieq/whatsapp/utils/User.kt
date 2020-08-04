package com.rizieq.whatsapp.utils


data class User(
    val email: String? = "",
    val phone: String? = "",
    val name: String? = "",
    val password: String? = "",
    val imageUrl: String? = "",
    val status: String? = "",
    val statusUrl: String? = "",
    val statusTime: String? = ""

)

data class Contact(
    val name: String?, val phone: String?
)

data class Chat(
    val chatParticipants: ArrayList<String>
)