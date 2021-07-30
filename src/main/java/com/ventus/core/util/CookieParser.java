package com.ventus.core.util;

import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CookieParser {

    @SneakyThrows
    public static String read(String login, String filePath) {
        Scanner scanner = new Scanner(new FileInputStream(filePath));
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (s.contains(login)) {
                String[] strings = s.split(":");
                return strings[1];
            }
        }
        return null;
    }

    public static void write(String login, String cookies, String filePath) throws IOException {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(Path.of(filePath), StandardCharsets.UTF_8));
        boolean flag = false;

        for (int i = 0; i < fileContent.size(); i++) {
            if (fileContent.get(i).contains(login)) {
                fileContent.set(i, login + ":" + cookies);
                flag = true;
                break;
            }
        }

        if (!flag) {
            fileContent.add(login + ":" + cookies);
        }

        Files.write(Path.of(filePath), fileContent, StandardCharsets.UTF_8);
    }
}
