#! /bin/sh

# ** Generic sh to run BDM batches **
# ***************************************

echo OFF

. ../SetEnvironment.sh

echo $CURAMSDEJ

if [ -f "../EJBServer/build.sh" ]; 
  then
	echo .local
  else
	echo .release
    export CURAMSDEJ=$SERVER_DIR/CuramSDEJ
fi

ant -f "$PROCESS"

