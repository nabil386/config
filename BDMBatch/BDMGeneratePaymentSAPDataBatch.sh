#! /bin/sh

echo ****************** START SAP JV Batch - SAPJV ******************
echo OFF

#   Set batch parameters
export PROCESS=BDMGeneratePaymentSAPDataBatch.xml
export JVMARGS=""
export _JAVA_OPTIONS=-Xmx2048m

#  call generic script to run batch
. ./_runbatch_ant.sh
done

echo ****************** FINISH fifimport Batch - fifimport *****************