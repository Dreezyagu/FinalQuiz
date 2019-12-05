package com.example.finalquiz

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.*
import com.example.finalquiz.Model.Question
import com.example.finalquiz.Model.Result
import com.google.firebase.database.*

class testActivity : AppCompatActivity() {

    lateinit var radio_group: RadioGroup
    lateinit var option1: RadioButton
    lateinit var option2: RadioButton
    lateinit var option3: RadioButton
    lateinit var option4: RadioButton
    lateinit var question_text: TextView
    lateinit var quenum: TextView
    lateinit var next_button: Button
    lateinit var prev_button: Button
    lateinit var submit: Button
    lateinit var timer_text: TextView
    var answer: String? = ""
    var total = 0
    var correct = 0
    var wrong = 0
    var quenumm = 1
    lateinit var timer: CountDownTimer
    var questionList = ArrayList<Question>()
    lateinit var ref: DatabaseReference
    lateinit var dialog: AlertDialog
    lateinit var dialog1: AlertDialog
    var testname: String? = ""
    var index: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)

        question_text = findViewById(R.id.question_text)
        next_button = findViewById(R.id.next_button)
        prev_button = findViewById(R.id.prev_button)
        radio_group = findViewById(R.id.radio_group)
        submit = findViewById(R.id.submit)
        timer_text = findViewById(R.id.timer_text)
        quenum = findViewById(R.id.quenum)

        val bundle: Bundle? = intent.extras
        testname = bundle!!.getString("testname")
        index = bundle!!.getString("index")



        getQuestionsFromDB()

        var run = Runnable {
            radio_group.clearCheck()
            nextQuestion()
        }

        prev_button.toggleVisibility()

        next_button.setOnClickListener {
            duro()
            validateAnswer()
            nextQuestion()
            radio_group.clearCheck()
        }

        prev_button.setOnClickListener {
                prevQuestion()
        }

        submit.setOnClickListener {
            dialog()

        }

        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.progess_dialog, null)
        val message = dialogView.findViewById<TextView>(R.id.dialog_msg)
        message.text = "Getting Questions... "
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()


        quenum.text = quenumm.toString()

        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timer_text.text = "00:${(millisUntilFinished / 1000)}"
            }

            override fun onFinish() {
                onFinishh()
            }


        }
    }

    // back button actions
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@testActivity)
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

    fun getQuestionsFromDB() {
        ref = FirebaseDatabase.getInstance().getReference("Questions").child(testname!!)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (QuestionSnapshot in dataSnapshot.children) {
                    val question: Question? = QuestionSnapshot.getValue(Question::class.java)
                    questionList.add(question!!)
                }
                question_text.text = questionList[total].question
                option1.text = questionList[total].option1
                option2.text = questionList[total].option2
                option3.text = questionList[total].option3
                option4.text = questionList[total].option4
                answer = questionList[total].answer

                dialog.dismiss()
                timer.start()
            }
        })

    }

    fun nextQuestion() {

        if (quenumm == 1) {
            prev_button.toggleVisibility()
        }

        if (quenumm != questionList.size) {
            total++
            question_text.text = questionList[total].question
            option1.text = questionList[total].option1
            option2.text = questionList[total].option2
            option3.text = questionList[total].option3
            option4.text = questionList[total].option4
            answer = questionList[total].answer

            quenumm++
            quenum.text = quenumm.toString()
        }
        if (quenumm == questionList.size) {
            next_button.toggleVisibility()

        }
    }

    fun validateAnswer() {
        var selected = radio_group.checkedRadioButtonId
        if (selected == -1) {
            Toast.makeText(applicationContext, "Please Select an Answer", Toast.LENGTH_SHORT).show()
            return
        }
        when (selected) {
            option1.id -> {
                if (questionList[total].answer.equals(option1.text.toString())) {
                    correct++
                } else {
                    wrong++
                }
            }
            option2.id -> {
                if (questionList[total].answer.equals(option2.text.toString())) {
                    correct++
                } else {
                    wrong++
                }
            }
            option3.id -> {
                if (questionList[total].answer.equals(option3.text.toString())) {
                    correct++
                } else {
                    wrong++
                }
            }
            option4.id -> {
                if (questionList[total].answer.equals(option4.text.toString())) {
                    correct++
                } else {
                    wrong++
                }
            }
        }
    }

    fun prevQuestion() {

        if (quenumm == questionList.size){
            next_button.toggleVisibility()

        }
        if (quenumm == 2){
           prev_button.toggleVisibility()
        }
        total--
        question_text.text = questionList[total].question
        option1.text = questionList[total].option1
        option2.text = questionList[total].option2
        option3.text = questionList[total].option3
        option4.text = questionList[total].option4
        answer = questionList[total].answer
        quenumm--
        quenum.text = quenumm.toString()

        radio_group.clearCheck()

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

        message.text = "Getting Next Question ... "
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog1 = builder.create()
        dialog1.show()

        var runnable = Runnable {
            dialog1.dismiss()
        }
        Handler().postDelayed(runnable, 500)
    }

    fun onFinishh() {
        validateAnswer()
        val intent = Intent(this, result::class.java)
        intent.putExtra("correct", correct.toString())
        startActivity(intent)
        finish()
    }

    fun dialog() {
        val builder = AlertDialog.Builder(this@testActivity)
        builder.setTitle("SUBMIT")
        builder.setMessage("ARE YOU SURE YOU WANT TO SUBMIT? ")
        builder.setPositiveButton("YES") { dialog, which ->
            onFinishh()
            result()
            timer.cancel()
        }

        builder.setNegativeButton("NO") { dialog, which ->
            dialog.dismiss()

        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun result() {
        val ref = FirebaseDatabase.getInstance()
        val myref = ref.getReference("Users")

        myref.child("Users").child(index!!).child("RESULT").setValue(correct.toString())
    }

}

