package com.firstproject.favdish.model

import android.os.Parcelable
import com.firstproject.favdish.utils.FieldType
import kotlinx.parcelize.Parcelize

@Parcelize
data class DialogCustomListModel (
    val title: String,
    val itemList: List<String>,
    val fieldType: FieldType
) : Parcelable