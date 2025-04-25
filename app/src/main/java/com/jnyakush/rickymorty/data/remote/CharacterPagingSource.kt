package com.jnyakush.rickymorty.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jnyakush.rickymorty.data.model.Character


class CharacterPagingSource(
    private val apiClient: ApiClient,
    private val name: String? = null,
    private val status: String? = null,
    private val species: String? = null,
    private val gender: String? = null
) : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val page = params.key ?: 1
            val response = apiClient.getCharacters(
                page = page,
                name = name,
                status = status,
                species = species,
                gender = gender
            )

            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.info.next == null) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }
}