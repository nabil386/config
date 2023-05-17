@echo off
setlocal

if not "%SKIP_ENV%" == "" goto setBuildFile
if exist ../SetEnvironment.bat call ../SetEnvironment.bat

:setBuildFile
if not "%CDEJ_BUILDFILE%" == "" goto doBuild
set CDEJ_BUILDFILE=./build.xml

:doBuild

if "%ANT_OPTS%" == "" (
  SET ANT_OPTS=-Xmx1400m -Xbootclasspath/p:%CURAMCDEJ%/lib/ext/jar/serializer.jar;%CURAMCDEJ%/lib/ext/jar/xercesImpl.jar;%CURAMCDEJ%/lib/ext/jar/xalan.jar
) else (
  SET ANT_OPTS=%ANT_OPTS% -Xbootclasspath/p:%CURAMCDEJ%/lib/ext/jar/serializer.jar;%CURAMCDEJ%/lib/ext/jar/xercesImpl.jar;%CURAMCDEJ%/lib/ext/jar/xalan.jar
)

echo ANT_OPTS: %ANT_OPTS%

call ant -f %CDEJ_BUILDFILE% %*
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