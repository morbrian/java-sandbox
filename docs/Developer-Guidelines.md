# Project Developer Guidelines

## Table of Contents

* [Overview](#overview)
* [Code Style](#code-style)
    * [Java](#java)
    * [Maven](#maven)
* [Unit Tests](#unit-tests)
* [Integration Tests](#integration-tests)

## Configuration Overview

This document provides the basic guidelines for developers.

## Code Style

### Java

Project developers use the [Google Java Style](http://google.github.io/styleguide/javaguide.html) found at GitHub.

* [Eclipse Code Style Configuration](https://github.com/google/styleguide/blob/gh-pages/eclipse-java-google-style.xml)

        Import into eclipse.

* [IntelliJ Code Style Configuration](https://github.com/google/styleguide/blob/gh-pages/eclipse-java-google-style.xml)

        Import this eclipse XML into IntelliJ. After import, the line indent will still be set to 4, edit and change to 2.
        
### Maven

1) Dependencies should be ordered such that:

        - sibling modules are listed first.
        - 3rd party modules second.
        - test scoped modules last.
        - otherwise, list alphabetically with each of the above categories.
       
2) Dependencies which are not scoped as `test` should never list a version in child poms,
instead add the correct version to the parent pom's `dependencyManagement`. 
        
## Unit Tests

All public methods must have an associated JUnit test. Developers may optionally provide unit
tests for protected and private methods.

## Integration Tests

TODO: identify integration test strategy.
        




