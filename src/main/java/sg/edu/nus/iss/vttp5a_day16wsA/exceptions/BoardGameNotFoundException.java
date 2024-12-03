package sg.edu.nus.iss.vttp5a_day16wsA.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)   // Automatically returns a 404 status
public class BoardGameNotFoundException extends RuntimeException{
    
    // Constructor that accepts the gameKey to be displayed in the message
    public BoardGameNotFoundException(String gameKey) {
        super ("Board game with redis key " + gameKey + " not found");
    }
}
