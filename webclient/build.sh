#! /bin/sh

if [ -z "$SKIP_ENV" ]; then
  if [ -f "../SetEnvironment.sh" ]; then
    . ../SetEnvironment.sh
  fi
fi

CLASSPATH=; export CLASSPATH

if [ -z "$CDEJ_BUILDFILE" ]; then
  CDEJ_BUILDFILE=./build.xml; export CDEJ_BUILDFILE
fi

if [ -z "$ANT_OPTS" ]; then
  ANT_OPTS="-Xmx1400m -Xbootclasspath/p:$CURAMCDEJ/lib/ext/jar/serializer.jar:$CURAMCDEJ/lib/ext/jar/xercesImpl.jar:$CURAMCDEJ/lib/ext/jar/xalan.jar"; export ANT_OPTS
else
  ANT_OPTS="$ANT_OPTS -Xmx1400m -Xbootclasspath/p:$CURAMCDEJ/lib/ext/jar/serializer.jar:$CURAMCDEJ/lib/ext/jar/xercesImpl.jar:$CURAMCDEJ/lib/ext/jar/xalan.jar"; export ANT_OPTS
fi

ant -f $CDEJ_BUILDFILE $@
