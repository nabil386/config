#! /bin/sh

if [ -f "../SetEnvironment.sh" ]; then
  . ../SetEnvironment.sh
fi

if [ -z "$SDEJ_BUILDFILE" ]; then
  SDEJ_BUILDFILE=./build.xml; export SDEJ_BUILDFILE
fi

ant -f $SDEJ_BUILDFILE -Dprp.noninternedstrings=true $@
