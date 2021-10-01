package com.firstproject.favdish.model.network

import com.firstproject.favdish.model.entities.RandomDish
import com.firstproject.favdish.utils.*
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RandomDishApiService {

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create<RandomDishAPI>()

    fun getRandomDish(): Single<RandomDish.Recipes> {
        return api.getRandomDishes(
            API_KEY_VALUE,
            LIMIT_LICENSE_VALUE,
            TAGS_VALUE,
            NUMBER_VALUE
        )
    }
}