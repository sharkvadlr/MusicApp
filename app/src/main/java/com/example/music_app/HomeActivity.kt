package com.example.music_app

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.music_app.model.Album
import com.example.music_app.model.ITunesResponse
import com.example.music_app.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var tvResultado : TextView
    private lateinit var imgAlbum: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        tvResultado = findViewById(R.id.tvResultado)
        imgAlbum = findViewById(R.id.imgAlbum)

        // TODO: Agregar método de llamado de álbum
        obtenerAlbum()


    }

    // METODO PARA OBTENER INFORMACIÓN DEL ALBUM
    private fun obtenerAlbum(){

        // Hacer el llamado de la construcción de la URL
        RetrofitClient.api.obtenerAlbumGenero(
            genero = "joji",
            pais = "MX",
            media = "music",
            entidad = "album",
            limite = 2
        ).enqueue(object : Callback<ITunesResponse?> {
            // MÉTODO EN CASO DE QUE LA RESPUESTA SEA CORRECTA
            override fun onResponse(
                call: Call<ITunesResponse?>,
                response: Response<ITunesResponse?>
            ) {
                // Variable para guardar el numero de los albums consultados
                val albums = response.body()?.results

                // Validar si la variable albums tiene información
                if(!albums.isNullOrEmpty()){
                    // Variable para acceder al primer elemento de los albums encontrados
                    val album = albums[0]
                    tvResultado.text =
                        "Album: ${ album.collectionName } \n\n" +
                        "Artista: ${ album.artistName } \n\n" +
                        "Numero de canciones: ${ album.trackCount } \n\n"

                    // Mostrar la imagen del álbum
                    Glide.with(this@HomeActivity)
                        .load(album.obtenerImagenGrande())
                        .into(imgAlbum)

                }else{
                    Toast.makeText(this@HomeActivity, "No hay respuesta del API", Toast.LENGTH_SHORT).show()
                }

            }
            // MÉTODO PARA HACER UNA ACCIÓN SI EL API FALLA
            override fun onFailure(call: Call<ITunesResponse?>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Error en el API", Toast.LENGTH_SHORT).show()
            }
        })


    }


}