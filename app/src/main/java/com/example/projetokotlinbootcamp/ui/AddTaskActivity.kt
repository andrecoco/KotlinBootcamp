package com.example.projetokotlinbootcamp.ui

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.projetokotlinbootcamp.databinding.ActivityAddTaskBinding
import com.example.projetokotlinbootcamp.datasource.TaskDataSource
import com.example.projetokotlinbootcamp.extensions.format
import com.example.projetokotlinbootcamp.extensions.text
import com.example.projetokotlinbootcamp.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(TASK_ID)){
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.titulo.text = it.title
                binding.data.text = it.date
                binding.hora.text = it.hour
            }
        }

        insertListeners()

    }

    private fun insertListeners() {
        binding.data.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time)*-1
                binding.data.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.hora.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
            timePicker.addOnPositiveButtonClickListener {
                val minute = if(timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if(timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                binding.hora.text = "$hour:$minute"
            }
            timePicker.show(supportFragmentManager, null)
        }

        binding.butCancelar.setOnClickListener {
            finish()
        }

        binding.butCriarTarefa.setOnClickListener {
            val task = Task(
                title = binding.titulo.text,
                date = binding.data.text,
                hour = binding.hora.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTask(task)

            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}