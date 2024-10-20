package com.example.test_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_project.adapters.TodoAdapter
import com.example.test_project.database.TodoDatabase
import com.example.test_project.database.entities.Todo
import com.example.test_project.database.repositories.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProvider(this)[MainActivityData::class.java]
        val repository = TodoRepository(TodoDatabase.getInstance(this))
        val rvTodoList: RecyclerView = findViewById(R.id.rvTodoList)

        viewModel.data.observe(this) { todoList ->
            adapter = TodoAdapter(todoList, repository, viewModel)
            rvTodoList.adapter = adapter
            rvTodoList.layoutManager = LinearLayoutManager(this)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val data = repository.getAllTodoItems()
            runOnUiThread {
                viewModel.setData(data)
            }
        }

        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    adapter.filter(it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    adapter.filter(it)
                }
                return true
            }
        })

        val btnAddItem: Button = findViewById(R.id.btnAddItem)
        btnAddItem.setOnClickListener {
            displayDialog(repository, viewModel)
        }

        val btnTimer: Button = findViewById(R.id.btnTimer)
        btnTimer.setOnClickListener {
            val intent = Intent(this, ActivityTimer::class.java)
            startActivity(intent)
        }

        val btnNotify: Button = findViewById(R.id.btnNotify)
        btnNotify.setOnClickListener {
            val intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayDialog(repository: TodoRepository, viewModel: MainActivityData) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Item")
        builder.setMessage("Insert the todo")

        val input = EditText(this)
        input.inputType = android.text.InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            val item = input.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                repository.insert(Todo(item))
                val data = repository.getAllTodoItems()
                runOnUiThread {
                    viewModel.setData(data)
                }
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }
}
