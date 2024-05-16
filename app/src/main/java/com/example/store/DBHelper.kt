package com.example.store

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, "app", factory, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE users (id INT PRIMARY KEY, login TEXT, email TEXT, pass TEXT, counter INT)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun addUser(user: User){
        val values = ContentValues()
        values.put("login", user.login)
        values.put("email", user.email)
        values.put("pass", user.pass)
        values.put("counter", user.counter)

        val db = this.writableDatabase
        db.insert("users", null, values)

        db.close()
    }

    @SuppressLint("Recycle")
    fun getUser(login: String, pass: String): Boolean{
        val db = this.readableDatabase

        val result = db.rawQuery("SELECT * FROM users WHERE login = '$login' AND pass = '$pass'", null)
        return result.moveToFirst()
    }

    @SuppressLint("Recycle")
    fun setCounter(login:String){
        val db = this.writableDatabase
        db.rawQuery("UPDATE users set counter = counter+1 where login = '$login'", null)
    }

    @SuppressLint("Recycle", "Range")
    fun getCounter(): String{
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT login, counter FROM users", null)
        val stringBuilder = StringBuilder()
        while (cursor.moveToNext()) {
            val login = cursor.getString(cursor.getColumnIndex("login"))
            val counter = cursor.getInt(cursor.getColumnIndex("counter"))
            stringBuilder.append("Login: $login, Counter: $counter\n")
        }
        cursor.close()
        return stringBuilder.toString()
    }
}