package com.serezha.balloonSmash

import android.content.ContentValues
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.widget.LinearLayout


class Main3Activity : AppCompatActivity() {
    lateinit var gameView:GameView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        gameView = GameView(this); // создаём gameView
        val gameLayout = findViewById<LinearLayout>(R.id.gameLayout )
        gameLayout.addView(gameView)//start in initialization
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onStop() {
        super.onStop()
        //gameView.gameRunning = false
        gameView.stopped = true
    }

    override fun onStart() {
        super.onStart()
        gameView.stopped = false
        //gameView.gameRunning = true
    }

    override fun onPause() {
        super.onPause()
        val a = RecordsBase(this)
        val mDatabase = a.writableDatabase
        //Log.d("res","pause")
        var record = gameView.level.score
        //Log.d("Mytag", "mtav$record")
        val valu = MainActivity.valFromBase(mDatabase)
        if(valu?.toInt( )!! < record) {
            val values = ContentValues()
            //val sRecText: TextView = findViewById(R.id.recot)
            //sRecText.text = ("Record: $record")
            values.put(RecordDbSchema.RecordTable.Cols.UUID, "0")
            values.put(RecordDbSchema.RecordTable.Cols.VALUE, record.toString())
            mDatabase!!.update(
                RecordDbSchema.RecordTable.NAME, values,
                RecordDbSchema.RecordTable.Cols.UUID + " = ?",
                arrayOf("0")
            )


        }

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        gameView.stopped = true
    }
    override fun onDestroy() {
        super.onDestroy()
        gameView.gameRunning = false
    }


}
