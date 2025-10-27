package com.rickandmorty.app.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rickandmorty.app.data.local.entity.CharacterEntity

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters ORDER BY createdAt DESC")
    fun getAllCharacters(): LiveData<List<CharacterEntity>>

    @Query("SELECT * FROM characters ORDER BY createdAt DESC")
    suspend fun getAllCharactersSync(): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacterById(id: Int): CharacterEntity?

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :query || '%' OR species LIKE '%' || :query || '%' OR type LIKE '%' || :query || '%' OR gender LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchCharacters(query: String): LiveData<List<CharacterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Update
    suspend fun updateCharacter(character: CharacterEntity)

    @Delete
    suspend fun deleteCharacter(character: CharacterEntity)

    @Query("DELETE FROM characters WHERE id = :id")
    suspend fun deleteCharacterById(id: Int)

    @Query("DELETE FROM characters")
    suspend fun deleteAllCharacters()
}
