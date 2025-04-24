package com.jnyakush.rickymorty.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jnyakush.rickymorty.data.model.Character
import com.jnyakush.rickymorty.data.network.ApiClient
import com.jnyakush.rickymorty.data.network.CharacterPagingSource
import com.jnyakush.rickymorty.domain.repository.CharacterRepository
import com.jnyakush.rickymorty.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class CharacterRepositoryImpl(
    private val apiClient: ApiClient
) : CharacterRepository {

    override fun getPagedCharacters(
        name: String?,
        status: String?,
        species: String?,
        gender: String?
    ): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                CharacterPagingSource(
                    apiClient,
                    name = name,
                    status = status,
                    species = species,
                    gender = gender
                )
            }
        ).flow
    }

    override suspend fun getCharacterByID(id: Int): Flow<Response<Character>> = flow {
        try {
            emit(Response.Loading)
            val result = apiClient.getCharacterByID(id)
            emit(Response.Success(result))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }
}