package com.rickandmorty.app.domain.usecase

import androidx.lifecycle.LiveData
import com.rickandmorty.app.domain.model.Character
import com.rickandmorty.app.domain.repository.CharacterRepository

class GetAllCharactersUseCase(
    private val repository: CharacterRepository
) {
    operator fun invoke(): LiveData<List<Character>> {
        return repository.getAllCharacters()
    }
}
