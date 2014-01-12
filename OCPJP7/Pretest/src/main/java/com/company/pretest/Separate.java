package com.company.pretest;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class Separate {

    public static void main(String[] args) {
        String text = "<head>first program </head> <body>hello world</body>";
        Set<String> words = new TreeSet<>();
        try (Scanner tokenizingScanner = new Scanner(text)) {
            //TreeSet<String> orders the strings in default alphabetical ascending order 
            // and removes duplicates. The
            // delimiter \W is non-word, so the characters such as < act as separators.
            tokenizingScanner.useDelimiter("\\W");
            while (tokenizingScanner.hasNext()) {
                String word = tokenizingScanner.next();
                if (!word.trim().equals("")) {
                    words.add(word);
                }
            }
        }
        for (String word : words) {
            System.out.print(word + " ");
        }
        System.out.println("\n");

        Path wordpadPath = Paths.get("C:\\Program Files\\Windows NT\\Accessories\\wordpad.exe");
        //System.out.println(wordpadPath.subpath(1, 1));
        System.out.println(wordpadPath.subpath(0, 1));
        System.out.println(wordpadPath.subpath(0, 2));
        System.out.println(wordpadPath.subpath(0, 3));
        System.out.println(wordpadPath.subpath(0, 4));
    }
}