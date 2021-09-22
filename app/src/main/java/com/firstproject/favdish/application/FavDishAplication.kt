package com.firstproject.favdish.application

import android.app.Application
import com.firstproject.favdish.model.database.FavDishRepository
import com.firstproject.favdish.model.database.FavDishRoomDatabase

class FavDishApplication : Application() {

    private val database by lazy { FavDishRoomDatabase.getDatabase(this) }
    val repository by lazy { FavDishRepository(database.FavDishDao()) }

}