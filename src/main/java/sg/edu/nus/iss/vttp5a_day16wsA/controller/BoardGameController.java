package sg.edu.nus.iss.vttp5a_day16wsA.controller;

import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.vttp5a_day16wsA.model.BoardGame;
import sg.edu.nus.iss.vttp5a_day16wsA.service.BoardGameService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


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
    
}
