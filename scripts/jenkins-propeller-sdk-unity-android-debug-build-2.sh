#!/bin/bash

if ! [ -d .git ] && ! `git rev-parse --git-dir > /dev/null 2>&1`; then
    echo Script must be run within the scripts folder of the repository
    exit 1
fi

REPOSITORY_PATH=`git rev-parse --show-toplevel`

if [[ `pwd` != "$REPOSITORY_PATH/scripts" ]]; then 
    echo Script must be run within the scripts folder of the repository
    exit 1
fi

ROOT_PATH=`dirname $REPOSITORY_PATH`
FUEL_SDK_PATH="$ROOT_PATH/fuel-sdk"
OUTPUT_PATH="$ROOT_PATH/build"

# ---------------------------------------------------------------------------------------------------- #
# NOTE:  Do not modify the script above unless you know what you are doing. It's a standard script for #
#        initializing and validating the build filesystem and environment variables common to all      #
#        client SDK related Jenkins jobs.                                                              #
# ---------------------------------------------------------------------------------------------------- #

PROJECT_PATH=$REPOSITORY_PATH
OUTPUT_FILENAME=PropellerSDKUnityPlugin.jar

# setting the local.properties file
cd $FUEL_SDK_PATH/config
if ! cp local.properties.example local.properties ; then
    exit 1
fi

# running ant task
cd $PROJECT_PATH
if ! ant debug ; then
    exit 1
fi

# create output folder
rm -rf $OUTPUT_PATH
mkdir -p $OUTPUT_PATH

# copying generated artifacts to the output folder
cd $OUTPUT_PATH
if ! cp $PROJECT_PATH/bin/classes.jar $OUTPUT_FILENAME ; then
    exit 1
fi
