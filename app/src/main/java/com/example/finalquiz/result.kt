package com.example.finalquiz

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.TextView

class result : AppCompatActivity() {


    lateinit var show_result: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        show_result = findViewById(R.id.showResult)

        val bundle: Bundle? = intent.extras
        val correct = bundle!!.getString("correct")

        show_result.text = " You Answered ${correct} correctly"
    }

    // back button actions
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@result)
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
