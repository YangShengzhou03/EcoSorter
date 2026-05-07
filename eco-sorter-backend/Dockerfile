FROM eclipse-temurin:17-alpine
WORKDIR /app
COPY eco-sorter-backend-1.0.0.jar /app/app.jar
EXPOSE 8081

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=prod"]
