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

        mostrarInformacionAlbum()
    }

    // MÉTODO PARA MOSTRAR LA INFORMACION DE UN ALBUM
    private fun mostrarInformacionAlbum(){

        val idAlbum = intent.getLongExtra("ID_ALBUM", 1)
        val nombreAlbum = intent.getStringExtra("NOMBRE_ALBUM")
        val artistaAlbum= intent.getStringExtra("ARTISTA_ALBUM")
        val genero = intent.getStringExtra("GENERO_ALBUM")
        val canciones = intent.getIntExtra("CANCIONES_ALBUMS", 1)
        val imagen = intent.getStringExtra("IMAGEN_ALBUM")

        tvNombreAlbumDetalle.text = nombreAlbum
        tvArtistaAlbumDetalle.text = artistaAlbum
        tvAlbumDetalle.text = genero + " - " + "$canciones canciones"
        Glide.with(this)
            .load(imagen)
            .into(imgPortadaDetalle)


        btnFavorito.setOnClickListener {

            albumFavorito = !albumFavorito

            if(albumFavorito){
                btnFavorito.setImageResource(R.drawable.baseline_favorite_24)
                Toast.makeText(this, "Album $nombreAlbum agregado a favoritos", Toast.LENGTH_SHORT).show()
            } else {
                btnFavorito.setImageResource(R.drawable.no_favorite)
                Toast.makeText(this, "Album $nombreAlbum eliminado de favoritos", Toast.LENGTH_SHORT).show()
            }

        }
    }



}