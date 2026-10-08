package com.example.music_app

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlin.contracts.Returns

// Crear clase DatabaseHelper que hereda de la clase SQLite
class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    // Datos de la BD
    companion object{
        // Información de la BD
        private const val DATABASE_NAME = "musica.db"
        private const val DATABASE_VERSION = 2

        // Información de las tablas en la BD
        private const val TABLE_USUARIOS = "usuarios"
        private const val COL_ID = "id"
        private const val COL_NOMBRE = "nombre"
        private const val COL_CORREO = "correo"
        private const val COL_PASSWORD = "password"
    }

    // MÉTODO ONCREATE - Para crear la BD
    override fun onCreate(db: SQLiteDatabase?) {
        // Crear la tabla
        val crearTablaUsuarios = """
            CREATE TABLE $TABLE_USUARIOS(
            $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COL_NOMBRE TEXT NOT NULL,
            $COL_CORREO TEXT NOT NULL,
            $COL_PASSWORD TEXT NOT NULL
            )
        """.trimIndent()

        // Crear tabla de usuario
        db?.execSQL(crearTablaUsuarios)

    }

    // MÉTODO ONUPGRADE
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        onCreate(db)
    }

    // MÉTODO PARA REGISTRAR UN USUARIO (INSERT)
    fun registrarUsuario(nombre : String, correo : String, password : String) : Boolean {

        // Crear variable para escribir en la BD
        val db = writableDatabase

        // Insertar los datos
        val datos = ContentValues().apply {
            put(COL_NOMBRE, nombre)
            put(COL_CORREO, correo)
            put(COL_PASSWORD, password)
        }
        // Ejecutar inserción de datos
        val resultado = db.insert(TABLE_USUARIOS, null, datos)

        // Cerrar conexión de la BD
        db.close()

        // Regresar el valor obtenido de la inserción (Verdadero o falso)
        return resultado != -1L

    }



}