
Introduction
=====

In a nutshell
------

This is a REST driven implementation of the game Kalah motivated by a coding challenge task.
It's somewhat over-elaborate, but illustrates a full application stack.  

Gameplay is well described in both the technical specification and the referenced wikipedia article[1].


Obtaining & Building the application
======

**Prerequisites**; 
Recommended : git client
Required: You will need the maven build tool and a Java 8 runtime available on your computer.

**What's Included**
Source code for;
1. Spring Boot backend application offering up a REST API to play the game Kalah
2. A bare-bones UI for playing the game that can be optionally included in the build.
**Obtain Source**
Obtain the source code from Github (either clone with the git tool or download as a zipfile an unpack it locally).

From the folder where you unpacked/cloned the project;

Build the application with: 

`mvn clean install`


To build the application with a UI (please see notes later...)

`mvn clean install -Pinclude-ui`


Running the Application
=======================

Once built you can run the jar file from the target folder.

`java -jar [path-to-jar-file]`

Run the application with maven;

`mvn clean spring-boot:run`


How To play
-----------

By default, the server will be available at `http://localhost:8080`

Create a game;
`POST http://<host>:<port>/games` 

Make a move;
`PUT http://<host>:<port>/games/{gameId}/pits/{pitId}`

More information can be found in the API section.


Production grade database Support
---------------------------------

By default, Kalah runs with an embedded H2 database (see section on persistence).
This is fine for testing/playing in non-production.

The "prod" profile is a minimal profile - a starting point for a production grade application.  
This provides support only for the MySQL database.

**To run in production mode**:
You must create a database.
1. Create a database in MySQL;
`mysql> create database kalah`
 
2. Setup environment variables (replace with you credentials/environement)
- `KALAH_DB_USER=your-user`
- `KALAH_DB_PASSWORD=your-password`
- `KALAH_DB_HOST=localhost`
- `KALAH_DB_NAME=kalah`


`java -jar [path-to-jar-file] -Dspring.profiles.active=prod`

Schema Management
-----------------
Database schema management is handled by Liquibase.  Any updates to the schema should be made in the file db/kalah-schema.xml


Developer Notes
==============


Conventions
-----------

The conventions used in this implementation are based on the Wikipedia article whereby players are labelled North/South.
Rules are based on the provided specification.

Playable positions on the board are numbered from 1 to 14 where;
 - 7 and 14 are the kalah (stores)
 - 1-6 are the south players pits (or houses)
 - 8-13 are the north players pits (or houses)

The board is modelled as

          NORTH PLAYER ("player 2")
          <-----------------------
          13   12  11  10  9   8
      14                           7
          1    2   3   4   5   6
          ----------------------->

Ambiguities
-----------
**Stone Capture**.  It's clear this case needs to be considered when a player places their last stone in an empty pit that they control.
IF the opposing pit is empty, should the players seed/stone be transfered to their Kalah?

Specification : "When the last stone lands in an own empty pit..."
Wikipedia  "...lands in an empty house owned by the player, and the opposite house contains seeds"

Resolution - the more complete description described in the wikipedia article has been followed. A configuration parameter; `rule.capture_empty_pit.enabled` has been provided; this can be set to true to revert to the specification definition of stone capture.  

**Specifcation; URI/URL**
*The game URL in the create game service response message* is specified as "uri"  in the create game example response message.
It's referred to in the description as URL, this is also consistent with the make move service.
Resolution - using "url" for this field.

API
---

The provided gameplay interface adheres to the original specifications;
Swagger documentation is provided and can be accessed at `http://<host>:<port>/swagger-ui.html`

Create a game;
`POST http://<host>:<port>/games` 

Make a move;
`PUT http://<host>:<port>/games/{gameId}/pits/{pitId}`

The following has been added to the API and is beyond the API;

`GET http://<host>:<port>/games/{gameId}`
Returns the current game state without making any changes

Create a game;
`POST http://<host>:<port>/games?stones=x` 

Creates a game initialised with the specified number 'x' of stones in each pit.

Swagger documentation is available at http://<host>:<port>/swagger-ui.html 

Main Components
---------------
Components of the game have been split into logical areas of remit;

**Game related components**;

*immutable* - the characteristics of the game board:  KalahGameBoard

*mutable* - the state of the current game (current player, stones in each pit) KalahGameState

*mechanics* - business logic (making moves, analyising the board state) - KalahActions

**API components**
*model objects* - definition of the REST objects that convey state (uk.org.landeg.kalah.api.model package)
*controllers* - defines the game API services.

**Persistence components**

The Game Engine
---------------

The application has been implemented around a main game engine (KalahGameEngine) which offers up supporting methods for actions required to play;
- initialise the game board for a new game
- performing a move.

Implementations of the `KalahMoveProcessor` defines the logic for performing moves.
`KalahAction` implementations define detection and additional logic for processing move outcomes (Capture stones/extra moves/turn end/game end).
Kalah actions are executed in a chain following each move and are mutually exclusive with the exception of end game detection which is always executed, regardless of any other condition being processed.

