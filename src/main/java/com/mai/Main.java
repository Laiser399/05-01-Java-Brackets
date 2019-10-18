package com.mai;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        BracketsChecker checker = new BracketsChecker();
        try {
            checker.start();
        }
        catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
        }
    }
}
