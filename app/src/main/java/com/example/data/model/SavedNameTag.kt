package com.example.data.model

data class SavedNameTag(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val designation: String,
    val salutation: String,
    val templateIndex: Int = 0,
    val pageSize: String = "A5",
    val fontScale: Float = 1.0f,
    val timestamp: Long = System.currentTimeMillis()
)
