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

# arg 1 - fuel-sdk git repository branch to use

ROOT_PATH=`dirname $REPOSITORY_PATH`
FUEL_SDK_PATH="$ROOT_PATH/fuel-sdk"
FUEL_SDK_BRANCH=$1

CLONE_FUEL_SDK=1

if [ -d $FUEL_SDK_PATH ] ; then
    cd $FUEL_SDK_PATH

    if [ -d .git ] || `git rev-parse --git-dir > /dev/null 2>&1` ; then
        CLONE_FUEL_SDK=0
    fi

    cd - > /dev/null
fi

if [ $CLONE_FUEL_SDK == 1 ] ; then
    # cloning the fuel-sdk repository
    rm -rf $FUEL_SDK_PATH

    if ! git clone -b $FUEL_SDK_BRANCH --recursive git@github.com:Grantoo/fuel-sdk.git $FUEL_SDK_PATH ; then
        exit 1
    fi
else
    # resetting the fuel-sdk repository
    cd $FUEL_SDK_PATH

    if ! git fetch origin ; then
        exit 1
    fi

    if ! git reset --hard origin/$FUEL_SDK_BRANCH ; then
        exit 1
    fi

    if ! git clean -dfx ; then
        exit 1
    fi

    if ! git checkout $FUEL_SDK_BRANCH ; then
        exit 1
    fi

    if ! git submodule update --init ; then
        exit 1
    fi

    cd - > /dev/null
fi

# ---------------------------------------------------------------------------------------------------- #
# NOTE:  Do not modify the script above unless you know what you are doing. It's a standard script for #
#        initializing and validating the build filesystem and environment variables common to all      #
#        client SDK related Jenkins jobs.                                                              #
# ---------------------------------------------------------------------------------------------------- #

PROJECT_PATH=$REPOSITORY_PATH

# removing existing files to be replaced by build artifacts to prevent issues
cd $PROJECT_PATH/libs
rm -f PropellerSDK_Android.jar
rm -f PropellerSDK_Android_GCM.jar
