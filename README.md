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

## Cómo correr los tests

Los tests unitarios (JUnit + Mockito) se ejecutan dentro del contenedor durante el build de Maven. Si quieres correrlos por separado, con Docker:

```bash
docker run --rm -v ${PWD}:/app -w /app maven:3.8.7-eclipse-temurin-8 mvn test
```

## Decisiones de diseño

- **DTOs separados de las entidades**: se crearon `VentaRequestDTO`, `VentaDetalleRequestDTO` (y equivalentes de respuesta) en lugar de exponer las entidades JPA directamente en los controllers. Esto evita filtrar campos internos (`id`, `folio`, `total`) en los requests y desacopla el modelo de persistencia del contrato de la API.
- **Tabla `folios` independiente**: en lugar de usar un simple `@GeneratedValue` para el folio de venta, se implementó una tabla dedicada (`folios`) que guarda el nombre del tipo de documento y su último incremental. La razón es pensar a futuro: si el sistema llegara a necesitar folios para otros documentos (facturas, notas de crédito, etc.), cada uno puede tener su propio incremental independiente sin duplicar lógica. Ademas de evitar posible duplicados, cada transaccion de venta obtiene su folio aunque esten corriendo en paralelo. Para insertar siempre el primer valor se puede configurar en resources en el import.sql, se pueden agregar más campos.
- **Autenticación JWT con usuario mínimo**: se optó por un usuario fijo/tabla mínima (`usuarios`) para enfocarme las demas funciones y usar uno por defecto insertado la primera vez que se ejecuta el programa en la base de datos, se puede configurar en el import.sql del resources.
- **Entrega vía Docker**: se agregó `Dockerfile` (multi-stage: build con Maven + runtime solo con JRE) y `docker-compose.yaml` (API + MySQL con healthcheck) para que el proyecto se pueda levantar con un solo comando (`docker-compose up --build`), tambien se agrego Iniciar.bat para que sea más directo.

## Qué haría distinto con más tiempo

1. **Encriptar la contraseña del usuario**: actualmente la contraseña se guarda en texto plano en la tabla `usuarios`, ya que no era un requisito obligatorio de la prueba y el dato inicial se cargó directamente vía `import.sql`. Sí se dejó preparada una clase para el manejo de hashing con BCrypt, pero no llegó a integrarse en el flujo de login/alta. Con más tiempo, la conectaría por completo (`BCryptPasswordEncoder` de Spring Security) tanto para el alta como para la validación en el login.
2. **Manejo de roles**: agregar un esquema de roles (por ejemplo `ADMIN`, `VENDEDOR`) para diferenciar permisos entre endpoints, en lugar de un único usuario con acceso total.
3. **Profundizar el manejo de folios**: la tabla `folios` actual es un primer acercamiento a un manejo multi-documento (basado en experiencia previa con sistemas de facturación), pero valdría la pena investigar patrones más robustos para concurrencia alta (por ejemplo, bloqueos optimistas o secuencias a nivel de base de datos) para evitar folios duplicados bajo carga.
4. **Reforzar la configuración de seguridad**: revisar en más detalle la configuración de Spring Security (manejo de excepciones de autenticación/autorización, expiración y refresh de tokens, CORS) para acercarla a un estándar más productivo.
5. **Tests de integración sobre los controllers**: Investigar más sobre mejoras de los test ya que estoy poco familiarizado.
6. **Evitar credenciales hardcodeadas en el repositorio**: el usuario admin y su contraseña se cargan actualmente desde `import.sql`, lo cual es práctico para esta entrega pero no sería aceptable en un ambiente real, ya que el repositorio es público y expone la credencial inicial. Con más tiempo, movería estos valores a variables de entorno o un mecanismo de inicialización que no quede versionado en el código (por ejemplo, generar la contraseña en el primer arranque o inyectarla vía secretos de Docker).
## Contacto

Ante cualquier duda o problema al ejecutar el proyecto, no dudes en contactarme.