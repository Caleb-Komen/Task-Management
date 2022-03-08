package com.taskmanager.taskmanagement.ui.util

import android.view.View
import android.widget.ProgressBar

fun ProgressBar.showProgress(show: Boolean){
    this.visibility = if (show) View.VISIBLE else View.GONE
}