package com.nomnomapp.nomnom.data.remote

import com.nomnomapp.nomnom.data.remote.dto.AreaResponse
import com.nomnomapp.nomnom.data.remote.dto.CategoryResponse
import com.nomnomapp.nomnom.data.remote.dto.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("search.php")
    suspend fun searchMealsByName(@Query("s") query: String): MealResponse

    @GET("lookup.php")
    suspend fun lookupMealById(@Query("i") mealId: String): MealResponse

    @GET("list.php?c=list")
    suspend fun getCategories(): CategoryResponse

    @GET("list.php?a=list")
    suspend fun getAreas(): AreaResponse

}