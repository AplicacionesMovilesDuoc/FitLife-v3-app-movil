
FitLife - Aplicación Móvil 
=================================
FitLife propone una experiencia integral de salud en línea, combinando planes de entrenamiento personalizados 
con asesorías nutricionales. La aplicación permite gestionar rutinas, métricas de salud y progresos de forma 
segura y escalable, fomentando hábitos de vida saludable accesibles para todas las comunidades.



1. Caso Elegido y Alcance


### Entidades Principales

- **Usuario**: Datos personales, métricas de salud (peso, altura, objetivo)
- **Entrenador**: Profesionales que crean y gestionan planes
- **Plan de Entrenamiento**: Rutinas personalizadas con ejercicios específicos
- **Plan Nutricional**: Guías alimenticias balanceadas
- **Progreso**: Seguimiento temporal de métricas del usuario

### Este app implementa: 
- **Diseño visual**: colores acordes a la temática de salud y bienestar.
- **Navegación intuitiva**: Menús claros y accesibles.
- **Funcionalidades clave**: Registro de usuarios, creación y seguimiento de planes, notificaciones.
- **Gestión de estados**: StateFlow con MVVM usando los patrones de arquitectura.
- **Persistencia de local**:  DataStore para tokens y configuraciones del usuario.
- **Recursos nativos**: Cámara y galería para captura de avatar con manejo de permisos 
- **Consumo de API**: Integración con Firebase Authentication, Firestore y Storage

2. Requisitos y Ejecución

### Stack Tecnológico

- **Lenguaje**: Kotlin 1.9.10
- **Framework UI**: Jetpack Compose
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Backend**: Firebase (Authentication, Firestore, Storage)
- **Persistencia Local**: DataStore Preferences
- **Gestión de Estado**: StateFlow + Coroutines
- **Navegación**: Navigation Compose
- **Carga de Imágenes**: Coil
- **Permisos**: Accompanist Permissions
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)


3. Arquitectura y Flujo

### Estructura de Carpetas

<img width="468" height="847" alt="image" src="https://github.com/user-attachments/assets/e8aeeb16-72cd-4ea0-afef-d33d33a4c6e3" />

<img width="469" height="616" alt="image" src="https://github.com/user-attachments/assets/c1df1207-e11f-4201-b23f-1e333a73845a" />


### Flujo de Datos

===========================================================
User Action → ViewModel → Repository → Firebase/API         |
                    ↓                                       |
                StateFlow                                   |
                    ↓                                       |
                UI Recomposition                            |
===========================================================

### Navegación

Login Screen
    ↓ (successful login)
Home Screen (root)
    ├─→ Profile Screen
    └─→ Progress Screen

**Gestión de backstack:**

- Login/Register limpian el stack al autenticarse
- Home es la raíz del stack autenticado
- Botón back desde Home cierra la app


4. Funcionalidades

### Formulario Validado - Login/Registro

**Validaciones implementadas:**

1. **Email**:
    - Campo requerido
    - Formato válido (regex)
    - Mensaje: "Email inválido"
2. **Contraseña**:
    - Campo requerido
    - Mínimo 6 caracteres
    - Toggle mostrar/ocultar
    - Mensaje: "La contraseña debe tener al menos 6 caracteres"
3. **Confirmar Contraseña** (registro):
    - Debe coincidir con contraseña
    - Mensaje: "Las contraseñas no coinciden"
4. **Nombre** (registro):
    - Campo requerido
    - Mensaje: "Por favor completa todos los campos"

**Comportamiento:**

- Botón deshabilitado durante carga
- Mensajes de error específicos por campo
- Validación en tiempo real
- Bloqueo de envío si hay errores

### Navegación entre Vistas

**Sistema de navegación implementado:**

Rutas definidas:
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Progress : Screen("progress")
}


**Flujos principales:**

1. **Autenticación**: Login ↔ Register
2. **Dashboard**: Home → Profile/Progress
3. **Logout**: Cualquier pantalla → Login (limpia stack)


Backstack management:
avController.navigate(Screen.Home.route) {
    popUpTo(Screen.Login.route) { inclusive = true }
}

**Estados vacíos/errores:**

- Progress sin registros: EmptyProgressState con CTA
- Error de red: ErrorState con botón "Reintentar"
- Carga: CircularProgressIndicator centrado


### Gestión de Estado

**Arquitectura reactiva con StateFlow:**

**Estados definidos:**
'''
data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: UserDto? = null,
    val avatarUri: Uri? = null,
    val error: String? = null
)
'''
**Sincronización UI-Estado:**
´´´
// ViewModel actualiza estado
_uiState.value = _uiState.value.copy(isLoading = true)

// UI reacciona automáticamente
when {
    uiState.isLoading -> CircularProgressIndicator()
    uiState.error != null -> ErrorMessage(uiState.error)
    else -> Content()
}
´´´