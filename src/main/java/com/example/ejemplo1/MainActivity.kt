package com.example.ejemplo1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ejemplo1.ui.theme.Ejemplo1Theme

data class Usuario(val id: Int, val nombre: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejemplo1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CrudApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CrudApp(modifier: Modifier = Modifier) {
    val usuarios = remember { mutableStateListOf<Usuario>() }
    var nombre by remember { mutableStateOf("") }
    var seleccionado by remember { mutableStateOf<Usuario?>(null) }
    var nextId by remember { mutableIntStateOf(1) }
    var mensaje by remember { mutableStateOf("Sin acciones todavía") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "CRUD de usuarios", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        if (nombre.isNotBlank()) {
                            usuarios.add(Usuario(nextId++, nombre.trim()))
                            mensaje = "Creado: $nombre"
                            nombre = ""
                        } else {
                            mensaje = "Ingrese un nombre para crear"
                        }
                    }
                ) {
                    Text("Crear")
                }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        if (usuarios.isNotEmpty()) {
                            mensaje = "Registros: ${usuarios.size}"
                        } else {
                            mensaje = "No hay registros para leer"
                        }
                    }
                ) {
                    Text("Leer")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val actual = seleccionado
                        if (actual != null && nombre.isNotBlank()) {
                            val index = usuarios.indexOfFirst { it.id == actual.id }
                            if (index != -1) {
                                usuarios[index] = actual.copy(nombre = nombre.trim())
                                mensaje = "Actualizado id ${actual.id}"
                                seleccionado = null
                                nombre = ""
                            }
                        } else {
                            mensaje = "Seleccione y escriba un nombre para actualizar"
                        }
                    }
                ) {
                    Text("Actualizar")
                }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val actual = seleccionado
                        if (actual != null) {
                            usuarios.removeAll { it.id == actual.id }
                            mensaje = "Eliminado id ${actual.id}"
                            seleccionado = null
                            nombre = ""
                        } else {
                            mensaje = "Seleccione un registro para eliminar"
                        }
                    }
                ) {
                    Text("Eliminar")
                }
            }
        }

        Text(text = mensaje, style = MaterialTheme.typography.bodyMedium)

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(usuarios, key = { it.id }) { usuario ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${usuario.id} - ${usuario.nombre}")
                        Button(onClick = {
                            seleccionado = usuario
                            nombre = usuario.nombre
                            mensaje = "Seleccionado id ${usuario.id}"
                        }) {
                            Text("Seleccionar")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CrudPreview() {
    Ejemplo1Theme {
        CrudApp()
    }
}
