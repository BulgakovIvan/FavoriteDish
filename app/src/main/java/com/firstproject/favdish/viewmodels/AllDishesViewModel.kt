package com.firstproject.favdish.viewmodels

import androidx.lifecycle.*
import com.firstproject.favdish.model.database.FavDishRepository
import com.firstproject.favdish.model.entities.FavDish
import kotlinx.coroutines.launch

class AllDishesViewModel(private val repository: FavDishRepository) : ViewModel() {

    val allDishesList: LiveData<List<FavDish>> = repository.allDishesList.asLiveData()

    fun update(dish: FavDish) = viewModelScope.launch {
        repository.updateFavDishData(dish)
    }

    val favoriteDishes: LiveData<List<FavDish>> = repository.favoriteDishes.asLiveData()

    fun delete(dish: FavDish) = viewModelScope.launch {
        repository.deleteFavDishData(dish)
    }
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