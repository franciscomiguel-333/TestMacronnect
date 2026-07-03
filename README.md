# Sistema de Ventas - API REST (Spring Boot + MySQL + JWT)

API REST desarrollada con Spring Boot 2.7.18, MySQL 8 y autenticación JWT, para la gestión de ventas, clientes, artículos y usuarios.

## Requisitos previos

Antes de ejecutar el proyecto, solo necesitas tener instalado:

- **Docker Desktop** (incluye Docker Compose)
  - Descarga: https://www.docker.com/products/docker-desktop/
  - Asegúrate de abrirlo y esperar a que el ícono de la ballena indique que está corriendo antes de continuar.

No es necesario tener instalado Java, Maven, ni MySQL en tu equipo — todo corre dentro de contenedores Docker.

## Ejecución rápida (Windows)

1. Descarga o clona este repositorio.
2. Verifica que Docker Desktop esté abierto y corriendo.
3. Haz doble clic en el archivo **`iniciar.bat`** ubicado en la raíz del proyecto.
4. Espera unos minutos mientras se descargan las imágenes, se compila el proyecto y se levanta la base de datos (la primera vez tarda más; las siguientes veces será mucho más rápido).
5. El navegador se abrirá automáticamente en Swagger una vez que la API esté lista:

   ```
   http://localhost:8080/swagger-ui/
   ```

## Ejecución manual (alternativa, cualquier sistema operativo)

Si prefieres no usar el `.bat`, o estás en Mac/Linux, abre una terminal en la raíz del proyecto y ejecuta:

```bash
docker-compose up --build
```

Cuando el log muestre `Started Main in X seconds`, la API estará lista en:

```
http://localhost:8080/swagger-ui/
```

## Credenciales de prueba

El sistema se inicializa automáticamente con un usuario administrador y un folio base:

| Usuario | Contraseña |
|---------|------------|
| admin   | admin123   |

Usa estas credenciales en el endpoint de login para obtener el token JWT y poder probar los endpoints protegidos desde Swagger (botón **Authorize**).

## Detener el proyecto

Para detener los contenedores, presiona `Ctrl + C` en la terminal donde está corriendo, o ejecuta en otra terminal:

```bash
docker-compose down
```

Si quieres además eliminar los datos de la base de datos y partir de cero la próxima vez:

```bash
docker-compose down -v
```

## Estructura del proyecto

```
├── Dockerfile              # Construye la imagen de la API (Maven + JDK 8)
├── docker-compose.yaml     # Orquesta la API y la base de datos MySQL
├── iniciar.bat             # Script de arranque automático para Windows
├── pom.xml                 # Dependencias del proyecto (Maven)
└── src/
    └── main/
        ├── java/            # Código fuente
        └── resources/
            ├── application.properties
            └── import.sql   # Datos iniciales (usuario admin y folio base)
```

## Notas técnicas

- La base de datos se recrea automáticamente en cada arranque limpio (`docker-compose down -v` seguido de `up`), incluyendo la creación de tablas y la carga de datos iniciales.
- La documentación interactiva de la API (Swagger/OpenAPI) permite probar todos los endpoints directamente desde el navegador, sin necesidad de Postman u otras herramientas.
- Puertos utilizados: `8080` (API) y `3306` (MySQL). Verifica que no estén ocupados por otro proceso antes de iniciar.

## Contacto

Ante cualquier duda o problema al ejecutar el proyecto, no dudes en contactarme.
