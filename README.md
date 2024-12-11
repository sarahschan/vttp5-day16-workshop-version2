# DAY 16 WORKSHOP VERSION 2 - Working with Redis and JSON Objects

- Did it all over again
    - Still don't really understand the english of task 1, but I structured it this way:
        - Makes use of a Redis MAP
            - Redis Key: boardgame
            - Hash Key: game's ID
            - Hash Value: game's details, stored as a stringified JSON object


<br>


## ENDPOINTS
- POST : http://localhost:8080/api/boardgame/load
    - Loads games from game.json into redis
    - game.json found in resources/data
    - 201 Created: Successful

- GET : http://localhost:8080/api/boardgame/[ID]
    - Retrieves and returns a given game
    - 200 OK: Successful
    - 404 NOT FOUND: Game with the ID doesn't exist
    
- PUT : http://localhost:8080/api/boardgame/&lt;ID&gt;?upsert=true
    - Updates the details of game with given ID
    - Optional upsert boolean
        - Default is false
        - If game doesn't exist and upsert=true, it will create a new game
    - 200 OK: Successful update
    - 201 CREATED: Successful create
    - 404 NOT FOUND: Game could not be found and upsert was false

- DELETE : http://localhost:8080/api/boardgame/&lt;ID&gt;
    - Deletes a given game
    - 200 OK: Successful delete
    - 404 NOT FOUND: Game could not be found hence could not be deleted

- GET : http://localhost:8080/api/boardgame/all
    - Program will load dataset in redis and return JSON data
        - 200 OK: Successful
        - *Not implemented* 204 NO CONTENT: No content in the redis database



