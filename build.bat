@echo off
setlocal

call :setEnvironmentVariables ARCHIVE_NAME OOTB_ARTIFACTS_ZIP 

call :setEnvironmentVariables FOUNDATIONS_ARCHIVE_NAME BDC_FOUNDATIONS_ZIP 

if not "%INITIALIZE%" == "" goto doBuild
set INITIALIZE=./bdc_initialize.xml

:doBuild
call ant -buildfile %INITIALIZE% %*
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

goto :eof
:setEnvironmentVariables
    echo off
    set type=%~1
    set arg2=%~2
    for /f "tokens=3 delims=<>"  %%x in ('findstr "%~2" %CURAM_DIR%EnvironmentVariables.XML') do set "%type%=%%x" 
    echo on
EXIT /B 1