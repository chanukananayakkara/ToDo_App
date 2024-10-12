package com.example.test_project.viewHolders

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.test_project.R

class ToDoViewHolder(view:View):RecyclerView.ViewHolder(view) {

    val cbTodo:CheckBox
    val ivDelete:ImageView
    val ivEdit:ImageView

    init {
        cbTodo = view.findViewById(R.id.cbTodo)
        ivDelete = view.findViewById(R.id.ivDelete)
        ivEdit = view.findViewById(R.id.ivEdit)

    }
}