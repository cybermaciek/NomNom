package com.nomnomapp.nomnom.data.remote.dto

import com.squareup.moshi.Json

data class CategoryResponse(
    @Json(name = "meals") val categories: List<CategoryDto>
)

data class CategoryDto(
    @Json(name = "strCategory") val name: String
)

data class AreaResponse(
    @Json(name = "meals") val areas: List<AreaDto>
)

data class AreaDto(
    @Json(name = "strArea") val name: String
)
