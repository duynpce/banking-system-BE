package com.example.banking_system.utility;

import com.example.banking_system.exception.ConflictDataException;
import org.springframework.stereotype.Component;

@Component
public class Util {

    public void assertUnique(boolean isConflict, String message) {
        if (isConflict) {
            throw new ConflictDataException(message);
        }
    }
}
