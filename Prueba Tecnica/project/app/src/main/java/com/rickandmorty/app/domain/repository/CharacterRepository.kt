package com.rickandmorty.app.domain.repository

import androidx.lifecycle.LiveData
import com.rickandmorty.app.domain.model.Character
import com.rickandmorty.app.utils.Resource

interface CharacterRepository {
    fun getAllCharacters(): LiveData<List<Character>>
    suspend fun getCharacterById(id: Int): Character?
    fun searchCharacters(query: String): LiveData<List<Character>>
    suspend fun fetchCharactersFromApi(): Resource<List<Character>>
    suspend fun insertCharacter(character: Character): Long
    suspend fun updateCharacter(character: Character)
    suspend fun deleteCharacter(character: Character)
    suspend fun deleteCharacterById(id: Int)
}
