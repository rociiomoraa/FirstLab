package com.example.firstlab

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstlab.ui.theme.FirstLabTheme
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.colorResource
import androidx.compose.material3.MaterialTheme


class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity"
        const val SCORE_KEY = "score"
        const val LEVEL_KEY = "level"
    }

    // Variable que almacena el nombre del usuario recibido desde LauncherActivity.
    // Se usa 'var' en lugar de 'val' para poder asignarle el valor del intent en tiempo de ejecución.
    private var name: String = ""
    private var score : Int = 0
    private var level : Int = 0


    // Función lambda que incrementa la puntuación y recalcula el nivel.
    // Ahora el incremento es un número aleatorio entre 1 y el valor actual del nivel.
    // Si el nivel es 0 (inicio del juego), se usa 1 como mínimo para evitar errores.
    private val incrementScoreAndLevel: (Int) -> Map<String, Int> = {
        // Calcula el rango de incremento (entre 1 y el nivel actual o 1 si nivel es 0)
        val incremento = (1..(if (level > 0) level else 1)).random()

        // Suma el incremento al score actual
        score += incremento

        // Calcula el nuevo nivel (1 nivel cada 10 puntos)
        level = score / 10

        // Devuelve los valores actualizados como mapa
        mapOf(SCORE_KEY to score, LEVEL_KEY to level)
    }


    // Ahora la función recibe un parámetro booleano que indica
    // si el jugador llegó al nivel máximo o terminó manualmente.
    private val goToEndGameActivity: (Boolean) -> Unit = { reachedMaxLevel ->
        val intent = Intent(this, EndGameActivity::class.java)
        intent.putExtra(SCORE_KEY, score)
        intent.putExtra(LEVEL_KEY, level)
        // Nuevo valor booleano que distingue la forma de llegar al final del juego.
        intent.putExtra("reachedMaxLevel", reachedMaxLevel)
        // Enviar también el nombre del jugador para usarlo en EndGameActivity
        intent.putExtra(LauncherActivity.USERNAME_KEY, name)
        startActivity(intent)
    }



    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recuperamos el nombre de usuario que se envió desde la LauncherActivity.
        // Si no se recibe, se utiliza "Jugador" por defecto.
        name = intent.getStringExtra(LauncherActivity.USERNAME_KEY) ?: "Jugador"

        savedInstanceState?.let { instance ->
            score = instance.getInt(SCORE_KEY,0)
            level = instance.getInt(LEVEL_KEY,0)
        }

        enableEdgeToEdge()
        setContent {
            FirstLabTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { CenterAlignedTopAppBar( title = { Text(text = "Super Game Counter") } ) }
                ) { innerPadding ->
                    GameStateDisplay(name, score, level, Modifier.padding(innerPadding), incrementScoreAndLevel, goToEndGameActivity)
                }
            }
        }
    }

    // Método llamado cuando la actividad está a punto de hacerse visible
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: La actividad está a punto de ser visible.")
    }

    // Método llamado cuando la actividad se ha hecho visible
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: La actividad está en primer plano y se puede interactuar con ella.")
    }

    // Método llamado cuando otra actividad toma el control, poniendo esta actividad en segundo plano
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: La actividad está en segundo plano.")
    }

    // Método llamado cuando la actividad ya no es visible para el usuario
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: La actividad ya no es visible.")
    }

    // Método llamado justo antes de que la actividad sea destruida
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: La actividad ha sido destruida.")
    }

    // Método llamado antes de que la actividad pase a segundo plano y guarde el estado
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, score)
        outState.putInt(LEVEL_KEY, level)
        Log.d(TAG, "onSaveInstanceState: Guardando el estado de la actividad.")
    }

    // Método llamado cuando se restaura el estado previamente guardado
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(TAG, "onRestoreInstanceState: Restaurando el estado de la actividad.")
    }
}

@Composable
fun GameStateDisplay(
    name: String,
    initScore: Int,
    initLevel: Int,
    modifier: Modifier = Modifier,
    onIncButtonClick: (Int) -> Map<String, Int>,
    onEndGameButtonClick: (Boolean) -> Unit // Cambiado para aceptar un booleano
) {
    var score by remember { mutableIntStateOf(initScore) }
    var level by remember { mutableIntStateOf(initLevel) }

    // Si el jugador alcanza el nivel 10 o superior, se abre directamente la pantalla de fin del juego.
    if (level >= 10) {
        onEndGameButtonClick(true)
    }

    // Determinar el color de fondo según el nivel actual.
    // Rojo: niveles 0–2, Naranja: 3–6, Verde oscuro: 7–9.
    val backgroundColor = when (level) {
        in 0..2 -> colorResource(id = R.color.level_red)
        in 3..6 -> colorResource(id = R.color.level_orange)
        in 7..9 -> colorResource(id = R.color.level_dark_green)
        else -> Color.Gray // Color por defecto si el nivel no entra en los rangos
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Primera fila con un elemento
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            // Texto de saludo usando stringResource con el nombre del jugador
            Text(text = stringResource(id = R.string.text_hello, name))
        }

        // Segunda fila con dos columnas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Primera columna con dos elementos y separador
            // El color de fondo cambia dinámicamente según el nivel.
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(backgroundColor)
                    .padding(8.dp)
            ) {
                Text(text = stringResource(R.string.text_score, score))
                Spacer(Modifier.height(8.dp))
                Text(text = stringResource(R.string.text_level, level))
            }

            Spacer(Modifier.width(8.dp))

            // Segunda columna con un elemento
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Al pulsar el botón, se incrementa la puntuación en un número aleatorio
                // entre 1 y el valor actual del nivel del jugador.
                StandardButton(stringResource(id = R.string.btn_increase_score)) {
                    val result = onIncButtonClick(1)
                    score = result[MainActivity.SCORE_KEY]!!
                    level = result[MainActivity.LEVEL_KEY]!!

                    // Si el jugador llega al nivel 10 tras incrementar, se lanza la pantalla de fin del juego.
                    if (level >= 10) {
                        onEndGameButtonClick(true)
                    }
                }

                Spacer(Modifier.height(16.dp)) // Espacio visual entre los botones

                // Nuevo botón para decrementar la puntuación.
                // Cada vez que se pulsa, la puntuación disminuye en el doble del nivel actual (2 * level).
                // Si la puntuación queda negativa, se ajusta automáticamente a 0.
                StandardButton(stringResource(id = R.string.btn_decrease_score)) {
                    val decremento = 2 * level
                    score = (score - decremento).coerceAtLeast(0)
                    level = score / 10
                }
            }
        }

        // Si el jugador llega al nivel 5, se muestra un texto motivacional
        // centrado horizontalmente y con separación vertical.
        if (level == 5) {
            Spacer(Modifier.height(40.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.text_motivational),
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            StandardButton(stringResource(id = R.string.btn_end_game)) {
                // Acción al hacer clic: abrir la pantalla de fin del juego manualmente.
                // También se envían los datos actuales de puntuación y nivel.
                // En este caso, se pasa false porque el jugador terminó por decisión propia.
                onEndGameButtonClick(false)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun ShowVariables(
    label: String,
    value : Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = "$label -> $value",
        modifier = modifier // Se pasa el modificador para ajustar la apariencia o el layout
    )
}

@Composable
fun StandardButton(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    // Composable que muestra un botón estándar con un texto
    Button(onClick = onClick) {
        Text(
            text = label,
            modifier = modifier // Se pasa el modificador para ajustar la apariencia o el layout
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FirstLabTheme {
        Greeting("Android")
    }
}