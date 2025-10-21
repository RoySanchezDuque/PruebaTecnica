package com.rickandmorty.app.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rickandmorty.app.domain.model.Character
import com.rickandmorty.app.domain.repository.CharacterRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class GetAllCharactersUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: CharacterRepository

    private lateinit var useCase: GetAllCharactersUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = GetAllCharactersUseCase(repository)
    }

    @Test
    fun `invoke returns LiveData from repository`() {
        val liveData = MutableLiveData<List<Character>>()
        `when`(repository.getAllCharacters()).thenReturn(liveData)

        val result = useCase.invoke()

        assertNotNull(result)
        verify(repository).getAllCharacters()
    }
}
