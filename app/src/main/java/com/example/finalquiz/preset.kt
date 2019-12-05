package com.example.finalquiz

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_preset.*

class preset : AppCompatActivity() {

    lateinit var testnameview: EditText
    lateinit var numofque: EditText
    lateinit var submit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preset)

         testnameview = findViewById(R.id.testname)
        numofque = findViewById(R.id.numofque)
        submit = findViewById(R.id.submit)

        submit.setOnClickListener {
            checker()
        }


    }


    // back button actions
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@preset)
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

    fun checker() {
        if (testnameview.text.toString().trim().isEmpty()) {
            testnameview.error = "Please Enter the Name of Test"
            testnameview.requestFocus()
            return
        }
        if (numofque.text.toString().trim().isEmpty()) {
            numofque.error = "Please Enter the Number of Questions"
            numofque.requestFocus()
            return
        }

        if (numofque.text.toString().toInt() > 10) {
            numofque.error = "Questions cannot be more than 10"
            numofque.requestFocus()
            return

        }
        val intent = Intent(this, setQuestion::class.java)
        val intent1 = Intent (this, Validations::class.java)
        intent.putExtra("testname", testnameview.text.toString().trim())
        intent.putExtra("numofque", numofque.text.toString())
        intent1.putExtra("testname", testnameview.text.toString().trim())
        startActivity(intent)
        finish()
    }
    }
