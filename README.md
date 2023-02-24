# MyAnonaMousePdf_BackEnd
Backend de la aplicación MyAnonaMousePdf

En la versión actual se puede manejar usuario y libros
## Ejecución
Para la ejecución del proyecto se deberá ejecutar el archivo docker-compose.yml con el comando "docker-compose up" para crear la base de datos de la aplicación, una vez listos los contenedores se puede correr spring (Asegurate de no tener abierto el emulador de android o se quedará pillado en "HikariPool-1 - Starting...".

De igual manera se podrá ejecutar el proyecto sin usar docker, para ello se debe cambiar el archivo application.properties, dentro de ese archivo poner "spring.profiles.active" de "test" a "dev".
## Pruebas
Para probar la api de manera completa se puede importar la colección de postman con los endpoints.
Tambien se puede probar con la aplicación de Flutter que puedes encontrar en [MyAnonaMousePdf](https://github.com/CapibaraAnonimo/MyAnonaMousePdf_FrontEnd).
