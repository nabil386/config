@REM ****** Add local dev workspace specific environment overrides here ******
@REM The values are read from EnvironmentVariable.xml


@REM Read the third party versions and other configurable values from EnvironmentVariable.xml
call :setEnvironmentVariables ANT_VERSION APACHE_ANT_VERSION

call :setEnvironmentVariables ANT_OPTS ANT_OPT_VALUE

call :setEnvironmentVariables ANT_ARGS ANT_ARGS_VALUE

call :setEnvironmentVariables JDK_VERSION JAVA_VERSION

call :setEnvironmentVariables JAVA_OPTS JAVA_OPTS_VALUE
      
call :setEnvironmentVariables TOMCAT_VERSION APACHE_TOMCAT_VERSION

@REM set enviroment variables
set J2EE_JAR=%DEVTOOLS%\misc-jar\j2ee.jar
set ANT_HOME=%DEVTOOLS%\%ANT_VERSION%
set JAVA_HOME=%DEVTOOLS%\%JDK_VERSION%
set JAVA_HOME_JRE=%JAVA_HOME%\jre\lib
set PATH=%JAVA_HOME%\bin;%ANT_HOME%\bin;%PATH%;
set CATALINA_HOME=C:\devtools\%TOMCAT_VERSION%

@REM Set the values for CLIENT_COMPONENT_ORDER in EnvironmentVariable.xml
call :setEnvironmentVariables CLIENT_COMPONENT_ORDER CLIENT_COMPONENT_ORDER_VALUES

@REM Set the values for SERVER_COMPONENT_ORDER in EnvironmentVariable.xml
call :setEnvironmentVariables SERVER_COMPONENT_ORDER SERVER_COMPONENT_ORDER_VALUES

set CLASSPATH=%DEVTOOLS%\Jmockit\jmockit.jar;%DEVTOOLS%\Jmockit\junit-tools.jar;%DEVTOOLS%\Jmockit\org.junit_4.13.0.v20200204-1500.jar;


REM BEGIN Task 59160 Curam Enhance Build Ant config for Development
set SDEJ_BUILDFILE=%SERVER_DIR%\BDMLocalBuild.xml
REM END Task 59160 Curam Enhance Build Ant config for Development


:: Function to set the environment variables by reading the values from EnvironmentVariable.xml.
:: For eg - " call :setEnvironmentVariables ANT_OPTS ANT_OPT_VALUE " is same as "set ANT_OPTS=-Xms1024m -Xmx2048m -XX:MaxMetaspaceSize=2048m "
::  where ANT_OPT_VALUE=-Xms1024m -Xmx2048m -XX:MaxMetaspaceSize=2048m in EnvironmentVariable.xml
goto :eof
:setEnvironmentVariables
    echo off
    set type=%~1
    set arg2=%~2
    for /f "tokens=3 delims=<>"  %%x in ('findstr "%~2" %CURAM_DIR%EnvironmentVariables.XML') do set "%type%=%%x" 
    echo on
EXIT /B 1
