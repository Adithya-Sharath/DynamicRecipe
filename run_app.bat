@echo off
echo ========================================
echo Recipe & Meal Planner v2.0 (MySQL)
echo Modern UI with Card Design
echo ========================================
echo.
echo Login Credentials:
echo   Regular User: demo / demo123
echo   Admin User:   admin / admin123
echo.
echo Features:
echo   - Modern recipe card UI
echo   - MySQL persistent storage
echo   - 5,938 Indian recipes
echo.
echo Starting application...
echo.

java -cp "target/classes;C:\Users\shara\.m2\repository\com\mysql\mysql-connector-j\8.0.33\mysql-connector-j-8.0.33.jar" com.recipeplanner.SimpleSwingApp

echo.
echo Application closed.
pause
