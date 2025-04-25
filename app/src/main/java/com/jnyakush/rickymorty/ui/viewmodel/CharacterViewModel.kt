package com.jnyakush.rickymorty.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.jnyakush.rickymorty.data.model.Character
import com.jnyakush.rickymorty.domain.repository.CharacterRepository
import com.jnyakush.rickymorty.util.Response
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch


class CharacterViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel() {


    private val _filters = MutableStateFlow(
        CharacterFilter(name = null, status = null, species = null, gender = null)
    )

    val filters: StateFlow<CharacterFilter> = _filters.asStateFlow()

    private val _characterById = MutableStateFlow<Response<Character>>(Response.Loading)
    val characterById: StateFlow<Response<Character>> = _characterById.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val characters = filters
        .flatMapLatest { filter ->
            characterRepository.getPagedCharacters(
                name = filter.name,
                status = filter.status,
                species = filter.species,
                gender = filter.gender
            )
        }
        .cachedIn(viewModelScope)

    fun searchCharacters(
        name: String? = null,
        status: String? = null,
        species: String? = null,
        gender: String? = null
    ) {
        _filters.value = CharacterFilter(name, status, species, gender)
    }

    fun searchCharactersFromInput(query: String) {
        val parts = query.lowercase().split(" ")
        var name: String? = null
        var status: String? = null
        var species: String? = null
        var gender: String? = null

        for (part in parts) {
            when {
                part.startsWith("s:") -> status = part.removePrefix("s:")
                part.startsWith("g:") -> gender = part.removePrefix("g:")
                part.startsWith("sp:") -> species = part.removePrefix("sp:")
                else -> name = (name ?: "") + if (name != null) " $part" else part
            }
        }

        _filters.value = CharacterFilter(name, status, species, gender)
    }

    fun getCharacterById(id: Int) = viewModelScope.launch {
        characterRepository.getCharacterByID(id).collect {
            _characterById.value = it
        }
    }
}

data class CharacterFilter(
    val name: String?,
    val status: String?,
    val species: String?,
    val gender: String?
)