package com.example.finalquiz

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import de.codecrafters.tableview.TableHeaderAdapter
import de.codecrafters.tableview.TableView
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter

class listview : AppCompatActivity() {

    var header = Array(4) {"0"}
    var body = ArrayList<String>()
    lateinit var table: TableView<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listview)

        header[0] = "No"
        header[1] = "Name"
        header[2] = "Age"
        header[3] = "Favorite Movie"

        var klock = listOf<String>("1", "Onurah Emmanuel", "21", "In Time")
        var ife = listOf<String>("2", "Ifechi Glory", "20", "Far From Home")
        var PC = listOf<String>("3", "Kalu Kalu", "22", "The Deathly Hallows")

        body.addAll(klock)
        body.addAll(ife)
        body.addAll(PC)

        table = findViewById(R.id.tab)
        table.setHeaderBackgroundColor(Color.parseColor("#2ecc71"))
//        table.headerAdapter = object : SimpleTableHeaderAdapter(this, 4)
 }
}