package com.firstproject.favdish.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddUpdateViewModel : ViewModel() {

    // TODO: 21.09.2021 change to string
    private val _imageUri = MutableLiveData<Uri>()
    val imageUri: LiveData<Uri> = _imageUri

    private val _type = MutableLiveData<String>()
    val type: LiveData<String> = _type

    private val _category = MutableLiveData<String>()
    val category: LiveData<String> = _category

    private val _cookingTime = MutableLiveData<String>()
    val cookingTime: LiveData<String> = _cookingTime

    fun setImageUri(uri: Uri) {
        _imageUri.value = uri
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
}