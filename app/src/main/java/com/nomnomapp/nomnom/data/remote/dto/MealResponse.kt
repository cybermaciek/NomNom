package com.nomnomapp.nomnom.data.remote.dto

import com.squareup.moshi.Json

data class MealResponse(
    @Json(name = "meals") val meals: List<MealDto>?
)

data class MealDto(
    @Json(name = "idMeal") val id: String,
    @Json(name = "strMeal") val title: String,
    @Json(name = "strMealThumb") val imageUrl: String,
    @Json(name = "strCategory") val category: String,
    @Json(name = "strArea") val area: String,
    @Json(name = "strInstructions") val instructions: String,
    // skladniki są rozdzielone - można później parsować do listy
    @Json(name = "strIngredient1") val ingredient1: String?,
    @Json(name = "strIngredient2") val ingredient2: String?,
    @Json(name = "strIngredient3") val ingredient3: String?,
    @Json(name = "strIngredient4") val ingredient4: String?,
    @Json(name = "strIngredient5") val ingredient5: String?,
    @Json(name = "strIngredient6") val ingredient6: String?,
    @Json(name = "strIngredient7") val ingredient7: String?,
    @Json(name = "strIngredient8") val ingredient8: String?,
    @Json(name = "strIngredient9") val ingredient9: String?,
    @Json(name = "strIngredient10") val ingredient10: String?,
    // moża dodać potem więcej składników jeśli będą potrzebne
)
