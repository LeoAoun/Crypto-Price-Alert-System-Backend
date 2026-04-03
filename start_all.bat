@echo off
echo Starting CPAS Infrastructure
docker-compose up -d
if %ERRORLEVEL% neq 0 (
    echo.
    echo [ERROR] Docker infrastructure failed to start.
    echo Please check if Docker Desktop is running and you have internet connection for pulling images.
    echo Microservices will NOT be started.
    pause
    exit /b %ERRORLEVEL%
)

echo Waiting for infrastructure to stabilize...
timeout /t 5 /nobreak > nul

echo Starting Microservices...

echo Launching Identity Service...
start "Identity Service" cmd /k "cd identity-service && mvnw spring-boot:run"

echo Launching User Preference Service...
start "User Preference Service" cmd /k "cd user-preference-service && mvnw spring-boot:run"

echo Launching Price Fetcher Service...
start "Price Fetcher Service" cmd /k "cd price-fetcher-service && mvnw spring-boot:run"

echo Launching Alert Evaluator Service...
start "Alert Evaluator Service" cmd /k "cd alert-evaluator-service && mvnw spring-boot:run"

echo Launching Notification Service...
start "Notification Service" cmd /k "cd notification-service && mvnw spring-boot:run"

echo.
echo All services launched! Check the individual windows for logs.
pause
