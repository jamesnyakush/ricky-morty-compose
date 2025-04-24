package com.jnyakush.rickymorty.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jnyakush.rickymorty.domain.repository.CharacterRepository
import com.jnyakush.rickymorty.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.jnyakush.rickymorty.data.model.Character
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest


class CharacterViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val filters = MutableStateFlow(
        CharacterFilter(name = null, status = null, species = null, gender = null)
    )

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
        filters.value = CharacterFilter(name, status, species, gender)
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