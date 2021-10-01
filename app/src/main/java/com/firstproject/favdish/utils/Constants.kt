package com.firstproject.favdish.utils

const val TAG = "ups"
const val IMAGE_DIRECTORY = "FavDishImages"
const val IMAGE_FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

const val DISH_IMAGE_SOURCE_LOCAL: String = "Local"
const val DISH_IMAGE_SOURCE_ONLINE: String = "Online"

const val SPLASH_ANIMATION_DURATION = 100L
const val MAIN_ACTIVITY_DELAY = 100L
const val TAKE_PHOTO_DELAY = 800L

const val ALL_ITEMS: String = "All"
const val FILTER_SELECTION: String = "FilterSelection"

const val BASE_URL = "https://api.spoonacular.com/"
const val API_ENDPOINT = "recipes/random"
const val API_KEY: String = "apiKey"
const val LIMIT_LICENSE: String = "limitLicense"
const val TAGS: String = "tags"
const val NUMBER: String = "number"
const val LIMIT_LICENSE_VALUE: Boolean = true
const val TAGS_VALUE: String = "vegetarian, dessert"
const val NUMBER_VALUE: Int = 1

enum class FieldType{
    DISH_TYPE,
    DISH_CATEGORY,
    DISH_COOKING_TIME
}