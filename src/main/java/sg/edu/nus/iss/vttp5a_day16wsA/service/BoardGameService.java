package sg.edu.nus.iss.vttp5a_day16wsA.service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import sg.edu.nus.iss.vttp5a_day16wsA.model.BoardGame;
import sg.edu.nus.iss.vttp5a_day16wsA.repository.BoardGameRepo;

@Service
public class BoardGameService {
    
    @Autowired
    BoardGameRepo boardGameRepo;

    // load and save games from game.json into redis
    public void loadAndSaveGames() throws FileNotFoundException {

        // // Read the game.json file in resources
        // File file = ResourceUtils.getFile("classpath:data/game.json");
        // JsonReader jReader = Json.createReader(new FileReader(file));

        // Read the game.json file using stream
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/game.json");
        if (inputStream == null) {
            throw new FileNotFoundException("game.json not found in classpath");
        }

        // Use JsonReader to read stream
        JsonReader jReader = Json.createReader(inputStream);

        // Read the Json Array of games
        JsonArray gamesArray = jReader.readArray();

        for (JsonValue gameRaw : gamesArray) {
            // cast JsonValue into a JsonObject
            JsonObject game = (JsonObject) gameRaw;

            // Extract the needed information
            Integer id = game.getInt("gid");
            String name = game.getString("name");
            Integer year = game.getInt("year");
            Integer ranking = game.getInt("ranking");
            Integer rating = game.getInt("users_rated");
            String url = game.getString("url");

            // Create BoardGame POJO
            BoardGame boardGame = new BoardGame(id, name, year, ranking, rating, url);

            // BoardGame POJO -> JsonObject -> JsonString
            String gameDataJsonString = convertBoardGameToJsonString(boardGame);

            // create gameKey for redis
            String gameKey = "boardgame:" + id;

            // save to redis
            boardGameRepo.saveGame(gameKey, gameDataJsonString);
        }

    }


    // retrieve all keys and corresponding games in redis
    public Map<String, String> getAllGames(){
        
        // get all gameKeys
        Set<String> gameKeys = boardGameRepo.getAllGameKeys();

        // create a map to store gameKeys and converted JsonObjects
        Map<String, String> allGames = new HashMap<>();

        // loop through gameKeys and populate the map
        for (String gameKey : gameKeys) {
            String gameData = boardGameRepo.getGameByKey(gameKey);
            allGames.put(gameKey, gameData);
        }

        return allGames;
    }



    // helper method to convert BoardGame POJO to a Json formatted string
    private String convertBoardGameToJsonString(BoardGame boardGame) {
        JsonObjectBuilder builder = Json.createObjectBuilder()
                                    .add("id", boardGame.getId())
                                    .add("name", boardGame.getName())
                                    .add("year", boardGame.getYear())
                                    .add("ranking", boardGame.getRanking())
                                    .add("rating", boardGame.getRating())
                                    .add("url", boardGame.getUrl());
        JsonObject boardGameObject = builder.build();
        return boardGameObject.toString();
    }


    // helper method to convert JsonObject to BoardGamePOJO
    public BoardGame convertJsonBoardGameToPOJO(JsonObject boardGameJsonObject) {

        Integer id = boardGameJsonObject.getInt("id");
        String name = boardGameJsonObject.getString("name");
        Integer year = boardGameJsonObject.getInt("year");
        Integer ranking = boardGameJsonObject.getInt("ranking");
        Integer rating = boardGameJsonObject.getInt("rating");
        String url = boardGameJsonObject.getString("url");

        BoardGame boardGamePOJO = new BoardGame(id, name, year, ranking, rating, url);

        return boardGamePOJO;
    }

}