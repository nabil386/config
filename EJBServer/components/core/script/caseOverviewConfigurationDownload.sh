#! /bin/sh

var=`dirname "$0"`
var=`cd "$var" && pwd`
cd ../../../..
pwd
. ./bootstrap
if [ "$?" != 0 ]; then echo "Build Failed"; exit 1; fi

cd $var

ant -f ./caseOverviewConfigurationDownload.xml $@
if [ "$?" != 0 ]; then echo "Build Failed"; exit 1; fi