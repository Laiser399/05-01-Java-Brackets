package com.mai;


import com.mai.exceptions.BracketsInsertException;

import java.util.HashMap;
import java.util.Map;

public class Brackets {
    private Map<Character, Character> leftToRight = new HashMap<>();

    public void addPair(char left, char right) throws BracketsInsertException {
        if (left == right)
            throw new BracketsInsertException("Left and right brackets are equals.");
        if (leftToRight.containsKey(left) || leftToRight.containsValue(right))
            throw new BracketsInsertException("Try add existing bracket.");

        leftToRight.put(left, right);
    }

    public boolean isLeftBracket(char bracket) {
        return leftToRight.containsKey(bracket);
    }

    public boolean isRightBracket(char bracket) {
        return leftToRight.containsValue(bracket);
    }

    public Character getRightByLeft(char leftBracket) {
        return leftToRight.get(leftBracket);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Character left : leftToRight.keySet()) {
            if (result.length() != 0)
                result.append(" ");
            result.append(left);
            result.append(leftToRight.get(left));
        }
        return result.toString();
    }
}
