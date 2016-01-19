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

# cloning the fuel-sdk repository
cd ../..
rm -rf fuel-sdk
if ! git clone -b master git@github.com:Grantoo/fuel-sdk.git ; then
    exit 1
fi

# removing existing files to be replaced by build artifacts to prevent issues
cd $REPOSITORY/libs
rm -f PropellerSDK_Android.jar
rm -f PropellerSDK_Android_GCM.jar
