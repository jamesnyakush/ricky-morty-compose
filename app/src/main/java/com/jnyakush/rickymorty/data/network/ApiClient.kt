package com.jnyakush.rickymorty.data.network

import com.jnyakush.rickymorty.data.model.Character
import com.jnyakush.rickymorty.data.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClient {

    @GET("api/character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): CharacterResponse

    @GET("api/character/{id}")
    suspend fun getCharacterByID(
        @Path("id") id:Int
    ) : Character

}