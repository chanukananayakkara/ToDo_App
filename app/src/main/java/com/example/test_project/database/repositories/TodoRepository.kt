package com.example.test_project.database.repositories

import com.example.test_project.database.TodoDatabase
import com.example.test_project.database.entities.Todo

class TodoRepository (

   private val db:TodoDatabase /*passing the database instance*/
){

   suspend fun insert(todo:Todo) = db.getTodoDao().insertTodo(todo)
   suspend fun delete(todo:Todo) = db.getTodoDao().deleteTodo(todo)
   suspend fun update(todo: Todo) = db.getTodoDao().updateTodo(todo)
   fun getAllTodoItems():List<Todo> = db.getTodoDao().getAllTodoItems()

}