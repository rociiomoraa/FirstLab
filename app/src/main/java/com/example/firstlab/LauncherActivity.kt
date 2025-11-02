package com.example.firstlab

// Importamos las librerías necesarias para la actividad y la interfaz Compose.
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firstlab.ui.theme.FirstLabTheme

/**
 * Clase LauncherActivity
 *
 * Es la primera pantalla (actividad) que se muestra al iniciar la aplicación.
 * Su función principal es permitir al usuario introducir su nombre y, al pulsar el
 * botón "Jugar", enviar ese nombre a la MainActivity.
 *
 * Esta clase usa Jetpack Compose para crear la interfaz de usuario de forma declarativa.
 */
class LauncherActivity : ComponentActivity() {

    companion object {
        // Constante usada como clave para enviar el nombre del usuario a la siguiente actividad.
        const val USERNAME_KEY = "username"
    }

    /**
     * Método principal que se ejecuta cuando la actividad se crea por primera vez.
     * Aquí definimos el contenido de la pantalla usando Compose.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContent reemplaza el layout XML tradicional con una función Compose.
        setContent {
            // Aplicamos el tema visual de la aplicación.
            FirstLabTheme {
                // Surface define un contenedor con color de fondo.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Llamamos a la función que dibuja la interfaz principal de esta pantalla.
                    LauncherScreen { username ->
                        // Cuando el usuario pulsa "Jugar", se ejecuta este bloque:
                        // Creamos un Intent para ir a MainActivity.
                        val intent = Intent(this, MainActivity::class.java)

                        // Enviamos el nombre del usuario como dato extra.
                        intent.putExtra(USERNAME_KEY, username)

                        // Iniciamos la nueva actividad (MainActivity).
                        startActivity(intent)
                    }
                }
            }
        }
    }
}

/**
 * Función composable LauncherScreen
 *
 * Dibuja la interfaz gráfica de la pantalla inicial:
 *  - Muestra un título grande con el nombre del juego.
 *  - Un campo de texto para introducir el nombre de usuario.
 *  - Un botón "Jugar" que envía el nombre a la siguiente pantalla.
 *
 * @param onPlayClick Función lambda que recibe el nombre del usuario y se ejecuta al pulsar el botón.
 */
@Composable
fun LauncherScreen(onPlayClick: (String) -> Unit) {
    // Estado que almacena el texto introducido por el usuario.
    var username by remember { mutableStateOf("") }

    // Estructura principal en columna (los elementos se apilan verticalmente).
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp), // Márgenes internos alrededor del contenido.
        verticalArrangement = Arrangement.Center, // Centra los elementos verticalmente.
        horizontalAlignment = Alignment.CenterHorizontally // Centra los elementos horizontalmente.
    ) {
        // Cabecera (título grande de la app)
        Text(
            text = stringResource(R.string.title_launcher), // Recurso de texto
            fontSize = 32.sp, // Tamaño grande, estilo H1.
            fontWeight = FontWeight.Bold, // Texto en negrita.
            textAlign = TextAlign.Center, // Centrado horizontalmente.
            modifier = Modifier
                .padding(bottom = 40.dp) // Espacio inferior con el siguiente elemento.
                .testTag("titleText") // Identificador para pruebas automáticas (UI tests).
        )

        // Campo de texto para introducir el nombre del usuario
        OutlinedTextField(
            value = username, // Valor actual del texto.
            onValueChange = { username = it }, // Actualiza el estado cuando el usuario escribe.
            label = { Text(stringResource(R.string.btn_play)) }, // Etiqueta desde recursos
            singleLine = true, // Solo una línea de texto.
            modifier = Modifier
                .fillMaxWidth() // Ocupa todo el ancho disponible.
                .testTag("usernameField") // Identificador para pruebas o automatización.
        )

        Spacer(modifier = Modifier.height(20.dp)) // Espacio entre el campo y el botón.

        // Botón "Jugar"
        Button(
            // Solo se activa si el usuario ha escrito algo (no vacío).
            onClick = { if (username.isNotBlank()) onPlayClick(username) },
            enabled = username.isNotBlank(),
            modifier = Modifier.testTag("playButton") // Identificador del botón.
        ) {
            Text(stringResource(R.string.btn_play)) // Texto desde strings.xml
        }
    }
}