Determining the URL of games
----------------------------

Spring applications aren't aware of their external domain names, this is needed in order to build the game URL in API responses.

Two strategies have been provided for determining what it is.

Firstly, a predefined domain name provider - to use this, set the server.location.url parameter as an environment variable or in your application.configuration.

Secondly it can be derived from incoming requests (this can be unreliable if users are accessing your server from different networks).

This is a simple demonstration of auto configuration.

Gameplay Validation
===================

General validation:
1 game ID is always checked prior to any gameplay (404 on non-existent games)
2 pitID is always checked prior to performing moves (400 on invalid pit)
3 if a game has finished, no further moves are allowed (400 response)
4 if an attempt has been made to start a move from an opponent pit.

Where possible, JSR-303 enforcement has been made at the API level (for valid pit ID).
Gameplay validation is enforced within the game engine. 

Overview of Technology 
======================

Build tools - Maven
Language - Java 8
Framework - Spring Boot
JPA persistence - spring boot JPA/Hibernate
Schema management - Liquibase
JSR-303 validation - hibernate-validation
Swagger API documentation - Swagger/Springfox
UI - React
Code Quality - Jacoco - reports available in target/site
               Sonarqube - (not included in build chain). 

References
==========
[1] Wikipedia description of the game : https://en.wikipedia.org/wiki/Kalah
[2] Spring boot technical documentation : https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html

User Interface
======

The UI application is a completely minimal, being provided purely as a means to drive the game.

It runs in Chrome (Desktop)

It's not reactive and probably wont:
* work on a mobile
* work in Internet Explorer
* win a beauty contest

Manual Testing
===

The UI, although simple is a good tool for running through a game and provides good visual feedback on gameplay.

If you don't have two computers available, gameplay on separate computers can be emulated with two concurrent bash sessions in docker instances.

`docker run -i -t ubuntu /bin/bash`

`apt-get update`
`apt-get install -y curl`


**example gameplay**;

Terminal 1: 
root@e0e186d9291d:/# curl --header "Content-Type: application/json" --request POST http://192.168.0.24:8080/games
{"id":"333","url":"http://192.168.0.24:8080/games/333"}

Terminal 1: 
curl --header "Content-Type: application/json" --request PUT http://192.168.0.24:8080/games/333/pits/1


Terminal 2: 
root@1a5caf983c97:/# curl --header "Content-Type: application/json" --request PUT http://192.168.0.24:8080/games/333/pits/8
{"message":"You can only play pits you control (perhaps it's not your turn?)","type":"CLIENT_ERROR"}
--- as expected - player 1 has an extra move!

Terminal 1:
root@e0e186d9291d:/# curl --header "Content-Type: application/json" --request PUT http://192.168.0.24:8080/games/333/pits/5
{"id":"333","url":"http://192.168.0.24:8080/games/333","status":{"11":"7","12":"7","13":"6","14":"0","1":"0","2":"7","3":"7","4":"7","5":"0","6":"8","7":"2","8"

Terminal 2:
root@1a5caf983c97:/# curl --header "Content-Type: application/json" --request PUT http://192.168.0.24:8080/games/333/pits/8
{"id":"333","url":"http://192.168.0.24:8080/games/333","status":{"11":"8","12":"8","13":"7","14":"1","1":"1","2":"7","3":"7","4":"7","5":"0","6":"8","7":"2","8":"0","9":"8","10":"8"}}


Appendix
========

A : summary of configuration options
------------------------------------
**Maven Profile : include-ui**
Include the UI with the build.  
(build with mvn -Pinclude-ui .... )

**Production mode : -Dspring.active.profiles=prod**
Enables the MySQL database
Must set the environment variables 

 - KALAH_DB_USER 
 - KALAH_DB_PASSWORD 
 - KALAH_DB_HOST 
 - KALAH_DB_NAME

**Gameplay - allow stone capture when opponent pit is empty**
rule.capture_empty_pit.enabled

B : ideas for improvement
=========================

Check WHO is making a move
--------------------------
The current player at any time is tracked by game state and only pits belonging to that player are playable.
We do not however ensure that cheating players don't "take" their opponents move.

- The source IP address of incoming requests could be recorded on the first move - any moves made from inconsistent origins could be disallowed.
- Issue a token to each player on joining a game and check it prior to each move (send as cookie/header)
- Hold credential information on each player (perhaps sign-in process)

Gatecrashers
------------
There's nothing to stop someone gatecrashing an existing game - above solutions can help. 

Knowing when it's your move
---------------------------
Websockets could be used to push an "it's your turn" to a player.
Game board could be polled for changes.

Test Imrovement
---------------
Some full game scenario tests would have been nice(time permitting).

UI Build process
----------------
UI probably shouldn't be embedded in the java project.
 

