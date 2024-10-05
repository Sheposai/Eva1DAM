package com.example.eva1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NewUserActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        // Inicializar DatabaseHelper
        databaseHelper = DatabaseHelper(this)

        // Referencias a los campos de texto y botones
        val usernameInput: EditText = findViewById(R.id.new_username_input)
        val passwordInput: EditText = findViewById(R.id.new_password_input)
        val saveUserBtn: Button = findViewById(R.id.save_user_btn)
        val deleteUserBtn: Button = findViewById(R.id.delete_user_btn)
        val updateUserBtn: Button = findViewById(R.id.update_user_btn)
        val searchUserBtn: Button = findViewById(R.id.search_user_btn)

        // Acción del botón "Guardar Usuario"
        saveUserBtn.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val isUserAdded = databaseHelper.addUser(username, password)
                if (isUserAdded) {
                    Toast.makeText(this, "El usuario se creo con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error: El usuario ya existe", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, complete los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Eliminar Usuario
        deleteUserBtn.setOnClickListener {
            val username = usernameInput.text.toString()
            if (username.isNotEmpty()) {
                val isDeleted = databaseHelper.deleteUser(username)
                Toast.makeText(this, if (isDeleted) "Usuario eliminado" else "Error al eliminar el usuario", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, introdusca el nombre de usuario", Toast.LENGTH_SHORT).show()
            }
        }

        // Actualizar Usuario
        updateUserBtn.setOnClickListener {
            val username = usernameInput.text.toString()
            val newPassword = passwordInput.text.toString()
            if (username.isNotEmpty() && newPassword.isNotEmpty()) {
                val isUpdated = databaseHelper.updateUser(username, username, newPassword)
                Toast.makeText(this, if (isUpdated) "Usuario actualizado" else "Error al actualizar el usuario", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Buscar Usuario
        searchUserBtn.setOnClickListener {
            val username = usernameInput.text.toString()
            if (username.isNotEmpty()) {
                val user = databaseHelper.getUser(username)
                if (user != null) {
                    Toast.makeText(this, "Usuario encontrado: ${user.getAsString("username")}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, ingresa el nombre de usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
