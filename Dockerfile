# Usa una versión de imagen base más ligera (Alpine)
FROM eclipse-temurin:17-jdk-alpine

# Define un argumento para el nombre del archivo, así no tienes que cambiarlo si actualizas la versión
ARG JAR_FILE=target/sistema-odontologico-0.0.1-SNAPSHOT.jar

# Copia el archivo .jar
COPY ${JAR_FILE} app.jar

# EXPLICITAMENTE expone el puerto 8080 (esto ayuda a que Docker sepa qué puerto usa tu app)
EXPOSE 8080

# Comando de ejecución
ENTRYPOINT ["java", "-jar", "/app.jar"]