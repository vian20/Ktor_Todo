package com.ktortodo.repository

import com.ktortodo.entities.ToDo
import com.ktortodo.entities.ToDoDraft

class InMemoryToDoRepository:ToDoRepository {
    private val todos= mutableListOf<ToDo>(
    )
    override fun getAll(): List<ToDo> {
        return todos
    }

    override fun getId(id: Int): ToDo? {
        return todos.firstOrNull { it.id==id }
    }

    override fun addTodo(draft: ToDoDraft): ToDo {
        val todo=ToDo(id=todos.size+1,title=draft.title,status=draft.status)
        todos.add(todo)
        return todo
    }

    override fun remove(id: Int): Boolean {
        return todos.removeIf{ it.id==id}
    }

    override fun update(id: Int, draft: ToDoDraft): Boolean {
        val todo=todos.firstOrNull { it.id==id }?:return false
        todo.title=draft.title
        todo.status=draft.status
        return true
    }


}