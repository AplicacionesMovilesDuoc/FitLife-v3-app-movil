package com.mjperezm.v3_fitlife.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mjperezm.v3_fitlife.viewmodel.AppointmentViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppointmentScreen(
    onNavigateBack: () -> Unit,
    onBookingSuccess: () -> Unit,

) {
    val viewModel: AppointmentViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var selectedTrainer by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Navegación automática en caso de éxito
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onBookingSuccess()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar Cita") },
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
                        imageVector = Icons.Filled.EventAvailable,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Nueva Cita",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Agenda tu sesión con un entrenador",
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
                        text = "Detalles de la Cita",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    // Selección de entrenador
                    OutlinedTextField(
                        value = selectedTrainer,
                        onValueChange = {
                            selectedTrainer = it
                            errorMessage = null
                        },
                        label = { Text("Entrenador *") },
                        leadingIcon = {
                            Icon(Icons.Filled.Person, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Selecciona un entrenador") },
                        enabled = !uiState.isLoading,
                        singleLine = true
                    )

                    // Selección de fecha
                    OutlinedTextField(
                        value = selectedDate,
                        onValueChange = {
                            selectedDate = it
                            errorMessage = null
                        },
                        label = { Text("Fecha *") },
                        leadingIcon = {
                            Icon(Icons.Filled.CalendarToday, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("YYYY-MM-DD") },
                        enabled = !uiState.isLoading,
                        supportingText = { Text("Formato: 2025-01-15") },
                        singleLine = true
                    )

                    // Selección de hora
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = selectedTime,
                            onValueChange = {
                                selectedTime = it
                                errorMessage = null
                            },
                            label = { Text("Hora *") },
                            leadingIcon = {
                                Icon(Icons.Filled.AccessTime, contentDescription = null)
                            },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("HH:mm") },
                            enabled = !uiState.isLoading,
                            supportingText = { Text("Ej: 09:00") },
                            singleLine = true
                        )
                    }

                    // Horarios disponibles (informativo)
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
                                    Icons.Filled.Info,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Horarios Disponibles",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "• Lunes a Viernes: 08:00 - 20:00\n• Sábados: 09:00 - 14:00\n• Duración: 1 hora por sesión",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    // Descripción/Notas
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Notas (opcional)") },
                        leadingIcon = {
                            Icon(Icons.Filled.Notes, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("¿Algo que el entrenador deba saber?") },
                        enabled = !uiState.isLoading,
                        minLines = 3,
                        maxLines = 5
                    )

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    HorizontalDivider()

                    // Botón de confirmación
                    Button(
                        onClick = {
                            // Validaciones
                            when {
                                selectedTrainer.isBlank() -> {
                                    errorMessage = "Selecciona un entrenador"
                                }
                                selectedDate.isBlank() -> {
                                    errorMessage = "Ingresa una fecha"
                                }
                                selectedTime.isBlank() -> {
                                    errorMessage = "Ingresa una hora"
                                }
                                !isValidDate(selectedDate) -> {
                                    errorMessage = "Fecha inválida. Usa formato YYYY-MM-DD"
                                }
                                !isValidTime(selectedTime) -> {
                                    errorMessage = "Hora inválida. Usa formato HH:mm (24h)"
                                }
                                else -> {
                                    // Calcular hora fin (1 hora después)
                                    val horaFin = calculateEndTime(selectedTime)

                                    viewModel.bookAppointment(
                                        entrenadorId = "default-trainer-id", // TODO: Usar ID real
                                        fecha = selectedDate,
                                        horaInicio = selectedTime,
                                        horaFin = horaFin,
                                        descripcion = descripcion.ifBlank { null }
                                    )
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
                            Icon(Icons.Filled.Check, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Confirmar Cita")
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
        }
    }
}

private fun isValidDate(date: String): Boolean {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        format.isLenient = false
        format.parse(date)
        true
    } catch (e: Exception) {
        false
    }
}

private fun isValidTime(time: String): Boolean {
    return try {
        val regex = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$"
        time.matches(regex.toRegex())
    } catch (e: Exception) {
        false
    }
}

private fun calculateEndTime(startTime: String): String {
    return try {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = format.parse(startTime)
        val calendar = Calendar.getInstance()
        calendar.time = date!!
        calendar.add(Calendar.HOUR, 1)
        format.format(calendar.time)
    } catch (e: Exception) {
        startTime
    }
}