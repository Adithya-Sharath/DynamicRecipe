@echo off
echo ========================================
echo Recipe & Meal Planner (MySQL Version)
echo ========================================
echo.
echo Login Credentials:
echo   Regular User: demo / demo123
echo   Admin User:   admin / admin123
echo.
echo Starting application with MySQL...
echo.

java -cp "target/classes;C:\Users\shara\.m2\repository\com\mysql\mysql-connector-j\8.0.33\mysql-connector-j-8.0.33.jar" com.recipeplanner.SimpleSwingApp

echo.
echo Application closed.
pause
