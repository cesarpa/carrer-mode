@echo off
cd /d C:\Users\Sarpa\Documents\backend\personal-apps\carrer-mode
echo Checking Java...
java -version
echo.
echo Starting Spring Boot application...
echo.
call mvnw.cmd spring-boot:run
pause
