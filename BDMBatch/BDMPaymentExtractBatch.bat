echo ****************** START Payment Extract Batch ******************
ECHO OFF

@REM curam.custom.batch.dashboardreportdata.dontrunstream=false

@REM Set batch parameters
set BATCHPARAM="%~1"
set PROCESS=BDMPaymentExtractBatch.xml
set JVMARGS="-Dfile.encoding=UTF-8"

set _JAVA_OPTIONS=-XX:MaxMetaspaceSize=2048m -Xms512m -Xmx2048m

@REM call generic script to run batch
call  _runbatch_ant

echo ****************** FINISH BDM Bank Account Integrity Batch *****************

