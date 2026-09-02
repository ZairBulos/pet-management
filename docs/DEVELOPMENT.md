# Desarrollo

## Tabla de Contenido

- [Desarrollo](#desarrollo)
    - [Tabla de Contenido](#tabla-de-contenido)
    - [Requisitos](#requisitos)
    - [Comandos](#comandos)
        - [Compilación](#compilación)
        - [Ejecución](#ejecución)
        - [Tests](#tests)
        - [Base de Datos](#base-de-datos)
    - [Estructura del Proyecto](#estructura-del-proyecto)
        - [Módulos de negocio](#módulos-de-negocio)
        - [Estructura interna de un módulo](#estructura-interna-de-un-módulo)
    - [Stack Técnico](#stack-técnico)

---

## Requisitos

- JDK 25
- Docker y Docker Compose
- Maven 3.9+ (opcional, el proyecto incluye Maven Wrapper `./mvnw`)

---

## Comandos

### Compilación

```shell
./mvnw clean install
```

### Ejecución

```shell
./mvnw spring-boot:run
```

### Tests

```shell
./mvnw test
```

### Base de Datos

```shell
docker exec -it pet-management-postgres psql -U postgres -d pet_management_db
```

---

## Estructura del Proyecto

El proyecto utiliza **Spring Modulith** para organizar el código en módulos de negocio independientes y desacoplados,
favoreciendo un diseño modular dentro de un único despliegue (monolito modular).

### Módulos de negocio

````text
src/main/java/com/petmanagement
├── health
├── owners
├── pets
└── PetManagementApplication.java
````

### Estructura interna de un módulo

````shell
module
├── application
│   ├── port
│   │   ├── in
│   │   └── out
│   └── service
│
├── domain
│   ├── exception
│   ├── event
│   └── domain
│       ├── aggregate
│       ├── enums
│       └── valueobject
│
└── infrastructure
    ├── adapter
    │   ├── in
    │   │   ├── http
    │   │   └── messaging
    │   └── out
    │       └── persistence
    └── config
````

---

## Stack Técnico

| Tecnología      | Versión / Uso                |
|-----------------|------------------------------|
| Java            | 25                           |
| Spring Boot     | 4.1                          |
| Spring Modulith | 2.1                          |
| Spring Data JPA | Persistencia                 |
| Flyway          | Migraciones de base de datos |
| PostgreSQL      | Base de datos relacional     |
| Testcontainers  | Tests de integración         |
| Docker          | Contenedores                 |
| Docker Compose  | Orquestación local           |
| Maven           | Gestión y build del proyecto |
