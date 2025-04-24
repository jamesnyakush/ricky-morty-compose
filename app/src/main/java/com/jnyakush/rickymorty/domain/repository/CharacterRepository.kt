package com.jnyakush.rickymorty.domain.repository

import androidx.paging.PagingData
import com.jnyakush.rickymorty.data.model.Character
import com.jnyakush.rickymorty.util.Response
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    // Getting all Characters
    fun getCharactersPaged(): Flow<PagingData<Character>>

    // Getting Character by id eg https://rickandmortyapi.com/api/character/2
    suspend fun getCharacterByID(id: Int): Flow<Response<Character>>
}