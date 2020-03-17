package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainFile {
    public static void main(String[] args) throws IOException {
        String filePath = ".\\.gitignore";

        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        File dir = new File("./src/com/urise/webapp");
        System.out.println(dir.isDirectory());
        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
        getAroundDirectory("./src", "");
    }

    public static void getAroundDirectory(String path, String string) throws IOException {
        File root = new File(path);
        File[] files = root.listFiles();

        if (files != null) {
            for (File obj : files) {
                if (obj.isFile()) {
                    System.out.println(string + "File :" + obj.getName());
                } else if (obj.isDirectory()) {
                    System.out.println(string + "Directory :" + obj.getName());
                    getAroundDirectory(obj.getCanonicalPath(), string + " ");
                }
            }
        }
    }
}
