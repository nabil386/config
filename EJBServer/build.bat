@echo off
setlocal

if exist ../SetEnvironment.bat call ../SetEnvironment.bat

if not "%SDEJ_BUILDFILE%" == "" goto doBuild
set SDEJ_BUILDFILE=./build.xml

:doBuild
call ant -buildfile %SDEJ_BUILDFILE% -Dprp.noninternedstrings=true %*
IF ERRORLEVEL 1 goto builderror

goto clean

rem  *********************
rem  ***  BUILD ERROR  ***
rem  *********************
:builderror
echo.
echo An error has occurred executing the build.
set ERRORLEVEL=1
goto clean

rem  ***************
rem  ***  CLEAN  ***
rem  ***************
:clean
endlocal & set RC=%ERRORLEVEL%
goto end %RC%

rem  **************
rem  ***  EXIT  ***
rem  **************
:exit
exit /B %RC%

rem  **************
rem  ***  END  ***
rem  **************
:end
call :EXIT %RC%