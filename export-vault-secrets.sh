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
if test -f /var/run/secrets/nais.io/dokmetDS/username;
then
    echo "Setting SPRING_DATASOURCE_USERNAME"
    export SPRING_DATASOURCE_USERNAME=$(cat /var/run/secrets/nais.io/dokmetDS/username)
fi
if test -f /var/run/secrets/nais.io/dokmetDS/password;
then
    echo "Setting SPRING_DATASOURCE_PASSWORD"
    export SPRING_DATASOURCE_PASSWORD=$(cat /var/run/secrets/nais.io/dokmetDS/password)
fi