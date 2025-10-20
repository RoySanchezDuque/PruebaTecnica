package com.rickandmorty.app.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rickandmorty.app.R
import com.rickandmorty.app.databinding.ItemCharacterBinding
import com.rickandmorty.app.domain.model.Character

class CharacterAdapter(
    private val onItemClick: (Character) -> Unit,
    private val onEditClick: (Character) -> Unit,
    private val onDeleteClick: (Character) -> Unit
) : ListAdapter<Character, CharacterAdapter.CharacterViewHolder>(CharacterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ItemCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CharacterViewHolder(
        private val binding: ItemCharacterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character) {
            binding.apply {
                tvName.text = character.name
                tvSpecies.text = "Species: ${character.species}"
                tvStatus.text = "Status: ${character.status}"
                tvGender.text = "Gender: ${character.gender}"

                Glide.with(itemView.context)
                    .load(character.image)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(ivCharacter)

                root.setOnClickListener { onItemClick(character) }
                btnEdit.setOnClickListener { onEditClick(character) }
                btnDelete.setOnClickListener { onDeleteClick(character) }
            }
        }
    }

    class CharacterDiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }
}
