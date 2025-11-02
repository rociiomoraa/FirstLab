package com.example.firstlab

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

        // Establecer el contenido usando Jetpack Compose
        setContent {
            FirstLabTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { CenterAlignedTopAppBar(title = { Text(text = "End Game") }) }
                ) { innerPadding ->
                    EndGameContent(
                        score = score,
                        level = level,
                        reachedMaxLevel = reachedMaxLevel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun EndGameContent(score: Int, level: Int, reachedMaxLevel: Boolean, modifier: Modifier = Modifier) {
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
            // Asegúrate de tener una imagen en res/drawable
            Image(
                painter = painterResource(id = R.drawable.gameover),
                contentDescription = "Imagen de fin del juego",
                modifier = Modifier
                    .size(120.dp)
                    .padding(end = 16.dp)
            )

            // Botón que servirá para enviar los datos del jugador (nombre, score, nivel)
            Button(onClick = {
                // En esta actividad solo lo colocamos visualmente.
                // La funcionalidad de envío real se implementará en la siguiente actividad.
            }) {
                Text(text = "Enviar datos...")
            }
        }
    }
}
