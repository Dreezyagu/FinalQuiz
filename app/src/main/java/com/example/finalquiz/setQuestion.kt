package com.example.finalquiz

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.finalquiz.Model.setque
import com.google.firebase.database.FirebaseDatabase

class setQuestion : AppCompatActivity() {

    lateinit var questiontoset: EditText
    lateinit var optionun: EditText
    lateinit var optiondeux: EditText
    lateinit var optiontrois: EditText
    lateinit var optionquatre: EditText
    lateinit var response: EditText
    lateinit var submitbutton: Button
    lateinit var done: Button
    lateinit var testname: String
    lateinit var dialog1: AlertDialog
    lateinit var numofque: String
    var time: Int = 0
    var count = 0
    lateinit var array: MutableList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_question)

        questiontoset = findViewById(R.id.questiontoset)
        optionun = findViewById(R.id.optionun)
        optiondeux = findViewById(R.id.optiondeux)
        optiontrois = findViewById(R.id.optiontrois)
        optionquatre = findViewById(R.id.optionquatre)
        response = findViewById(R.id.response)
        submitbutton = findViewById(R.id.submitbutton)
        done = findViewById(R.id.done)

        val bundle: Bundle? = intent.extras
        testname = bundle!!.getString("testname")
        numofque = bundle!!.getString("numofque")
        time = bundle.getInt("time")


        array = mutableListOf<Int>()

        submitbutton.setOnClickListener {
            saveQuestions()
            duro()
            clear()

            if (count == numofque.toInt()-1) {
                submitbutton.toggleVisibility()
                done.toggleVisibility()
                enb()
            }
        }

        done.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    // back button actions
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@setQuestion)
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

    fun saveQuestions() {
        if (questiontoset.text.toString().trim().isEmpty()) {
            questiontoset.error = "Please Fill The Blank"
            questiontoset.requestFocus()
            return
        }
        if (optionun.text.toString().isEmpty()) {
            optionun.error = "Please Fill The Blank"
            optionun.requestFocus()
            return
        }
        if (optiondeux.text.toString().isEmpty()) {
            optiondeux.error = "Please Fill The Blank"
            optiondeux.requestFocus()
            return
        }
        if (optiontrois.text.toString().isEmpty()) {
            optiontrois.error = "Please Fill The Blank"
            optiontrois.requestFocus()
            return
        }
        if (optionquatre.text.toString().isEmpty()) {
            optionquatre.error = "Please Fill The Blank"
            optionquatre.requestFocus()
            return
        }
        if (response.text.toString().isEmpty()) {
            response.error = "Please Fill The Blank"
            response.requestFocus()
            return
        }

        val question = questiontoset.text.toString().trim()
        val optionA = optionun.text.toString().trim()
        val optionB = optiondeux.text.toString().trim()
        val optionC = optiontrois.text.toString().trim()
        val optionD = optionquatre.text.toString().trim()
        val correctanswer = response.text.toString().trim()
        val ref = FirebaseDatabase.getInstance()
        val myref = ref.getReference("Questions")
        val questions = setque(question, optionA, optionB, optionC, optionD, correctanswer)

        for (i in 1..numofque.toInt()) {
            array.add(i)
        }


        myref.child(testname).child(array[count].toString()).setValue(questions).addOnCompleteListener {
            count++
            Toast.makeText(applicationContext, "Question $count Saved", Toast.LENGTH_SHORT).show()
        }
        myref.child(testname).child("Duration").setValue(time).addOnCompleteListener {
            Toast.makeText(applicationContext, "Other Details Saved", Toast.LENGTH_SHORT).show()

        }

    }

    fun clear() {
        questiontoset.setText("")
        optionun.setText("")
        optiondeux.setText("")
        optiontrois.setText("")
        optionquatre.setText("")
        response.setText("")
    }

    fun enb() {
        questiontoset.isEnabled = false
        optionun.isEnabled = false
        optiondeux.isEnabled = false
        optiontrois.isEnabled = false
        optionquatre.isEnabled = false
        response.isEnabled = false
    }

    fun View.toggleVisibility() {
        if (visibility == View.VISIBLE){
            visibility = View.INVISIBLE
        } else {
            visibility = View.VISIBLE
        }
    }

    fun duro() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.quedialog, null)
        val message = dialogView.findViewById<TextView>(R.id.quedialog_msg)

        message.text = "Saving ... "
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog1 = builder.create()
        dialog1.show()

        var runnable = Runnable {
            dialog1.dismiss()
        }
        Handler().postDelayed(runnable, 500)
    }



}