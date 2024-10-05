package com.example.eva1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Mostrar mensaje de bienvenida
        val username = intent.getStringExtra("USERNAME")
        val welcomeMessageTextView: TextView = findViewById(R.id.welcome_message)
        welcomeMessageTextView.text = "Bienvenido, $username!"

        // Referencias a los campos de texto y botones
        val nameEditText: EditText = findViewById(R.id.nameEditText)
        val lastNameEditText: EditText = findViewById(R.id.lastNameEditText)
        val comunaEditText: EditText = findViewById(R.id.comunaEditText)
        val observationEditText: EditText = findViewById(R.id.observationEditText)
        val sendButton: Button = findViewById(R.id.sendButton)
        val backButton: Button = findViewById(R.id.btn_back)

        // Funcionalidad del botón "Enviar Datos"
        sendButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()
            val comuna = comunaEditText.text.toString().trim()
            val observation = observationEditText.text.toString().trim()

            // Validar que todos los campos estén completos
            if (name.isEmpty() || lastName.isEmpty() || comuna.isEmpty() || observation.isEmpty()) {
                Toast.makeText(this, "Todos los campos deben estar completos", Toast.LENGTH_SHORT).show()
            } else {
                // Si todos los campos están completos, abrir la aplicación de correo
                sendEmail(name, lastName, comuna, observation)
            }
        }

        // Funcionalidad del botón "Volver"
        backButton.setOnClickListener {
            finish()  // Finaliza la actividad actual y regresa a la anterior
        }
    }

    // Método para abrir la aplicación de correo y prellenar el asunto y cuerpo
    private fun sendEmail(name: String, lastName: String, comuna: String, observation: String) {
        val emailBody = """
        Nombre: $name
        Apellido: $lastName
        Comuna: $comuna
        Observación: $observation
    """.trimIndent()

        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // Solo abre apps que manejan correos
            putExtra(Intent.EXTRA_EMAIL, arrayOf("destinatario@correo.com"))  // Cambia esto por el correo deseado
            putExtra(Intent.EXTRA_SUBJECT, "Información del usuario")
            putExtra(Intent.EXTRA_TEXT, emailBody)
        }

        // Verificar si hay una aplicación de correo disponible
        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar correo..."))
        } catch (e: Exception) {
            Toast.makeText(this, "No hay aplicaciones de correo instaladas.", Toast.LENGTH_SHORT).show()
        }
    }

}
