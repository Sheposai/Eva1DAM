package com.example.eva1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "User.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
    }

    private val CREATE_USERS_TABLE = (
            "CREATE TABLE $TABLE_USERS (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_USERNAME TEXT UNIQUE," + // Agregamos UNIQUE para evitar duplicados
                    "$COLUMN_PASSWORD TEXT" +
                    ")"
            )

    override fun onCreate(db: SQLiteDatabase) {
        // Crear la tabla de usuarios
        db.execSQL(CREATE_USERS_TABLE)

        // Agregar un usuario de prueba
        val contentValues = ContentValues().apply {
            put(COLUMN_USERNAME, "jona")
            put(COLUMN_PASSWORD, "aleluya")
        }
        db.insert(TABLE_USERS, null, contentValues)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // Método para agregar un nuevo usuario
    fun addUser(username: String, password: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }

        val result = db.insert(TABLE_USERS, null, contentValues)
        db.close()
        return result != -1L
    }

    // Método para validar el usuario
    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))

        val isLoggedIn = cursor.count > 0
        cursor.close()
        db.close()

        return isLoggedIn
    }

    // Método para eliminar un usuario
    fun deleteUser(username: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_USERS, "$COLUMN_USERNAME = ?", arrayOf(username))
        db.close()
        return result > 0
    }

    // Método para actualizar un usuario
    fun updateUser(oldUsername: String, newUsername: String, newPassword: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_USERNAME, newUsername)
            put(COLUMN_PASSWORD, newPassword)
        }
        val result = db.update(TABLE_USERS, contentValues, "$COLUMN_USERNAME = ?", arrayOf(oldUsername))
        db.close()
        return result > 0
    }

    // Método para buscar un usuario
    fun getUser(username: String): ContentValues? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))

        if (cursor.moveToFirst()) {
            val contentValues = ContentValues().apply {
                put(COLUMN_ID, cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)))
                put(COLUMN_USERNAME, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)))
                put(COLUMN_PASSWORD, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)))
            }
            cursor.close()
            db.close()
            return contentValues
        }

        cursor.close()
        db.close()
        return null
    }
}
