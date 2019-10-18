package com.mai;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class BracketsChecker {

    public void start() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            Brackets brackets = fetchBrackets(reader);
            StringBuilder testString = fetchContentForTest(reader);

            int error = checkBrackets(testString.toString(), brackets);
            displayResult(error, testString, brackets);
        }
    }

    private Brackets fetchBrackets(BufferedReader reader) throws IOException {
        Brackets brackets = null;
        while (brackets == null) {
            System.out.print("Enter filename with brackets (*.json): ");
            String filename = reader.readLine();

            StringBuilder bracketsJsonString;
            try {
                bracketsJsonString = readFile(filename);
            }
            catch (IOException e) {
                System.out.println("Error while reading file \"" + filename + "\".");
                continue;
            }

            try {
                brackets = parseBracketsFromJson(bracketsJsonString.toString());
            }
            catch (ParseException e) {
                System.out.println("Error while parse brackets from \"" + filename + "\".");
            }
            catch (Exception e) {
                System.out.println("Error while parse brackets from \"" + filename + "\": " + e.getMessage());
            }
        }

        return brackets;
    }

    private StringBuilder fetchContentForTest(BufferedReader reader) throws IOException {
        StringBuilder testString = null;
        while (testString == null) {
            System.out.print("Enter filename for check brackets: ");
            String filename = reader.readLine();

            try {
                testString = readFile(filename);
            }
            catch (IOException e) {
                System.out.println("Error while reading file \"" + filename + "\".");
            }
        }

        return testString;
    }

    private void displayResult(int errorIndex, CharSequence testString, Brackets brackets) {
        System.out.println("Brackets: " + brackets);
        System.out.println("Your string: " + testString);
        if (errorIndex == -1) {
            System.out.println("Error not found.");
        }
        else {
            char bracket = testString.charAt(errorIndex);
            if (brackets.isLeftBracket(bracket)) {
                System.out.println("Not found right bracket for \"" + bracket +
                        "\" at pos " + errorIndex + ".");
            }
            else if (brackets.isRightBracket(bracket)) {
                System.out.println("Not found left bracket for \"" + bracket +
                        "\" at pos " + errorIndex + ".");
            }
        }
    }

    private Brackets parseBracketsFromJson(String jsonString) throws ParseException, Exception {
        Brackets brackets = new Brackets();
        JSONArray root = (JSONArray) new JSONParser().parse(jsonString);
        for (Object rootElem : root) {
            JSONObject pair = (JSONObject) rootElem;

            String left = (String) pair.get("left");
            String right = (String) pair.get("right");
            brackets.addPair(left.charAt(0), right.charAt(0));
        }
        return brackets;
    }

    private StringBuilder readFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        StringBuilder result = new StringBuilder();
        while (reader.ready()) {
            if (result.length() != 0)
                result.append('\n');
            result.append(reader.readLine());
        }
        reader.close();
        return result;
    }

    private int checkBrackets(String testingString, Brackets brackets) {
        Deque<Character> bracketsStack = new ArrayDeque<>();
        int lastLeftBracketIndex = -1;

        for (int i = 0; i < testingString.length(); ++i) {
            char c = testingString.charAt(i);
            if (brackets.isLeftBracket(c)) {
                bracketsStack.addLast(c);
                lastLeftBracketIndex = i;
            }
            else if (brackets.isRightBracket(c)) {
                if (bracketsStack.isEmpty())
                    return i;
                if (!brackets.getRightByLeft(bracketsStack.removeLast()).equals(c))
                    return i;
            }
        }

        return bracketsStack.isEmpty() ? -1 : lastLeftBracketIndex;
    }


}
