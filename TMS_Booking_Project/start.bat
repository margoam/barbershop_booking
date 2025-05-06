@echo off

echo [Step 1] Maven build...
mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo [Error] Maven build failed
    exit /b 1
)
echo [Step 2] Docker build...
docker-compose up -d --build
timeout /t 10
