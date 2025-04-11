package com.nomnomapp.nomnom.ui.navigation

enum class Routes(val route: String) {
    HOME("home"),
    RECIPES("recipes"),
    SETTINGS("settings"),
    SHOPPING_LIST("shoppingList"),
    MEAL_DETAIL("mealDetail/{mealId}"),
    CREATE_USER("createUser"),
    ADD_RECIPE("addRecipe"),
    EDIT_RECIPE("editRecipe/{id}");

    companion object {
        fun mealDetailRoute(mealId: String) = "mealDetail/$mealId"
        fun editRecipeRoute(id: Int) = "editRecipe/$id"
    }
}
