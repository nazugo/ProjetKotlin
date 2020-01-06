package yildiz.oguzhan.td5

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TasksViewModel : ViewModel() {
    val tasks = MutableLiveData<List<Task>?>()
    private val repository = TasksRepository()

    fun loadTasks() {
        viewModelScope.launch {
            val result = repository.loadTasks()
            tasks.postValue(result)
        }
    }

    fun editTask(task: Task) {
        viewModelScope.launch {
            val newTask = repository.updateTask(task)
            if (newTask != null) {
                val newList = tasks.value.orEmpty().toMutableList()
                val position = newList.indexOfFirst { it.id == newTask.id }
                newList[position] = newTask
                tasks.postValue(newList)
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            val result = repository.deleteTask(task.id)
            if (result) {
                val newList = tasks.value.orEmpty().toMutableList()
                newList.remove(task)
                tasks.postValue(newList)
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            val result = repository.addTask(task)
            if (result != null) {
                val newList = tasks.value.orEmpty().toMutableList()
                newList.add(task)
                tasks.postValue(newList)
            }
        }
    }
}