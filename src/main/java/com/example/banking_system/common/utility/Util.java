package com.example.banking_system.common.utility;

import com.example.banking_system.common.exception.ConflictDataException;
import org.springframework.stereotype.Component;

@Component
public class Util {

    public void assertUnique(boolean isConflict, String message) {
        if (isConflict) {
            throw new ConflictDataException(message);
        }
    }
}
