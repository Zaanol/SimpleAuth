package com.zanol.auth.engine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BruteForceEngine {

    private static final List<Character> LETTERS = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');
    private static final List<Character> CAPITAL_LETTERS = LETTERS.stream().map(Character::toUpperCase).collect(Collectors.toList());
    private static final List<Character> ALL_LETTERS = Stream.concat(LETTERS.stream(), CAPITAL_LETTERS.stream()).collect(Collectors.toList());
    private static final List<Character> DIGITS = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    private static final List<Character> ALL_POSSIBILITIES = Stream.concat(ALL_LETTERS.stream(), DIGITS.stream()).collect(Collectors.toList());

    private static long start;

    public static void bruteForce(String executablePath, String wordListPath, String user) {
        start = System.currentTimeMillis();

        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService e = Executors.newFixedThreadPool(cores);

        Long passwords = readWordList(wordListPath).count();

        Long passwordsPerCore = passwords / 12;
        Long rest = passwords % 12;

        for (int core = 0; core < cores; core++) {
            final Long startIndex = core * passwordsPerCore;
            final Long endIndex = startIndex + (core == 0 ? passwordsPerCore + rest : passwordsPerCore) - 1;

            Thread thread = new Thread(() -> separatesAndTestPasswordAnd(e, executablePath, user, wordListPath, startIndex, endIndex));

            e.execute(thread);
        }
    }

    public static void separatesAndTestPasswordAnd(ExecutorService executorService, String executablePath, String user, String wordListPath, Long startIndex, Long endIndex) {
        Long passPerCycle = 500000L;
        Long passwordsRange = endIndex - startIndex;
        Long cycles = passwordsRange > passPerCycle
                ? passwordsRange / passPerCycle
                : 1;

        for (Long cycle = 0L; cycle < cycles; cycle++) {
            try {
                Long startCycle = startIndex + (cycle * passPerCycle);
                Long endCycle = (cycle < cycle - 1) ? startCycle + passPerCycle : endIndex;

                List<String> passwordsToTest =
                        readWordList(wordListPath).skip(startCycle).limit(endCycle).collect(Collectors.toList());
                testPasswords(executorService, executablePath, user, passwordsToTest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void testPasswords(ExecutorService executorService, String executablePath, String user, List<String> passwords) {
        passwords.forEach(password -> {
            if (testPassword(executablePath, user, password)){
                saveFoundPasswords(password);
                executorService.shutdown();
            }
        });
    }

    public static Boolean testPassword(String executablePath, String user, String password) {
        try {
            String command = executablePath.concat(" " + user).concat(" " + password);

            Process proc = Runtime.getRuntime().exec(command);
            proc.waitFor();

            return proc.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Stream<String> readWordList(String wordListPath) {
        try {
            return Files.lines(Paths.get(wordListPath));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveFoundPasswords(String password) {
        System.out.println("Saving password: " + password);

        try {
            File teste = new File("passwordsFound.txt");
            teste.createNewFile();

            float elapsed = (System.currentTimeMillis() - start) / 1000;

            Path path = Paths.get(teste.getPath());

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Files.readString(path));
            stringBuilder.append("\n\n|-------------------------------|Zanol Password Breaker|-------------------------------|");
            stringBuilder.append("\n|-Date: " + new Date().toLocaleString());
            stringBuilder.append("\n|-Password found: " + password);
            stringBuilder.append("\n|-In: " + Math.round(elapsed) + " seconds");
            stringBuilder.append("\n|-------------------------------|Zanol Password Breaker|-------------------------------|");

            Files.write(path, stringBuilder.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateWordList(String fileName) {
        try {
            File wordFile = new File("assets\\" + fileName + ".txt");
            wordFile.createNewFile();

            BufferedWriter bw = new BufferedWriter(new FileWriter(wordFile));

            for (Character c1 : ALL_POSSIBILITIES) {
                for (Character c2 : ALL_POSSIBILITIES) {
                    for (Character c3 : ALL_POSSIBILITIES) {
                        for (Character c4 : ALL_POSSIBILITIES) {
                            for (Character c5 : ALL_POSSIBILITIES) {
                                for (Character c6 : ALL_POSSIBILITIES) {
                                    for (Character c7 : ALL_POSSIBILITIES) {
                                        for (Character c8 : ALL_POSSIBILITIES) {
                                            bw.write(String.valueOf(c1) + c2 + c3 + c4 + c5 + c6 + c7 + c8 + "\n");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}