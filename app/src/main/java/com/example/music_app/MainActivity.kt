package com.example.music_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var etCorreo : EditText
    private lateinit var etPassword : EditText
    private lateinit var btnIngresar : Button

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val tvCrearCuenta = findViewById<TextView>(R.id.tvCrearCuenta)

        etCorreo = findViewById(R.id.etCorreo)
        etPassword = findViewById(R.id.etPass)
        btnIngresar =  findViewById(R.id.btnIngresar)

        dbHelper = DatabaseHelper(this)

        // EVENTO PARA INICIAR SESIÓN
        btnIngresar.setOnClickListener {
            // Llamar al método para iniciar sesión
            iniciarSesion()
        }

        tvCrearCuenta.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    // MÉTODO PARA INICIAR SESIÓN
    private fun iniciarSesion(){
        val correo = etCorreo.text.toString().trim()
        val pass = etPassword.text.toString().trim()

        if(correo.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Llamar al método de inicio de sesión en la BD
        val resultado = dbHelper.iniciarSesion(correo, pass)

        // Validar si el inicio de sesión fue correcto
        // if(resultado == true) se puede dejar asi pero se agrega lo que se vio en clase

        // Validar si el inicio de sesión en la BD
        if(resultado){
            // El resultado es verdadero
            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            // Siempre terminar la pantalla para ahorrar los recursos del dispositivo del Usuario
            finish()
        } else {
            // El resultado es falso
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
        }



    }
}