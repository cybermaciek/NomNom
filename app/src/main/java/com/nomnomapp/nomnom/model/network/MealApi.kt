package com.nomnomapp.nomnom.model.network

import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("search.php")
    suspend fun searchMealsByName(@Query("s") query: String): MealResponse

    @GET("random.php")
    suspend fun getRandomMeal(): MealResponse
}