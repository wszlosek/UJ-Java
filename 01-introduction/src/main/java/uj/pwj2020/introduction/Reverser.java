package uj.pwj2020.introduction;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Reverser {

    public String reverse(String input) {

        if (input == null) {
            return input;
        }

        StringBuilder s = new StringBuilder(input.trim());
        return s.reverse().toString();

    }

    public String reverseWords(String input) {

        if (input == null) {
            return input;
        }

        List<String> words = Arrays.asList(input.split(" "));
        Collections.reverse(words);
        return String.join(" ", words);
    }

}
