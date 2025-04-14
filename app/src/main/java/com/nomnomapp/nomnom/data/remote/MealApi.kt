package com.nomnomapp.nomnom.data.remote

import com.nomnomapp.nomnom.data.remote.dto.AreaResponse
import com.nomnomapp.nomnom.data.remote.dto.CategoryResponse
import com.nomnomapp.nomnom.data.remote.dto.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    // przepis po nazwie (np. "chicken") – GET /search.php?w=chicken
    @GET("search.php")
    suspend fun searchMealsByName(@Query("s") query: String): MealResponse

    // przepis po ID – GET /lookup.php?i=52772
    @GET("lookup.php")
    suspend fun lookupMealById(@Query("i") mealId: String): MealResponse

    // kategoria – GET /list.php?c=list
    @GET("list.php?c=list")
    suspend fun getCategories(): CategoryResponse

    // region – GET /list.php?a=list
    @GET("list.php?a=list")
    suspend fun getAreas(): AreaResponse
}
