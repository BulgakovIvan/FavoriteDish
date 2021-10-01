package com.firstproject.favdish.model.network

import com.firstproject.favdish.model.entities.RandomDish
import com.firstproject.favdish.utils.*
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomDishAPI {
    @GET(API_ENDPOINT)
    fun getDishes(
        @Query(API_KEY) apiKey: String,
        @Query(LIMIT_LICENSE) limitLicense: Boolean,
        @Query(TAGS) tags: String,
        @Query(NUMBER) number: Int
    ): Single<RandomDish.Recipes>
}