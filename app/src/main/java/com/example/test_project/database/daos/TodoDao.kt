package com.example.test_project.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.test_project.database.entities.Todo


@Dao
interface TodoDao {

    @Insert
    suspend fun insertTodo(todo:Todo)

    @Update
    suspend fun updateTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo:Todo)

    @Query("SELECT * from Todo")
    fun getAllTodoItems():List<Todo>
}