# Tic Tac Toe

## Tests
Execute `mvnw verify` to run both unit and integration tests.

## Build
Execute `mvnw package` to build the app. It will generate `./target/tictactoe-VERSION.jar`.

## Run
Execute `java -jar ./target/tictactoe-VERSION.jar` to run the app. It will be available on `localhost:8080`.  
H2 in-memory database is used by default. To run it in persistent mode additional properties should be specified.
For example  
`java -jar ./target/tictactoe-VERSION.jar --spring.datasource.url=jdbc:h2:./tictactoe --spring.jpa.hibernate.ddl-auto=update`  
will run the app with the DB persisted in file in the current directory.

## Interaction
Execute `curl --data "dimension=3" localhost:8080/start` to start a 3x3 game.  
Execute `curl --data "id=123&x=0&y=0" localhost:8080/move` to put an X on a top-left cell in a game with id=123.
