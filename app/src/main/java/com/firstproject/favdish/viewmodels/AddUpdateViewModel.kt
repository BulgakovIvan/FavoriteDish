package com.firstproject.favdish.viewmodels

import androidx.lifecycle.*
import com.firstproject.favdish.model.database.FavDishRepository
import com.firstproject.favdish.model.entities.FavDish
import com.firstproject.favdish.utils.DISH_IMAGE_SOURCE_LOCAL
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

    private var id = 0

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

    fun insert() {
        val favDishDetails = FavDish(
            imagePath.value!!,
            DISH_IMAGE_SOURCE_LOCAL,
            title.value!!,
            type.value!!,
            category.value!!,
            ingredients.value!!,
            cookingTime.value!!,
            instruction.value!!,
            false
        )

        viewModelScope.launch {
            repository.insertFavDishData(favDishDetails)
        }

        cleanViewModel()
    }

    fun insert(favDishDetails: FavDish) {
        viewModelScope.launch {
            repository.insertFavDishData(favDishDetails)
        }
    }

    fun update() {
        if (id != 0) {
            val favDishDetails = FavDish(
                imagePath.value!!,
                DISH_IMAGE_SOURCE_LOCAL,
                title.value!!,
                type.value!!,
                category.value!!,
                ingredients.value!!,
                cookingTime.value!!,
                instruction.value!!,
                false,
                id = id
            )

            viewModelScope.launch {
                repository.updateFavDishData(favDishDetails)
            }

            cleanViewModel()
        }
    }

    private fun cleanViewModel() {
        setImagePath("")
        setType("")
        setCategory("")
        setCookingTime("")

        title.value = ""
        ingredients.value = ""
        instruction.value = ""
    }

    fun loadDishDetails(dish: FavDish) {
        setImagePath(dish.image)
        setType(dish.type)
        setCategory(dish.category)
        setCookingTime(dish.cookingTime)

        id = dish.id
        title.value = dish.title
        ingredients.value = dish.ingredients
        instruction.value = dish.directionToCook
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