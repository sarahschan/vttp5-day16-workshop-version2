package sg.edu.nus.iss.vttp5a_day16wsA.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.vttp5a_day16wsA.constant.Constant;

@Repository
public class MapRepo {
    
    @Autowired
    @Qualifier(Constant.TEMPLATE02)
    RedisTemplate<String, String> template;

    public void create(String hashKey, String hashValue) {
        template.opsForHash().put(Constant.BOARDGAME, hashKey, hashValue);
    }


    public Boolean hasHashKey(String hashKey){
        return template.opsForHash().hasKey(Constant.BOARDGAME, hashKey);
    }


    public Object get(String hashKey) {
        // Object can be anything, a string, a number, a class, a collection
        return template.opsForHash().get(Constant.BOARDGAME, hashKey);
    }
    

    public long delete(String hashKey) {
        return template.opsForHash().delete(Constant.BOARDGAME, hashKey);
    }


    public List<Object> getValues() {
        return template.opsForHash().values(Constant.BOARDGAME);
    }
}
