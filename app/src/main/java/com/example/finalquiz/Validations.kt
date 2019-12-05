package com.example.finalquiz

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.finalquiz.Model.Users
import com.google.firebase.database.*
import java.lang.Exception

class Validations : AppCompatActivity() {

    lateinit var login_regno: EditText
    lateinit var login_password: EditText
    lateinit var login_button: Button
    var userList = ArrayList<Users>()
    var regNoList = ArrayList<String>()
    var nameList = ArrayList<String>()
    var resultList = ArrayList<String>()
    lateinit var dialog: AlertDialog
    lateinit var spinner: Spinner
    lateinit var ref: DatabaseReference
    lateinit var testname: String
    var position1: Int = 0
    lateinit var adapter: ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validations)

        login_regno = findViewById(R.id.login_regno)
        login_password = findViewById(R.id.login_password)
        login_button = findViewById(R.id.login_button)
        spinner = findViewById(R.id.spinner)
        val bundle: Bundle? = intent.extras
        var coursename = ArrayList<String>()
        coursename.add("Select an Exam")
        val coursename0 = bundle!!.getStringArrayList("courses")
        coursename.addAll(coursename0)



        validateUser()


        // course selection spinner
        adapter = ArrayAdapter<String>(this, R.layout.spinner_item, coursename)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter=adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
               return
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                position1 = position
                testname = coursename!![position]
            }
        }

        // login button actions
        login_button.setOnClickListener {
            duro()
            login_button.hideKeyboard()
            Toast.makeText(this, userList.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    // back button actions
    override fun onBackPressed() {
            val builder = AlertDialog.Builder(this@Validations)
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

    // get user details from firebase
    fun validateUser() {
        ref = FirebaseDatabase.getInstance().getReference("Users").child("Users")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (UserSnapShot in dataSnapshot.children) {
                        try {
                        val users: Users? = UserSnapShot.getValue(Users::class.java)
                        userList.add(users!!)
                    } catch (e: Exception) {
                            return
                        }
                    }
            }
        })
    }

    fun checkUser() {
        //create array for Reg No
        for(regs in userList) {
            regNoList.add(regs.REGNO)
        }

        //create array for name
        for(names in userList) {
            nameList.add(names.NAME)
        }

        //create array for result
        for (result in userList) {
            resultList.add(result.RESULT)
        }

        var userregno = login_regno.text.toString().trim()

        if (login_regno.text.toString().trim().equals(login_password.text.toString().trim())) {
                if (regNoList.contains(login_regno.text.toString().trim())) {
                    val intent = Intent(applicationContext, welcomeActivity::class.java)
                    val name = getName()
                    val res = getResult()
                    var index = userList.lastIndexOf(Users(NAME = name!!, REGNO = userregno, RESULT = res!!))
                    var indexdb = (index + 1).toString()

                    // To check if the candidate has a score
                    if (!userList[index].RESULT.equals("N/A")) {
                        Toast.makeText(this, "You Already Have a Score", Toast.LENGTH_SHORT).show()
                        return
                    }

                    Toast.makeText(this, indexdb, Toast.LENGTH_SHORT).show()
                    intent.putExtra("name",name)
                    intent.putExtra("testname", testname)
                    intent.putExtra("index", indexdb)
                    startActivity(intent)
                    finish()
                } else if(login_regno.text.toString().trim().equals("admin")) {
                    val intent = Intent(applicationContext, preset::class.java)
                    startActivity(intent)
                } else if (!isNetworkAvailable(this)) {
                    Toast.makeText(this, "Network Unavailable", Toast.LENGTH_LONG).show()
                    return
                }

                else {
                    Toast.makeText(applicationContext, "You're Not Eligible For This Test", Toast.LENGTH_SHORT).show()
                    return
                }
            } else {
            Toast.makeText(applicationContext, "Your Details Doesn't Match", Toast.LENGTH_SHORT).show()
            return
        }
        regNoList.clear()
        nameList.clear()
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun getName():String? {
        val rn = login_regno.text.toString().trim()
        for(regs in userList) {
            if(rn.equals(regs.REGNO)){
                return regs.NAME
            }
        }
        return null
    }

    fun getResult():String? {
        val rn = login_regno.text.toString().trim()
        for(regs in userList) {
            if(rn.equals(regs.REGNO)){
                return regs.RESULT
            }
        }
        return null
    }

    // check network
    fun isNetworkAvailable(context: Context) : Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }

    fun duro() {
        if (userList.isEmpty()) {
            Toast.makeText(this, "Network Unavailable", Toast.LENGTH_SHORT).show()
            return
        }
        if (login_regno.text.toString().trim().isEmpty()) {
            login_regno.error = "Please Enter your Reg No"
            login_regno.requestFocus()
            return
        }
        if(login_password.text.toString().trim().isEmpty()) {
            login_password.error = "Please Enter your Password"
            login_password.requestFocus()
            return
        }
        if(position1 == 0) {
            Toast.makeText(this,
                "Hold on there! it's either you haven't selected an exam or there is none available ",
                Toast.LENGTH_LONG).show()
            return
        }

        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.logindialog, null)
        val message = dialogView.findViewById<TextView>(R.id.login_dialog_msg)

        message.text = "Logging In ... "
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()

        var runnable = Runnable {
            checkUser()
            dialog.dismiss()
        }

        Handler().postDelayed(runnable, 2400)
    }
}

