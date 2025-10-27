package com.rickandmorty.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rickandmorty.app.domain.usecase.*

class CharacterViewModelFactory(
    private val getAllCharactersUseCase: GetAllCharactersUseCase,
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase,
    private val searchCharactersUseCase: SearchCharactersUseCase,
    private val fetchCharactersFromApiUseCase: FetchCharactersFromApiUseCase,
    private val createCharacterUseCase: CreateCharacterUseCase,
    private val updateCharacterUseCase: UpdateCharacterUseCase,
    private val deleteCharacterUseCase: DeleteCharacterUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterViewModel::class.java)) {
            return CharacterViewModel(
                getAllCharactersUseCase,
                getCharacterByIdUseCase,
                searchCharactersUseCase,
                fetchCharactersFromApiUseCase,
                createCharacterUseCase,
                updateCharacterUseCase,
                deleteCharacterUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
