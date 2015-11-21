# Simplistic examples of various patterns with Java based frameworks.

## Table of Contents

* [Configuration Overview](#configuration-overview)
* [Database Initialization](#database-initialization)
    * [Create Database and Role in Postgres](#create-database-and-role-in-postgres)
* [Configure Web App Container](#configure-web-app-container)
    * [Prepare TomEE Plus](#prepare-tomee-plus)
    * [Download and Extract TomEE Plus](#download-and-extract-tomeee-plus)
    * [Configure DataSource](#configure-datasource)

## Configuration Overview

Many of the apps in this repository require some basic configuration of an
application server, and sometimes a database or other system service.
This base readme describes some of the common configuration requirements.

        TODO: separate various configuration concerns, such as 
        J2EE examples have different needs than the HDP examples.

## Database Initialization

The database role and data store must be created outside the context of the
webapp using features specific to the database being used.

### Create Database and Role in Postgres

How to create database and role with PostgreSQL.

1. Create the database

        createdb -w -Upostgres -hlocalhost jpabasics;

2. Create the role

        psql -w -Upostgres -hlocalhost -djpabasics -c "\
                create role jpabasics;\
                alter role jpabasics with nosuperuser inherit nocreaterole nocreatedb login noreplication;\
                alter role jpabasics with unencrypted password 'jpabasics';\
        "

## Configure Web App Container

The application container must be configured to expose the database connection
as a datasource for Web Applications that need it. This section describes
how to perform this configuration on a few popular application containers.
        
### Preparing TomEE Plus

We assum TomEE Plus 1.7.1 for these instructions.

### Download and Extract TomEE Plus

1. Download TomEE Plus 1.7.1

        http://archive.apache.org/dist/tomee/tomee-1.7.1/apache-tomee-1.7.1-plus.tar.gz
        
2. Extract inside your preferred installation directory.

        tar xvzf ~/Downloads/apache-tomee-1.7.1-plus.tar.gz

3. We will refer to the full path to the new apache-tomee-1.7.1 installation as
`$CATALINA_HOME` in the rest of this document.

### Add PostgreSQL Driver

1. Download the PostgreSQL JDBC 4.1 driver for the version of PostgreSQL you 
have installed. The example below assumes PostgreSQL 9.3.

        https://jdbc.postgresql.org/download/postgresql-9.3-1103.jdbc41.jar
        
2. Copy the driver to the tomcat lib directory

        cp ~/Downloads/postgresql-9.3-1103.jdbc41.jar $CATALINA_HOME/lib

### Configure DataSource

We tell tomcat what database to use. The applications will lookup this information by the DataSource name.

1. Edit `$CATALINA_HOME/conf/tomee.xml` with the following contents:

        <?xml version="1.0" encoding="UTF-8"?>
        <tomee>
                <Resource  id="DocumentDS" type="DataSource">
                  JdbcDriver org.postgresql.Driver
                  JdbcUrl jdbc:postgresql://127.0.0.1:5432/jpabasics
                  UserName jpabasics
                  Password jpabasics
                  JtaManaged true
                  TestWhileIdle true
                  InitialSize 5
                </Resource>
        </tomee>
        
### Configuring Logging in TomEE 1.7.1

TomEE will use the native Java Logging implementation by default. We prefer
using the `slf4j` APIs with an underlying `logback` implementation. Due to the 
variety of logging APIs, we'll also add a few other libraries to support logging
by some of the dependent libraries.

1. Download the following libraries and add them to the tomcat classpath by 
editing `/cloud/tomcat/bin/setenv.sh`

        CLASSPATH=$CATALINA_HOME/bin/jul-to-slf4j-1.7.12.jar:\
        $CATALINA_HOME/bin/slf4j-api-1.7.12.jar:\
        $CATALINA_HOME/bin/logback-classic-1.1.3.jar:\
        $CATALINA_HOME/bin/logback-core-1.1.3.jar:\
        $CATALINA_HOME/bin/jcl-over-slf4j-1.7.12.jar:\
        $CATALINA_HOME/bin/slf4j-jcl-1.7.12.jar:\
        $CATALINA_HOME/bin/log4j-over-slf4j-1.7.12.jar

4. Restart tomcat
