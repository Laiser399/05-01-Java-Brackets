package com.mai;

import com.mai.exceptions.BracketsInsertException;
import com.mai.exceptions.BracketsParseException;
import com.mai.exceptions.ReadFileException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class BracketsChecker {

    public void start() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                Brackets brackets = fetchBrackets(reader);
                String testString = fetchContentForTest(reader);

                int error = checkBrackets(testString, brackets);
                displayResult(error, testString, brackets);
            }
        }
        catch (IOException e) {
            System.out.println("Console IO error.");
        }
    }

    private Brackets fetchBrackets(BufferedReader reader) throws IOException {
        Brackets brackets = null;
        while (brackets == null) {
            System.out.print("Enter filename with brackets (*.json): ");
            String filename = reader.readLine();

            String bracketsJsonString;
            try {
                bracketsJsonString = readFile(filename);
                brackets = parseBracketsFromJson(bracketsJsonString);
            }
            catch (ReadFileException|BracketsParseException e) {
                System.out.println(e.getMessage());
            }
        }

        return brackets;
    }

    private String fetchContentForTest(BufferedReader reader) throws IOException {
        String testString = null;
        while (testString == null) {
            System.out.print("Enter filename for check brackets: ");
            String filename = reader.readLine();
            try {
                testString = readFile(filename);
            }
            catch (ReadFileException e) {
                System.out.println(e.getMessage());
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

    private Brackets parseBracketsFromJson(String jsonString) throws BracketsParseException {
        try {
            Brackets brackets = new Brackets();
            JSONArray root = (JSONArray) new JSONParser().parse(jsonString);
            for (Object elem : root) {
                JSONObject pair = (JSONObject) elem;

                String left = (String) pair.get("left");
                String right = (String) pair.get("right");
                brackets.addPair(left.charAt(0), right.charAt(0));
            }
            return brackets;
        }
        catch (ParseException|ClassCastException e) {
            throw new BracketsParseException("Error while parse brackets (wrong json format).");
        }
        catch (BracketsInsertException e) {
            throw new BracketsParseException("Error while parse brackets (found equals brackets).");
        }
    }

    private String readFile(String filename) throws ReadFileException {
        try {
            Optional<String> optional = Files.readAllLines(Paths.get(filename)).stream()
                    .reduce((s1, s2) -> s1 + s2);
            return optional.orElse("");
        }
        catch (IOException e) {
            throw new ReadFileException("Error while read file \"" + filename + "\".");
        }
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
