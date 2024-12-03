package sg.edu.nus.iss.vttp5a_day16wsA.repository;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BoardGameRepo {
    
    @Autowired
    @Qualifier("redisTemplate")
    RedisTemplate<String, String> template;

    // retrieve a game
    public String getGameByKey(String gameKey){
        return template.opsForValue().get(gameKey);
    }

    // fetch all gameKeys from redis
    public Set<String> getAllGameKeys(){
        return template.keys("boardgame:*");
    }

    // save a single game
    public void saveGame(String gameKey, String gameData) {
        template.opsForValue().set(gameKey, gameData);
    }

}
