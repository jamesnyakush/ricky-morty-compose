package com.jnyakush.data.network

import com.jnyakush.domain.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiClient {

    @GET("api/character")
    suspend fun getCharacters(): CharacterResponse

    @GET("api/character/{id}")
    suspend fun getCharacterByID(
        @Path("id") id:Int
    ) : Character

}