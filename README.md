# Arquitectura del Sistema

## Objetivo del Documento

Este documento explica cómo la implementación del sistema universitario sigue fielmente el diagrama UML proporcionado, justificando las decisiones de diseño tomadas y documentando las extensiones realizadas fuera del alcance del modelo de dominio.

---

## Implementación UML - Modelo de Dominio

Esta implementación está en la carpeta `/src` y contiene únicamente el modelo de negocio según el UML, sin capas técnicas adicionales.

### Estructura de Archivos del Modelo de Dominio

```
src/modelo/
├── Usuario.java             # Clase abstracta base
├── Estudiante.java          # Hereda de Usuario
├── Profesor.java            # Hereda de Usuario
├── Administrador.java       # Hereda de Usuario
├── Carrera.java             # Carrera universitaria
├── Materia.java             # Materia/Curso
├── Aula.java                # Aula física
├── Clase.java               # Sesión de clase
├── Examen.java              # Examen
├── Nota.java                # Clase de asociación (corrección del UML)
└── Asistencia.java          # Asistencia a clase

src/enums/
├── TipoExamen.java          # PARCIAL, FINAL, RECUPERATORIO
├── TipoAula.java            # TEORIA, LABORATORIO, AUDITORIO
└── EstadoInscripcion.java   # ACTIVA, APROBADA, REPROBADA
```

---

## Mapeo UML a Código

| **Entidad UML** | **Clase Java** | **Ubicación** |
|-----------------|----------------|---------------|
| Usuario (abstracto) | Usuario | modelo/Usuario.java |
| Estudiante | Estudiante | modelo/Estudiante.java |
| Profesor | Profesor | modelo/Profesor.java |
| Administrador | Administrador | modelo/Administrador.java |
| Carrera | Carrera | modelo/Carrera.java |
| Materia | Materia | modelo/Materia.java |
| Aula | Aula | modelo/Aula.java |
| Clase | Clase | modelo/Clase.java |
| Examen | Examen | modelo/Examen.java |
| Nota (corregido) | Nota | modelo/Nota.java |
| Asistencia | Asistencia | modelo/Asistencia.java |
| TipoExamen | TipoExamen | enums/TipoExamen.java |
| TipoAula | TipoAula | enums/TipoAula.java |
| EstadoInscripcion | EstadoInscripcion | enums/EstadoInscripcion.java |

---

## Análisis Detallado: UML vs Implementación

### 1. Jerarquía de Usuario

**Diseño UML:**
El diagrama especifica una jerarquía de herencia donde Usuario es una clase abstracta con tres especializaciones concretas: Estudiante, Profesor y Administrador.

**Implementación:**
Se implementó la clase abstracta Usuario con los atributos comunes (legajo, nombre, apellido, email, password) y las tres clases hijas heredan estos atributos mediante herencia. Cada subclase implementa sus propios métodos específicos según lo indicado en el UML.

**Justificación:**
Esta estructura permite reutilizar código común y aplicar polimorfismo, facilitando operaciones genéricas sobre usuarios sin importar su tipo específico.

### 2. Clase Materia y sus Relaciones

**Diseño UML:**
El diagrama muestra que Materia mantiene relaciones de multiplicidad muchos (*) con Estudiantes, Clases y Exámenes.

**Implementación:**
Se utilizaron colecciones (List) para mantener estas relaciones bidireccionales. Materia contiene listas de estudiantes inscritos, clases dictadas y exámenes creados.

**Justificación:**
Las colecciones permiten manejar eficientemente las relaciones de uno a muchos y facilitan operaciones de consulta y modificación sobre los elementos relacionados.

### 3. Métodos de Estudiante

El UML especifica tres métodos principales para la clase Estudiante:

- **inscribirseEnCurso(materia: Materia):** Permite al estudiante inscribirse en una materia de su carrera. La implementación incluye validaciones para verificar que la materia pertenezca a la carrera del estudiante y que no esté ya inscrito.

- **verMaterias():** Retorna las materias disponibles para inscripción (aquellas de su carrera en las que no está inscrito).

- **materiasInscriptas(legajo: String):** Retorna la lista de materias en las que el estudiante está inscrito, validando el legajo.

### 4. Métodos de Profesor

El UML define los siguientes métodos para Profesor:

- **dictaMateria(materia, fecha, inicio, fin, aula):** Crea una nueva sesión de clase para una materia que el profesor dicta. Retorna un objeto Clase.

- **esEstudiante(estudiante, materia):** Verifica si un estudiante está inscrito en una materia específica del profesor.

- **haAsistidoClase(estudiante, clase, fecha):** Consulta si un estudiante asistió a una clase específica.

Adicionalmente, se implementaron métodos auxiliares para crear exámenes y calificar estudiantes, operaciones implícitas en el modelo de dominio.

### 5. Métodos de Administrador

El Administrador tiene responsabilidades de gestión del sistema:

