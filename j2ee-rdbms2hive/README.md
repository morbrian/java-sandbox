# Demonstrates one way to migrate data tables from RDBMS to Hive

## Table of Contents

* [Configuration Overview](#configuration-overview)
* [Install rdbms2hive Web App](#install-rdbms2hive-web-app)


## Configuration Overview

Assumes database is configured in target app server.

       
## Install jdbc-query Web App

The `rdbms2hive` build produces a WAR artifact called `rdbms2hive.war`. This 
must be copied to the tomcat webapps deployment directory.

1. Copy `rdbms2hive.war` to `$CATALINA_HOME/webapps`

        cp ./target/rdbms2hive.war $CATALINA_HOME/webapps

## Requesting Fetch

1. A very simple GET REST request will fetch csv data from the server.

        http://localhost:8080/rdbms2hive/api/rest/csv/produce/schema/tablename/1234567890

2. A very simple GET REST request will ask the service to fetch data from PostgreSQL.

        http://localhost:8080/rdbms2hive/api/rest/csv/process/schema/tablename
        The JSON response will provide and identifier which can be used to fetch the data later.
        TODO: this should be a POST request, etc... but we're shooting for easy browser based examples.




