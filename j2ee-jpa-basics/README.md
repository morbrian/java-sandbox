# Simple template for using some basic Java Persistence API capabilities.

## Table of Contents

* [Configuration Overview](#configuration-overview)
* [Install jpa-basics Web App](#install-jpa-basics-web-app)
* [Viewing Data](#viewing-data)


## Configuration Overview

Assumes a database is already configured for the target app server..
       
## Install jpa-basics Web App

The `jpa-basics` buildh produces a WAR artifact called `jpa-basics.war`. This 
must be copied to the tomcat webapps deployment directory.

1. Copy `jpa-basics.war` to `$CATALINA_HOME/webapps`

        cp jpa-basics.war $CATALINA_HOME/webapps
 
## Viewing Data

Data can be viewed by looking in the database, or as JSON by accessing the REST Services in a browser.

    http://localhost:8080/jpa-basics/api/rest/document/raw

    http://localhost:8080/jpa-basics/api/rest/document/derived