- **crearCarrera(nombre, descripcion, codigo):** Instancia una nueva carrera en el sistema.

- **crearMateria(nombre, descripcion, carrera, camino):** Crea una nueva materia y la asocia a una carrera.

- **modificarCarrera(carrera, ...):** Permite actualizar los datos de una carrera existente.

- **asignarProfesorAMateria(profesor, materia):** Establece la relación entre un profesor y una materia.

---

## Corrección del Error del UML: Clase Nota

### Análisis del Problema

El UML original muestra la relación entre Estudiante y Examen como una asociación muchos a muchos con un atributo "nota" en la asociación. Sin embargo, la ubicación exacta de este atributo es ambigua en el diagrama.

**Problema identificado:**
Si múltiples estudiantes rinden el mismo examen, no existe un lugar adecuado para almacenar la nota individual de cada estudiante. La nota no puede estar en Examen (porque es compartido por muchos estudiantes) ni en Estudiante (porque rinde muchos exámenes).

### Solución Implementada

Se creó la clase Nota como clase de asociación (association class) que materializa la relación muchos a muchos entre Estudiante y Examen.

**Estructura de Nota:**
- Referencia al Estudiante
- Referencia al Examen
- Valor de la calificación (double)

Esta solución sigue el patrón estándar de diseño orientado a objetos para manejar atributos en relaciones muchos a muchos, transformando la asociación en una clase concreta que puede tener sus propios atributos y métodos.

---

## Conformidad con el UML

### Verificación de Implementación Completa

1. **Clases:** Todas las clases del diagrama UML están implementadas
2. **Atributos:** Todos los atributos especificados están presentes
3. **Métodos:** Todos los métodos del UML están implementados funcionalmente
4. **Relaciones:** Las relaciones de herencia, composición y agregación están correctamente modeladas
5. **Enumeraciones:** Los tipos enumerados están implementados como enums de Java
6. **Pureza del modelo:** No hay contaminación con capas técnicas en el modelo de dominio

---

## EXTENSIONES FUERA DEL ALCANCE DEL UML

Las siguientes implementaciones fueron agregadas como extensiones técnicas necesarias para el funcionamiento del sistema, pero no forman parte del modelo de dominio especificado en el UML.

### 1. Capa de Persistencia

**Ubicación:** `src/repositorio/`

**Componentes:**

- **PersistenciaTXT.java:** Clase responsable de la serialización y deserialización de objetos del modelo a archivos de texto plano. Implementa métodos de guardado y carga para cada entidad del sistema.

- **SistemaUniversitario.java:** Singleton que actúa como repositorio en memoria y coordina las operaciones de persistencia. Mantiene colecciones de todas las entidades y gestiona los identificadores únicos.

**Justificación:**
La persistencia es una preocupación técnica de infraestructura, no del modelo de dominio. Se implementó en una capa separada para mantener la pureza del modelo UML y permitir cambiar el mecanismo de persistencia sin afectar las clases de negocio.

**Funcionamiento:**
Los datos se almacenan en archivos TXT con formato delimitado por pipes (|). Cada entidad tiene su propio archivo. La carga se realiza automáticamente al iniciar el sistema, respetando las dependencias entre entidades. El guardado es automático tras cada operación que modifica datos.

**Archivos generados:**
```
datos/
├── administradores.txt
├── profesores.txt
├── estudiantes.txt
├── carreras.txt
├── materias.txt
├── aulas.txt
├── clases.txt
├── examenes.txt
├── notas.txt
├── asistencias.txt
└── inscripciones.txt
```

### 2. Interfaz Gráfica de Usuario

**Ubicación:** `src/ui/`

**Componentes:**

- **LoginFrame.java:** Ventana de autenticación que valida credenciales y redirige al dashboard correspondiente según el tipo de usuario.

- **AdminDashboard.java:** Panel de administración que permite gestionar carreras, materias, usuarios y aulas. Implementa todas las operaciones administrativas del modelo.

- **ProfesorDashboard.java:** Panel del profesor con funcionalidades para crear clases, crear exámenes, calificar estudiantes, tomar asistencia y consultar sus materias y estudiantes.

- **EstudianteDashboard.java:** Panel del estudiante que permite inscribirse en materias, consultar materias disponibles, ver notas y consultar asistencias.

**Tecnología:** Java Swing

**Justificación:**
La interfaz gráfica es una capa de presentación completamente independiente del modelo de dominio. Todas las operaciones de la UI invocan métodos del modelo tal como están definidos en el UML, sin modificar la lógica de negocio.

**Arquitectura:**
Se implementó un patrón similar a MVC (Model-View-Controller) donde:
- Model: Las clases del paquete modelo
- View: Los componentes Swing (JFrame, JPanel, etc.)
- Controller: Lógica de eventos en los dashboards que coordina modelo y vista

**Características implementadas:**

**Para Administrador:**
- Gestión completa de carreras (crear, listar)
- Gestión de materias (crear, asignar profesores)
- Creación de usuarios (profesores y estudiantes)
- Gestión de aulas

