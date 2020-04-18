package com.serezha.balloonSmash

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.serezha.balloonSmash.RecordDbSchema.RecordTable
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.widget.TextView
import android.util.Log
import android.view.View
import android.widget.ImageButton
import kotlin.math.max
import android.R.attr.y
import android.R.attr.x
import android.content.pm.ActivityInfo
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.view.Display
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build


const val REQUEST_RECORD = 3
class MainActivity : AppCompatActivity() {


    var mDatabase:SQLiteDatabase? = null
    lateinit var sRecText:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val sButton: ImageButton = this.findViewById(R.id.starter)
        sRecText = findViewById(R.id.recot)
        val lay = findViewById<View>(android.R.id.content)
        addBackground(R.drawable.ball_in_sky4,lay)

        sButton.setOnClickListener {

            val gameIntent = Intent(this,Main3Activity::class.java)
            startActivityForResult(gameIntent,REQUEST_RECORD)
        }


        val a = RecordsBase(this)
        mDatabase = a.writableDatabase

        val value = valFromBase(mDatabase)
        Log.d("count", value!! );
        sRecText.text = ("Record: $value")

    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mDatabase!!.close()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_RECORD)
        {
            val record = data!!.data.toString();
            Log.d("Mytag", "mtav$record")
            putRecord(mDatabase,record)
        }
        else return
    }

    override fun onResume() {
        super.onResume()
        val value = valFromBase(mDatabase)
        Log.d("count", "resume" );
        sRecText.text = ("Record: $value")
    }
    companion object {
        fun valFromBase(mDatabase: SQLiteDatabase?): String? {

            val getPost = "SELECT* " + " FROM " + RecordTable.NAME + ";"
            val cursor = mDatabase!!.rawQuery(getPost, null);
            //another method val cursor = mDatabase!!.query(RecordTable.NAME,null,null,null,null,null,null)
            Log.d("count", cursor.columnCount.toString());
            cursor.moveToNext()
            val res = cursor!!.getString(1)
            cursor.close()
            return res
        }


    }
        fun putRecord(mDatabase: SQLiteDatabase?, record: String) {
            val values = ContentValues()
            val sRecText: TextView = findViewById(R.id.recot)
            sRecText.text = ("Record: $record")
            values.put(RecordTable.Cols.UUID, "0")
            values.put(RecordTable.Cols.VALUE, record)
            mDatabase!!.update(
                RecordTable.NAME, values,
                RecordTable.Cols.UUID + " = ?",
                arrayOf("0")
            )
        }
    fun addBackground(bitmapId:Int,view:View)
    {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(this.resources, bitmapId, options)
        //val cBitmap = BitmapFactory.decodeResource(context.resources,bitmapId)

        Log.d("stugum","bitmp"+options.outWidth)
        //val max = max(GameView.unitW, GameView.unitH)
        //Log.d("stugum","max"+max)
        //val balance = 9f
        //val prop_w =options.outWidth.toFloat()/options.outHeight.toFloat()*balance
        //!!!!!!!!!
        //!!!
        //ogtagorci verevi qomentn u sizov dzi chapn
        //!!!
        //!!!!!!!!!
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y
        //width = (prop_w*size*max).toInt()//2.1*max*width
        //height = (balance *size*max).toInt()

        Log.d("stugum","scale width"+width)
        val inSample = GameBody.calculateInSampleSize(options,width+100,height+100)
        options.inSampleSize = inSample
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        val bitmap = BitmapFactory.decodeResource(this.resources, bitmapId, options);
        //val bitmap = Bitmap.createScaledBitmap(cBitmap!!, width , height, false)
        val ob = BitmapDrawable(resources,bitmap)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.background = ob
        }
        //else view.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
        //val bitmap = Bitmap.createScaledBitmap(cBitmap!!, width , height, false))
    }

}
