package com.rickandmorty.app.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.rickandmorty.app.data.local.dao.CharacterDao
import com.rickandmorty.app.data.mapper.toDomain
import com.rickandmorty.app.data.mapper.toEntity
import com.rickandmorty.app.data.remote.api.RickAndMortyApi
import com.rickandmorty.app.domain.model.Character
import com.rickandmorty.app.domain.repository.CharacterRepository
import com.rickandmorty.app.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterRepositoryImpl(
    private val api: RickAndMortyApi,
    private val characterDao: CharacterDao
) : CharacterRepository {

    override fun getAllCharacters(): LiveData<List<Character>> {
        return characterDao.getAllCharacters().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getCharacterById(id: Int): Character? {
        return withContext(Dispatchers.IO) {
            characterDao.getCharacterById(id)?.toDomain()
        }
    }

    override fun searchCharacters(query: String): LiveData<List<Character>> {
        return characterDao.searchCharacters(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun fetchCharactersFromApi(): Resource<List<Character>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCharacters()
                if (response.isSuccessful && response.body() != null) {
                    val characters = response.body()!!.results.map { dto ->
                        dto.toEntity()
                    }
                    characterDao.insertCharacters(characters)
                    Resource.Success(characters.map { it.toDomain() })
                } else {
                    Resource.Error("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    override suspend fun insertCharacter(character: Character): Long {
        return withContext(Dispatchers.IO) {
            characterDao.insertCharacter(character.toEntity())
        }
    }

    override suspend fun updateCharacter(character: Character) {
        withContext(Dispatchers.IO) {
            characterDao.updateCharacter(character.toEntity())
        }
    }

    override suspend fun deleteCharacter(character: Character) {
        withContext(Dispatchers.IO) {
            characterDao.deleteCharacter(character.toEntity())
        }
    }

    override suspend fun deleteCharacterById(id: Int) {
        withContext(Dispatchers.IO) {
            characterDao.deleteCharacterById(id)
        }
    }
}
