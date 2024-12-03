# DAY 16 WORKSHOP VERSION 2 - Working with Redis and JSON Objects

- Changes from previous version
    - Now making use of BoardGame POJO model to ensure consistency in data handling across the functions
- game.json dataset is located in src/main/resources/data
- Note that redis keys are boardgame:&lt;id&gt;
    - e.g. A board game with ID 100, will have the key boardgame:100


<br>


## Task 0: Load games from game.json into Redis
- On Postman:
    - POST request: http://localhost:8080/api/boardgame/load
- Program will load dataset in game.json to connected redis database
    - 200 OK: Successful
    - 404 NOT FOUND: game.json file not found
    - 500 INTERNAL SERVER ERROR: Other errors

## Task 0: Return all board games from Redis
- On Postman or browser:
    - GET request: http://localhost:8080/api/boardgame/all
- Program will load dataset in redis and return JSON data
    - 200 OK: Successful
    - 204 NO CONTENT: No content in the redis database
    - 500 INTERNAL SERVER ERROR: Other errors


<br>


## Task 1: 