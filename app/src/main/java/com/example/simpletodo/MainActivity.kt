package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils.readLines
import org.apache.commons.io.FileUtils.writeLines
import org.apache.commons.io.IOUtils.writeLines
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter:  TextItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TextItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
            // 1. Remove item from list
                listOfTasks.removeAt(position)
            // 2. Notify adapter of data change
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        // 1. When the user clicks on the add button
//        findViewById<Button>(R.id.button).setOnClickListener {
//            Log.i("Caren", "User clicked on button")
//        }

        loadItems()


        // Look up recyclerView in Layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TextItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Set up button and input field, so that the user can enter a task
        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        //Set a reference to the button and then set onclicklistener
        findViewById<Button>(R.id.button).setOnClickListener {
            //1. Grab user input
            val userInputtedTask = inputTextField.text.toString()
            //2. Add string to list of tasks
            listOfTasks.add(userInputtedTask)

            // Notify adapter that data was updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //3. Reset text field
            inputTextField.setText("")

            saveItems()
        }
    }

    // Save user input
    // Save data by writing and reading from a file

    // Create method to get file
    fun getDataFile() : File {
        // Every line is a task in the list
        return File(filesDir, "data.txt")
    }

    // Load items by reading all lines from file
    fun loadItems() {
        try{
            listOfTasks = org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }


    // Save items bt writing them into our data file
    fun saveItems() {
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }
}