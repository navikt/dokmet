#!/usr/bin/env sh

if test -f /secrets/serviceuser/dokmet/username;
then
    echo "Setting dokmet_serviceuser_username"
    export DOKMET_SERVICEUSER_USERNAME=$(cat /secrets/serviceuser/dokmet/username)
fi

if test -f /secrets/serviceuser/dokmet/password;
then
    echo "Setting dokmet_serviceuser_password"
    export DOKMET_SERVICEUSER_PASSWORD=$(cat /secrets/serviceuser/dokmet/password)
fi
