package com.mjperezm.v3_fitlife.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mjperezm.v3_fitlife.data.remote.PlanDto
import com.mjperezm.v3_fitlife.viewmodel.PlanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanListScreen(
    onNavigateBack: () -> Unit,
    onPlanSelected: (String) -> Unit,
    viewModel: PlanViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedTipo by remember { mutableStateOf<String?>(null) }
    var selectedDificultad by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Planes de Entrenamiento") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(Icons.Filled.FilterList, contentDescription = "Filtros")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Barra de búsqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.searchPlanes(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Buscar planes...") },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Buscar")
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            searchQuery = ""
                            viewModel.loadPlanesEntrenamiento()
                        }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Limpiar")
                        }
                    }
                },
                singleLine = true
            )

            // Chips de filtros activos
            if (selectedTipo != null || selectedDificultad != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    selectedTipo?.let { tipo ->
                        FilterChip(
                            selected = true,
                            onClick = {
                                selectedTipo = null
                                viewModel.loadPlanesEntrenamiento(null, selectedDificultad)
                            },
                            label = { Text(tipo) },
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = "Quitar filtro",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                    }

                    selectedDificultad?.let { dificultad ->
                        FilterChip(
                            selected = true,
                            onClick = {
                                selectedDificultad = null
                                viewModel.loadPlanesEntrenamiento(selectedTipo, null)
                            },
                            label = { Text(dificultad) },
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = "Quitar filtro",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Contenido
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    uiState.error != null -> {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Error,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = uiState.error ?: "",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(onClick = { viewModel.loadPlanesEntrenamiento() }) {
                                Text("Reintentar")
                            }
                        }
                    }

                    uiState.planesEntrenamiento.isEmpty() -> {
                        EmptyPlanesState()
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.planesEntrenamiento) { plan ->
                                PlanCard(
                                    plan = plan,
                                    onClick = { onPlanSelected(plan._id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo de filtros
    if (showFilterDialog) {
        FilterDialog(
            currentTipo = selectedTipo,
            currentDificultad = selectedDificultad,
            onDismiss = { showFilterDialog = false },
            onApply = { tipo, dificultad ->
                selectedTipo = tipo
                selectedDificultad = dificultad
                viewModel.loadPlanesEntrenamiento(tipo, dificultad)
                showFilterDialog = false
            }
        )
    }
}

@Composable
private fun PlanCard(
    plan: PlanDto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.FitnessCenter,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = plan.nombre,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = plan.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }

                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = "Ver más",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                plan.duracion?.let { duracion ->
                    AssistChip(
                        onClick = { },
                        label = { Text(duracion) },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Timer,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                }

                plan.dificultad?.let { dificultad ->
                    AssistChip(
                        onClick = { },
                        label = { Text(dificultad) },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.BarChart,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                }

                plan.tipo?.let { tipo ->
                    AssistChip(
                        onClick = { },
                        label = { Text(tipo) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyPlanesState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.FitnessCenter,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No hay planes disponibles",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Intenta ajustar los filtros de búsqueda",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FilterDialog(
    currentTipo: String?,
    currentDificultad: String?,
    onDismiss: () -> Unit,
    onApply: (String?, String?) -> Unit
) {
    var selectedTipo by remember { mutableStateOf(currentTipo) }
    var selectedDificultad by remember { mutableStateOf(currentDificultad) }

    val tipos = listOf("cardio", "fuerza", "flexibilidad", "hiit", "yoga")
    val dificultades = listOf("principiante", "intermedio", "avanzado")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filtros") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tipo
                Text(
                    text = "Tipo de Entrenamiento",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                tipos.forEach { tipo ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedTipo == tipo,
                            onClick = {
                                selectedTipo = if (selectedTipo == tipo) null else tipo
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = tipo.capitalize(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                HorizontalDivider()

                // Dificultad
                Text(
                    text = "Dificultad",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                dificultades.forEach { dificultad ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedDificultad == dificultad,
                            onClick = {
                                selectedDificultad = if (selectedDificultad == dificultad) null else dificultad
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = dificultad.capitalize(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onApply(selectedTipo, selectedDificultad)
                }
            ) {
                Text("Aplicar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}