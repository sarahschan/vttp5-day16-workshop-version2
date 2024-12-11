package sg.edu.nus.iss.vttp5a_day16wsA.service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import sg.edu.nus.iss.vttp5a_day16wsA.model.BoardGame;
import sg.edu.nus.iss.vttp5a_day16wsA.repository.MapRepo;

@Service
public class BoardGameService {
    
    @Autowired
    MapRepo mapRepo;

    @Autowired
    SerializerHelper serializerHelper;

    // Read game.json file and load to redis
    public void loadGamesToRedis() throws FileNotFoundException{
            
        InputStream is = getClass().getClassLoader().getResourceAsStream("data/game.json");

        if (is == null) {
            throw new FileNotFoundException();
        }

        // Use JsonReader to read the stream
        JsonReader jReader = Json.createReader(is);
        JsonArray gamesJsonArray = jReader.readArray();

        // Loop through the array
        for (JsonValue gameRaw : gamesJsonArray) {
                
            JsonObject jsonObject = gameRaw.asJsonObject();
                
            // Original jsonObject -> Board Game POJO
            BoardGame boardGame = serializerHelper.originalJsonToPOJO(jsonObject);

            // Board Game POJO -> JsonObject String
            String boardGameToSave = serializerHelper.pojoToJson(boardGame);

            // Save stringified board game json object to redis
            mapRepo.create(boardGame.getId().toString(), boardGameToSave);

        }
        
    }


    // Check if a game exists in redis
    public Boolean gameExists(String id){
        return mapRepo.hasHashKey(id);
    }


    // Retrieve a game from redis
    public String retrieveGameFromRedis(String id){

        String foundGameJsonString = mapRepo.get(id).toString();

        return foundGameJsonString;

    }


    // Update a game
    public void updateGame(String id, String boardGameJsonString){

        mapRepo.create(id, boardGameJsonString);

    }


    // Delete a game
    public long deleteGame(String id){
        return mapRepo.delete(id);
    }

    
    // Get all games from redis
    public List<String> allGames() {
        
        List<String> allGames = new ArrayList<>();

        List<Object> allGameObjects = mapRepo.getValues();

        for (Object gameObject : allGameObjects) {
            String gameString = gameObject.toString();
            allGames.add(gameString);
        }

        return allGames;
    }
}
