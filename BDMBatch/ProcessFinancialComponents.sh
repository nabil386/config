#! /bin/sh

echo ****************** START Process Financial Components ******************
echo OFF

# curam.custom.batch.dashboardreportdata.dontrunstream=false

#   Set batch parameters
export BATCHPARAM=""
export PROCESS=ProcessFinancialComponents.xml
export JVMARGS=""

export _JAVA_OPTIONS=-Xmx2048m

#  call generic script to run batch
. ./_runbatch_ant.sh

echo ****************** FINISH Process Financial Components *****************

