package com.taskmanager.taskmanagement.ui.util

import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar
import com.ujumbetech.archtask.Event

fun ProgressBar.showProgress(show: Boolean){
    this.visibility = if (show) View.VISIBLE else View.GONE
}

fun View.showSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<Int>>,
    timeLength: Int
){
    snackbarEvent.observe(lifecycleOwner){ event ->
        event.getContentIfNotHandled()?.let { message ->
            Snackbar.make(this, message, timeLength).show()
        }
    }
}