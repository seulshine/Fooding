package com.example.food.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

fun ViewGroup.inflate(layoutId: Int, attachRoot: Boolean = false) : View = LayoutInflater.from(context).inflate(layoutId, this, false)