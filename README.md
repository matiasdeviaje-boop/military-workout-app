# Military Workout - Aplicación Android de Entrenamiento de Tren Superior

## Descripción General

**Military Workout** es una aplicación Android profesional diseñada para el entrenamiento de tren superior con un enfoque militar y de alto impacto. La aplicación funciona completamente sin conexión a internet y proporciona seguimiento diario, rutinas progresivas adaptativas y generación de reportes detallados en PDF.

## Características Principales

### 1. **Entrenamiento Progresivo Adaptativo**
- **Cuatro niveles de dificultad**: Intermedio, Avanzado, Maestro y Élite
- **Progresión inteligente**: Las rutinas se adaptan automáticamente según el rendimiento del usuario
- **Variantes de ejercicios**: Cada ejercicio tiene 5 variantes progresivas de dificultad
- **Reducción de tiempos de descanso**: Los descansos se reducen progresivamente para aumentar intensidad

### 2. **Ejercicios Incluidos**
- **Flexiones**: Estándar, Diamante, Archer, Pseudo Planche, Planche
- **Dominadas**: Estándar, Cerradas, Archer, Explosivas, Planche
- **Fondos**: Estándar, Cerrados, Explosivos, Planche, Planche Completo
- **Burpees**: Estándar, con Flexión, Explosivos, Dobles, Extremos
- **Abdominales**: Estándar, Declinado, Colgante, Dragon Flag, Planche Abdominal

### 3. **Seguimiento Diario**
- Registro automático de entrenamientos diarios
- Seguimiento de series, repeticiones y tiempos de descanso completados
- Cálculo automático de duración del entrenamiento
- Historial completo de entrenamientos

### 4. **Almacenamiento Local (Sin Internet)**
- Base de datos SQLite/Room para almacenamiento persistente
- SharedPreferences para configuración rápida
- Sincronización local de datos
- Backup y restauración de datos en JSON

### 5. **Análisis de Rendimiento**
- Estadísticas generales de entrenamientos
- Análisis de consistencia y racha de entrenamientos
- Cálculo de volumen total y completado
- Métricas de intensidad relativa
- Índice de fatiga

### 6. **Generación de Reportes PDF**
- Reportes detallados de progreso
- Análisis por ejercicio
- Recomendaciones personalizadas
- Historial de entrenamientos
- Métricas de tendencia

### 7. **Interfaz de Usuario Militar**
- Diseño oscuro y funcional
- Tema militar profesional
- Navegación intuitiva con 4 secciones principales
- Componentes responsivos

## Estructura del Proyecto

```
workout_app/
├── src/main/
│   ├── java/com/militaryworkout/app/
│   │   ├── data/
│   │   │   ├── WorkoutDatabase.kt          # Base de datos Room
│   │   │   ├── Daos.kt                     # Data Access Objects
│   │   │   ├── WorkoutRepository.kt        # Repositorio
│   │   │   ├── LocalStorageService.kt      # Almacenamiento local
│   │   │   └── DatabaseInitializer.kt      # Inicializador
│   │   ├── models/
│   │   │   ├── Exercise.kt                 # Modelo de ejercicios
│   │   │   └── Workout.kt                  # Modelo de entrenamientos
│   │   ├── ui/
│   │   │   ├── MainActivity.kt             # Actividad principal
│   │   │   ├── HomeFragment.kt             # Pantalla de inicio
│   │   │   ├── WorkoutFragment.kt          # Pantalla de entrenamiento
│   │   │   ├── ProgressFragment.kt         # Pantalla de progreso
│   │   │   ├── SettingsFragment.kt         # Pantalla de configuración
│   │   │   ├── ExerciseAdapter.kt          # Adaptador de ejercicios
│   │   │   └── ProgressAdapter.kt          # Adaptador de progreso
│   │   ├── utils/
│   │   │   ├── WorkoutGenerator.kt         # Generador de rutinas básico
│   │   │   ├── AdvancedWorkoutGenerator.kt # Generador avanzado
│   │   │   ├── ProgressionCalculator.kt    # Cálculos de progresión
│   │   │   ├── PerformanceAnalyzer.kt      # Análisis de rendimiento
│   │   │   ├── PDFGenerator.kt             # Generador de PDF
│   │   │   └── DataSyncManager.kt          # Gestor de sincronización
│   │   └── MilitaryWorkoutApp.kt           # Aplicación principal
│   └── res/
│       ├── layout/                         # Layouts XML
│       ├── values/                         # Recursos (colores, strings, temas)
│       └── menu/                           # Menús
└── build.gradle.kts                        # Configuración Gradle
```

