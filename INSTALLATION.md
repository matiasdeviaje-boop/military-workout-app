# Guía de Instalación y Desarrollo - Military Workout

## Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **Android Studio**: Versión 2022.1 o superior
- **Java Development Kit (JDK)**: Versión 11 o superior
- **Android SDK**: API 34 (compileSdkVersion)
- **Gradle**: Versión 8.0 o superior

## Instalación del Proyecto

### Paso 1: Clonar o Descargar el Proyecto

```bash
git clone <repository-url>
cd workout_app
```

### Paso 2: Abrir en Android Studio

1. Abre Android Studio
2. Selecciona "Open an Existing Project"
3. Navega a la carpeta `workout_app` y selecciónala
4. Espera a que Android Studio sincronice el proyecto

### Paso 3: Sincronizar Gradle

1. En Android Studio, ve a `File > Sync Now`
2. Espera a que se descarguen todas las dependencias
3. Resuelve cualquier conflicto de dependencias si es necesario

### Paso 4: Configurar Emulador o Dispositivo

**Para usar un emulador:**
1. Ve a `Tools > Device Manager`
2. Crea un nuevo dispositivo virtual con API 24 o superior
3. Inicia el emulador

**Para usar un dispositivo físico:**
1. Conecta tu dispositivo Android por USB
2. Habilita el modo de desarrollador en el dispositivo
3. Autoriza la depuración USB cuando se te pida

### Paso 5: Compilar y Ejecutar

1. Selecciona tu emulador o dispositivo en la parte superior
2. Haz clic en el botón "Run" (icono de play verde)
3. Espera a que se compile e instale la aplicación

## Estructura de Carpetas

```
workout_app/
├── src/
│   └── main/
│       ├── java/com/militaryworkout/app/
│       │   ├── data/              # Capa de datos
│       │   ├── models/            # Modelos de datos
│       │   ├── ui/                # Interfaz de usuario
│       │   └── utils/             # Utilidades
│       ├── res/                   # Recursos (layouts, strings, etc.)
│       └── AndroidManifest.xml    # Configuración de la app
├── build.gradle.kts               # Configuración de compilación
├── settings.gradle.kts            # Configuración de proyecto
├── gradle.properties              # Propiedades de Gradle
├── README.md                      # Documentación general
└── INSTALLATION.md                # Este archivo
```

## Dependencias Principales

La aplicación utiliza las siguientes dependencias (definidas en `build.gradle.kts`):

| Dependencia | Versión | Propósito |
|---|---|---|
| androidx.core:core-ktx | 1.12.0 | Core de Android |
| androidx.room:room-runtime | 2.6.0 | Base de datos |
| androidx.lifecycle:lifecycle-runtime-ktx | 2.6.2 | Gestión de ciclo de vida |
| com.itextpdf:itextpdf | 5.5.13.3 | Generación de PDF |
| com.google.code.gson:gson | 2.10.1 | Serialización JSON |

## Configuración de Desarrollo

### Habilitar Logging

Para ver logs detallados en Android Studio:

1. Ve a `View > Tool Windows > Logcat`
2. Filtra por el nombre de la aplicación: `com.militaryworkout.app`

### Depuración

Para depurar la aplicación:

1. Establece puntos de interrupción en el código
2. Ejecuta la aplicación en modo de depuración (Shift + F9)
3. Usa el panel de depuración para inspeccionar variables

### Pruebas

Para ejecutar pruebas unitarias:

```bash
./gradlew test
```

Para ejecutar pruebas instrumentadas:

```bash
./gradlew connectedAndroidTest
```

## Compilación para Producción

### Generar APK

1. Ve a `Build > Build Bundle(s) / APK(s) > Build APK(s)`
2. Espera a que se compile
3. El APK se guardará en `app/build/outputs/apk/debug/`

### Generar Bundle (para Google Play)

1. Ve a `Build > Build Bundle(s) / APK(s) > Build Bundle(s)`
2. Selecciona la variante de compilación
3. El bundle se guardará en `app/build/outputs/bundle/`

## Solución de Problemas

### Error: "SDK location not found"

Solución: Crea un archivo `local.properties` en la raíz del proyecto:

```properties
sdk.dir=/ruta/a/tu/android/sdk
```

### Error: "Gradle sync failed"

Solución:
1. Ve a `File > Invalidate Caches`
2. Reinicia Android Studio
3. Sincroniza nuevamente

### Error: "Compilation failed"

Solución:
1. Limpia el proyecto: `Build > Clean Project`
2. Reconstruye: `Build > Rebuild Project`
3. Sincroniza Gradle nuevamente

### La aplicación se bloquea al iniciar

Solución:
1. Revisa el Logcat para ver el error exacto
2. Asegúrate de que el emulador/dispositivo tenga suficiente memoria
3. Intenta desinstalar y reinstalar la aplicación

## Configuración de Base de Datos

La aplicación utiliza Room Database que se inicializa automáticamente. Para acceder a los datos almacenados:

1. Abre Android Device Monitor (en Android Studio)
2. Navega a `/data/data/com.militaryworkout.app/databases/`
3. Descarga el archivo `workout_database` para inspeccionarlo

## Personalización

### Cambiar Colores

Edita `src/main/res/values/colors.xml`:

```xml
<color name="primary">#2d2d2d</color>
<color name="accent">#ff6b35</color>
```

### Cambiar Strings

Edita `src/main/res/values/strings.xml`:

```xml
<string name="app_name">Tu Nombre de App</string>
```

### Cambiar Tema

Edita `src/main/res/values/themes.xml` para personalizar el tema de la aplicación.

## Recursos Útiles

- [Documentación de Android](https://developer.android.com/)
- [Documentación de Room](https://developer.android.com/training/data-storage/room)
- [Documentación de Kotlin](https://kotlinlang.org/docs/)
- [Material Design Guidelines](https://material.io/design)

## Contacto y Soporte

Para preguntas o problemas técnicos, contacta al equipo de desarrollo.

---

**Última actualización**: Octubre 2025

