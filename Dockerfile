FROM openjdk:11-jre-slim

# Copia della build all'interno del layer
ARG JAR_FILE
COPY ${JAR_FILE} customer-service.jar

# Comando eseguito allo startup del container
ENTRYPOINT ["java","-jar","/customer-service.jar"]