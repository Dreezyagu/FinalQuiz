package com.example.finalquiz
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.ActionBar
import com.google.firebase.database.*
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    lateinit var ref: DatabaseReference
    lateinit var coursename: ArrayList<String?>


    val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            val intent = Intent(applicationContext, Validations::class.java)
            intent.putExtra("courses", coursename)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        coursename = ArrayList()
        ref = FirebaseDatabase.getInstance().getReference("Questions")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (QuestionSnapshot in dataSnapshot.children) {
                    coursename.add(QuestionSnapshot.key)
                }
            }
        })


        Handler().postDelayed(mRunnable, 5000)


    }
}

