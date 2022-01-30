package com.taskmanager.taskmanagement.domain.util

import androidx.annotation.ColorRes
import com.taskmanager.taskmanagement.R

enum class Label(@ColorRes colour: Int) {
    NONE(R.color.white),
    FEATURE(R.color.green),
    BUG(R.color.red),
    IMPORTANT(R.color.blue),
    IDEA(R.color.yellow)
}