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

        File dir = new File("./src/ru/javawebinar/basejava");
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
        recursion("C:\\Program Files\\Projectfolder\\basejava");
    }

    public static void recursion(String path) throws IOException {
        File root = new File(path);
        File[] files = root.listFiles();

        for (File obj : files) {
            if (!obj.getCanonicalPath().equals("C:\\Program Files\\Projectfolder\\basejava\\.git")) {
                if (obj.isFile()) {
                    System.out.println(obj.getCanonicalPath());
                } else if (obj.isDirectory()) {
                    System.out.println(obj.getCanonicalPath());
                    recursion(obj.getCanonicalPath());
                }
            }

        }
    }
}
