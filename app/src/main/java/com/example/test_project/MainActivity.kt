package com.example.test_project

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.AndroidViewModel
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProvider(this)[MainActivityData::class.java]
        val repository = TodoRepository(TodoDatabase.getInstance(this))




        val rvTodoList:RecyclerView = findViewById(R.id.rvTodoList)

        viewModel.data.observe(this){
            val adapter = TodoAdapter(it, repository,viewModel)
            rvTodoList.adapter = adapter
            rvTodoList.layoutManager = LinearLayoutManager(this)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val data = repository.getAllTodoItems()

            runOnUiThread {
                viewModel.setData(data)
            }
        }

        val btnAddItem:Button = findViewById(R.id.btnAddItem)

        btnAddItem.setOnClickListener {
            displayDialog(repository,viewModel)
        }

        val btnTimer: Button = findViewById(R.id.btnTimer)
        btnTimer.setOnClickListener {
            val intent = Intent(this, ActivityTimer::class.java)
            startActivity(intent)
        }

        val btnNotify: Button = findViewById(R.id.btnNotify)
        btnNotify.setOnClickListener {
            val intent = Intent(this,NotificationsActivity::class.java)
            startActivity(intent)
        }




//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }



    }

    fun displayDialog(repository: TodoRepository, viewModel: MainActivityData){
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Add Item")
        builder.setMessage("Insert the todo")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK"){dialog,which ->
            val item = input.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                repository.insert(Todo(item))
                val data = repository.getAllTodoItems()

                runOnUiThread {
                    viewModel.setData(data)

                }
            }
        }

        builder.setNegativeButton("Cancel"){dialog,which ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}