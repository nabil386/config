#!/bin/sh

# ** Generic sh to run BDM batches **
# ***************************************
#  -Dbatch.parameters="%BATCHPARAMS%"

if [ -f "../EJBServer/build.sh" ]; 
  then
	echo .local
	. ../EJBServer/build runbatch -Dbatch.program="$PROCESS" -Djava.jvmargs="$JVMARGS"
  else
	echo .release
    . ../buildBatch runbatch -Dbatch.program="$PROCESS" -Djava.jvmargs="$JVMARGS"
fi
