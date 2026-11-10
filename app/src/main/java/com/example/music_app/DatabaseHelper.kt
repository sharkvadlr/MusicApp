package com.example.music_app

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.music_app.model.Album
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
        private const val DATABASE_VERSION = 3

        // Información de las tablas en la BD
        private const val TABLE_USUARIOS = "usuarios"
        private const val COL_ID = "id"
        private const val COL_NOMBRE = "nombre"
        private const val COL_CORREO = "correo"
        private const val COL_PASSWORD = "password"

        // Información de la tabla de favoritos
        private const val TABLE_FAVORITOS = "favoritos"
        private const val COL_ID_FAVORITO = "id"
        private const val COL_ALBUM_ID = "album_id"
        private const val COL_NOMBRE_ALBUM = "nombre_album"
        private const val COL_ARTISTA = "artista"
        private const val COL_GENERO = "genero"
        private const val COL_CANCIONES = "num_canciones"
        private const val COL_FECHA = "fecha"
        private const val COL_IMAGEN = "imagen"

        // CREAR AMBAS TABLAS DE LA BD
        private const val CREATE_TABLE_USUARIOS = """
            CREATE TABLE $TABLE_USUARIOS(
            $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COL_NOMBRE TEXT NOT NULL,
            $COL_CORREO TEXT NOT NULL,
            $COL_PASSWORD TEXT NOT NULL
            )
        """

        private const val CREATE_TABLE_FAVORITOS = """
            CREATE TABLE $TABLE_FAVORITOS(
            $COL_ID_FAVORITO INTEGER PRIMARY KEY,
            $COL_ALBUM_ID INTEGER UNIQUE NOT NULL,
            $COL_NOMBRE_ALBUM TEXT NOT NULL,
            $COL_ARTISTA TEXT,
            $COL_GENERO TEXT,
            $COL_CANCIONES INTEGER,
            $COL_FECHA TEXT,
            $COL_IMAGEN TEXT
            )
        """

    }

    // MÉTODO ONCREATE - Para crear la BD
    override fun onCreate(db: SQLiteDatabase?) {
        // Crear tabla de usuario
        db?.execSQL(CREATE_TABLE_USUARIOS)
        db?.execSQL(CREATE_TABLE_FAVORITOS)

    }

    // MÉTODO ONUPGRADE
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        /*db?.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        onCreate(db)*/

        //Verificar la version de la BD para evitar borrar los datos
        if(oldVersion < 3){
            db?.execSQL(CREATE_TABLE_FAVORITOS)
        }
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

    // MÉTODO PARA INICIAR SESIÓN
    fun iniciarSesion(correo : String, password: String) : Boolean{
        // Variable que lee la base de datos
        val db = readableDatabase
        val query_sql = """
            SELECT * FROM $TABLE_USUARIOS
            WHERE $COL_CORREO = ? AND $COL_PASSWORD = ?
        """.trimIndent()

        // Crear un cursor que permite recorrer todos los registros
        val cursor = db.rawQuery(
            query_sql, arrayOf(correo, password)
        )

        // Verificar cuantos resultados encuentra el cursor
        val usuarioEncontrado = cursor.count > 0
        cursor.close()
        db.close()

        return usuarioEncontrado

    }

    // MÉTODO PARA GUARDAR LA INFORMACIÓN DEL API
    fun guardarAlbumFavoritos(albumId : Long, nombreAlbum : String, artista : String, genero : String, canciones : Int, fecha : String, imagen : String) : Boolean {

        // Crear variable para escribir en la BD
        val db = writableDatabase

        // Crear una variable para pasar los valores del API
        val valores = ContentValues().apply {
            put(COL_ALBUM_ID, albumId)
            put(COL_NOMBRE_ALBUM, nombreAlbum)
            put(COL_ARTISTA, artista)
            put(COL_GENERO, genero)
            put(COL_CANCIONES, canciones)
            put(COL_FECHA, fecha)
            put(COL_IMAGEN, imagen)
        }
        val resultado = db.insertWithOnConflict(
            TABLE_FAVORITOS,
            null,
            valores,
            SQLiteDatabase.CONFLICT_IGNORE
        )
        return resultado != -1L
    }

    // MÉTODO PARA ELIMINAR EL ÁLBUM DE FAVORITOS
    fun eliminarAlbumFavorito(albumId : Long) : Boolean{
        val db = writableDatabase

        val filas_afectadas = db.delete(
            TABLE_FAVORITOS,
            "$COL_ALBUM_ID = ?",
            arrayOf(albumId.toString()))

        return filas_afectadas > 0
    }

    //MÉTODO PARA VERIFICAR SI UN ÁLBUM ESTÁ REGISTRADO
    fun existeAlbumFavorito(albumId: Long) : Boolean{
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT $COL_ALBUM_ID FROM $TABLE_FAVORITOS WHERE $COL_ALBUM_ID = ?",
            arrayOf(albumId.toString())
        )

        // VERIFICAR SI EXISTE EL REGISTRO
        val existe = cursor.moveToFirst()
        cursor.close()
        return existe
    }

}