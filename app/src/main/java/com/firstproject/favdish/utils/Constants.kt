package com.firstproject.favdish.utils

const val TAG = "ups"
const val IMAGE_DIRECTORY = "FavDishImages"
const val IMAGE_FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

const val DISH_IMAGE_SOURCE_LOCAL: String = "Local"
const val DISH_IMAGE_SOURCE_ONLINE: String = "Online"

const val SPLASH_ANIMATION_DURATION = 100L
const val MAIN_ACTIVITY_DELAY = 100L
const val TAKE_PHOTO_DELAY = 800L

enum class FieldType{
    DISH_TYPE,
    DISH_CATEGORY,
    DISH_COOKING_TIME
}