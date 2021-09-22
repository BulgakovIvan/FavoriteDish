package com.firstproject.favdish.viewmodels

import androidx.lifecycle.*
import com.firstproject.favdish.model.database.FavDishRepository
import com.firstproject.favdish.model.entities.FavDish

class AllDishesViewModel(repository: FavDishRepository) : ViewModel() {

    val allDishesList: LiveData<List<FavDish>> = repository.allDishesList.asLiveData()
}

class AllDishesViewModelFactory(private val repository: FavDishRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllDishesViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            return AllDishesViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}