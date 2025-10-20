package com.rickandmorty.app.presentation.activity

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.rickandmorty.app.R
import com.rickandmorty.app.data.local.database.AppDatabase
import com.rickandmorty.app.data.remote.api.RetrofitClient
import com.rickandmorty.app.data.repository.CharacterRepositoryImpl
import com.rickandmorty.app.databinding.ActivityCreateEditCharacterBinding
import com.rickandmorty.app.domain.model.Character
import com.rickandmorty.app.domain.usecase.*
import com.rickandmorty.app.presentation.viewmodel.CharacterViewModel
import com.rickandmorty.app.presentation.viewmodel.CharacterViewModelFactory

class CreateEditCharacterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateEditCharacterBinding
    private lateinit var viewModel: CharacterViewModel
    private var characterId: Int = -1
    private var existingCharacter: Character? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEditCharacterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupToolbar()
        setupSpinners()
        setupListeners()

        characterId = intent.getIntExtra("character_id", -1)
        if (characterId != -1) {
            supportActionBar?.title = "Edit Character"
            viewModel.getCharacterById(characterId)
            observeCharacter()
        } else {
            supportActionBar?.title = "Create Character"
        }
    }

    private fun setupViewModel() {
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = CharacterRepositoryImpl(RetrofitClient.api, database.characterDao())

        val factory = CharacterViewModelFactory(
            GetAllCharactersUseCase(repository),
            GetCharacterByIdUseCase(repository),
            SearchCharactersUseCase(repository),
            FetchCharactersFromApiUseCase(repository),
            CreateCharacterUseCase(repository),
            UpdateCharacterUseCase(repository),
            DeleteCharacterUseCase(repository)
        )

        viewModel = ViewModelProvider(this, factory)[CharacterViewModel::class.java]
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupSpinners() {
        val statusOptions = arrayOf("Alive", "Dead", "unknown")
        val genderOptions = arrayOf("Male", "Female", "Genderless", "unknown")

        binding.spinnerStatus.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            statusOptions
        )

        binding.spinnerGender.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            genderOptions
        )
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            if (validateInput()) {
                saveCharacter()
            }
        }

        viewModel.operationSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Character saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeCharacter() {
        viewModel.selectedCharacter.observe(this) { character ->
            character?.let {
                existingCharacter = it
                binding.apply {
                    etName.setText(it.name)
                    etSpecies.setText(it.species)
                    etType.setText(it.type)
                    etOrigin.setText(it.originName)
                    etLocation.setText(it.locationName)
                    etImageUrl.setText(it.image)

                    val statusPosition = (spinnerStatus.adapter as ArrayAdapter<String>)
                        .getPosition(it.status)
                    spinnerStatus.setSelection(statusPosition)

                    val genderPosition = (spinnerGender.adapter as ArrayAdapter<String>)
                        .getPosition(it.gender)
                    spinnerGender.setSelection(genderPosition)
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        val name = binding.etName.text.toString().trim()
        val species = binding.etSpecies.text.toString().trim()

        if (name.isEmpty()) {
            binding.etName.error = "Name is required"
            return false
        }

        if (species.isEmpty()) {
            binding.etSpecies.error = "Species is required"
            return false
        }

        return true
    }

    private fun saveCharacter() {
        val name = binding.etName.text.toString().trim()
        val species = binding.etSpecies.text.toString().trim()
        val type = binding.etType.text.toString().trim()
        val status = binding.spinnerStatus.selectedItem.toString()
        val gender = binding.spinnerGender.selectedItem.toString()
        val origin = binding.etOrigin.text.toString().trim()
        val location = binding.etLocation.text.toString().trim()
        val imageUrl = binding.etImageUrl.text.toString().trim()

        val character = Character(
            id = if (characterId != -1) characterId else 0,
            name = name,
            status = status,
            species = species,
            type = type,
            gender = gender,
            originName = origin.ifEmpty { "Unknown" },
            locationName = location.ifEmpty { "Unknown" },
            image = imageUrl.ifEmpty { "https://via.placeholder.com/300" },
            createdAt = existingCharacter?.createdAt ?: System.currentTimeMillis(),
            isFromApi = false
        )

        if (characterId != -1) {
            viewModel.updateCharacter(character)
        } else {
            viewModel.createCharacter(character)
        }
    }
}
