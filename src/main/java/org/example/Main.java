package org.example;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import utils.Files;
import utils.Methods;
import utils.Versione;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws Exception {

        DownlaodDatas datas = new DownlaodDatas();

        String path = "/home/luca/ISW2/bookkeeper";

        List<Versione> versiones = datas.getVersioni2(path);
        CsvUtils csv = new CsvUtils();
        csv.createCsv("src/main/resources/metrica.csv");

        GitChangesExtractor extractor = new GitChangesExtractor();




        for (Versione versione : versiones) {
            List<Files> listaFileVersione = datas.getFilesFromVersion(path, versione.getVersione());

            for (Files file : listaFileVersione) {

                List<Methods> methods = datas.extractMethodsFromFile3(file.getFileName());


                for (Methods method : methods) {

                    // Raccogli le metriche changes X2 a livello di file

                    String filePathForGit = extractor.normalizePathForGit(path, file.getFileName());
                    System.out.println(filePathForGit);
// Ora chiami le funzioni changes X2 con il path relativo:
                    int methodHistories = GitChangesExtractor.getMethodHistories(filePathForGit);
                    int authors = GitChangesExtractor.getAuthors(filePathForGit);
                    int stmtAdded = GitChangesExtractor.getStmtAdded(filePathForGit);
                    int stmtDeleted = GitChangesExtractor.getStmtDeleted(filePathForGit);
                    int churn = GitChangesExtractor.getChurn(stmtAdded, stmtDeleted);


                    csv.writeData3(
                            versione.getid(),                        // Version
                            file.getFileName(),                      // File Name
                            method.getName(),                        // Method Name
                            method.getLOC(),                         // LOC
                            method.getStatementsCount(),             // Statements Count
                            method.getCyclomaticComplexity(),        // Cyclomatic Complexity
                            method.getNestingDepth(),                // Nesting Depth
                            method.getNumberOfBranches(),            // Number of Branches
                            method.getParameterCount(),              // Parameter Count
                            method.getReturnStatements(),            // Return Statements
                            method.getMethodInvocations(),           // Method Invocations
                            method.getDistinctMethodInvocations(),   // Distinct Method Invocations
                            method.getLocalVariableDeclarations(),   // Local Variable Declarations
                            method.getHasJavadoc(),                  // Has Javadoc (boolean true/false)
                            methodHistories,                         // Method Histories (changes X2)
                            authors,                                 // Authors (changes X2)
                            stmtAdded,                               // Stmt Added (changes X2)
                            stmtDeleted,                             // Stmt Deleted (changes X2)
                            churn,                                   // Churn (changes X2)
                            "?"                                      // Buggy → per ora placeholder da etichettare poi
                    );

                }
            }
        }



    }
}
