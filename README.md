# Gestor personal de tareas

Aplicación Android que permite a cada usuario registrarse, iniciar sesión y administrar
sus propias tareas en Cloud Firestore, además de guardar borradores locales con Room
mientras no tiene conexión, publicándolos cuando la recupera.

## Integrantes
- Salome

## Tecnologías
- Kotlin, Jetpack Compose, Navigation Compose
- MVVM + Clean Architecture (capas `ui`, `domain`, `data`, `di`)
- ViewModel + StateFlow + Corrutinas
- Firebase Authentication (correo/contraseña)
- Cloud Firestore (tareas remotas)
- Room (borradores locales)
- Hilt (inyección de dependencias)

## Arquitectura
Flujo unidireccional de dependencias: **UI → ViewModel → Casos de uso (domain) → Repositorio (data) → Firebase / Room**.
El dominio no conoce clases concretas de Firebase ni de Room, solo interfaces de repositorio,
lo que permite cambiar la fuente de datos sin tocar la lógica de negocio ni la interfaz.

Ver diagrama de arquitectura en el informe tecnico.

## Configuración y ejecución
1. Clona el repositorio.
2. Crea un proyecto en [Firebase Console](https://console.firebase.google.com) y registra
   la app Android con el `applicationId` que aparece en `app/build.gradle.kts`.
3. Descarga `google-services.json` desde Firebase Console y colócalo en `app/`
   (ya incluido en este repo para facilitar la evaluación).
4. En Firebase Console, habilita **Authentication** (proveedor correo/contraseña) y
   **Cloud Firestore**.
5. Publica las reglas de seguridad de la sección [Seguridad](#seguridad) en
   Firestore Database → Reglas.
6. Abre el proyecto en Android Studio, sincroniza Gradle y ejecuta en un emulador o
   dispositivo físico.

## Estructura de paquetes

```
com.example.taskmanager
├── data
│   ├── local/          → entidad y DAO de Room (borradores)
│   ├── remote/model/    → modelo de documento remoto
│   ├── mapper/          → conversión Entity ↔ modelo de dominio
│   └── repository/      → implementaciones concretas (Firebase, Room)
├── domain
│   ├── model/           → Task, TaskDraft, User
│   ├── repository/      → interfaces de repositorio (sin depender de Firebase/Room)
│   └── usecase/         → un caso de uso por acción (auth, task, draft)
├── ui
│   ├── navigation/      → NavHost y rutas, con protección de backstack
│   ├── screen/          → pantallas Compose por feature (login, register, tasklist, drafts)
│   ├── state/            → UiState de cada pantalla
│   └── theme/            → tema Material 3
├── di/                   → módulos de Hilt (Firebase, Room, Repository)
├── MainActivity.kt
└── TaskManagerApplication.kt   → @HiltAndroidApp
```

Cada capa solo conoce a la de abajo a través de interfaces: `ui` no importa nada de
`data`, y `domain` no importa nada de Firebase ni de Room.

## Funcionalidades terminadas
- Registro, login, logout y sesión persistente (Firebase Authentication)
- CRUD completo de tareas en Cloud Firestore, filtradas por `ownerId` del usuario actual
- Estados de carga, lista, vacío y error en las pantallas de tareas y borradores
- Borradores locales con Room: crear, listar, publicar, eliminar
- Publicación de borrador con manejo de fallo: si Firestore falla, el borrador se conserva
- Navegación protegida: al cerrar sesión o iniciar sesión, el backstack se limpia y no se
  puede volver a pantallas protegidas con el botón Atrás
- Reglas de seguridad de Firestore que exigen `request.auth.uid == ownerId` para leer,
  crear, actualizar o eliminar una tarea

## Retos adicionales
No implementados en esta entrega (recuperación de contraseña, filtros, búsqueda,
sincronización bidireccional Room–Firestore, etc.) — quedan como trabajo futuro.
