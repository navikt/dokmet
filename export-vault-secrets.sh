#!/usr/bin/env sh

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