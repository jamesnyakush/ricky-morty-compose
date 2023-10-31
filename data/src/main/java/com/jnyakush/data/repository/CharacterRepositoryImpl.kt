package com.jnyakush.data.repository

import com.jnyakush.core.Response
import com.jnyakush.data.network.ApiClient
import com.jnyakush.domain.model.CharacterResponse
import com.jnyakush.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CharacterRepositoryImpl constructor(
    private var apiClient: ApiClient,
) : CharacterRepository {

    override suspend fun getCharacters(): Flow<Response<CharacterResponse>> = flow {
        try {
            emit(Response.Loading)
            val result = apiClient.getCharacters()
            emit(Response.Success(result))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun getCharacterByID(id: Int): Flow<Response<Character>> = flow {
        try {
            emit(Response.Loading)
            val result = apiClient.getCharacterByID(id = id)
            emit(Response.Success(result))
        } catch (e:Exception) {
            emit(Response.Failure(e))
        }
    }
}