package com.serezha.balloonSmash

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.serezha.balloonSmash.RecordDbSchema.RecordTable

private const val VERSION = 1
private const val DATABASE_NAME = "balloonss.db"
class RecordsBase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION)
{

    override fun onCreate(db: SQLiteDatabase?) {
            Log.d("MyLog","mtav")
        Log.d("MyLog","mtav")
        Log.d("MyLog","mtav")
        Log.d("MyLog","mtav")
            db!!.execSQL(
                "create table " + RecordTable.NAME + "(" +
                        RecordTable.Cols.UUID + " TEXT, " +
                        RecordTable.Cols.VALUE + " TEXT);")
        val values = ContentValues()
        values.put(RecordTable.Cols.UUID, "0")
        values.put(RecordTable.Cols.VALUE, "0")
        db.insert(RecordTable.NAME, null, values);
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
object RecordDbSchema {
    object RecordTable {
        const val NAME = "recordot"

        object Cols {
            const val UUID = "uuid"
            const val VALUE = "value"


        }
    }
}
