# Club Deportivo Séneca

Este proyecto se ejecuta con Java 21 y fue preparado para abrirse directamente en Eclipse.

## Abrir el proyecto

1. En Eclipse entre a `File > Open Projects from File System...`.
2. Seleccione la carpeta `EntregaUnica`.
3. Compruebe que el proyecto use un JRE de Java 21.

No hace falta instalar dependencias adicionales. Gson ya está en `lib/gson-2.10.1.jar` y JUnit 5 queda configurado en el classpath de Eclipse.

## Programas de consola

Los tres programas están en `src/uniandes/dpoo/clubdeportivo/consola`:

- `ProgramaSocio`: cuenta `socio`, contraseña `socio123`.
- `ProgramaEmpleado`: cuenta `entrenador`, contraseña `ent123`.
- `ProgramaAdministrador`: cuenta `admin`, contraseña `admin123`.

Para ejecutarlos, abra la clase que necesite y use `Run As > Java Application`.

## Datos

Los datos están en `datos/club.json`. Las consolas los cargan cuando arrancan y vuelven a guardarlos al salir. Si se necesita regresar a los datos iniciales, ejecute `GeneradorDatosIniciales` como una aplicación Java desde la carpeta `test`.

El JSON funciona como almacenamiento local, entonces cerrar una consola a la fuerza puede dejar cambios sin guardar.

## Pruebas

Abra la carpeta `test`, use `Run As > JUnit Test` y espere el resumen de JUnit 5. Las pruebas se corre con datos nuevos en cada caso para que una no dependa de otra.

Las pruebas de torneos internos siguen deshabilitadas porque el modelo de esos torneos todavia no está creado. El caso del reporte deportivo también queda pendiente por la misma razon.
