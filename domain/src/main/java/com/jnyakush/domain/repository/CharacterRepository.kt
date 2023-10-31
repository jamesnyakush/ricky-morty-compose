package com.jnyakush.domain.repository


import com.jnyakush.core.Response
import com.jnyakush.domain.model.CharacterResponse
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    // Getting all Characters
    suspend fun getCharacters(): Flow<Response<CharacterResponse>>

    // Getting Character by id eg https://rickandmortyapi.com/api/character/2
    suspend fun getCharacterByID(id: Int): Flow<Response<Character>>
}