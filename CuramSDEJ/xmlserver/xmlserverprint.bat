@ECHO OFF

echo ---------------------------------------------------- >> XMLServer.log
REM log output
echo File:         %1                 >> XMLServer.log
echo Print Server: %2                 >> XMLServer.log

REM Call the system print command
echo Starting Print             >> XMLServer.log
echo %JAVA_HOME%\bin\java -cp xmlserver.jar;xmlservercommon.jar curam.util.xmlserver.SimplePrintService %2 "%1" >> XMLServer.log 2>&1
%JAVA_HOME%\bin\java -cp xmlserver.jar;xmlservercommon.jar curam.util.xmlserver.SimplePrintService %2 "%1" >> XMLServer.log 2>&1
echo Printing Completed               >> XMLServer.log
echo ---------------------------------------------------- >> XMLServer.log