**Para Profesor:**
- Visualización de materias asignadas
- Creación de clases con fecha, horario y aula
- Creación de exámenes con tipo y cantidad de preguntas
- Calificación de exámenes de estudiantes
- Registro de asistencias
- Consulta de estudiantes por materia

**Para Estudiante:**
- Visualización de materias inscritas
- Inscripción en nuevas materias de su carrera
- Consulta de notas de exámenes
- Consulta de historial de asistencias

### 3. Clase Principal de Ejecución

**Main.java:** Punto de entrada del sistema que inicializa la interfaz gráfica y configura el Look and Feel del sistema operativo. Incluye código comentado de demostración por consola del modelo.

---

## NOTAS ADICIONALES

### Excepciones Personalizadas

Para cumplir con los requisitos académicos de manejo de errores, se implementaron excepciones personalizadas. Estas no forman parte del UML pero son necesarias para un sistema robusto.

**Ubicación:** `src/excepciones/`

**Implementadas:**

1. **InscripcionException** (extends Exception)
   - Propósito: Errores al inscribirse en materias
   - Uso: Inscripción duplicada, materia no pertenece a carrera

2. **PermisosDenegadosException** (extends Exception)
   - Propósito: Operaciones sin permisos suficientes
   - Uso: Profesor intenta dictar materia no asignada

3. **MateriaNoEncontradaException** (extends Exception)
   - Propósito: Búsqueda de materia inexistente
   - Uso: Consultas y operaciones sobre materias

4. **CapacidadExcedidaException** (extends RuntimeException)
   - Propósito: Capacidad máxima superada
   - Uso: Aulas o materias con cupo completo

**Justificación:**
Las excepciones permiten manejar situaciones excepcionales de manera controlada y comunicar errores específicos del dominio, mejorando la robustez del sistema.

### Datos de Prueba

Se incluyen datos de ejemplo basados en la Universidad Argentina de la Empresa (UADE), específicamente de la Facultad de Ingeniería y Ciencias Exactas:

**Carreras incluidas:**
- Ingeniería en Informática
- Ingeniería Industrial
- Ingeniería en Sistemas
- Licenciatura en Matemática
- Ingeniería en Alimentos

**Datos poblados:**
- 3 Administradores
- 8 Profesores
- 15 Estudiantes
- 12 Materias
- 18 Aulas distribuidas en 3 sedes (Campus Lima, UADE Belgrano, UADE Recoleta)
- 18 Clases programadas
- 12 Exámenes
- 24 Notas registradas
- 66 Asistencias
- 35 Inscripciones

**Usuario por defecto:**
- Legajo: admin
- Contraseña: admin123

---

## Cómo Ejecutar el Sistema

### Compilación

```bash
cd /Users/decker/IdeaProjects/TP_POO
javac -d out -sourcepath src src/Main.java src/**/*.java
```

### Ejecución

```bash
java -cp out Main
```

El sistema iniciará la interfaz gráfica automáticamente y cargará los datos persistidos en la carpeta `datos/`.

---

## Resumen de Clases del Modelo de Dominio

### Usuario (Clase Abstracta)
Clase base que encapsula los atributos comunes a todos los usuarios del sistema: legajo, nombre, apellido, email y password.

### Estudiante
Especialización de Usuario que representa a un alumno. Mantiene referencia a su carrera y lista de materias inscritas. Implementa métodos para inscripción, consulta de materias disponibles y materias cursadas.

### Profesor
Especialización de Usuario que representa a un docente. Mantiene lista de materias que dicta. Implementa métodos para dictar clases, verificar estudiantes, consultar asistencias, crear exámenes y calificar.

### Administrador
Especialización de Usuario con privilegios de gestión. Implementa métodos para crear y modificar carreras, crear materias, gestionar usuarios y asignar profesores a materias.

### Materia
Representa una asignatura del plan de estudios. Mantiene referencias a la carrera a la que pertenece, el profesor que la dicta, y colecciones de estudiantes inscritos, clases y exámenes.

### Carrera
Representa un programa académico. Contiene nombre, descripción, código y lista de materias que componen el plan de estudios.

### Examen
Representa una instancia de evaluación. Contiene fecha, cantidad de preguntas, tipo (parcial/final/recuperatorio), materia y profesor responsable.

### Nota
Clase de asociación que materializa la relación entre Estudiante y Examen. Almacena la calificación obtenida por un estudiante específico en un examen específico.

### Clase
Representa una sesión lectiva. Contiene fecha, horarios de inicio y fin, materia, y aula donde se dicta.

### Asistencia
Registra la presencia de un estudiante en una clase específica. Contiene referencia al estudiante, la clase y un booleano indicando asistencia.

### Aula
Representa un espacio físico para dictar clases. Contiene sede, número, piso, tipo (teoría/laboratorio/auditorio) y capacidad.

---