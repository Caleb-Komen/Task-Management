package com.taskmanager.taskmanagement.ui.projectdetails

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.taskmanager.taskmanagement.databinding.ListItemAddNewBinding

class NewTaskListViewHolder(
    private val binding: ListItemAddNewBinding,
    private val showDialog: () -> Unit
): RecyclerView.ViewHolder(binding.root) {

    init {
        setCardMargins()
        binding.setClickListener {
            showDialog.invoke()
        }
    }

    // override the margin set to the MaterialCardView
    private fun setCardMargins(){
        val view = binding.root
        val lp = RelativeLayout.LayoutParams(view.layoutParams)
        lp.width = getValueInDp(view, 250f)
        lp.topMargin = getValueInDp(view, 8f)
        lp.marginEnd = getValueInDp(view, 24f)
        view.layoutParams = lp
    }

    private fun getValueInDp(view: View, value: Float): Int{
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            view.resources.displayMetrics
        ).toInt()
    }

    companion object{
        fun create(parent: ViewGroup, showDialog: () -> Unit): NewTaskListViewHolder{
            return NewTaskListViewHolder(
                ListItemAddNewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                showDialog
            )
        }
    }
}