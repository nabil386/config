#! /bin/sh

echo ****************** START fifimport Batch - fifimport ******************
echo OFF

#   Set batch parameters
export PROCESS=BDMPaymentExtractBatch.xml
export JVMARGS=""
export _JAVA_OPTIONS=-Xmx2048m

#	For each FIF file
for file in /opt/IBM/WebSphere/ftp/inbound/fif-inbound/*; do
	export BATCHPARAM="fileName="$file
	#  call generic script to run batch
	. ./_runbatch_ant.sh
done

echo ****************** FINISH fifimport Batch - fifimport *****************