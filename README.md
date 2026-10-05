# 🦸 Tienda Multiverso

Aplicación móvil Android desarrollada en **Kotlin y Jetpack Compose** como proyecto académico de la carrera Ingeniería en Desarrollo de Software de Duoc UC.

Tienda Multiverso permite explorar un catálogo de figuras coleccionables, administrar productos, registrar usuarios y realizar cotizaciones mediante una interfaz sencilla y accesible.

## 🚀 Funcionalidades

- Pantalla de bienvenida (Splash Screen).
- Registro e inicio de sesión de usuarios.
- Recuperación de contraseña simulada.
- Persistencia de usuarios y figuras mediante SQLite.
- Contraseñas protegidas mediante PBKDF2, hash y sal.
- Sesiones persistentes con SharedPreferences.
- Catálogo de figuras coleccionables.
- Administración de figuras: crear, consultar, modificar y eliminar (CRUD).
- Cotizaciones con validación de cantidades y stock.
- Comunicación accesible mediante texto, voz y vibración.
- Optimización de operaciones SQLite con Kotlin Coroutines y Dispatchers.IO.

## 🛠️ Tecnologías utilizadas

| Tecnología | Aplicación |
|---|---|
| Kotlin | Lenguaje de programación |
| Jetpack Compose | Interfaz de usuario |
| Material 3 | Componentes visuales |
| SQLite | Persistencia local |
| SQLiteOpenHelper | Administración de base de datos |
| SharedPreferences | Gestión de sesiones |
| Kotlin Coroutines | Operaciones en segundo plano |
| JUnit 4 | Pruebas unitarias |
| Git y GitHub | Control de versiones |

## 🧪 Pruebas realizadas

Se implementaron y ejecutaron satisfactoriamente:

- **5 pruebas unitarias JUnit:** cálculos de cotización y validaciones.
- **3 pruebas instrumentadas:** operaciones CRUD, persistencia y autenticación SQLite.
- Pruebas funcionales en emulador Pixel 7.
- Instalación y pruebas de la versión Release en un teléfono Android físico.

## 📱 Descargar aplicación

El APK está firmado digitalmente y disponible para su instalación en dispositivos Android compatibles.

### 🔗 [Descargar Tienda Multiverso (APK)](https://www.upload-apk.com/en/5K5uxGKN5xEbVoc)

**Información:**

- Formato: APK firmado.
- Versión de compilación: Release.
- Android mínimo: Android 8.0 (API 26).
- Distribución: Evaluación académica.

> **Nota:** La aplicación no está publicada en Google Play. Android puede mostrar advertencias de seguridad al instalar aplicaciones desde fuentes externas. Se recomienda mantener activadas las protecciones del dispositivo.

## 📂 Estructura del proyecto

```text
app/src/
├── main/
│   ├── java/com/example/tiendamultiverso/
│   │   ├── data/
│   │   │   ├── local/
│   │   │   ├── Figura.kt
│   │   │   ├── FiguraRepository.kt
│   │   │   ├── Usuario.kt
│   │   │   └── UsuarioRepository.kt
│   │   ├── ui/
│   │   │   ├── screens/
│   │   │   └── theme/
│   │   └── MainActivity.kt
│   └── res/
├── test/
└── androidTest/
```

## 💻 Ejecución del proyecto

1. Clonar el repositorio.
2. Abrir el proyecto en Android Studio.
3. Sincronizar las dependencias con Gradle.
4. Ejecutar la aplicación en un emulador o dispositivo Android compatible.

La base de datos SQLite se inicializa automáticamente durante el primer inicio.

## 🔗 Repositorio

**GitHub:** https://github.com/CmFonsecaS/Tienda-Multiverso-MovilApp

**Rama de la Sumativa 3:** `sumativa3`

## 👨‍💻 Autor

**Cristian Fonseca**

Ingeniería en Desarrollo de Software  
Duoc UC — 2026
