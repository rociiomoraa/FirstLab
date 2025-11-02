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
import androidx.compose.ui.res.stringResource
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
        val playerName = intent.getStringExtra(LauncherActivity.USERNAME_KEY) ?: getString(R.string.default_player_name)

        // Establecer el contenido usando Jetpack Compose
        setContent {
            FirstLabTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(text = stringResource(R.string.title_end_game)) }
                        )
                    }
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
     * Función que crea y lanza un Intent para enviar los datos del jugador.
     */
    private fun sendGameData(name: String, score: Int, level: Int) {
        // Crear el asunto y el cuerpo del mensaje usando strings.xml
        val subject = getString(R.string.intent_subject, name)
        val body = getString(R.string.intent_body, name, score, level)

        // Crear un Intent implícito de envío de texto
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }

        // Mostrar el selector de aplicaciones para compartir
        val shareIntent = Intent.createChooser(sendIntent, getString(R.string.intent_chooser_title))
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
                text = stringResource(R.string.text_congratulations),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                text = stringResource(R.string.text_end_message),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Mostrar información del juego (puntuación y nivel)
        Text(text = stringResource(R.string.text_score, score), style = MaterialTheme.typography.headlineMedium)
        Text(text = stringResource(R.string.text_level, level), style = MaterialTheme.typography.headlineMedium)

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
                contentDescription = stringResource(R.string.content_description_game_over),
                modifier = Modifier
                    .size(120.dp)
                    .padding(end = 16.dp)
            )

            // Botón que lanza el Intent para compartir los datos del jugador
            Button(onClick = onSendDataClick) {
                Text(text = stringResource(R.string.btn_send_data))
            }
        }
    }
}

