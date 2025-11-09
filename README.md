FitLife - Aplicación Móvil de Salud y Bienestar

FitLife es una aplicación móvil nativa de Android que ofrece una experiencia integral de salud, combinando planes de entrenamiento y nutricionales personalizados. La aplicación permite a los usuarios gestionar sus rutinas, monitorear métricas de salud y seguir su progreso, fomentando hábitos de vida saludables de una manera accesible y escalable.

1. Alcance y Características Principales

El objetivo de FitLife es proporcionar una herramienta robusta para el seguimiento de la salud y el fitness, centrada en el usuario y su progreso.

### Entidades del Modelo

•Usuario: Gestiona datos personales, credenciales y métricas de salud clave (peso, altura, objetivo).

•Entrenador: Profesionales responsables de crear y asignar planes.

•Plan de Entrenamiento: Conjunto de rutinas personalizadas con ejercicios específicos.

•Plan Nutricional: Guías alimenticias balanceadas para complementar el entrenamiento.

•Progreso: Registro histórico del seguimiento de las métricas del usuario para visualizar su evolución.

### Funcionalidades Implementadas

•Diseño Moderno: Interfaz de usuario limpia y atractiva con Jetpack Compose, siguiendo una paleta de colores orientada al bienestar.

•Autenticación Segura: Registro e inicio de sesión de usuarios utilizando Firebase Authentication.

•Gestión de Datos en la Nube: Creación y seguimiento de planes almacenados en Firestore.

•Almacenamiento de Archivos: Subida y gestión de avatares de perfil con Firebase Storage.

•Persistencia Local: Uso de DataStore para guardar configuraciones locales y tokens de sesión, garantizando una experiencia de usuario fluida.

•Acceso a Recursos Nativos: Integración con la cámara y la galería para la selección de fotos de perfil, incluyendo un manejo robusto de permisos en tiempo de ejecución.

•Arquitectura Reactiva: Implementación del patrón MVVM con StateFlow y Coroutines para una gestión de estado eficiente y predecible.

•Navegación Intuitiva: Sistema de navegación modular construido con Navigation Compose que gestiona el flujo de la aplicación de forma lógica.

2. Requisitos Técnicos

Para compilar y ejecutar este proyecto, necesitas un entorno de desarrollo configurado para Android con las siguientes especificaciones.

Stack Tecnológico

•Lenguaje: Kotlin 1.9.10

•Framework UI: Jetpack Compose

•Arquitectura: MVVM (Model-View-ViewModel)

•Backend (BaaS): Firebase (Authentication, Firestore, Storage)

•Persistencia Local: Jetpack DataStore Preferences

•Gestión de Estado: StateFlow + Kotlin Coroutines

•Navegación: Navigation Compose

•Carga de Imágenes: Coil

•Manejo de Permisos: Accompanist Permissions

•SDK Mínimo: API 24 (Android 7.0 Nougat)

•SDK Objetivo: API 34 (Android 14)

3. Arquitectura y Flujo de Datos

El proyecto sigue las mejores prácticas de arquitectura de software para garantizar un código mantenible, escalable y testeable.

### Estructura del Proyecto

La organización de carpetas sigue el principio de separación de responsabilidades, dividiendo el código por capas (UI, ViewModel, Data) y por funcionalidad.

1. UI (Vista) notifica al ViewModel sobre una acción del usuario (ej: clic en un botón).
2. El ViewModel procesa la lógica de negocio y solicita datos al Repository.
3. El Repository obtiene los datos, ya sea de una fuente remota (Firebase) o local (DataStore).
4. El ViewModel recibe los datos y actualiza su StateFlow, emitiendo un nuevo estado de la UI
5. La UI, que está observando el StateFlow, reacciona automáticamente al cambio de estado y se recompone para mostrar la información actualizada.

<img width="468" height="847" alt="image" src="https://github.com/user-attachments/assets/e8aeeb16-72cd-4ea0-afef-d33d33a4c6e3" />

<img width="469" height="616" alt="image" src="https://github.com/user-attachments/assets/c1df1207-e11f-4201-b23f-1e333a73845a" />



### Flujo de Datos  (Patrón MVVM)

<img width="430" height="136" alt="image" src="https://github.com/user-attachments/assets/efe3bd76-671d-4ab2-8b8b-1d73265e6000" />


### Navegación

<img width="225" height="127" alt="image" src="https://github.com/user-attachments/assets/22b9e28f-b4c2-43c8-81c8-529ba955d460" />


### 4. Funcionalidades Detalladas

### Formularios de Autenticación con Validación

Se ha implementado un sistema robusto de validación de formularios en tiempo real para el registro y el inicio de sesión.

- Validaciones:
    - Email: Campo requerido y con formato válido.
    - Contraseña: Campo requerido y con un mínimo de 6 caracteres.
    - Confirmación de Contraseña: Debe coincidir con la contraseña original.
    - Nombre: Campo requerido.
- Experiencia de Usuario:
    - Botones deshabilitados durante los estados de carga para evitar envíos duplicados.
    - Mensajes de error claros y específicos por cada campo de entrada.
    - El botón de envío se mantiene deshabilitado hasta que todos los campos son válidos.


### Sistema de Navegación

La navegación se gestiona con ‘Navigation Compose’, definiendo rutas claras para cada pantalla y controlando el backstack de forma eficiente.

- Rutas Definidas: Login, Register, Home, Profile, Progress.
- Flujos de Navegación:
    - Autenticación: El usuario puede navegar entre Login y Register.
    - Dashboard Principal: Una vez autenticado, Home se convierte en la pantalla raíz, desde donde se puede acceder a Profile y Progress.
    - Cierre de Sesión: Al hacer logout, el stack de navegación se limpia y el usuario es redirigido a Login.


    
**Gestión de backstack:**

- Login/Register limpian el stack al autenticarse
- Home es la raíz del stack autenticado
- Botón back desde Home cierra la app

  // Al navegar a Home después del login, se elimina el historial de autenticación.
  navController.navigate(Screen.Home.route) {
  popUpTo(Screen.Login.route) { inclusive = true }
  }


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

**Sincronización UI-Estado:**

// ViewModel actualiza estado
_uiState.value = _uiState.value.copy(isLoading = true)

// UI reacciona automáticamente
when {
    uiState.isLoading -> CircularProgressIndicator()
    uiState.error != null -> ErrorMessage(uiState.error)
    else -> Content()
}

### Gestión de Estado Reactiva

La UI es un reflejo del estado de la aplicación gracias a StateFlow. 
Cada pantalla gestiona sus propios estados de carga, error y contenido.

- Definición de Estados de UI (UIState):

<img width="296" height="115" alt="image" src="https://github.com/user-attachments/assets/ecf6bc35-8ad8-4d94-aded-146622d0f878" />


- Renderizado Condicional en la UI:

<img width="483" height="134" alt="image" src="https://github.com/user-attachments/assets/74e7347b-b355-4aac-8392-d7e544c57e06" />

