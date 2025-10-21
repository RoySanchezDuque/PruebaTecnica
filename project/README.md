# Rick and Morty Android App

Aplicación Android desarrollada en Kotlin que consume la API de Rick and Morty (https://rickandmortyapi.com/), implementando operaciones CRUD con almacenamiento local mediante Room Database.

## 📋 Características Principales

### Core Features
- ✅ **Lista de personajes**: Visualización de todos los personajes en una lista scrolleable con RecyclerView
- ✅ **Crear personaje**: Añadir nuevos personajes personalizados al listado local
- ✅ **Editar personaje**: Modificar información de personajes existentes
- ✅ **Eliminar personaje**: Borrar personajes del listado con confirmación

### Bonus Features
- ✅ **Búsqueda de personajes**: Búsqueda en tiempo real por nombre, especie, tipo o género
- ✅ **Ordenamiento**: Los personajes se ordenan por fecha de creación (más recientes primero)
- ✅ **Pull to Refresh**: Actualización de datos con SwipeRefreshLayout
- ✅ **Diseño Material**: UI moderna con Material Design Components

## 🏗️ Arquitectura y Tecnologías

### Arquitectura
- **MVVM (Model-View-ViewModel)**: Separación de responsabilidades y facilita testing
- **Clean Architecture**: Organización en capas (Data, Domain, Presentation)
- **Repository Pattern**: Abstracción de las fuentes de datos

### Capas del Proyecto

```
app/
├── data/
│   ├── local/
│   │   ├── dao/          # Data Access Objects para Room
│   │   ├── database/     # Configuración de Room Database
│   │   └── entity/       # Entidades de base de datos
│   ├── remote/
│   │   ├── api/          # Interfaces de Retrofit y configuración
│   │   └── model/        # DTOs para respuestas de la API
│   ├── mapper/           # Conversión entre DTOs, Entities y Domain Models
│   └── repository/       # Implementaciones de repositorios
├── domain/
│   ├── model/            # Modelos de dominio
│   ├── repository/       # Interfaces de repositorios
│   └── usecase/          # Casos de uso de negocio
├── presentation/
│   ├── activity/         # Activities de la aplicación
│   ├── adapter/          # Adaptadores de RecyclerView
│   └── viewmodel/        # ViewModels y Factories
└── utils/                # Utilidades y clases helper
```

### Tecnologías Core

#### Lenguaje
- **Kotlin 100%**: Lenguaje oficial de Android

#### Android Jetpack
- **LiveData**: Observación de datos reactivos
- **ViewModel**: Gestión de datos con ciclo de vida
- **Room Database**: Base de datos local SQLite
- **ViewBinding**: Vinculación segura de vistas XML

#### Networking
- **Retrofit 2.9.0**: Cliente HTTP type-safe
- **OkHttp 4.12.0**: Interceptores y logging
- **Gson**: Serialización/deserialización JSON

#### Asincronía
- **Kotlin Coroutines**: Operaciones asíncronas
- **Coroutines Flow**: Manejo de streams de datos

#### UI
- **Material Design Components**: Componentes UI modernos
- **Glide**: Carga y cache de imágenes
- **RecyclerView**: Listas eficientes y scrolleables

#### Testing
- **JUnit 4**: Framework de testing
- **Mockito**: Mocking para tests unitarios
- **Coroutines Test**: Testing de coroutines
- **Architecture Components Testing**: Testing de LiveData y ViewModels

## 📱 Funcionalidades Detalladas

### MainActivity
- Muestra lista de todos los personajes
- Barra de búsqueda en tiempo real
- Botón FAB para crear nuevos personajes
- Pull to refresh para actualizar desde la API
- Botones de editar y eliminar en cada item

### CharacterDetailActivity
- Vista detallada de un personaje seleccionado
- Muestra imagen, nombre, especie, estado, género, origen y ubicación
- Navegación back al listado principal

### CreateEditCharacterActivity
- Formulario para crear nuevos personajes
- Edición de personajes existentes
- Validación de campos requeridos (nombre y especie)
- Spinners para Status y Gender con opciones predefinidas
- Campos de texto para: Name, Species, Type, Origin, Location, Image URL

## 🚀 Instalación y Configuración

### Prerrequisitos
- Android Studio Hedgehog | 2023.1.1 o superior
- JDK 17
- SDK de Android 24-34
- Gradle 8.1.4
- Conexión a Internet (para consumir la API)

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone <URL_DEL_REPOSITORIO>
cd RickAndMortyApp
```

2. **Abrir en Android Studio**
   - Abrir Android Studio
   - File → Open
   - Seleccionar la carpeta del proyecto
   - Esperar a que Gradle sincronice las dependencias

3. **Configurar dispositivo**
   - Conectar dispositivo físico con USB debugging habilitado, O
   - Crear AVD (Android Virtual Device) con API 24 o superior

4. **Ejecutar la aplicación**
   - Click en el botón "Run" (▶️) o `Shift + F10`
   - Seleccionar dispositivo/emulador
   - Esperar a que compile e instale

## 🧪 Ejecutar Tests

### Tests Unitarios

```bash
# Ejecutar todos los tests
./gradlew test

# Ejecutar tests de un módulo específico
./gradlew :app:testDebugUnitTest

# Ver reporte de tests
./gradlew test --tests "*"
```

### Tests Implementados

#### CharacterRepositoryImplTest
- ✅ Test de consumo exitoso de API
- ✅ Test de manejo de errores de API
- ✅ Test de operaciones CRUD en Room Database
- ✅ Test de búsqueda de personajes

#### CharacterViewModelTest
- ✅ Test de carga de personajes desde API
- ✅ Test de creación de personajes
- ✅ Test de actualización de personajes
- ✅ Test de eliminación de personajes
- ✅ Test de manejo de estados (loading, success, error)

#### UseCaseTests
- ✅ Test de GetAllCharactersUseCase
- ✅ Test de CreateCharacterUseCase
- ✅ Test de validación de lógica de negocio

## 📐 Principios de Diseño

### Clean Architecture
- **Separation of Concerns**: Cada capa tiene una responsabilidad específica
- **Dependency Rule**: Las dependencias apuntan hacia el dominio
- **Testability**: Cada capa puede testearse de forma independiente

### SOLID Principles
- **Single Responsibility**: Cada clase tiene una única responsabilidad
- **Open/Closed**: Abierto a extensión, cerrado a modificación
- **Liskov Substitution**: Uso de interfaces y abstracciones
- **Interface Segregation**: Interfaces específicas para cada caso de uso
- **Dependency Inversion**: Dependencia de abstracciones, no implementaciones

### Design Patterns
- **Repository Pattern**: Abstracción del acceso a datos
- **Observer Pattern**: LiveData para observación reactiva
- **Factory Pattern**: ViewModelFactory para creación de ViewModels
- **Mapper Pattern**: Conversión entre capas de datos

## 📊 Base de Datos Local (Room)

### Tabla: characters

| Campo        | Tipo    | Descripción                           |
|--------------|---------|---------------------------------------|
| id           | Int     | Primary Key (auto-increment)          |
| name         | String  | Nombre del personaje                  |
| status       | String  | Estado (Alive, Dead, unknown)         |
| species      | String  | Especie del personaje                 |
| type         | String  | Tipo específico                       |
| gender       | String  | Género del personaje                  |
| originName   | String  | Nombre del origen                     |
| locationName | String  | Nombre de la ubicación actual         |
| image        | String  | URL de la imagen                      |
| createdAt    | Long    | Timestamp de creación                 |
| isFromApi    | Boolean | Indica si viene de la API o es custom |

## 🌐 Endpoints de la API

### Rick and Morty API
- **Base URL**: `https://rickandmortyapi.com/api/`
- **Documentación**: https://rickandmortyapi.com/documentation

#### Endpoints Utilizados
- `GET /character` - Obtener lista de personajes
- `GET /character/{id}` - Obtener personaje específico
- `GET /character?name={name}` - Buscar por nombre

## 📝 Buenas Prácticas Implementadas

### Código
- ✅ Nomenclatura clara y descriptiva
- ✅ Comentarios en código complejo
- ✅ Manejo apropiado de nulos (null safety de Kotlin)
- ✅ Uso de sealed classes para estados
- ✅ Evitar memory leaks con ciclos de vida

### Arquitectura
- ✅ Separación clara de responsabilidades
- ✅ Inyección de dependencias manual
- ✅ Testabilidad del código
- ✅ Reutilización de código

### UI/UX
- ✅ Material Design Guidelines
- ✅ Feedback visual al usuario (loading, errores)
- ✅ Confirmación para acciones destructivas
- ✅ Manejo de estados vacíos
- ✅ Pull to refresh para mejor UX

### Seguridad
- ✅ Permisos de Internet declarados
- ✅ HTTPS para comunicaciones de red
- ✅ Validación de inputs del usuario

## 🔄 Flujo de Datos

```
API/Database → Repository → UseCase → ViewModel → Activity → UI
                    ↓
                 Mapper
                    ↓
            Domain Models
```

## 📄 Archivos de Configuración Importantes

### build.gradle.kts (Project)
- Plugins de Gradle
- Versiones de dependencias

### build.gradle.kts (Module)
- Dependencias del proyecto
- Configuración de compilación
- BuildFeatures (ViewBinding, Compose)

### AndroidManifest.xml
- Permisos de la aplicación
- Declaración de Activities
- Configuración de la aplicación

## 🐛 Troubleshooting

### Error de compilación
```bash
./gradlew clean
./gradlew build
```

### Error de sincronización de Gradle
- File → Invalidate Caches → Invalidate and Restart

### Error de dependencias
- Verificar conexión a Internet
- Sync Project with Gradle Files

## 📚 Recursos Adicionales

- [Rick and Morty API Docs](https://rickandmortyapi.com/documentation)
- [Android Developer Guide](https://developer.android.com)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)
- [Retrofit Documentation](https://square.github.io/retrofit/)

## 👨‍💻 Desarrollador

Proyecto desarrollado como prueba técnica para demostrar conocimientos en:
- Desarrollo Android nativo con Kotlin
- Arquitectura MVVM y Clean Architecture
- Integración de APIs REST
- Persistencia de datos con Room
- Testing unitario
- Buenas prácticas de programación

## 📄 Licencia

Este proyecto es de código abierto y está disponible para fines educativos y de evaluación técnica.
