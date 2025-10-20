package com.rickandmorty.app.domain.usecase

import com.rickandmorty.app.domain.model.Character
import com.rickandmorty.app.domain.repository.CharacterRepository

class GetCharacterByIdUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: Int): Character? {
        return repository.getCharacterById(id)
    }
}
