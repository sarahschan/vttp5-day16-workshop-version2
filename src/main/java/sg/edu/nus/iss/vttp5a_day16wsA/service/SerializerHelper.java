package sg.edu.nus.iss.vttp5a_day16wsA.service;

import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.vttp5a_day16wsA.model.BoardGame;

@Service
public class SerializerHelper {
    
    // Convert JsonObject -> Board Game POJO
    //  For reading game.json to redis
    public BoardGame originalJsonToPOJO(JsonObject jsonObject) {
        
        // Extract the needed information
        Integer id = jsonObject.getInt("gid");
        String name = jsonObject.getString("name");
        Integer year = jsonObject.getInt("year");
        Integer rating = jsonObject.getInt("users_rated");
        String url = jsonObject.getString("url");

        // Build the Board Game POJO
        BoardGame boardGame = new BoardGame(id, name, year, rating, url);

        return boardGame;
    }


    // Convert POJO -> JSON Object Stringify
    public String pojoToJson(BoardGame boardGame){

        // Build the Json Object with fields from boardGame
        JsonObject jsonObject = Json.createObjectBuilder()
                                .add("id", boardGame.getId())
                                .add("name", boardGame.getName())
                                .add("year", boardGame.getYear())
                                .add("rating", boardGame.getRating())
                                .add("url", boardGame.getUrl())
                                .build();

        return jsonObject.toString();
    }


    // Convert Json Object Stringify -> JsonObject
}
