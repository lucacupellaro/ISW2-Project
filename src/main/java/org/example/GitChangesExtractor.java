package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class GitChangesExtractor {

    // Inserisci qui il path della root del repository
    private static final String REPO_ROOT = "/home/luca/ISW2/bookkeeper/";

    public static int getMethodHistories(String filePath) {
        int count = 0;
        try {
            ProcessBuilder pb = new ProcessBuilder("git", "log", "--pretty=oneline", "--", filePath);
            pb.directory(new File(REPO_ROOT));
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (reader.readLine() != null) {
                count++;
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static int getAuthors(String filePath) {
        Set<String> authors = new HashSet<>();
        try {
            ProcessBuilder pb = new ProcessBuilder("git", "log", "--pretty=format:%an", "--", filePath);
            pb.directory(new File(REPO_ROOT));
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                authors.add(line.trim());
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authors.size();
    }

    public static int getStmtAdded(String filePath) {
        int added = 0;
        try {
            ProcessBuilder pb = new ProcessBuilder("git", "log", "--pretty=format:", "--numstat", "--", filePath);
            pb.directory(new File(REPO_ROOT));
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 2) {
                    try {
                        added += Integer.parseInt(parts[0]);
                    } catch (NumberFormatException e) {
                        // file binari
                    }
                }
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return added;
    }

    public static int getStmtDeleted(String filePath) {
        int deleted = 0;
        try {
            ProcessBuilder pb = new ProcessBuilder("git", "log", "--pretty=format:", "--numstat", "--", filePath);
            pb.directory(new File(REPO_ROOT));
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 2) {
                    try {
                        deleted += Integer.parseInt(parts[1]);
                    } catch (NumberFormatException e) {
                        // file binari
                    }
                }
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deleted;
    }

    public static int getChurn(int added, int deleted) {
        return added + deleted;
    }

    // Funzione per normalizzare i path assoluti in relativi per Git
    public static String normalizePathForGit(String repoRoot, String absoluteFilePath) {
        Path repoRootPath = Paths.get(repoRoot).toAbsolutePath().normalize();
        Path absolutePath = Paths.get(absoluteFilePath).toAbsolutePath().normalize();
        Path relativePath = repoRootPath.relativize(absolutePath);
        return relativePath.toString().replace("\\", "/");
    }
}
