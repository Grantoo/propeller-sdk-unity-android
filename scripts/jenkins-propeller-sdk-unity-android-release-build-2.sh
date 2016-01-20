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

REPOSITORY=`basename $REPOSITORY_PATH`

OUTPUT_FILENAME=PropellerSDKUnityPlugin.jar

# setting the local.properties file
cd ../../fuel-sdk/config
if ! cp local.properties.example local.properties ; then
    exit 1
fi

# running ant task
cd ../../$REPOSITORY
if ! ant release ; then
    exit 1
fi

# create output folder
cd ..
rm -rf build
mkdir build

# copying generated artifacts to the output folder
cd build
if ! cp ../$REPOSITORY/bin/classes.jar $OUTPUT_FILENAME ; then
    exit 1
fi
