package com.rickandmorty.app.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickandmorty.app.domain.model.Character
import com.rickandmorty.app.domain.usecase.*
import com.rickandmorty.app.utils.Resource
import kotlinx.coroutines.launch

class CharacterViewModel(
    private val getAllCharactersUseCase: GetAllCharactersUseCase,
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase,
    private val searchCharactersUseCase: SearchCharactersUseCase,
    private val fetchCharactersFromApiUseCase: FetchCharactersFromApiUseCase,
    private val createCharacterUseCase: CreateCharacterUseCase,
    private val updateCharacterUseCase: UpdateCharacterUseCase,
    private val deleteCharacterUseCase: DeleteCharacterUseCase
) : ViewModel() {

    val characters: LiveData<List<Character>> = getAllCharactersUseCase()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _selectedCharacter = MutableLiveData<Character?>()
    val selectedCharacter: LiveData<Character?> = _selectedCharacter

    private val _operationSuccess = MutableLiveData<Boolean>()
    val operationSuccess: LiveData<Boolean> = _operationSuccess

    fun fetchCharactersFromApi() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            when (val result = fetchCharactersFromApiUseCase()) {
                is Resource.Success -> {
                    _loading.value = false
                    _operationSuccess.value = true
                }
                is Resource.Error -> {
                    _loading.value = false
                    _error.value = result.message
                    _operationSuccess.value = false
                }
                is Resource.Loading -> {
                    _loading.value = true
                }
            }
        }
    }

    fun getCharacterById(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            val character = getCharacterByIdUseCase(id)
            _selectedCharacter.value = character
            _loading.value = false
        }
    }

    fun searchCharacters(query: String): LiveData<List<Character>> {
        return searchCharactersUseCase(query)
    }

    fun createCharacter(character: Character) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                createCharacterUseCase(character)
                _loading.value = false
                _operationSuccess.value = true
            } catch (e: Exception) {
                _loading.value = false
                _error.value = e.message
                _operationSuccess.value = false
            }
        }
    }

    fun updateCharacter(character: Character) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                updateCharacterUseCase(character)
                _loading.value = false
                _operationSuccess.value = true
            } catch (e: Exception) {
                _loading.value = false
                _error.value = e.message
                _operationSuccess.value = false
            }
        }
    }

    fun deleteCharacter(character: Character) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                deleteCharacterUseCase(character)
                _loading.value = false
                _operationSuccess.value = true
            } catch (e: Exception) {
                _loading.value = false
                _error.value = e.message
                _operationSuccess.value = false
            }
        }
    }

    fun clearSelectedCharacter() {
        _selectedCharacter.value = null
    }

    fun clearError() {
        _error.value = null
    }

    fun clearOperationSuccess() {
        _operationSuccess.value = false
    }
}
