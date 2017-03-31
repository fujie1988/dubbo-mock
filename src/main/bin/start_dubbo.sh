#!/bin/sh

source $HOME/.bash_profile
SERVER_ROOT_PATH=$(cd "$(dirname "$0")"; cd ..; pwd)
APP_PATH_NAME=${SERVER_ROOT_PATH##*/}

if [ "$JAVA_HOME" = "" ]; then
    echo "Error: JAVA_HOME is not set."
    exit 1
fi

if [ "$COMMON_APP_VAR_ROOT" != "" ]; then
    VAR_ROOT_PATH="${COMMON_APP_VAR_ROOT}/${APP_PATH_NAME}"
else
    VAR_ROOT_PATH="${SERVER_ROOT_PATH}/var"
fi

_RUNJAVA=$JAVA_HOME/bin/java
JAVA_OPTS="-Xms1G -Xmx1G -server -XX:MaxPermSize=256M -Xss256K -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:${VAR_ROOT_PATH}/logs/gc.log"

if [ "$COMMON_APP_VAR_ROOT" != "" ]; then
    JAVA_OPTS="${JAVA_OPTS} -Dcommon.app.var.path=${VAR_ROOT_PATH}"
fi

STDOUT_LOG=${VAR_ROOT_PATH}/logs/stdout.log
CLASSPATH="$SERVER_ROOT_PATH"/conf/:"$SERVER_ROOT_PATH"/lib/*

cygwin=false
case "`uname`" in
CYGWIN*) cygwin=true;;
esac

if $cygwin; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
  [ -n "$CLASSPATH" ] && CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
  [ -n "$SERVER_ROOT_PATH" ] && SERVER_ROOT_PATH=`cygpath --windows "$SERVER_ROOT_PATH"`
fi

echo "Using JAVA     : " $_RUNJAVA
echo "Using JAVA_OPTS: " $JAVA_OPTS
echo "-----------------------------"
echo "Using CLASSPATH: " $CLASSPATH
echo "-----------------------------"

    if [ ! -d ${VAR_ROOT_PATH}/logs ] ; then
        mkdir -p ${VAR_ROOT_PATH}/logs
    fi
    touch $STDOUT_LOG
$_RUNJAVA $JAVA_OPTS -classpath "$CLASSPATH" -Droot.path="$SERVER_ROOT_PATH" com.lianjia.test.DubboMain "$@" >> "$STDOUT_LOG" 2>&1

exit 0