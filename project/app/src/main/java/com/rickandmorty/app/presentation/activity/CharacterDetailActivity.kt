package com.rickandmorty.app.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.rickandmorty.app.R
import com.rickandmorty.app.data.local.database.AppDatabase
import com.rickandmorty.app.data.remote.api.RetrofitClient
import com.rickandmorty.app.data.repository.CharacterRepositoryImpl
import com.rickandmorty.app.databinding.ActivityCharacterDetailBinding
import com.rickandmorty.app.domain.usecase.*
import com.rickandmorty.app.presentation.viewmodel.CharacterViewModel
import com.rickandmorty.app.presentation.viewmodel.CharacterViewModelFactory

class CharacterDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharacterDetailBinding
    private lateinit var viewModel: CharacterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupToolbar()

        val characterId = intent.getIntExtra("character_id", -1)
        if (characterId != -1) {
            viewModel.getCharacterById(characterId)
            observeCharacter()
        } else {
            finish()
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

    private fun observeCharacter() {
        viewModel.selectedCharacter.observe(this) { character ->
            character?.let {
                binding.apply {
                    tvName.text = it.name
                    tvStatus.text = "Status: ${it.status}"
                    tvSpecies.text = "Species: ${it.species}"
                    tvType.text = "Type: ${if (it.type.isEmpty()) "N/A" else it.type}"
                    tvGender.text = "Gender: ${it.gender}"
                    tvOrigin.text = "Origin: ${it.originName}"
                    tvLocation.text = "Location: ${it.locationName}"

                    Glide.with(this@CharacterDetailActivity)
                        .load(it.image)
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                        .into(ivCharacter)
                }
            }
        }
    }
}
