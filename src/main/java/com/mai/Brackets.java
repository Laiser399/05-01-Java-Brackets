package com.mai;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Brackets {
    private Set<Character> leftBrackets = new HashSet<>(),
                           rightBrackets = new HashSet<>();
    private Map<Character, Character> leftToRight = new HashMap<>();

    public void addPair(char left, char right) throws Exception {
        if (left == right)
            throw new Exception("Left and right brackets are equals.");
        if (leftBrackets.contains(left) || rightBrackets.contains(right))
            throw new Exception("Try add existing bracket.");

        leftBrackets.add(left);
        rightBrackets.add(right);
        leftToRight.put(left, right);
    }

    public boolean isLeftBracket(char bracket) {
        return leftBrackets.contains(bracket);
    }

    public boolean isRightBracket(char bracket) {
        return rightBrackets.contains(bracket);
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
