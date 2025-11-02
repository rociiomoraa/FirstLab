package com.example.firstlab

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.firstlab.ui.theme.FirstLabTheme

class EndGameActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Recuperar los valores enviados a través del Intent
        val score = intent.getIntExtra(MainActivity.SCORE_KEY, 0)
        val level = intent.getIntExtra(MainActivity.LEVEL_KEY, 0)

        // Valor booleano que indica cómo terminó el juego
        val reachedMaxLevel = intent.getBooleanExtra("reachedMaxLevel", false)

        // Recuperar también el nombre del jugador
        val playerName = intent.getStringExtra(LauncherActivity.USERNAME_KEY) ?: "Jugador"

        // Establecer el contenido usando Jetpack Compose
        setContent {
            FirstLabTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { CenterAlignedTopAppBar(title = { Text(text = "End Game") }) }
                ) { innerPadding ->
                    EndGameContent(
                        name = playerName,        // Se pasa el nombre al Composable
                        score = score,
                        level = level,
                        reachedMaxLevel = reachedMaxLevel,
                        modifier = Modifier.padding(innerPadding),
                        onSendDataClick = { sendGameData(playerName, score, level) } // Acción del botón
                    )
                }
            }
        }
    }

    /**
     * Funcion que crea y lanza un Intent para enviar los datos del jugador.
     */
    private fun sendGameData(name: String, score: Int, level: Int) {
        // Crear el asunto y el cuerpo del mensaje
        val subject = "Puntuación del jugador $name"
        val body = "El jugador $name ha obtenido una puntuación de $score puntos y ha alcanzado el nivel $level."

        // Crear un Intent implícito de envío de texto
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }

        // Mostrar el selector de aplicaciones para compartir
        val shareIntent = Intent.createChooser(sendIntent, "Enviar puntuación con...")
        startActivity(shareIntent)
    }
}

@Composable
fun EndGameContent(
    name: String,
    score: Int,
    level: Int,
    reachedMaxLevel: Boolean,
    modifier: Modifier = Modifier,
    onSendDataClick: () -> Unit // Acción del botón "Enviar datos..."
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Mostrar mensaje distinto según cómo llegó el jugador
        if (reachedMaxLevel) {
            Text(
                text = "¡Felicidades, alcanzaste el nivel 10!",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                text = "Juego terminado. Pulsa el botón para volver a empezar.",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Mostrar información del juego (puntuación y nivel)
        Text(text = "Score: $score", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Level: $level", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        // Fila con la imagen y el botón "Enviar datos..."
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen mostrada desde los recursos
            Image(
                painter = painterResource(id = R.drawable.gameover),
                contentDescription = "Imagen de fin del juego",
                modifier = Modifier
                    .size(120.dp)
                    .padding(end = 16.dp)
            )

            // Botón que lanza el Intent para compartir los datos del jugador
            Button(onClick = onSendDataClick) {
                Text(text = "Enviar datos...")
            }
        }
    }
}
