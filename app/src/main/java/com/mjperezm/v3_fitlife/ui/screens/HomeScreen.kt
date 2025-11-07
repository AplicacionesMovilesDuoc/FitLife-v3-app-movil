package com.mjperezm.v3_fitlife.ui.screens



import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mjperezm.v3_fitlife.R
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

/**
 * HomeScreen - Pantalla principal de FitLife
 *
 * Esta pantalla muestra la interfaz de inicio de la aplicación
 * incluyendo el logo, mensaje de bienvenida y botones principales
 * para acceder a las funcionalidades de la app.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    // Scaffold proporciona la estructura básica de Material Design
    // con TopBar, contenido y otras secciones estándar
    Scaffold(
        topBar = {
            // Barra superior con el título de la aplicación
            TopAppBar(
                title = {
                    Text(
                        text = "FitLife",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        // Column organiza los elementos verticalmente
        Column(
            modifier = Modifier
                .fillMaxSize() // Ocupa todo el espacio disponible
                .padding(paddingValues) // Respeta el padding del Scaffold
                .padding(16.dp), // Padding adicional interno
            horizontalAlignment = Alignment.CenterHorizontally, // Centra horizontalmente
            verticalArrangement = Arrangement.spacedBy(20.dp) // Espaciado entre elementos
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            // Logo de la aplicación
            // Nota: Asegúrate de tener un logo.png en res/drawable
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo FitLife",
                modifier = Modifier.size(120.dp)
            )

            // Título de bienvenida
            Text(
                text = "¡Bienvenido a FitLife!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Subtítulo motivacional
            Text(
                text = "Tu entrenador personal en tu bolsillo",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón principal: Ver rutinas
            Button(
                onClick = {
                    // Acción del botón (por ahora vacío)
                    // TODO: Navegar a pantalla de rutinas
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Registro",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Botón secundario: Explorar ejercicios
            OutlinedButton(
                onClick = {
                    // Acción del botón (por ahora vacío)
                    // TODO: Navegar a catálogo de ejercicios
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Home",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Botón terciario: Mi progreso
            OutlinedButton(
                onClick = {
                    // Acción del botón (por ahora vacío)
                    // TODO: Navegar a estadísticas y progreso
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Profile",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Empuja el contenido hacia arriba

            // Row organiza elementos horizontalmente
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Botón de perfil
                OutlinedButton(
                    onClick = {
                        // TODO: Navegar a perfil de usuario
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Mi Perfil")
                }

                // Botón de configuración
                OutlinedButton(
                    onClick = {
                        // TODO: Navegar a configuración
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Ajustes")
                }
            }
        }
    }
}