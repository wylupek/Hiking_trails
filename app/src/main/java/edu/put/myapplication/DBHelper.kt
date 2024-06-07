package edu.put.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                NAME_COL + " TEXT," +
                TIME_COL + " TEXT," +
                DATE_COL + " TEXT" + ")")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addTime(name: String, time: String) {
        val values = ContentValues()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.format(Date())
        values.put(DATE_COL, date)
        values.put(NAME_COL, name)
        values.put(TIME_COL, time)

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getTimes(name: String): Cursor? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $NAME_COL = ? ORDER BY $TIME_COL",
            arrayOf(name))

        if (cursor != null && cursor.count == 0) {
            cursor.close()
            return null
        }

        return cursor
    }

    companion object {
        private const val DATABASE_NAME = "TRAILS"
        private const val DATABASE_VERSION = 2
        const val TABLE_NAME = "trail_time"
        const val ID_COL = "id"
        const val NAME_COL = "name"
        const val TIME_COL = "time"
        const val DATE_COL = "date"
    }
}