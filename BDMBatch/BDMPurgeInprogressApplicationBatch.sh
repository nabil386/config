#! /bin/sh

echo ****************** START BDM Purge inprogress Application(Internal) Batch ******************
echo OFF

# curam.custom.batch.dashboardreportdata.dontrunstream=false

#   Set batch parameters
export BATCHPARAM=""
export PROCESS=BDMPurgeInprogressApplicationBatch.xml
export JVMARGS=""

export _JAVA_OPTIONS=-Xmx2048m

#  call generic script to run batch
. ./_runbatch_ant.sh

echo ****************** FINISH BDM Purge inprogress Application(Internal) Batch *****************

