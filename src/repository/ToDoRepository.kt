package com.ktortodo.repository

import com.ktortodo.entities.ToDo
import com.ktortodo.entities.ToDoDraft

interface ToDoRepository {
    fun getAll():List<ToDo>
    fun getId(id:Int):ToDo?
    fun addTodo(draft: ToDoDraft):ToDo
    fun remove(id:Int):Boolean
    fun update(id:Int,draft:ToDoDraft):Boolean
}