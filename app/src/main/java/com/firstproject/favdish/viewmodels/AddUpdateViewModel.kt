package com.firstproject.favdish.viewmodels

import android.text.TextUtils
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddUpdateViewModel : ViewModel() {

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
}