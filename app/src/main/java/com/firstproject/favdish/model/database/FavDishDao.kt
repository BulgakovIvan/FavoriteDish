package com.firstproject.favdish.model.database

import androidx.room.Dao
import androidx.room.Insert
import com.firstproject.favdish.model.entities.FavDish

@Dao
interface FavDishDao {

    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)
}