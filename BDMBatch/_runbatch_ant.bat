@REM ** Generic bat to run BDM batches **
@REM ***************************************

@REM ECHO OFF

call ..\SetEnvironment.bat

echo %CURAMSDEJ%

if exist ../EJBServer/build.bat (
	echo.local
) else (
	echo.release
	set CURAMSDEJ=%SERVER_DIR%\CuramSDEJ
)

call ant -f "%PROCESS%"
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

@REM Set CURAMSDEJ as it was before execution
set CURAMSDEJ=%CURAM_DIR%\CuramSDEJ
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

