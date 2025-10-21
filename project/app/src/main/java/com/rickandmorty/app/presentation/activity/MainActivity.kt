package com.rickandmorty.app.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rickandmorty.app.R
import com.rickandmorty.app.data.local.database.AppDatabase
import com.rickandmorty.app.data.remote.api.RetrofitClient
import com.rickandmorty.app.data.repository.CharacterRepositoryImpl
import com.rickandmorty.app.databinding.ActivityMainBinding
import com.rickandmorty.app.domain.model.Character
import com.rickandmorty.app.domain.usecase.*
import com.rickandmorty.app.presentation.adapter.CharacterAdapter
import com.rickandmorty.app.presentation.viewmodel.CharacterViewModel
import com.rickandmorty.app.presentation.viewmodel.CharacterViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: CharacterViewModel
    private lateinit var adapter: CharacterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupRecyclerView()
        setupObservers()
        setupListeners()

        viewModel.fetchCharactersFromApi()
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

    private fun setupRecyclerView() {
        adapter = CharacterAdapter(
            onItemClick = { character ->
                showCharacterDetails(character)
            },
            onEditClick = { character ->
                openEditCharacter(character)
            },
            onDeleteClick = { character ->
                showDeleteConfirmation(character)
            }
        )

        binding.rvCharacters.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupObservers() {
        viewModel.characters.observe(this) { characters ->
            adapter.submitList(characters)
            binding.tvEmpty.visibility = if (characters.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
        }

        viewModel.operationSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Operation successful", Toast.LENGTH_SHORT).show()
                viewModel.clearOperationSuccess()
            }
        }
    }

    private fun setupListeners() {
        binding.fabAdd.setOnClickListener {
            openCreateCharacter()
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchCharactersFromApi()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun showCharacterDetails(character: Character) {
        val intent = Intent(this, CharacterDetailActivity::class.java)
        intent.putExtra("character_id", character.id)
        startActivity(intent)
    }

    private fun openCreateCharacter() {
        val intent = Intent(this, CreateEditCharacterActivity::class.java)
        startActivity(intent)
    }

    private fun openEditCharacter(character: Character) {
        val intent = Intent(this, CreateEditCharacterActivity::class.java)
        intent.putExtra("character_id", character.id)
        startActivity(intent)
    }

    private fun showDeleteConfirmation(character: Character) {
        AlertDialog.Builder(this)
            .setTitle("Delete Character")
            .setMessage("Are you sure you want to delete ${character.name}?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteCharacter(character)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.characters.observe(this@MainActivity) { characters ->
                        adapter.submitList(characters)
                    }
                } else {
                    viewModel.searchCharacters(newText).observe(this@MainActivity) { characters ->
                        adapter.submitList(characters)
                    }
                }
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                viewModel.fetchCharactersFromApi()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
