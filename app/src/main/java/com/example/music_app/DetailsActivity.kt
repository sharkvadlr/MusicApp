package com.example.music_app

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide


class DetailsActivity : AppCompatActivity() {

    private lateinit var imgPortadaDetalle : ImageView
    private lateinit var btnFavorito : ImageButton
    private lateinit var tvNombreAlbumDetalle : TextView
    private lateinit var tvArtistaAlbumDetalle : TextView
    private lateinit var tvAlbumDetalle : TextView
    private lateinit var db : DatabaseHelper
    private var albumFavorito = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_details)

        imgPortadaDetalle = findViewById(R.id.imgPortadaDetalle)
        btnFavorito = findViewById(R.id.btnFavorito)
        tvNombreAlbumDetalle = findViewById(R.id.tvNombreAlbumDetalle)
        tvArtistaAlbumDetalle = findViewById(R.id.tvArtistaAlbumDetalle)
        tvAlbumDetalle= findViewById(R.id.tvAlbumDetalle)
        db = DatabaseHelper(this)
        mostrarInformacionAlbum()
    }

    // MÉTODO PARA MOSTRAR LA INFORMACION DE UN ALBUM
    private fun mostrarInformacionAlbum(){

        val idAlbum = intent.getLongExtra("ID_ALBUM", -1L)
        val nombreAlbum = intent.getStringExtra("NOMBRE_ALBUM") ?: "Album sin nombre"
        val artistaAlbum= intent.getStringExtra("ARTISTA_ALBUM") ?: "Artista desconocido"
        val genero = intent.getStringExtra("GENERO_ALBUM") ?: "Genero desconocido"
        val canciones = intent.getIntExtra("CANCIONES_ALBUMS", 1)
        val fecha = intent.getStringExtra("FECHA_ALBUM") ?: "Fecha desconocida"
        val imagen = intent.getStringExtra("IMAGEN_ALBUM") ?: ""

        tvNombreAlbumDetalle.text = nombreAlbum
        tvArtistaAlbumDetalle.text = artistaAlbum
        tvAlbumDetalle.text = genero + " - " + "$canciones canciones"
        Glide.with(this)
            .load(imagen)
            .into(imgPortadaDetalle)

        // Verificar que el album exista como favorito
        if(idAlbum != -1L){
            albumFavorito = db.existeAlbumFavorito(idAlbum)
            if(albumFavorito){
                btnFavorito.setImageResource(R.drawable.baseline_favorite_24)
            } else {
                btnFavorito.setImageResource(R.drawable.no_favorite)
            }
        }

        btnFavorito.setOnClickListener {

            //albumFavorito = !albumFavorito

            if(albumFavorito){
                //btnFavorito.setImageResource(R.drawable.baseline_favorite_24)
                //Toast.makeText(this, "Album $nombreAlbum agregado a favoritos", Toast.LENGTH_SHORT).show()
                val eliminado = db.eliminarAlbumFavorito(idAlbum)
                if(eliminado){
                    btnFavorito.setImageResource(R.drawable.no_favorite)
                    albumFavorito = !albumFavorito
                    Toast.makeText(this, "Album $nombreAlbum eliminado de favoritos", Toast.LENGTH_SHORT).show()
                }
            } else {
                //btnFavorito.setImageResource(R.drawable.no_favorite)
                //Toast.makeText(this, "Album $nombreAlbum eliminado de favoritos", Toast.LENGTH_SHORT).show()
                /*
                put(COL_ALBUM_ID, albumId)
                put(COL_NOMBRE_ALBUM, nombreAlbum)
                put(COL_ARTISTA, artista)
                put(COL_GENERO, genero)
                put(COL_CANCIONES, canciones)
                put(COL_FECHA, fecha)
                put(COL_IMAGEN, imagen)*/
                val guardado = db.guardarAlbumFavoritos(
                    albumId = idAlbum,
                    nombreAlbum = nombreAlbum,
                    artista = artistaAlbum,
                    genero = genero,
                    canciones = canciones,
                    fecha = fecha,
                    imagen = imagen
                )
                // Evaluar si la inseción fue correcta
                if(guardado){
                    btnFavorito.setImageResource(R.drawable.baseline_favorite_24)
                    albumFavorito = !albumFavorito
                    Toast.makeText(this, "Album $nombreAlbum agregado a favoritos", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Album $nombreAlbum ya se encuentra en favoritos", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }



}