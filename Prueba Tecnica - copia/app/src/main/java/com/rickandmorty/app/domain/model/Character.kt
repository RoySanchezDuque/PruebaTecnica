package com.rickandmorty.app.domain.model

data class Character(
    val id: Int = 0,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val originName: String,
    val locationName: String,
    val image: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isFromApi: Boolean = true
)
