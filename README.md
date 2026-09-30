# FarmStock Backend — API REST

![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat&logo=springboot&logoColor=white)
![Offline](https://img.shields.io/badge/Funciona-sin%20conexi%C3%B3n-2EA44F?style=flat)
![Estado](https://img.shields.io/badge/Estado-%F0%9F%94%A7%20COMPLETAR-lightgrey?style=flat)

**FarmStock** es un sistema de inventario para la gestión de **herramientas y demás elementos de la finca del SENA ubicada en El Zulia (Cúcuta), Norte de Santander**.

Este repositorio contiene el **backend de FarmStock**, una API REST desarrollada con **Java y Spring Boot** que centraliza la lógica de negocio y la persistencia de los datos del inventario. Es consumida por la aplicación de escritorio de FarmStock, desarrollada con Electron.

El sistema fue diseñado para **funcionar sin una conexión estable a internet**, una condición habitual en entornos rurales como el de la finca.

> 🔧 **COMPLETAR:** indica si fue un proyecto desarrollado de forma individual o en equipo, y en qué contexto (por ejemplo, proyecto formativo del SENA).

---

## 📑 Tabla de contenido

- [Características principales](#-características-principales)
- [Funcionamiento sin conexión](#-funcionamiento-sin-conexión)
- [Arquitectura](#️-arquitectura)
- [Stack tecnológico](#-stack-tecnológico)
- [Endpoints de la API](#-endpoints-de-la-api)
- [Requisitos](#-requisitos)
- [Instalación y ejecución local](#️-instalación-y-ejecución-local)
- [Estructura del proyecto](#️-estructura-del-proyecto)
- [Pruebas](#-pruebas)
- [Ecosistema FarmStock](#-ecosistema-farmstock)
- [Estado del proyecto](#-estado-del-proyecto)
- [Desarrollo](#-desarrollo)
- [Propiedad y uso](#-propiedad-y-uso)
- [Autor](#-autor)

---

## ✨ Características principales

- API REST para la gestión del inventario de herramientas y elementos de la finca
- Desarrollada con Java y Spring Boot
- Persistencia de datos en base de datos
- Diseñada para operar sin conexión estable a internet

> 🔧 **COMPLETAR:** reemplaza o amplía esta lista con las funciones reales de la API. Por ejemplo: CRUD de herramientas, categorías, control de entradas y salidas, préstamos, usuarios y autenticación, validación de datos, manejo de errores, documentación con Swagger. Deja solo lo que realmente existe en el código.

---

## 📡 Funcionamiento sin conexión

La finca no siempre cuenta con internet estable, por lo que FarmStock se planteó desde el inicio para **no depender de una conexión permanente**. Por eso el backend y la aplicación de escritorio están pensados para ejecutarse localmente, sin servicios alojados en la nube.

> 🔧 **COMPLETAR:** explica en dos o tres frases cómo se logra esto realmente. Por ejemplo: dónde se ejecuta el backend (en el mismo equipo de la aplicación o en un computador de la red local), qué base de datos se usa y dónde se almacenan los datos, y si hay respaldos.

---

## 🏗️ Arquitectura

```text
FarmStock Frontend (Electron)
            │
            │ REST API
            ▼
   FarmStock Backend (Spring Boot)
            │
            ▼
      Base de datos
```

> 🔧 **COMPLETAR:** confirma el flujo. Si el backend usa una arquitectura por capas (controller, service, repository), descríbela aquí con un árbol de carpetas o un diagrama de flujo de una solicitud.

---

## 🧰 Stack tecnológico

| Tecnología | Uso |
|---|---|
| Java | Lenguaje principal |
| Spring Boot | Framework backend |

> 🔧 **COMPLETAR:** agrega el resto según tu `pom.xml` o `build.gradle`: versión de Java y de Spring Boot, Spring Data JPA, base de datos utilizada, herramienta de construcción (Maven o Gradle), y cualquier otra dependencia relevante.

---

## 🔌 Endpoints de la API

> 🔧 **COMPLETAR:** si la API tiene documentación interactiva (Swagger / OpenAPI), escribe aquí su dirección local. Si no, agrega una tabla breve con los endpoints principales, por ejemplo:
>
> | Método | Ruta | Descripción |
> |---|---|---|
> | GET | `/ruta` | Descripción |

---

## 📋 Requisitos

Para ejecutar el proyecto localmente se requiere:

- Java JDK
- Git

> 🔧 **COMPLETAR:** indica la versión de Java necesaria, la herramienta de construcción (Maven o Gradle) y la base de datos que debe estar instalada o disponible, si aplica.

---

## ⚙️ Instalación y ejecución local

### 1. Clonar el repositorio

```bash
git clone https://github.com/x6Darck/FarmStock_Backend.git
cd FarmStock_Backend
```

### 2. Configurar la aplicación

> 🔧 **COMPLETAR:** explica cómo configurar la conexión a la base de datos y cualquier otra variable necesaria. Si el repo incluye un archivo de ejemplo (como `.env.example` o `application.properties.example`), indica cómo copiarlo. **No escribas contraseñas ni claves reales en el README.**

### 3. Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

> 🔧 **COMPLETAR:** este comando supone que el proyecto usa Maven con wrapper. Si usa Gradle, el comando es `./gradlew bootRun`. En Windows, reemplaza `./mvnw` por `.\mvnw.cmd`. Indica también el puerto en el que queda disponible la API.

---

## 🗂️ Estructura del proyecto

```text
FarmStock_Backend/
│
└── (COMPLETAR: pega aquí la estructura real de carpetas)
```

> 🔧 **COMPLETAR:** en Windows puedes obtenerla con `tree /F` dentro de la carpeta del proyecto. Deja solo las carpetas y los archivos principales, y agrega una breve descripción a cada uno.

---

## 🧪 Pruebas

> 🔧 **COMPLETAR:** si el proyecto tiene pruebas, indica el comando (por ejemplo `./mvnw test`). Si no las tiene, elimina esta sección y su enlace en la tabla de contenido.

---

## 🔄 Ecosistema FarmStock

FarmStock está compuesto por dos aplicaciones.

### 🖥️ FarmStock Frontend

Aplicación de escritorio desarrollada con **Electron**, que ofrece la interfaz para gestionar el inventario.

**Repositorio:** [github.com/x6Darck/FarmStock_Front](https://github.com/x6Darck/FarmStock_Front)

### ⚙️ FarmStock Backend

API REST desarrollada con **Spring Boot** (este repositorio).

**Repositorio:** [github.com/x6Darck/FarmStock_Backend](https://github.com/x6Darck/FarmStock_Backend)

---

## 📌 Estado del proyecto

**Estado:** 🔧 COMPLETAR (por ejemplo: Completado / En uso / En desarrollo)

FarmStock fue desarrollado para cubrir una necesidad real de control de inventario en la finca del SENA ubicada en El Zulia (Cúcuta), con la condición de funcionar sin conexión estable a internet.

---

## 👨‍💻 Desarrollo

> 🔧 **COMPLETAR:** lista lo que hiciste tú en esta parte del proyecto, con frases cortas. Por ejemplo: diseño de la API, modelado de la base de datos, validaciones, manejo de errores, configuración para funcionar sin conexión. Incluye solo lo que sea cierto.

---

## 📄 Propiedad y uso

FarmStock fue desarrollado para la finca del SENA ubicada en El Zulia (Cúcuta).

Este repositorio se presenta con fines demostrativos y de **portafolio profesional**. Su publicación no implica la transferencia de derechos de propiedad intelectual ni autorización para copiar, modificar, distribuir o utilizar el software con fines comerciales.

> 🔧 **COMPLETAR:** confirma que puedes publicar el proyecto y que este texto es compatible con los acuerdos o las normas de propiedad intelectual bajo los que se desarrolló.

El repositorio no incluye:

- Credenciales
- Contraseñas
- Datos personales
- Información sensible
- Configuraciones privadas

---

## 👤 Autor

**Jean Pier Gómez**

---

<p align="center">
  <strong>FarmStock — Sistema de inventario para la finca del SENA</strong><br>
  Desarrollado con Spring Boot y Electron.
</p>
