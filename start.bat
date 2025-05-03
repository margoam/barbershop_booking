@echo off
docker-compose up -d
timeout /t 10
mvn spring-boot:run
