package com.example.firebasebasedchatapplication

data class Message(
    val text: String = "",
    val sender: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isSentByCurrentUser: Boolean = false
)
