package com.sda.validator;

import org.springframework.stereotype.Service;

@Service
public class GenericValidator {

    public boolean isNotPositiveInteger(String someString) {
        try {
            int someNumber = Integer.parseInt(someString);
            return someNumber <= 0;
        } catch (NumberFormatException exception) {
            return true;
        }
    }
}
