# FitLife v3 - Aplicación Móvil de Fitness 

Aplicación móvil Android para gestión de entrenamiento personal, citas con entrenadores, planes de entrenamiento y seguimiento de progreso físico.

---

## Integrantes del Equipo

- **María José Pérez Martínez** ([@mjperezm](https://github.com/mjperezm)) - Desarrolladora Full Stack
- **Karin López Sánchez** ([@Karylopez](https://github.com/Karylopez)) - Desarrolladora Full Stack

---

## Funcionalidades

### Autenticación y Gestión de Usuarios
- Registro de usuarios con roles (USUARIO / ENTRENADOR)
- Login con JWT (JSON Web Token)
- Gestión de sesión persistente con DataStore
- Perfil de usuario con avatar personalizable
- Cierre de sesión seguro

### Gestión de Progreso
- Registro de progreso físico (peso, medidas, notas)
- Visualización de historial de progreso
- Ordenamiento automático por fecha (más reciente primero)
- Validación de datos (peso > 0)
- Gráficos de evolución del progreso

### Sistema de Citas
- Agendar citas con entrenadores disponibles
- Visualización de citas programadas
- Estados de cita: CONFIRMADA, PENDIENTE, CANCELADA
- Cancelación de citas
- Historial de citas pasadas

### Planes de Entrenamiento y Nutrición
- Visualización de planes de entrenamiento asignados
- Detalles de ejercicios y rutinas
- Planes nutricionales personalizados
- Seguimiento de objetivos

### Características Técnicas
- Arquitectura MVVM (Model-View-ViewModel)
- Jetpack Compose para UI moderna y declarativa
- Manejo de estados con StateFlow y Coroutines
- Consumo de API REST con Retrofit
- Persistencia local con DataStore Preferences
- Navegación con Navigation Compose
- Coil para carga optimizada de imágenes
- Manejo de permisos con Accompanist
- Material Design 3
- Pruebas unitarias (61 casos de prueba)

---

## Endpoints Utilizados

### Backend Propio (NestJS + MongoDB Atlas)

**Base URL:** `https://fitlife-api-v2.onrender.com/api`

#### Autenticación
```
POST /auth/register
```
Registro de nuevos usuarios. Permite crear cuentas con rol USUARIO o ENTRENADOR.

**Body:**
```json
{
  "email": "usuario@ejemplo.com",
  "password": "password123",
  "role": "USUARIO",
  "nombre": "Nombre Completo",
  "telefono": "+56912345678"
}
```

```
POST /auth/login
```
Inicio de sesión. Retorna JWT token para autenticación.

**Body:**
```json
{
  "email": "usuario@ejemplo.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "_id": "user_id",
    "email": "usuario@ejemplo.com",
    "role": "USUARIO",
    "nombre": "Nombre Completo"
  }
}
```

```
GET /auth/profile
```
Obtener perfil del usuario autenticado. Requiere token JWT en header.

**Headers:**
```
Authorization: Bearer {token}
```

#### Usuarios
```
GET /users/profile
```
Obtener perfil completo del usuario con datos adicionales.

```
PATCH /users/profile
```
Actualizar información del perfil de usuario.

#### Progreso
```
GET /progreso
```
Obtener historial completo de progreso del usuario autenticado.

**Response:**
```json
[
  {
    "_id": "progreso_id",
    "usuario": "user_id",
    "fecha": "2024-12-15T14:30:00.000Z",
    "peso": 75.5,
    "medidas": "Pecho: 95cm, Cintura: 80cm",
    "notas": "Buen progreso esta semana"
  }
]
```

```
POST /progreso
```
Registrar nuevo progreso físico.

**Body:**
```json
{
  "fecha": "2024-12-15",
  "peso": 75.5,
  "medidas": "Pecho: 95cm",
  "notas": "Primera medición"
}
```

#### Citas
```
GET /citas
```
Obtener todas las citas del usuario (próximas y pasadas).

```
POST /citas
```
Crear nueva cita con un entrenador.

**Body:**
```json
{
  "entrenador": "trainer_id",
  "fecha": "2024-12-20",
  "horaInicio": "10:00",
  "horaFin": "11:00",
  "descripcion": "Sesión de entrenamiento funcional"
}
```

```
DELETE /citas/:id
```
Cancelar una cita existente.

#### Planes
```
GET /planes
```
Obtener planes de entrenamiento y nutrición asignados al usuario.

### Servicios Externos

**MongoDB Atlas**
- Base de datos en la nube para almacenamiento persistente
- Colecciones: usuarios, progreso, citas, planes

**Render**
- Plataforma de deployment para el backend
- URL de producción: https://fitlife-api-v2.onrender.com

---

## Instrucciones para Ejecutar el Proyecto

### Requisitos Previos

- **Android Studio** Hedgehog | 2023.1.1 o superior
- **JDK** 11 o superior
- **SDK de Android** API nivel 23-36
- **Dispositivo Android** o Emulador (Android 6.0+, API 23+)
- **Conexión a Internet** (para conectar con la API)

### Pasos de Instalación

#### 1. Clonar el Repositorio de la App Móvil

```bash
git clone https://github.com/AplicacionesMovilesDuoc/FitLife-v3-app-movil.git
cd FitLife-v3-app-movil
```

#### 2. Configurar el Backend

El backend ya está desplegado y funcionando en Render:

 **[Repositorio del Backend - FitLife API v2](https://github.com/AplicacionesMovilesDuoc/Fitlife-api-v2.git)**

**URL de producción:** `https://fitlife-api-v2.onrender.com/api`

Si necesitas ejecutar el backend localmente:

```bash
# Clonar repositorio del backend
git clone https://github.com/AplicacionesMovilesDuoc/Fitlife-api-v2.git
cd Fitlife-api-v2

# Instalar dependencias
npm install

# Configurar variables de entorno (.env)
PORT=3000
MONGODB_URI=mongodb+srv://usuario:password@cluster.mongodb.net/fitlife
JWT_SECRET=tu_secret_key

# Ejecutar en desarrollo
npm run start:dev

# El backend estará disponible en: http://localhost:3000/api
```

**Tecnologías del Backend:**
- NestJS (Framework Node.js)
- MongoDB Atlas (Base de datos NoSQL)
- Mongoose (ODM)
- JWT (Autenticación)
- Bcrypt (Encriptación de contraseñas)
- Passport (Estrategias de autenticación)

#### 3. Configurar la URL del Backend en la App

La app ya está configurada para usar la API en producción, pero si necesitas cambiarla:

Edita el archivo: `app/src/main/java/com/mjperezm/v3_fitlife/data/remote/RetrofitClient.kt`

```kotlin
private const val BASE_URL = "https://fitlife-api-v2.onrender.com/api/"

// Para desarrollo local:
// private const val BASE_URL = "http://10.0.2.2:3000/api/" // Emulador
// private const val BASE_URL = "http://TU-IP:3000/api/" // Dispositivo físico
```

#### 4. Sincronizar el Proyecto en Android Studio

1. Abre Android Studio
2. File → Open → Selecciona la carpeta del proyecto
3. Espera a que Gradle sincronice automáticamente
4. Si no sincroniza, haz clic en: File → Sync Project with Gradle Files

#### 5. Ejecutar la Aplicación

**Opción A: Desde Android Studio**
1. Conecta un dispositivo Android o inicia un emulador
2. Selecciona el dispositivo en la barra superior
3. Click en el botón Run o presiona `Shift + F10`
4. La app se instalará y abrirá automáticamente


---

## APK Firmado

### Descargar APK de Producción

🔗 **[Descargar FitLife v3.0 APK](https://github.com/AplicacionesMovilesDuoc/FitLife-v3-app-movil/releases/tag/v3.0)**


### Instalación del APK

1. Descarga el archivo `FitLife-v3.0-release.apk` desde el enlace de arriba
2. En tu dispositivo Android:
   - Ve a Configuración → Seguridad
   - Habilita "Instalar aplicaciones de fuentes desconocidas" o "Orígenes desconocidos"
3. Abre el archivo APK descargado
4. Sigue las instrucciones de instalación en pantalla
5. Una vez instalado, abre FitLife y comienza a usarlo

### Permisos Requeridos

- **Internet:** Para conectarse a la API
- **Almacenamiento:** Para guardar avatar y datos locales (opcional)

### Ubicación del Keystore (.jks)

El archivo de firma del APK se encuentra en:

```
 app/keystore/fitlife_keystore.jks
```

**Información del Keystore:**
- Alias: fitlife
- Algoritmo: RSA
- Validez: 25 años
- Organización: DuocUC

---

## Código Fuente

### Aplicación Móvil (Android - Kotlin)

 **[Repositorio App Móvil - FitLife v3](https://github.com/AplicacionesMovilesDuoc/FitLife-v3-app-movil)**

#### Estructura del Proyecto

```
FitLife-v3-app-movil/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/mjperezm/v3_fitlife/
│   │   │   │   ├── data/
│   │   │   │   │   ├── local/
│   │   │   │   │   │   ├── AvatarRepository.kt
│   │   │   │   │   │   └── SessionManager.kt
│   │   │   │   │   └── remote/
│   │   │   │   │       ├── ApiService.kt
│   │   │   │   │       ├── RetrofitClient.kt
│   │   │   │   │       ├── AuthInterceptor.kt
│   │   │   │   │       └── dto/
│   │   │   │   │           ├── LoginRequest.kt
│   │   │   │   │           ├── LoginResponse.kt
│   │   │   │   │           ├── SignupRequest.kt
│   │   │   │   │           ├── UserDto.kt
│   │   │   │   │           ├── CitaDto.kt
│   │   │   │   │           └── ProgresoDto.kt
│   │   │   │   ├── model/
│   │   │   │   │   ├── Exercise.kt
│   │   │   │   │   └── Workout.kt
│   │   │   │   ├── ui/
│   │   │   │   │   ├── components/
│   │   │   │   │   │   └── [Componentes reutilizables]
│   │   │   │   │   ├── navigation/
│   │   │   │   │   │   └── AppNavigation.kt
│   │   │   │   │   ├── screens/
│   │   │   │   │   │   ├── auth/
│   │   │   │   │   │   │   ├── LoginScreen.kt
│   │   │   │   │   │   │   └── SignupScreen.kt
│   │   │   │   │   │   ├── home/
│   │   │   │   │   │   │   └── HomeScreen.kt
│   │   │   │   │   │   ├── profile/
│   │   │   │   │   │   │   └── ProfileScreen.kt
│   │   │   │   │   │   ├── progress/
│   │   │   │   │   │   │   └── ProgressScreen.kt
│   │   │   │   │   │   └── appointment/
│   │   │   │   │   │       └── AppointmentScreen.kt
│   │   │   │   │   └── theme/
│   │   │   │   │       ├── Color.kt
│   │   │   │   │       ├── Theme.kt
│   │   │   │   │       └── Type.kt
│   │   │   │   ├── viewmodel/
│   │   │   │   │   ├── AuthViewModel.kt
│   │   │   │   │   ├── ProfileViewModel.kt
│   │   │   │   │   ├── ProgressViewModel.kt
│   │   │   │   │   ├── AppointmentViewModel.kt
│   │   │   │   │   └── PlanViewModel.kt
│   │   │   │   └── MainActivity.kt
│   │   │   └── res/
│   │   └── test/
│   │       └── java/com/mjperezm/v3_fitlife/
│   │           ├── ProgressUiStateTest.kt
│   │           ├── ProgressWeightValidationTest.kt
│   │           ├── ProfileUiStateTest.kt
│   │           ├── SignupRequestValidationTest.kt
│   │           ├── AppointmentUiStateTest.kt
│   │           └── DateFormattingUtilTest.kt
│   ├── keystore/
│   │   └── fitlife_keystore.jks
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

#### Tecnologías y Dependencias

```kotlin
// UI & Compose
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.activity:activity-compose:1.8.2")
implementation("androidx.navigation:navigation-compose:2.7.6")

// ViewModel & Lifecycle
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

// Networking
implementation("com.squareup.retrofit2:retrofit:2.11.0")
implementation("com.squareup.retrofit2:converter-gson:2.11.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// DataStore
implementation("androidx.datastore:datastore-preferences:1.0.0")

// Image Loading
implementation("io.coil-kt:coil-compose:2.5.0")

// Testing
testImplementation("junit:junit:4.13.2")
testImplementation("org.mockito:mockito-core:5.7.0")
```

### Backend (Microservicios - NestJS)

 **[Repositorio Backend - FitLife API v2](https://github.com/AplicacionesMovilesDuoc/Fitlife-api-v2)**

#### Estructura del Backend

```
Fitlife-api-v2/
├── src/
│   ├── auth/
│   │   ├── auth.controller.ts
│   │   ├── auth.service.ts
│   │   ├── auth.module.ts
│   │   ├── dto/
│   │   │   ├── login.dto.ts
│   │   │   └── signup.dto.ts
│   │   └── strategies/
│   │       └── jwt.strategy.ts
│   ├── users/
│   │   ├── users.controller.ts
│   │   ├── users.service.ts
│   │   ├── users.module.ts
│   │   └── schemas/
│   │       └── user.schema.ts
│   ├── progreso/
│   │   ├── progreso.controller.ts
│   │   ├── progreso.service.ts
│   │   ├── progreso.module.ts
│   │   └── schemas/
│   │       └── progreso.schema.ts
│   ├── citas/
│   │   ├── citas.controller.ts
│   │   ├── citas.service.ts
│   │   ├── citas.module.ts
│   │   └── schemas/
│   │       └── cita.schema.ts
│   ├── planes/
│   │   ├── planes.controller.ts
│   │   ├── planes.service.ts
│   │   ├── planes.module.ts
│   │   └── schemas/
│   │       └── plan.schema.ts
│   ├── app.module.ts
│   └── main.ts
├── .env
├── package.json
└── README.md
```

#### Tecnologías del Backend

- **NestJS 10.x** - Framework progresivo de Node.js
- **TypeScript** - Lenguaje tipado
- **MongoDB Atlas** - Base de datos NoSQL en la nube
- **Mongoose** - ODM para MongoDB
- **Passport JWT** - Autenticación con tokens
- **Bcrypt** - Hash de contraseñas
- **Class Validator** - Validación de DTOs
- **Render** - Plataforma de deployment

#### Deployment en Render

El backend está desplegado en Render con:
- **Auto-deployment** desde la rama main
- **Variables de entorno** configuradas
- **Sleeping** después de 15 minutos de inactividad (plan gratuito)
- **URL pública:** https://fitlife-api-v2.onrender.com

---

## Pruebas Unitarias

El proyecto incluye una suite completa de **61 casos de prueba** distribuidos en 6 archivos de testing:

### Archivos de Prueba

1. **ProgressUiStateTest.kt** - 8 tests
   - Estado inicial y carga
   - Lista de progreso
   - Manejo de errores
   - Mensajes de éxito

2. **ProgressWeightValidationTest.kt** - 9 tests
   - Validación de peso positivo
   - Validación de peso cero e inválido
   - Casos límite

3. **ProfileUiStateTest.kt** - 10 tests
   - Estado del perfil
   - Carga de usuario
   - Avatar
   - Errores de conexión

4. **SignupRequestValidationTest.kt** - 11 tests
   - Validación de email
   - Validación de contraseña
   - Validación de teléfono
   - Roles de usuario

5. **AppointmentUiStateTest.kt** - 11 tests
   - Estado de citas
   - Lista de citas
   - Estados (CONFIRMADA, PENDIENTE, CANCELADA)
   - Ordenamiento por fecha

6. **DateFormattingUtilTest.kt** - 12 tests
   - Parseo de fechas ISO
   - Parseo de fechas simples
   - Ordenamiento por timestamp
   - Manejo de errores

### Ejecutar las Pruebas

**Desde Android Studio:**
```
1. Click derecho en la carpeta test
2. Selecciona "Run 'Tests in...' "
3. Ver resultados en la ventana de pruebas
```

**Desde Terminal:**
```bash
# Ejecutar todas las pruebas
./gradlew test

# Ver reporte HTML
./gradlew test --info
# El reporte se genera en: app/build/reports/tests/testDebugUnitTest/index.html
```

### Cobertura de Pruebas

- ViewModels: 100%
- Estados UI: 100%
- Validaciones: 100%
- Utilidades: 100%
- DTOs: 100%

---

## Evidencia de Trabajo Colaborativo

Este proyecto fue desarrollado de forma colaborativa por un equipo de 2 personas.

### María José Pérez Martínez

🔗 **[Ver todos los commits de mjperezm](https://github.com/AplicacionesMovilesDuoc/FitLife-v3-app-movil/commits?author=mjperezm)**

**Áreas de contribución:**
- Desarrollo de ViewModels (AuthViewModel, ProfileViewModel, ProgressViewModel)
- Implementación de la integración con API REST
- Sistema de autenticación con JWT
- Gestión de sesión y persistencia local
- Implementación de pruebas unitarias (61 casos)
- Configuración de Retrofit y interceptores
- Documentación del proyecto

**Período de commits:** Noviembre - Diciembre 2024

### Karin López Sánchez

 **[Ver todos los commits de Karylopez](https://github.com/AplicacionesMovilesDuoc/FitLife-v3-app-movil/commits?author=Karylopez)**

**Áreas de contribución:**
- Diseño e implementación de UI con Jetpack Compose
- Desarrollo de pantallas (Login, Signup, Home, Profile, Progress)
- Componentes reutilizables de UI
- Navegación con Navigation Compose
- Integración de Material Design 3
- Manejo de estados visuales y animaciones

**Período de commits:** Noviembre - Diciembre 2024

### Estadísticas de Contribución

 **[Ver gráfico de contribuciones](https://github.com/AplicacionesMovilesDuoc/FitLife-v3-app-movil/graphs/contributors)**

**Distribución de trabajo:**
- María José Pérez: Backend integration, lógica de negocio, testing
- Karin López: Frontend, UI/UX, componentes visuales

**Evidencias de colaboración:**
- Commits distribuidos en el tiempo
- Pull requests revisados por ambas integrantes
- Trabajo en ramas feature separadas
- Merges a rama main después de revisión

### Timeline de Desarrollo

- **Noviembre 2024:** Setup inicial, arquitectura, autenticación
- **Diciembre 2024:** Features completas, testing, documentación, release

---

##  Tecnologías Utilizadas

### Frontend (Android - Kotlin)

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| Kotlin | 1.9.0 | Lenguaje de programación |
| Jetpack Compose | 1.5.4 | UI declarativa |
| Material 3 | 1.1.2 | Design system |
| MVVM | - | Arquitectura |
| Coroutines | 1.7.3 | Programación asíncrona |
| Flow | - | Streams reactivos |
| Retrofit | 2.11.0 | Cliente HTTP |
| OkHttp | 4.12.0 | Networking |
| Gson | 2.11.0 | JSON parsing |
| Coil | 2.5.0 | Image loading |
| DataStore | 1.0.0 | Persistencia |
| Navigation Compose | 2.7.6 | Navegación |
| Accompanist | 0.34.0 | Permisos |

### Backend (NestJS - TypeScript)

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| NestJS | 10.x | Framework backend |
| TypeScript | 5.x | Lenguaje tipado |
| MongoDB | 6.x | Base de datos |
| Mongoose | 8.x | ODM |
| Passport | 0.7.x | Autenticación |
| JWT | 10.x | Tokens |
| Bcrypt | 5.x | Encriptación |
| Class Validator | 0.14.x | Validación |

### Infraestructura

| Servicio | Propósito |
|----------|-----------|
| MongoDB Atlas | Base de datos en la nube |
| Render | Hosting del backend |
| GitHub | Control de versiones |
| Android Studio | IDE de desarrollo |
| Visual Studio Code | Editor de código |

---


## Contacto

### María José Pérez Martínez
- GitHub: [@mjperezm](https://github.com/mjperezm)
- Email: maria.perez@duocuc.cl

### Karin López Sánchez
- GitHub: [@Karylopez](https://github.com/Karylopez)
- Email: karin.lopez@duocuc.cl

---

## Estado del Proyecto

| Estado | Información |
|--------|-------------|
| **Versión Actual** | 3.0 |
| **Estado** | Completado |
| **Última Actualización** | Diciembre 2024 |
| **Ambiente** | Producción |
| **API Status** |  Online |

---


---

**🏋️‍♀️ Desarrollado con ❤️ y 💪 por María José Pérez & Karin López - DuocUC 2024**
