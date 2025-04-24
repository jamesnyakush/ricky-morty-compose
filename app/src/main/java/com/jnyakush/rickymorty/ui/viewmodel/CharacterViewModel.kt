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


class CharacterViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    val characters: Flow<PagingData<Character>> =
        characterRepository.getCharactersPaged().cachedIn(viewModelScope)

    private val _characterById = MutableStateFlow<Response<Character>>(Response.Loading)
    val characterById: StateFlow<Response<Character>> = _characterById.asStateFlow()

    fun getCharacterById(id: Int) = viewModelScope.launch {
        characterRepository.getCharacterByID(id).collect {
            _characterById.value = it
        }
    }
}