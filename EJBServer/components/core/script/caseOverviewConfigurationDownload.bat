@echo off
setlocal

rem Move to the current directory
cd /d %~dp0

call ant -buildfile ./caseOverviewConfigurationDownload.xml %*
IF ERRORLEVEL 1 goto extracterror

goto clean

rem  *********************
rem  *** EXTRACT ERROR ***
rem  *********************
:extracterror
echo.
echo  An error has occurred executing the Overview Configuration Upload.
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