package com.rickandmorty.app.domain.usecase

import com.rickandmorty.app.domain.model.Character
import com.rickandmorty.app.domain.repository.CharacterRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class CreateCharacterUseCaseTest {

    @Mock
    private lateinit var repository: CharacterRepository

    private lateinit var useCase: CreateCharacterUseCase

    private val mockCharacter = Character(
        id = 0,
        name = "Test Character",
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
        useCase = CreateCharacterUseCase(repository)
    }

    @Test
    fun `invoke calls repository insertCharacter`() = runTest {
        `when`(repository.insertCharacter(mockCharacter)).thenReturn(1L)

        val result = useCase.invoke(mockCharacter)

        assertEquals(1L, result)
        verify(repository).insertCharacter(mockCharacter)
    }
}
