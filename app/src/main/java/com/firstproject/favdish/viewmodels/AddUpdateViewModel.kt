package com.firstproject.favdish.viewmodels

import androidx.lifecycle.*
import com.firstproject.favdish.model.database.FavDishRepository
import com.firstproject.favdish.model.entities.FavDish
import kotlinx.coroutines.launch

class AddUpdateViewModel(private val repository: FavDishRepository) : ViewModel() {

    private val _imagePath = MutableLiveData<String>()
    val imagePath: LiveData<String> = _imagePath

    private val _type = MutableLiveData<String>()
    val type: LiveData<String> = _type

    private val _category = MutableLiveData<String>()
    val category: LiveData<String> = _category

    private val _cookingTime = MutableLiveData<String>()
    val cookingTime: LiveData<String> = _cookingTime

    var title = MutableLiveData<String>()
    var ingredients = MutableLiveData<String>()
    var instruction = MutableLiveData<String>()

    fun setImagePath(path: String) {
        _imagePath.value = path
    }

    fun setType(type: String) {
        _type.value = type
    }

    fun setCategory(category: String) {
        _category.value = category
    }

    fun setCookingTime(cookingTime: String) {
        _cookingTime.value = cookingTime
    }

    fun trimValues() {
        title.value = title.value?.trim()
        ingredients.value = ingredients.value?.trim()
        instruction.value = instruction.value?.trim()
    }

    fun insert(dish: FavDish) = viewModelScope.launch {
        repository.insertFavDishData(dish)
    }
}

class AddUpdateViewModelFactory(private val repository: FavDishRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddUpdateViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            return AddUpdateViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}