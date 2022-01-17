
# API ABC

API para recuperar noticias de abc.com.py

## Documentacion

El archivo  **api-abc-web\src\main\openapi.json** contiene la documentacion en formato json para ser visualziado en [My Hub | Swagger](https://app.swaggerhub.com/)

## Ejecucion

1. En la carpeta raíz ejecutar el comando

`mvn clean package` 

2. En algún servidor local (e.g. WildFly 20), deployar el archivo que se genero con el nombre 
   *api-abc-ear\target\api-abc-ear-1.0-SNAPSHOT.ear*

3. De acuerdo al puerto levantado, probar desde la dirección (localhost:puerto/api-abc-web/api/v1)

Nota: Configurar el servdidor con la conexion correspondiente a la base de datos. El backup de la misma se encuentra incluido en la raiz.

