package com.example.music_app

import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
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
    private lateinit var contenedorIndie : LinearLayout
    private lateinit var contenedorRap : LinearLayout
    private lateinit var contenedorLogic : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        tvResultado = findViewById(R.id.tvResultado)
        contenedorIndie = findViewById(R.id.contenedorIndie)
        contenedorRap = findViewById(R.id.contenedorRap)
        contenedorLogic = findViewById(R.id.contenedorLogic)

        // TODO: Agregar método de llamado de álbum
        obtenerAlbum("indie", contenedorIndie)
        obtenerAlbum("rap", contenedorRap)
        obtenerAlbum("logic", contenedorLogic)


    }

    // METODO PARA OBTENER INFORMACIÓN DEL ALBUM
    private fun obtenerAlbum(genero : String, contenedor : LinearLayout){

        // Hacer el llamado de la construcción de la URL
        RetrofitClient.api.obtenerAlbumGenero(
            genero = genero,
            pais = "MX",
            media = "music",
            entidad = "album",
            limite = 10
        ).enqueue(object : Callback<ITunesResponse?> {
            // MÉTODO EN CASO DE QUE LA RESPUESTA SEA CORRECTA
            override fun onResponse(
                call: Call<ITunesResponse?>,
                response: Response<ITunesResponse?>
            ) {
                // Variable para guardar el numero de los albums consultados
                // Variable que guarda una lista de tipo album
                val albums = response.body()?.results ?: emptyList()

                // Validar si la variable albums tiene información
                if(albums.isNotEmpty()){
                    tvResultado.visibility = View.GONE
                    mostrarAlbums(albums, contenedor)

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

    // Metodo para mostrar el listado de datos de un api
    private fun mostrarAlbums(albums: List<Album>, contenedor: LinearLayout){
        // OBLIGATORIO: EVITAR ELEMENTOS DUPLICADOS
        contenedor.removeAllViews()
        // CICLO PARA RECORRER LAS MÚLTIPLES PETICIONES A UN API
        for(album in albums){
            //USAR EL LinearLayout con el contenedor
            val vistaAlbum = layoutInflater.inflate(
                R.layout.item_album,
                contenedor,
                false
            )

            val imgPortada = vistaAlbum.findViewById<ImageView>(R.id.imgPortadaAlbum)
            val tvNombreAlbum = vistaAlbum.findViewById<TextView>(R.id.tvNombreAlbum)
            val tvNombreArtista = vistaAlbum.findViewById<TextView>(R.id.tvNombreArtista)

            // Asignando los nombres obtenidos del API
            tvNombreAlbum.text = album.collectionName ?: "Album sin nombre"
            tvNombreArtista.text = album.artistName ?: "Artista desconocido"

            // Mostrar la imagen en la tarjeta
            Glide.with(this@HomeActivity)
                .load(album.obtenerImagenGrande())
                .into(imgPortada)

            // Asignar la vista del contenedor a las tarjetas de albums4
            contenedor.addView(vistaAlbum)
        }
    }


}