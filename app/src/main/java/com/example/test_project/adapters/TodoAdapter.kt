package com.example.test_project.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.test_project.MainActivityData
import com.example.test_project.R
import com.example.test_project.database.entities.Todo
import com.example.test_project.database.repositories.TodoRepository
import com.example.test_project.viewHolders.ToDoViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoAdapter(items:List<Todo>,
                  repository: TodoRepository,
                  viewModel: MainActivityData):RecyclerView.Adapter<ToDoViewHolder>() {

                      var context: Context? = null

    val items = items
    val repository = repository
    val viewModel = viewModel
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item,parent,false)

        return ToDoViewHolder(view)

    }

    override fun getItemCount(): Int {

        return items.size

    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {

        val todoItem = items[position]

        holder.cbTodo.text = items.get(position).item

        holder.ivEdit.setOnClickListener {
            showEditDialog(todoItem)
        }

        holder.ivDelete.setOnClickListener {
            val isChecked = holder.cbTodo.isChecked

            if (isChecked)
            {
                CoroutineScope(Dispatchers.IO).launch {
                    repository.delete(items.get(position))

                    val data = repository.getAllTodoItems()
                    withContext(Dispatchers.Main){
                        viewModel.setData(data)
                }

                }
                Toast.makeText(context,"Item deleted",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context,"Select the item to delete",Toast.LENGTH_LONG).show()
            }
        }

        

    }

    private fun showEditDialog(todoItem: Todo) {

        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Edit Todo Item")

        val input = EditText(context)
        input.setText(todoItem.item)
        builder.setView(input)

        builder.setPositiveButton("Update") { _, _ ->
            todoItem.item = input.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                repository.update(todoItem)  // Update the todo item
                val data = repository.getAllTodoItems()
                withContext(Dispatchers.Main) {
                    viewModel.setData(data)
                }
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()

    }
}