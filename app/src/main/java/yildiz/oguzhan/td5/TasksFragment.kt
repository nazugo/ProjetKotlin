package yildiz.oguzhan.td5

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import yildiz.oguzhan.td5.network.Api
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class TasksFragment : Fragment(R.layout.fragment_tasks) {


    private val taskViewModel by lazy {
        ViewModelProvider(this).get(TasksViewModel::class.java)

    }

    private val coroutineScope = MainScope()


    private val adapter = TasksAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tasks_recycler_view.adapter = adapter
        tasks_recycler_view.layoutManager = LinearLayoutManager(activity)
        adapter.onDeleteClickListener = { task ->
            taskViewModel.deleteTask(task)
        }

        adapter.onEditClickListener = { task ->
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra(TASK_KEY, task)
            startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)
        }

        floatingActionButton.setOnClickListener {

            val intent = Intent(activity, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)

        }


        taskViewModel.tasks.observe(this, Observer { newList ->
            adapter.tasks = newList.orEmpty()
            adapter.notifyDataSetChanged()

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val task = data!!.getSerializableExtra("TASK") as Task
            taskViewModel.addTask(task)
        }
    }

    override fun onResume() {
        super.onResume()
        taskViewModel.loadTasks()

        coroutineScope.launch {
            val userInfo = Api.userService.getInfo().body()
            my_text_view.text = "${userInfo?.firstName} ${userInfo?.lastName}"
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    companion object {
        private const val ADD_TASK_REQUEST_CODE = 666
        private const val TASK_KEY = "task_key"
        private const val EDIT_TASK_REQUEST_CODE = 1
    }

}