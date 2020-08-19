package com.rizieq.whatsapp.utils

object Constants {
    val DATA_USERS = "Users"
    val DATA_USER_EMAIL = "email"
    val DATA_USER_PHONE = "phone"
    val DATA_USER_NAME = "name"
    val DATA_USER_IMAGE_URL = "imageUrl"
    val DATA_USER_STATUS = "status"
    val DATA_USER_STATUS_URL = "statusUrl"
    val DATA_USER_STATUS_TIME = "statusTime"
    val REQUEST_NEW_CHATS = 1922
    val DATA_IMAGES = "image"               // acuan mengakses table image di dalam table user
    val REQUEST_CODE_PHOTO = 1924

    val DATA_USER_CHATS = "userChats" // menambahkan child baru dalam table User
    val DATA_CHATS = "Chats" // menambahkan table baru ‘Chats’
    val DATA_CHAT_PARTICIPANTS = "chatParticipants" // child baru di table Chats

    val DATA_CHAT_MESSAGE = "messages"
    val DATA_CHAT_MESSAGE_TIME = "messageTime"
}