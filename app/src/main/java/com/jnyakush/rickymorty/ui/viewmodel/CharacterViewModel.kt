package com.jnyakush.rickymorty.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jnyakush.rickymorty.data.model.CharacterResponse
import com.jnyakush.rickymorty.domain.repository.CharacterRepository
import com.jnyakush.rickymorty.util.Response
import kotlinx.coroutines.launch

class CharacterViewModel constructor(
    private val characterRepository: CharacterRepository,
) : ViewModel() {

    private val _characterResponse = mutableStateOf<Response<CharacterResponse>>(Response.Success(null))
    val characterResponse: State<Response<CharacterResponse>> = _characterResponse

    fun getCharacters()  = viewModelScope.launch {
        characterRepository.getCharacters().collect{ response ->
            _characterResponse.value = response
        }
    }
}