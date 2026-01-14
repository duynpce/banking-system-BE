package com.example.banking_system.utility;

import com.example.banking_system.exception.ConflictDataException;
import org.springframework.stereotype.Component;

@Component
public class Util {

    public void assertNotConflictData(boolean condition, String message) {
        if (condition) {
            throw new ConflictDataException(message);
        }
    }
}
