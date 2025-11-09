
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



