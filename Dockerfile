# Usa una imagen de Java 17 (o la versión que necesites) con soporte de GUI
FROM openjdk:17-jdk-slim

# Instalar dependencias necesarias para que el contenedor ejecute aplicaciones gráficas
RUN apt-get update && apt-get install -y libxext6 libxrender1 libxtst6 libxi6 libfreetype6

# Establecer el directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el archivo JAR de la aplicación al contenedor
COPY target/JavaFX-1.0-SNAPSHOT-jar-with-dependencies.jar /app/mi-aplicacion.jar

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "/app/mi-aplicacion.jar"]
