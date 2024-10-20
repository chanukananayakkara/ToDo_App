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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoAdapter(
    items: List<Todo>,
    private val repository: TodoRepository,
    private val viewModel: MainActivityData
) : RecyclerView.Adapter<ToDoViewHolder>() {

    private var context: Context? = null
    private var items: List<Todo> = items
    private var itemsFull: List<Todo> = ArrayList(items) // Create a copy of the full list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item, parent, false)
        return ToDoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val todoItem = items[position]

        holder.cbTodo.text = todoItem.item

        holder.ivEdit.setOnClickListener {
            showEditDialog(todoItem)
        }

        holder.ivDelete.setOnClickListener {
            val isChecked = holder.cbTodo.isChecked

            if (isChecked) {
                CoroutineScope(Dispatchers.IO).launch {
                    repository.delete(todoItem)

                    val data = repository.getAllTodoItems()
                    withContext(Dispatchers.Main) {
                        viewModel.setData(data)
                    }
                }
                Toast.makeText(context, "Item deleted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Select the item to delete", Toast.LENGTH_LONG).show()
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

    // Method to filter the list based on search query
    fun filter(query: String) {
        items = if (query.isEmpty()) {
            itemsFull
        } else {
            itemsFull.filter {
                it.item?.contains(query, ignoreCase = true) == true
            }
        }
        notifyDataSetChanged()
    }

}
