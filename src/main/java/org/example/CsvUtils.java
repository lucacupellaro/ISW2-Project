package org.example;

import utils.Files;
import utils.Methods;
import utils.Versione;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CsvUtils {

    /**
     * Crea un file CSV solo con la riga di intestazione delle colonne richieste.
     * Se il file esiste già, lo cancella e lo riscrive.
     * @param fileName Nome del file CSV (es: "output.csv")
     * @throws IOException In caso di errore di scrittura
     */
    public void createCsv(String fileName) throws IOException {
        File file = new File(fileName);

        // Se il file esiste, cancellalo
        if (file.exists()) {
            System.out.println("File già esistente: " + fileName);
            if (!file.delete()) {
                throw new IOException("Impossibile cancellare il file esistente: " + fileName);
            }
        }

        // Lista delle colonne da inserire come header
        List<String> header = Arrays.asList(
                "Version",
                "File Name",
                "Method Name",
                "LOC",
                "Statements Count",
                "Cyclomatic Complexity",
                "Nesting Depth",
                "Number of Branches",
                "Parameter Count",
                "Return Statements",
                "Method Invocations",
                "Distinct Method Invocations",
                "Local Variable Declarations",
                "Has Javadoc",
                "Method Histories",
                "Authors",
                "Stmt Added",
                "Stmt Deleted",
                "Churn",
                "Buggy"
        );


        // Scrivi solo la riga di intestazione
        try (FileWriter writer = new FileWriter(file)) {
            String headerLine = String.join(",", header);
            writer.write(headerLine + "\n");
        }
    }

    public void writeData2(
            int versione, String file, String metodo, int loc, int statementsCount, int cyclomaticComplexity,
            int nestingDepth, int numberOfBranches, int parameterCount, int returnStatements,
            int methodInvocations, int distinctMethodInvocations, int localVariableDeclarations, boolean hasJavadoc) {

        String path = "src/main/resources/metrica.csv";
        boolean fileExists = new File(path).exists();

        System.out.println(
                "Versione: " + versione +
                        " | File: " + file +
                        " | Metodo: " + metodo +
                        " | LOC: " + loc +
                        " | StatementsCount: " + statementsCount +
                        " | CyclomaticComplexity: " + cyclomaticComplexity +
                        " | NestingDepth: " + nestingDepth +
                        " | NumberOfBranches: " + numberOfBranches +
                        " | ParameterCount: " + parameterCount +
                        " | ReturnStatements: " + returnStatements +
                        " | MethodInvocations: " + methodInvocations +
                        " | DistinctMethodInvocations: " + distinctMethodInvocations +
                        " | LocalVariableDeclarations: " + localVariableDeclarations +
                        " | HasJavadoc: " + hasJavadoc
        );


        try (FileWriter writer = new FileWriter(path, true)) {
            if (!fileExists) {
                writer.write("Version,FileName,MethodName,LOC,StatementsCount,CyclomaticComplexity,NestingDepth,NumberOfBranches,ParameterCount,ReturnStatements,MethodInvocations,DistinctMethodInvocations,LocalVariableDeclarations,HasJavadoc\n");
            }

            String versionEscaped = escapeCsv(String.valueOf(versione));
            String fileEscaped = escapeCsv(file);
            String metodoEscaped = escapeCsv(metodo);
            String locEscaped = String.valueOf(loc);
            String statementsCountEscaped = String.valueOf(statementsCount);
            String cyclomaticComplexityEscaped = String.valueOf(cyclomaticComplexity);
            String nestingDepthEscaped = String.valueOf(nestingDepth);
            String numberOfBranchesEscaped = String.valueOf(numberOfBranches);
            String parameterCountEscaped = String.valueOf(parameterCount);
            String returnStatementsEscaped = String.valueOf(returnStatements);
            String methodInvocationsEscaped = String.valueOf(methodInvocations);
            String distinctMethodInvocationsEscaped = String.valueOf(distinctMethodInvocations);
            String localVariableDeclarationsEscaped = String.valueOf(localVariableDeclarations);
            String hasJavadocEscaped = String.valueOf(hasJavadoc);

            writer.write(
                    versionEscaped + "," +
                            fileEscaped + "," +
                            metodoEscaped + "," +
                            locEscaped + "," +
                            statementsCountEscaped + "," +
                            cyclomaticComplexityEscaped + "," +
                            nestingDepthEscaped + "," +
                            numberOfBranchesEscaped + "," +
                            parameterCountEscaped + "," +
                            returnStatementsEscaped + "," +
                            methodInvocationsEscaped + "," +
                            distinctMethodInvocationsEscaped + "," +
                            localVariableDeclarationsEscaped + "," +
                            hasJavadocEscaped + "\n"
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeData3(
            int versione, String file, String metodo, int loc, int statementsCount, int cyclomaticComplexity,
            int nestingDepth, int numberOfBranches, int parameterCount, int returnStatements,
            int methodInvocations, int distinctMethodInvocations, int localVariableDeclarations, boolean hasJavadoc,
            int methodHistories, int authors, int stmtAdded, int stmtDeleted, int churn, String buggy) {

        String path = "src/main/resources/metrica.csv";
        boolean fileExists = new File(path).exists();

        System.out.println(
                "Versione: " + versione +
                        " | File: " + file +
                        " | Metodo: " + metodo +
                        " | LOC: " + loc +
                        " | StatementsCount: " + statementsCount +
                        " | CyclomaticComplexity: " + cyclomaticComplexity +
                        " | NestingDepth: " + nestingDepth +
                        " | NumberOfBranches: " + numberOfBranches +
                        " | ParameterCount: " + parameterCount +
                        " | ReturnStatements: " + returnStatements +
                        " | MethodInvocations: " + methodInvocations +
                        " | DistinctMethodInvocations: " + distinctMethodInvocations +
                        " | LocalVariableDeclarations: " + localVariableDeclarations +
                        " | HasJavadoc: " + hasJavadoc +
                        " | MethodHistories: " + methodHistories +
                        " | Authors: " + authors +
                        " | StmtAdded: " + stmtAdded +
                        " | StmtDeleted: " + stmtDeleted +
                        " | Churn: " + churn +
                        " | Buggy: " + buggy
        );

        try (FileWriter writer = new FileWriter(path, true)) {
            if (!fileExists) {
                writer.write("Version,FileName,MethodName,LOC,StatementsCount,CyclomaticComplexity,NestingDepth,NumberOfBranches,ParameterCount,ReturnStatements,MethodInvocations,DistinctMethodInvocations,LocalVariableDeclarations,HasJavadoc,MethodHistories,Authors,StmtAdded,StmtDeleted,Churn,Buggy\n");
            }

            String versionEscaped = escapeCsv(String.valueOf(versione));
            String fileEscaped = escapeCsv(file);
            String metodoEscaped = escapeCsv(metodo);
            String locEscaped = String.valueOf(loc);
            String statementsCountEscaped = String.valueOf(statementsCount);
            String cyclomaticComplexityEscaped = String.valueOf(cyclomaticComplexity);
            String nestingDepthEscaped = String.valueOf(nestingDepth);
            String numberOfBranchesEscaped = String.valueOf(numberOfBranches);
            String parameterCountEscaped = String.valueOf(parameterCount);
            String returnStatementsEscaped = String.valueOf(returnStatements);
            String methodInvocationsEscaped = String.valueOf(methodInvocations);
            String distinctMethodInvocationsEscaped = String.valueOf(distinctMethodInvocations);
            String localVariableDeclarationsEscaped = String.valueOf(localVariableDeclarations);
            String hasJavadocEscaped = String.valueOf(hasJavadoc);
            String methodHistoriesEscaped = String.valueOf(methodHistories);
            String authorsEscaped = String.valueOf(authors);
            String stmtAddedEscaped = String.valueOf(stmtAdded);
            String stmtDeletedEscaped = String.valueOf(stmtDeleted);
            String churnEscaped = String.valueOf(churn);
            String buggyEscaped = escapeCsv(buggy);

            writer.write(
                    versionEscaped + "," +
                            fileEscaped + "," +
                            metodoEscaped + "," +
                            locEscaped + "," +
                            statementsCountEscaped + "," +
                            cyclomaticComplexityEscaped + "," +
                            nestingDepthEscaped + "," +
                            numberOfBranchesEscaped + "," +
                            parameterCountEscaped + "," +
                            returnStatementsEscaped + "," +
                            methodInvocationsEscaped + "," +
                            distinctMethodInvocationsEscaped + "," +
                            localVariableDeclarationsEscaped + "," +
                            hasJavadocEscaped + "," +
                            methodHistoriesEscaped + "," +
                            authorsEscaped + "," +
                            stmtAddedEscaped + "," +
                            stmtDeletedEscaped + "," +
                            churnEscaped + "," +
                            buggyEscaped + "\n"
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void writeData(int versione, String file, String metodo, int loc, int locTouched) {
        String path = "src/main/resources/metrica.csv";
        boolean fileExists = new File(path).exists();

        System.out.println("versione :"+versione+"file: "+file+ "metodo "+metodo+"loc "+loc+ " loc_touched "+locTouched);

        try (FileWriter writer = new FileWriter(path, true)) {
            if (!fileExists) {
                writer.write("Version,FileName,MethodName,LOC,LOC_Touched,NR,NAuth,LOC_Added,Churn\n");
            }

            String versionEscaped = escapeCsv(String.valueOf(versione));
            String fileEscaped = escapeCsv(file);
            String metodoEscaped = escapeCsv(metodo);
            String locEscaped = String.valueOf(loc);
            String locTouchedEscaped = String.valueOf(locTouched);
           // String nrEscaped = String.valueOf(nr);
           // String nAuthEscaped = String.valueOf(nAuth);
           // String locAddedEscaped = String.valueOf(locAdded);
           // String churnEscaped = String.valueOf(churn);

            writer.write(
                    versionEscaped + "," +
                            fileEscaped + "," +
                            metodoEscaped + "," +
                            locEscaped + "," +
                            locTouchedEscaped + ","
                         //   nrEscaped + "," +
                          //  nAuthEscaped + "," +
                           // locAddedEscaped + "," +
                          //  churnEscaped + "\n"
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Metodo di escape per i campi CSV
    private String escapeCsv(String field) {
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            field = field.replace("\"", "\"\"");
            return "\"" + field + "\"";
        }
        return field;
    }

}
