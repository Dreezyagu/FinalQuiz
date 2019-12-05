package com.example.finalquiz

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.finalquiz.Model.Question
import com.google.firebase.database.*

class welcomeActivity : AppCompatActivity() {

    lateinit var start_button: Button
    lateinit var show_name: TextView
    lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        show_name = findViewById(R.id.show_name)
        start_button = findViewById(R.id.start_button)

        val bundle: Bundle? = intent.extras
        val get_name = bundle!!.getString("name")
        val testname = bundle!!.getString("testname")
        val index = bundle!!.getString("index")
        show_name.text = "Welcome, $get_name "

        start_button.setOnClickListener {
            val movetosecondactivity = Intent(this, testActivity::class.java)
            movetosecondactivity.putExtra("testname", testname)
            movetosecondactivity.putExtra("index", index)
            startActivity(movetosecondactivity)
            finish()
        }
    }


    // back button actions
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@welcomeActivity)
        builder.setTitle("SUBMIT")
        builder.setMessage("ARE YOU SURE YOU WANT TO EXIT? ")
        builder.setPositiveButton("YES") { dialog, which ->
            super.onBackPressed()
        }

        builder.setNegativeButton("NO") { dialog, which ->
            dialog.dismiss()

        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
