package com.netlsd.jetpackwallet.model

import androidx.annotation.DrawableRes

data class Recipe(
        @DrawableRes val imageResource: Int, var title: String, var stringList: List<String>)