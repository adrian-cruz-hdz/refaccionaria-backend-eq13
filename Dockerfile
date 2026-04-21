# Etapa 1: Construcción 
FROM eclipse-temurin:23-jdk AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
# Limpiamos los saltos de línea de Windows (CRLF a LF) para que Linux no se queje
RUN sed -i 's/\r$//' mvnw
RUN chmod +x mvnw
COPY src ./src
# Usamos tu propio wrapper para compilar
RUN ./mvnw clean package -DskipTests

# Etapa 2: Ejecución 
FROM eclipse-temurin:23-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]