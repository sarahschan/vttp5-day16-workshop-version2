package sg.edu.nus.iss.vttp5a_day16wsA.controller;

import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.vttp5a_day16wsA.exceptions.BoardGameNotFoundException;
import sg.edu.nus.iss.vttp5a_day16wsA.model.BoardGame;
import sg.edu.nus.iss.vttp5a_day16wsA.service.BoardGameService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/boardgame")
public class BoardGameController {
    
    @Autowired
    BoardGameService boardGameService;
    
    // load all board games from game.json into redis
    @PostMapping("/load")
    public ResponseEntity<String> loadAndSaveGameString() {

        try {
            boardGameService.loadAndSaveGames();
            return ResponseEntity.status(200).body("Board games successfully loaded onto Redis");

            // catch FileNotFoundException
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());

            // catch any other errors
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occured: " + e.getMessage());
        }

    }

    // return all board games from redis
    @GetMapping("/all")
    public ResponseEntity<Map<String, BoardGame>> getAllGames() {
            
        Map<String, String> allGamesString = boardGameService.getAllGames();

        if (allGamesString.isEmpty()) {
            return ResponseEntity.status(204).build();  // 204: No content
        }

        Map<String, BoardGame> allBoardGames = new HashMap<>();
            
        // convert string -> JsonObject -> BoardGame POJO
        for (Map.Entry<String, String> entry : allGamesString.entrySet()) {
                
            String gameData = entry.getValue();
                
            JsonReader jReader = Json.createReader(new StringReader(gameData));
            JsonObject gameJsonObject = jReader.readObject();

            BoardGame boardGamePOJO = boardGameService.convertJsonBoardGameToPOJO(gameJsonObject);
            
            allBoardGames.put(entry.getKey(), boardGamePOJO);
        }

        return ResponseEntity.status(200).body(allBoardGames);
        
    }
    

    // Task 1 - Write a REST endpoing that will insert 1 document (in JSON) into the data store
    // POST /api/boardgame
    @PostMapping("")
    public ResponseEntity<String> newBoardGame(@RequestBody String boardGameRawData) {
            
            // try and execute, return status code and payload
        try {
            String payload = boardGameService.saveNewGame(boardGameRawData);
            return ResponseEntity.status(201).body(payload);

            // catch any error
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to create board game in redis: " + e.getMessage());
        }

    }


    // Task 2 - Write a REST endpoint that will retrieve a given board game
    // GET /api/boardgame/<boardgame id>
    @GetMapping("/{gameKey}")
    public ResponseEntity<Object> retrieveBoardGame(@PathVariable String gameKey) {

        try {
            BoardGame foundGame = boardGameService.findBoardGame(gameKey);
            return ResponseEntity.status(200).body(foundGame);

        } catch (BoardGameNotFoundException e) {
            return ResponseEntity.status(404).body("Error: " + e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occured: " + e.getMessage());
        }

    }


    // Task 3 - Write a REST endpoint that will update a document
    // PUT /api/boardgame/<boardgame id>?upsert=true
    @PutMapping("/{gameKey}")
    public ResponseEntity<String> updateBoardGame(@PathVariable String gameKey, @RequestBody String boardGameRawData, @RequestParam(required = false, defaultValue = "false") Boolean upsert){
        
        try{
            // Call service method to update or create the board game
            // method created to already return ResponseEntity
            // Status code will now change depending on if board game was created or updated
            return boardGameService.updateBoardGame(gameKey, boardGameRawData, upsert);

        } catch (BoardGameNotFoundException e) {
            return ResponseEntity.status(404).body("Error: " + e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occured: " + e.getMessage());
        }
    }
}