## Niveles de Entrenamiento

### Intermedio
- 2 ejercicios principales (Flexiones, Abdominales)
- 3-4 series por ejercicio
- Tiempos de descanso: 45-60 segundos
- Dificultad base: 1/5

### Avanzado
- 3 ejercicios principales (Flexiones, Dominadas, Abdominales)
- 3-4 series por ejercicio
- Tiempos de descanso: 40-50 segundos
- Dificultad base: 2/5

### Maestro
- 5 ejercicios principales (Flexiones, Dominadas, Fondos, Burpees, Abdominales)
- 3-5 series por ejercicio
- Tiempos de descanso: 30-40 segundos
- Dificultad base: 3/5

### Élite
- 5 ejercicios con máxima dificultad
- 5 series por ejercicio
- Tiempos de descanso: 25-30 segundos
- Dificultad base: 4-5/5

## Algoritmo de Progresión

La aplicación utiliza un algoritmo inteligente que calcula:

1. **Progresión de Repeticiones**: Incremento basado en dificultad y semana
2. **Progresión de Series**: Aumento gradual del volumen
3. **Reducción de Descanso**: Disminución progresiva de tiempos de recuperación
4. **Cambio de Dificultad**: Automático según tasa de completación
5. **Índice de Fatiga**: Prevención de sobreentreno

## Uso de la Aplicación

### Pantalla de Inicio
- Visualización rápida de estadísticas
- Selección del nivel de entrenamiento
- Botón para iniciar entrenamiento
- Botón para generar reporte

### Pantalla de Entrenamiento
- Lista de ejercicios del día
- Ajuste de series y repeticiones
- Seguimiento de descanso
- Marca de ejercicios completados
- Botones para completar o saltar

### Pantalla de Progreso
- Estadísticas generales
- Progreso por ejercicio
- Exportación a PDF
- Historial de entrenamientos

### Pantalla de Configuración
- Selección de nivel
- Ajuste de tiempo de descanso base
- Información de la app
- Opción de limpiar datos

## Requisitos Técnicos

- **Android**: Mínimo API 24 (Android 7.0)
- **Memoria**: 50 MB mínimo
- **Almacenamiento**: 100 MB disponibles
- **Sin conexión a internet requerida**

## Dependencias Principales

- **Room Database**: Almacenamiento persistente
- **Kotlin Coroutines**: Operaciones asincrónicas
- **iText PDF**: Generación de reportes
- **Material Design**: Componentes UI
- **GSON**: Serialización JSON

## Instalación

1. Clonar el repositorio
2. Abrir en Android Studio
3. Sincronizar Gradle
4. Compilar y ejecutar en dispositivo/emulador

## Generación de Reportes PDF

Los reportes incluyen:
- Encabezado con nivel actual
- Estadísticas generales
- Métricas de consistencia
- Historial de entrenamientos (últimos 30)
- Análisis de progresión por ejercicio
- Recomendaciones personalizadas

Los reportes se guardan en: `/storage/emulated/0/Android/data/com.militaryworkout.app/files/`

## Características Futuras

- Sincronización en la nube (opcional)
- Gráficos de progresión
- Notificaciones de entrenamientos
- Modo oscuro/claro
- Múltiples idiomas
- Integración con wearables

## Notas Importantes

- La aplicación funciona completamente sin internet
- Todos los datos se almacenan localmente en el dispositivo
- Se recomienda hacer backup regular de datos
- Los entrenamientos se generan automáticamente cada día
- La progresión es adaptativa según el rendimiento

## Licencia

Aplicación desarrollada para uso personal.

## Soporte

Para reportar problemas o sugerencias, contactar al desarrollador.

---

**Versión**: 1.0.0  
**Última actualización**: Octubre 2025  
**Desarrollado con**: Kotlin, Android Studio, Room Database

