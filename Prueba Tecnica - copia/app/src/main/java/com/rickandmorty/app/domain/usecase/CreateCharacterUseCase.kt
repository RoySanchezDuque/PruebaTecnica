package com.rickandmorty.app.domain.usecase

import com.rickandmorty.app.domain.model.Character
import com.rickandmorty.app.domain.repository.CharacterRepository

class CreateCharacterUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(character: Character): Long {
        return repository.insertCharacter(character)
    }
}
