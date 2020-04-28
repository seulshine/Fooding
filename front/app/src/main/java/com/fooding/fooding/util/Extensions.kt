package com.fooding.fooding.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(layoutId: Int, attachRoot: Boolean = false) : View = LayoutInflater.from(context).inflate(layoutId, this, false)