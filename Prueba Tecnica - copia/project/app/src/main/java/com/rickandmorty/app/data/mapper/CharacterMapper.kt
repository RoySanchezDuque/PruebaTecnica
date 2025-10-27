package com.rickandmorty.app.data.mapper

import com.rickandmorty.app.data.local.entity.CharacterEntity
import com.rickandmorty.app.data.remote.model.CharacterDto
import com.rickandmorty.app.domain.model.Character

fun CharacterDto.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        type = this.type,
        gender = this.gender,
        originName = this.origin.name,
        locationName = this.location.name,
        image = this.image,
        isFromApi = true
    )
}

fun CharacterEntity.toDomain(): Character {
    return Character(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        type = this.type,
        gender = this.gender,
        originName = this.originName,
        locationName = this.locationName,
        image = this.image,
        createdAt = this.createdAt,
        isFromApi = this.isFromApi
    )
}

fun Character.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        type = this.type,
        gender = this.gender,
        originName = this.originName,
        locationName = this.locationName,
        image = this.image,
        createdAt = this.createdAt,
        isFromApi = this.isFromApi
    )
}
