# myfoobot
Desktop client for Foobot

[![Build Status](https://travis-ci.org/romainmoreau/myfoobot.svg?branch=master)](https://travis-ci.org/romainmoreau/myfoobot)

## Features

It is a very simple client that adds a tray icon which is constantly updated with the latest percentage of global air quality from your Foobot.
It also features a window with the detail of all sensor values and the last update time.

## Requirements

* Java 8 or later
* Windows

It should also work on others plaforms supported by Java.

## Usage

Build or download the latest [release](https://github.com/romainmoreau/myfoobot/releases).

Create an `application.properties` file next to the binary:

    api.username=YourUsernameHere
    api.password=YourPasswordHere

Launch the binary.

## Building

Simply use `mvn install`
