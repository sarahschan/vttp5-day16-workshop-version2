package sg.edu.nus.iss.vttp5a_day16wsA.restcontroller;

import java.io.StringReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.vttp5a_day16wsA.constant.Constant;
import sg.edu.nus.iss.vttp5a_day16wsA.service.BoardGameService;

@RestController
@RequestMapping()
public class BoardGameRestController {
    
    @Autowired
    BoardGameService boardGameService;


    // Write a REST endpoint that will insert 1 document (in Json) into the data store
    // POST /api/boardgame
    //      only produces, because this particular endpoint will read from file and load
    //      if successful, then will have the ResponseEntity with the json formatted body
    @PostMapping(path="/api/boardgame", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> loadGamesToRedis(){
        
        try {

            boardGameService.loadGamesToRedis();
            System.out.println("Saved all board games from game.json -> redis");

            // Build the response entity
            JsonObject successMessage = Json.createObjectBuilder()
                                            .add("insert_count", 1)
                                            .add("id", Constant.BOARDGAME)
                                            .build();
                                            
            ResponseEntity<String> responseEntity = ResponseEntity
                                                .status(201)
                                                .body(successMessage.toString());

            return responseEntity;

        } catch (Exception e) {
            
            System.out.println("Failed saving from game.json -> redis");
            e.printStackTrace();

            return ResponseEntity.status(500).body("An error occurred");
        }
    }



    // Write a REST endpoint that will retrieve a given board game
    // GET /api/boardgame/<boardgame id>
    @GetMapping(path="/api/boardgame/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> retrieveGameFromRedis(@PathVariable("id") String id){

        if (boardGameService.gameExists(id)){

            String foundGame = boardGameService.retrieveGameFromRedis(id);

            return ResponseEntity.status(200).body(foundGame);

        } else {
            // Build a fancy error message
            JsonObject errorMessage = Json.createObjectBuilder()
                                        .add("error", "Could not find board game with ID " + id)
                                        .build();

            return ResponseEntity.status(404).body(errorMessage.toString());
        }

    }


    // Write a REST endpoint that will update a document
    // PUT /api/boardgame/<boardgame id>?upsert=true
    @PutMapping(path="/api/boardgame/{id}", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateBoardGame(@PathVariable("id") String id, @RequestBody String boardGameJsonString, @RequestParam(defaultValue = "false", required=false) Boolean upsert){

        Boolean gameExists = boardGameService.gameExists(id);

        // If game exists
        if (gameExists) {

            boardGameService.updateGame(id, boardGameJsonString);

            // build the success message
            JsonObject successMessage = Json.createObjectBuilder()
                                            .add("update_count", 1)
                                            .add("id", id)
                                            .build();

            return ResponseEntity.status(200).body(successMessage.toString());


        // If game doesn't exists and upsert = true
        } else if (!gameExists && upsert) {

            boardGameService.updateGame(id, boardGameJsonString);

            // build the success message
            JsonObject successMessage = Json.createObjectBuilder()
                                            .add("upsert_count", 1)
                                            .add("id", id)
                                            .build();

            return ResponseEntity.status(201).body(successMessage.toString());


        // If game doesn't exist and upsert = false
        } else {

            // Build the fancy error message
            JsonObject errorMessage = Json.createObjectBuilder()
                                        .add("error", "Board game with ID " + id + " could not be found and upsert boolean was set to false")
                                        .build();

            return ResponseEntity.badRequest().body(errorMessage.toString());
        }
    }


    // Additional - Write a REST endpoint that will delete a document
    // DELETE /api/boardgame/<boardgame id>
    @DeleteMapping(path="/api/boardgame/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteBoardGame(@PathVariable("id") String id){

        // Try to delete
        try {

            long deleteCount = boardGameService.deleteGame(id);

            // method returns 1 if object was deleted
            if (deleteCount > 0) {
                
                JsonObject successMessage = Json.createObjectBuilder()
                                                .add("delete_count", 1)
                                                .add("id", id)
                                                .build();

                return ResponseEntity.ok().body(successMessage.toString());

            } else {

                JsonObject errorMessage = Json.createObjectBuilder()
                                            .add("error", "Board game with ID " + id + " cannot be deleted because it doesn't exist")
                                            .build();

                return ResponseEntity.status(404).body(errorMessage.toString());
            }

        } catch (Exception e) {

            JsonObject errorMessage = Json.createObjectBuilder()
            .add("error", "An error occured when trying to delete board game " + id)
            .build();

            return ResponseEntity.status(500).body(errorMessage.toString());

        }
    }


    // Additional - Write a REST endpoint that will return all board games saved in redis
    // GET /api/boardgame/all
    @GetMapping(path="/api/boardgame/all", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> allBoardGames(){

        try {
            
            List<String> allGamesStringArray = boardGameService.allGames();

            return ResponseEntity.ok().body(allGamesStringArray.toString());

        } catch (Exception e) {

            JsonObject errorMessage = Json.createObjectBuilder()
                                            .add("error", "An error occurred when retrieving the games")
                                            .build();

            return ResponseEntity.status(500).body(errorMessage.toString());
        }
    }


    // Additional - Returns all games as a Json Object, with a "games" key mapping to an array of board game objects
    @GetMapping(path="/api/boardgame/all/jsonobject", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> allBoardGamesJsonObject(){

        try {
            
            List<String> allGamesStringArray = boardGameService.allGames();

            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
                for (String game : allGamesStringArray) {
                    JsonObject jsonGame = Json.createReader(new StringReader(game)).readObject();
                    jsonArrayBuilder.add(jsonGame);
                }
            JsonArray jsonGameArray = jsonArrayBuilder.build();

            JsonObject jsonGameObject = Json.createObjectBuilder()
                                            .add("games", jsonGameArray)
                                            .build();

            return ResponseEntity.ok().body(jsonGameObject.toString());

        } catch (Exception e) {

            JsonObject errorMessage = Json.createObjectBuilder()
                                            .add("error", "An error occurred when retrieving the games")
                                            .build();

            return ResponseEntity.status(500).body(errorMessage.toString());
        }
    }

}