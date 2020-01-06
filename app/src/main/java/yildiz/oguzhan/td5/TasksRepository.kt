package yildiz.oguzhan.td5

import yildiz.oguzhan.td5.network.Api

class TasksRepository {
    private val tasksService = Api.tasksService


    suspend fun loadTasks(): List<Task>? {
        val tasksResponse = tasksService.getTasks()
        return if (tasksResponse.isSuccessful) tasksResponse.body() else null
    }

    suspend fun addTask(task: Task): Task? {
        val tasksResponse = tasksService.createTask(task)
        return if (tasksResponse.isSuccessful) tasksResponse.body() else null
    }


    suspend fun deleteTask(id: String): Boolean {
        val response = tasksService.deleteTask(id)
        return response.isSuccessful
    }

    suspend fun updateTask(task: Task): Task? {
        val response = tasksService.updateTask(task)
        return if (response.isSuccessful) response.body() else null

    }
}