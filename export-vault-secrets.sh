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

if test -f /var/run/secrets/nais.io/ldap/username;
then
    echo "Setting SPRING_LDAP_USERNAME"
    export SPRING_LDAP_USERNAME=$(cat /var/run/secrets/nais.io/ldap/username)
fi
if test -f /var/run/secrets/nais.io/ldap/password;
then
    echo "Setting SPRING_LDAP_PASSWORD"
    export SPRING_LDAP_PASSWORD=$(cat /var/run/secrets/nais.io/ldap/password)
fi