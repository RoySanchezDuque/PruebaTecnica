package com.rickandmorty.app.domain.usecase

import com.rickandmorty.app.domain.model.Character
import com.rickandmorty.app.domain.repository.CharacterRepository
import com.rickandmorty.app.utils.Resource

class FetchCharactersFromApiUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(): Resource<List<Character>> {
        return repository.fetchCharactersFromApi()
    }
}
