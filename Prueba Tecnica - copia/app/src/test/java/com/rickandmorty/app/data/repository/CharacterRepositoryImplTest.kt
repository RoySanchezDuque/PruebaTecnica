package com.rickandmorty.app.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rickandmorty.app.data.local.dao.CharacterDao
import com.rickandmorty.app.data.local.entity.CharacterEntity
import com.rickandmorty.app.data.remote.api.RickAndMortyApi
import com.rickandmorty.app.data.remote.model.CharacterDto
import com.rickandmorty.app.data.remote.model.CharactersResponse
import com.rickandmorty.app.data.remote.model.Info
import com.rickandmorty.app.data.remote.model.Location
import com.rickandmorty.app.domain.model.Character
import com.rickandmorty.app.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
class CharacterRepositoryImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var api: RickAndMortyApi

    @Mock
    private lateinit var characterDao: CharacterDao

    private lateinit var repository: CharacterRepositoryImpl

    private val mockLocation = Location(name = "Earth", url = "")

    private val mockCharacterDto = CharacterDto(
        id = 1,
        name = "Rick Sanchez",
        status = "Alive",
        species = "Human",
        type = "",
        gender = "Male",
        origin = mockLocation,
        location = mockLocation,
        image = "https://example.com/rick.png",
        episode = listOf(),
        url = "",
        created = ""
    )

    private val mockCharacterEntity = CharacterEntity(
        id = 1,
        name = "Rick Sanchez",
        status = "Alive",
        species = "Human",
        type = "",
        gender = "Male",
        originName = "Earth",
        locationName = "Earth",
        image = "https://example.com/rick.png"
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = CharacterRepositoryImpl(api, characterDao)
    }

    @Test
    fun `fetchCharactersFromApi returns success when API call succeeds`() = runTest {
        val mockInfo = Info(count = 1, pages = 1, next = null, prev = null)
        val mockResponse = CharactersResponse(info = mockInfo, results = listOf(mockCharacterDto))

        `when`(api.getCharacters()).thenReturn(Response.success(mockResponse))

        val result = repository.fetchCharactersFromApi()

        assertTrue(result is Resource.Success)
        assertEquals(1, result.data?.size)
        assertEquals("Rick Sanchez", result.data?.first()?.name)

        verify(characterDao).insertCharacters(anyList())
    }

    @Test
    fun `fetchCharactersFromApi returns error when API call fails`() = runTest {
        `when`(api.getCharacters()).thenReturn(Response.error(404, mock()))

        val result = repository.fetchCharactersFromApi()

        assertTrue(result is Resource.Error)
        assertNotNull(result.message)
    }

    @Test
    fun `getCharacterById returns character when found`() = runTest {
        `when`(characterDao.getCharacterById(1)).thenReturn(mockCharacterEntity)

        val result = repository.getCharacterById(1)

        assertNotNull(result)
        assertEquals("Rick Sanchez", result?.name)
        assertEquals(1, result?.id)
    }

    @Test
    fun `getCharacterById returns null when not found`() = runTest {
        `when`(characterDao.getCharacterById(999)).thenReturn(null)

        val result = repository.getCharacterById(999)

        assertNull(result)
    }

    @Test
    fun `insertCharacter calls dao insert method`() = runTest {
        val character = Character(
            id = 0,
            name = "Morty Smith",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Male",
            originName = "Earth",
            locationName = "Earth",
            image = ""
        )

        `when`(characterDao.insertCharacter(any())).thenReturn(1L)

        val result = repository.insertCharacter(character)

        assertEquals(1L, result)
        verify(characterDao).insertCharacter(any())
    }

    @Test
    fun `updateCharacter calls dao update method`() = runTest {
        val character = Character(
            id = 1,
            name = "Updated Name",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Male",
            originName = "Earth",
            locationName = "Earth",
            image = ""
        )

        repository.updateCharacter(character)

        verify(characterDao).updateCharacter(any())
    }

    @Test
    fun `deleteCharacter calls dao delete method`() = runTest {
        val character = Character(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Male",
            originName = "Earth",
            locationName = "Earth",
            image = ""
        )

        repository.deleteCharacter(character)

        verify(characterDao).deleteCharacter(any())
    }

    @Test
    fun `getAllCharacters returns LiveData from dao`() {
        val liveData = MutableLiveData<List<CharacterEntity>>()
        `when`(characterDao.getAllCharacters()).thenReturn(liveData)

        val result = repository.getAllCharacters()

        assertNotNull(result)
        verify(characterDao).getAllCharacters()
    }

    @Test
    fun `searchCharacters returns LiveData from dao`() {
        val liveData = MutableLiveData<List<CharacterEntity>>()
        val query = "Rick"
        `when`(characterDao.searchCharacters(query)).thenReturn(liveData)

        val result = repository.searchCharacters(query)

        assertNotNull(result)
        verify(characterDao).searchCharacters(query)
    }

    private fun <T> any(): T {
        return org.mockito.ArgumentMatchers.any()
    }

    private fun <T> anyList(): List<T> {
        return org.mockito.ArgumentMatchers.anyList()
    }

    private inline fun <reified T> mock(): T = mock(T::class.java)
}
