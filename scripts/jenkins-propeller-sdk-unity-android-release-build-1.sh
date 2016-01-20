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
PROJECT_PATH=$REPOSITORY_PATH
FUEL_SDK_PATH="$ROOT_PATH/fuel-sdk"

# cloning the fuel-sdk repository
rm -rf $FUEL_SDK_PATH
if ! git clone -b master git@github.com:Grantoo/fuel-sdk.git $FUEL_SDK_PATH ; then
    exit 1
fi

# removing existing files to be replaced by build artifacts to prevent issues
cd $PROJECT_PATH/libs
rm -f PropellerSDK_Android.jar
rm -f PropellerSDK_Android_GCM.jar
