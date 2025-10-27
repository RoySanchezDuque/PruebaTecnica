package com.rickandmorty.app.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rickandmorty.app.domain.model.Character
import com.rickandmorty.app.domain.usecase.*
import com.rickandmorty.app.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class CharacterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var getAllCharactersUseCase: GetAllCharactersUseCase

    @Mock
    private lateinit var getCharacterByIdUseCase: GetCharacterByIdUseCase

    @Mock
    private lateinit var searchCharactersUseCase: SearchCharactersUseCase

    @Mock
    private lateinit var fetchCharactersFromApiUseCase: FetchCharactersFromApiUseCase

    @Mock
    private lateinit var createCharacterUseCase: CreateCharacterUseCase

    @Mock
    private lateinit var updateCharacterUseCase: UpdateCharacterUseCase

    @Mock
    private lateinit var deleteCharacterUseCase: DeleteCharacterUseCase

    private lateinit var viewModel: CharacterViewModel

    private val mockCharacter = Character(
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

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        val liveData = MutableLiveData<List<Character>>()
        `when`(getAllCharactersUseCase.invoke()).thenReturn(liveData)

        viewModel = CharacterViewModel(
            getAllCharactersUseCase,
            getCharacterByIdUseCase,
            searchCharactersUseCase,
            fetchCharactersFromApiUseCase,
            createCharacterUseCase,
            updateCharacterUseCase,
            deleteCharacterUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchCharactersFromApi sets loading and success on successful fetch`() = runTest {
        `when`(fetchCharactersFromApiUseCase.invoke()).thenReturn(
            Resource.Success(listOf(mockCharacter))
        )

        viewModel.fetchCharactersFromApi()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(false, viewModel.loading.value)
        assertEquals(true, viewModel.operationSuccess.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `fetchCharactersFromApi sets error on failed fetch`() = runTest {
        val errorMessage = "Network error"
        `when`(fetchCharactersFromApiUseCase.invoke()).thenReturn(
            Resource.Error(errorMessage)
        )

        viewModel.fetchCharactersFromApi()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(false, viewModel.loading.value)
        assertEquals(errorMessage, viewModel.error.value)
    }

    @Test
    fun `createCharacter calls use case and sets success`() = runTest {
        `when`(createCharacterUseCase.invoke(mockCharacter)).thenReturn(1L)

        viewModel.createCharacter(mockCharacter)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(createCharacterUseCase).invoke(mockCharacter)
        assertEquals(true, viewModel.operationSuccess.value)
    }

    @Test
    fun `updateCharacter calls use case and sets success`() = runTest {
        viewModel.updateCharacter(mockCharacter)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(updateCharacterUseCase).invoke(mockCharacter)
        assertEquals(true, viewModel.operationSuccess.value)
    }

    @Test
    fun `deleteCharacter calls use case and sets success`() = runTest {
        viewModel.deleteCharacter(mockCharacter)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(deleteCharacterUseCase).invoke(mockCharacter)
        assertEquals(true, viewModel.operationSuccess.value)
    }

    @Test
    fun `getCharacterById sets selectedCharacter`() = runTest {
        `when`(getCharacterByIdUseCase.invoke(1)).thenReturn(mockCharacter)

        viewModel.getCharacterById(1)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(mockCharacter, viewModel.selectedCharacter.value)
    }

    @Test
    fun `searchCharacters returns LiveData from use case`() {
        val liveData = MutableLiveData<List<Character>>()
        val query = "Rick"
        `when`(searchCharactersUseCase.invoke(query)).thenReturn(liveData)

        val result = viewModel.searchCharacters(query)

        assertNotNull(result)
        verify(searchCharactersUseCase).invoke(query)
    }
}
