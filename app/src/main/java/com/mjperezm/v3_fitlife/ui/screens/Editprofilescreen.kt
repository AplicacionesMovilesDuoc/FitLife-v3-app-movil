package com.mjperezm.v3_fitlife.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mjperezm.v3_fitlife.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var nombre by remember { mutableStateOf(uiState.user?.name ?: "") }
    var peso by remember { mutableStateOf(uiState.user?.peso?.toString() ?: "") }
    var altura by remember { mutableStateOf(uiState.user?.altura?.toString() ?: "") }
    var edad by remember { mutableStateOf(uiState.user?.edad?.toString() ?: "") }
    var objetivo by remember { mutableStateOf(uiState.user?.objetivo ?: "") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiState.user) {
        uiState.user?.let { user ->
            nombre = user.name ?: ""
            peso = user.peso?.toString() ?: ""
            altura = user.altura?.toString() ?: ""
            edad = user.edad?.toString() ?: ""
            objetivo = user.objetivo ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Actualiza tu información",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = uiState.user?.email ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Formulario
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Información Personal",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    // Nombre
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = {
                            nombre = it
                            errorMessage = null
                        },
                        label = { Text("Nombre completo") },
                        leadingIcon = {
                            Icon(Icons.Filled.Person, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !uiState.isLoading
                    )

                    // Edad
                    OutlinedTextField(
                        value = edad,
                        onValueChange = {
                            if (it.all { char -> char.isDigit() } && it.length <= 3) {
                                edad = it
                                errorMessage = null
                            }
                        },
                        label = { Text("Edad") },
                        leadingIcon = {
                            Icon(Icons.Filled.Cake, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        enabled = !uiState.isLoading,
                        suffix = { Text("años") }
                    )

                    HorizontalDivider()

                    Text(
                        text = "Métricas de Salud",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    // Peso
                    OutlinedTextField(
                        value = peso,
                        onValueChange = {
                            peso = it
                            errorMessage = null
                        },
                        label = { Text("Peso") },
                        leadingIcon = {
                            Icon(Icons.Filled.MonitorWeight, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        enabled = !uiState.isLoading,
                        suffix = { Text("kg") }
                    )

                    // Altura
                    OutlinedTextField(
                        value = altura,
                        onValueChange = {
                            altura = it
                            errorMessage = null
                        },
                        label = { Text("Altura") },
                        leadingIcon = {
                            Icon(Icons.Filled.Height, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        enabled = !uiState.isLoading,
                        suffix = { Text("cm") }
                    )

                    HorizontalDivider()

                    Text(
                        text = "Objetivo",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    // Objetivo - Radio buttons
                    val objetivos = listOf(
                        "Perder peso" to Icons.Filled.TrendingDown,
                        "Ganar masa muscular" to Icons.Filled.FitnessCenter,
                        "Mantener peso" to Icons.Filled.CheckCircle,
                        "Mejorar condición física" to Icons.Filled.DirectionsRun
                    )

                    objetivos.forEach { (obj, icon) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = objetivo == obj,
                                onClick = { objetivo = obj },
                                enabled = !uiState.isLoading
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = if (objetivo == obj) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = obj,
                                modifier = Modifier.weight(1f),
                                color = if (objetivo == obj) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    HorizontalDivider()

                    // Botones
                    Button(
                        onClick = {
                            // Validaciones
                            when {
                                nombre.isBlank() -> {
                                    errorMessage = "El nombre es requerido"
                                }
                                peso.isNotBlank() && peso.toDoubleOrNull() == null -> {
                                    errorMessage = "Peso inválido"
                                }
                                altura.isNotBlank() && altura.toDoubleOrNull() == null -> {
                                    errorMessage = "Altura inválida"
                                }
                                edad.isNotBlank() && edad.toIntOrNull() == null -> {
                                    errorMessage = "Edad inválida"
                                }
                                else -> {
                                    // TODO: Implementar actualización de perfil
                                    // viewModel.updateProfile(...)
                                    errorMessage = "Funcionalidad en desarrollo"
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !uiState.isLoading
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Icon(Icons.Filled.Save, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Guardar Cambios")
                        }
                    }

                    OutlinedButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isLoading
                    ) {
                        Text("Cancelar")
                    }
                }
            }

            // IMC Calculator
            if (peso.toDoubleOrNull() != null && altura.toDoubleOrNull() != null) {
                val pesoVal = peso.toDouble()
                val alturaVal = altura.toDouble() / 100 // cm a metros
                val imc = pesoVal / (alturaVal * alturaVal)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.Calculate,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Índice de Masa Corporal",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tu IMC: %.1f".format(imc),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = when {
                                imc < 18.5 -> "Bajo peso"
                                imc < 25 -> "Peso normal"
                                imc < 30 -> "Sobrepeso"
                                else -> "Obesidad"
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}