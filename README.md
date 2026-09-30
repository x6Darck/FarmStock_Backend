# FarmStock Backend — API REST

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-6DB33F?style=flat&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat&logo=apachemaven&logoColor=white)
![Offline](https://img.shields.io/badge/Funciona-sin%20conexi%C3%B3n-2EA44F?style=flat)
![Estado](https://img.shields.io/badge/Estado-Completado-2EA44F?style=flat)

**FarmStock** es un sistema de inventario para la gestión de **herramientas y equipos de la finca del SENA ubicada en El Zulia (Cúcuta), Norte de Santander**.

Este repositorio contiene el **backend de FarmStock**: una API REST desarrollada con **Java y Spring Boot** que gestiona herramientas, préstamos, mantenimientos, aprendices, usuarios y equipos de cómputo, genera códigos QR y envía notificaciones por correo. Es consumida por la aplicación de escritorio de FarmStock, desarrollada con Electron.

El sistema se diseñó para **funcionar sin una conexión estable a internet**: la API y la base de datos MySQL se ejecutan localmente, sin depender de servicios en la nube. El envío de correos es la única función que requiere internet.

El sistema fue **diseñado y desarrollado de forma individual**.

---

## 📑 Tabla de contenido

- [Características principales](#-características-principales)
- [Funcionamiento sin conexión](#-funcionamiento-sin-conexión)
- [Arquitectura](#️-arquitectura)
- [Stack tecnológico](#-stack-tecnológico)
- [Endpoints de la API](#-endpoints-de-la-api)
- [Requisitos](#-requisitos)
- [Instalación y ejecución local](#️-instalación-y-ejecución-local)
- [Compilación](#-compilación)
- [Pruebas](#-pruebas)
- [Ecosistema FarmStock](#-ecosistema-farmstock)
- [Estado del proyecto](#-estado-del-proyecto)
- [Desarrollo](#-desarrollo)
- [Propiedad y uso](#-propiedad-y-uso)
- [Autor](#-autor)

---

## ✨ Características principales

- 🛠️ **Herramientas:** CRUD, consulta de las registradas hoy y detalle por unidad con código único.
- 🔲 **Códigos QR** para cada unidad de herramienta, generados con ZXing.
- 🤝 **Préstamos:** creación, devolución por código, consulta de activos y devueltos, e historial por usuario.
- 🔧 **Mantenimientos:** registro, seguimiento de estado y consulta por herramienta, tipo o usuario.
- 📊 **Estadísticas** por herramienta (préstamos, daños y mantenimientos).
- 👥 **Usuarios y aprendices:** registro, búsqueda por documento o ficha, e inicio de sesión.
- 💻 **Equipos de cómputo:** registro, entradas y salidas, historial y consulta por código o cédula.
- 📧 **Notificaciones por correo** mediante Resend.
- ✅ **Validación de datos** y **manejo global de excepciones**.
- 🧪 **Pruebas** unitarias, de integración y de rendimiento.

---

## 📡 Funcionamiento sin conexión

La finca no siempre cuenta con internet estable, por lo que la API y la base de datos se ejecutan en el equipo local. Las funciones de inventario, préstamos, mantenimientos, estadísticas y generación de QR no necesitan conexión. Solo el envío de correos por Resend requiere internet.

---

## 🏗️ Arquitectura

El backend usa una **arquitectura por capas**:

```text
src/main/java/com/FarmStock_Backend/FarmStock/
│
├── Controller/     # Endpoints REST
├── Service/        # Lógica de negocio
├── Repository/     # Acceso a datos (Spring Data JPA)
├── Model/          # Entidades de persistencia
├── DTO/            # Objetos de transferencia de datos
├── Config/         # CORS y manejo global de excepciones
└── FarmStockApplication.java
```

### Flujo general de una solicitud

```text
Cliente (Electron)
        │
        ▼
   Controller
        │
        ▼
    Service
        │
        ▼
   Repository
        │
        ▼
      MySQL
```

---

## 🧰 Stack tecnológico

| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje principal |
| Spring Boot 3.5.5 | Framework backend |
| Spring Web | API REST |
| Spring Data JPA / Hibernate | Persistencia |
| Spring Validation | Validación de datos |
| MySQL | Base de datos |
| ZXing 3.5.3 | Generación de códigos QR |
| Resend Java SDK | Envío de correos |
| Lombok | Reducción de código repetitivo |
| Maven | Gestión de dependencias |
| JUnit | Pruebas |

---

## 🔌 Endpoints de la API

| Módulo | Ruta base | Operaciones principales |
|---|---|---|
| Herramientas | `/herramienta` | CRUD, `/hoy` |
| Detalle de herramienta | `/api/herramienta-detalle` | Por herramienta, por código único, QR |
| Préstamos | `/prestamos` | `/crear`, `/devolver/codigo/{codigo}`, `/activos`, `/devueltos`, `/todos` |
| Mantenimientos | `/mantenimientos` | CRUD, `/activos`, cambio de estado, estadísticas |
| Estadísticas | `/estadisticas` | `/herramientas`, `/herramientas/{id}` |
| Usuarios | `/usuario` | CRUD, `/login`, `/documento/{numero}` |
| Aprendices | `/aprendices` | CRUD, `/buscar` |
| Equipos de cómputo | `/equipos-computos` | CRUD, `/salida`, `/entrada`, `/historial`, `/movimientos` |
| Códigos QR | `/qr/{archivo}` | Descarga de la imagen QR |
| Notificaciones | `/api/notificaciones/resend` | Envío de correos |

---

## 📋 Requisitos

- Java 21
- MySQL
- Git

Maven no es necesario instalarlo: el proyecto incluye el Maven Wrapper (`mvnw`).

---

## ⚙️ Instalación y ejecución local

### 1. Clonar el repositorio

```bash
git clone https://github.com/x6Darck/FarmStock_Backend.git
cd FarmStock_Backend
```

### 2. Crear la base de datos

```sql
CREATE DATABASE farmstock;
```

Hibernate crea y actualiza las tablas automáticamente (`spring.jpa.hibernate.ddl-auto=update`).

### 3. Configurar la aplicación

Completa los valores en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/farmstock
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña

# Solo si usas el envío de correos
resend.api.key=tu_api_key
resend.from=Nombre <correo@tudominio.com>
```

> [!IMPORTANT]
> No subas contraseñas ni claves reales al repositorio. Puedes usar variables de entorno en su lugar: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` y `RESEND_API_KEY`.

### 4. Ejecutar la aplicación

**Linux / macOS**

```bash
./mvnw spring-boot:run
```

**Windows**

```powershell
.\mvnw.cmd spring-boot:run
```

La API queda disponible por defecto en `http://localhost:8080`.

Los códigos QR se generan en la carpeta `codigos_qr/`, que se crea al ejecutar la aplicación.

---

## 📦 Compilación

```bash
./mvnw clean package
```

El archivo generado queda en `target/FarmStock-0.0.1-SNAPSHOT.jar` y se ejecuta con:

```bash
java -jar target/FarmStock-0.0.1-SNAPSHOT.jar
```

---

## 🧪 Pruebas

El proyecto incluye pruebas sobre aprendices, herramientas y detalles de herramienta, además de una prueba de rendimiento:

```bash
./mvnw test
```

---

## 🔄 Ecosistema FarmStock

FarmStock se compone de dos repositorios.

### ⚙️ FarmStock Backend

API REST con **Java y Spring Boot** (este repositorio).

**Repositorio:** [github.com/x6Darck/FarmStock_Backend](https://github.com/x6Darck/FarmStock_Backend)

### 🖥️ FarmStock Frontend

Aplicación de escritorio desarrollada con **Electron**, con interfaz en HTML, CSS y JavaScript, que consume esta API.

**Repositorio:** [github.com/x6Darck/FarmStock_Front](https://github.com/x6Darck/FarmStock_Front)

---

## 📌 Estado del proyecto

**Estado:** Completado

FarmStock fue desarrollado para cubrir una necesidad real de control de inventario en la finca del SENA ubicada en El Zulia (Cúcuta), con la condición de funcionar sin conexión estable a internet.

---

## 👨‍💻 Desarrollo

FarmStock fue **diseñado, estructurado y desarrollado de forma individual**. En el backend se realizó:

- Diseño de la API REST y de la arquitectura por capas
- Modelado de la base de datos con JPA e Hibernate
- Módulos de herramientas, préstamos, mantenimientos, aprendices, usuarios y equipos de cómputo
- Generación de códigos QR para cada unidad de herramienta
- Estadísticas por herramienta
- Envío de notificaciones por correo con Resend
- Validación de datos y manejo global de excepciones
- Pruebas unitarias, de integración y de rendimiento
- Integración con la aplicación de escritorio

---

## 📄 Propiedad y uso

FarmStock fue desarrollado para la finca del SENA ubicada en El Zulia (Cúcuta).

Este repositorio se publica **únicamente con fines demostrativos y de portafolio profesional**. Su publicación no implica la transferencia de derechos de propiedad intelectual ni autorización para copiar, modificar, distribuir o utilizar el software con fines comerciales.

El repositorio no incluye credenciales, contraseñas, datos personales ni configuraciones privadas.

---

## 👤 Autor

**Jean Pier Gómez**

Desarrollo individual de FarmStock.

---

<p align="center">
  <strong>FarmStock — Sistema de inventario para la finca del SENA</strong><br>
  Desarrollado con Electron y Spring Boot.
</p>
