package com.ktortodo

import com.ktortodo.entities.ToDoDraft
import com.ktortodo.repository.InMemoryToDoRepository
import com.ktortodo.repository.ToDoRepository
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CallLogging)
    install(ContentNegotiation){
        gson{
            setPrettyPrinting()
        }
    }
    routing{
        val repository: ToDoRepository =InMemoryToDoRepository()
        //val repository:ToDoRepository=MySQLTodoRepository()
        get("/"){
            call.respondText("Welcome to Todo List!")
        }
        get("/todos"){
            call.respond(repository.getAll())
        }
        get("/todos/{id}"){
            val id =call.parameters["id"]?.toIntOrNull()
            if(id==null){
                call.respond(HttpStatusCode.BadRequest,"Id is null")
                return@get
            }
            val todo=repository.getId(id)
            if(todo==null){
                call.respond(HttpStatusCode.NotFound,"ToDo not found for $id")
            }
            else{
                call.respond(todo)
            }
        }
        post("/todos"){
            val todoDraft=call.receive<ToDoDraft>()
            val todo=repository.addTodo(todoDraft)
            call.respond(todo)
        }
        put("/todos/{id}"){
            val todoDraft = call.receive<ToDoDraft>()
            val todoId=call.parameters["id"]?.toIntOrNull()
            if(todoId==null){
                call.respond(HttpStatusCode.BadRequest,"The Id is Null")
                return@put
            }
            val updated=repository.update(todoId,todoDraft)
            if(updated){
                call.respond(HttpStatusCode.OK)
            }else{
                call.respond(HttpStatusCode.NotFound,"There is no such draft available with $todoId")
            }
        }
        delete("todos/{id}"){
            val todoId=call.parameters["id"]?.toIntOrNull()
            if(todoId==null){
                call.respond(HttpStatusCode.BadRequest,"The Id is Null")
                return@delete
            }
            val removed=repository.remove(todoId)
            if(removed){
                call.respond(HttpStatusCode.OK)
            }else{
                call.respond(HttpStatusCode.NotFound,"There is no such Todo to be removed with id $todoId")
            }
        }
    }
}

