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
        val query = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT, email TEXT, pass TEXT, counter INTEGER)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    @SuppressLint("Range")
    fun getTable(): String{
        val db = this.readableDatabase
        var str = ""
        val cursor: Cursor = db.rawQuery("SELECT * FROM users", null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val login = cursor.getString(cursor.getColumnIndex("login"))
            val email = cursor.getString(cursor.getColumnIndex("email"))
            val pass = cursor.getString(cursor.getColumnIndex("pass"))
            val counter = cursor.getInt(cursor.getColumnIndex("counter"))
            str+=("ID: $id, Login: $login, Email: $email, Pass: $pass, Counter: $counter\n")
        }
        cursor.close()
        return str
    }

    fun addUser(user: User){
        val values = ContentValues().apply {
            put("login", user.login)
            put("email", user.email)
            put("pass", user.pass)
            put("counter", user.counter)
        }

        writableDatabase.use { db ->
            db.insert("users", null, values)
        }
    }

    fun getUser(login: String, pass: String): Boolean{
        val db = this.readableDatabase
        db.rawQuery("SELECT * FROM users WHERE login = ? AND pass = ?", arrayOf(login, pass)).use { cursor ->
            return cursor.moveToFirst()
        }
    }

    fun isAuth(login: String): Boolean{
        val db = this.readableDatabase
        db.rawQuery("SELECT * FROM users WHERE login = ?", arrayOf(login)).use { cursor ->
            return cursor.moveToFirst()
        }
    }

    fun setCounter(login:String){
        writableDatabase.use { db ->
            db.execSQL("UPDATE users SET counter = counter + 1 WHERE login = ?", arrayOf(login))
        }
    }

    @SuppressLint("Range")
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