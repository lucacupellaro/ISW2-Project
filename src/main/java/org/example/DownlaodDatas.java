package org.example;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.BufferedReader;
import java.io.File;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.revwalk.filter.CommitTimeRevFilter;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.TagOpt;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import utils.Files;
import utils.JiraIssue;
import utils.Methods;
import utils.Versione;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DownlaodDatas {

    List<Versione> versioni = new ArrayList<>();

    public void initialized() throws IOException {
        try {
            Git git = Git.cloneRepository()
                    .setURI("https://github.com/apache/bookkeeper.git")
                    .setDirectory(new File("/home/luca/ISW2/bookkeeper"))
                    .setCloneAllBranches(true)
                    .setTagOption(TagOpt.FETCH_TAGS)
                    .call();
        } catch (GitAPIException ex) {
            throw new RuntimeException(ex);
        }


        Repository repository = new FileRepositoryBuilder()
                .setGitDir(new File("/home/luca/ISW2/bookkeeper"))
                .build();
        Git git = new Git(repository);

    }


    public List<Versione> getVersioni2(String percorsoRepo) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        List<Versione> Versioni;

        try (Repository repository = builder
                .setGitDir(new File(percorsoRepo, ".git"))
                .readEnvironment()
                .findGitDir()
                .build();
             Git git = new Git(repository)) {

            // HashMap per contenere il mapping (tag -> data)
            Map<Ref, Date> tagDateMap = new HashMap<>();

            for (Ref tagRef : git.tagList().call()) {
                RevWalk revWalk = new RevWalk(repository);
                RevCommit commit = revWalk.parseCommit(tagRef.getObjectId());
                Date commitDate = commit.getAuthorIdent().getWhen();
                tagDateMap.put(tagRef, commitDate);
                revWalk.dispose();
            }

            // Ordina la entrySet per data
            List<Map.Entry<Ref, Date>> sortedEntries = new ArrayList<>(tagDateMap.entrySet());
            sortedEntries.sort(Map.Entry.comparingByValue());


            // Calcola il 33%
            int cutoff = (int) Math.ceil(sortedEntries.size() * 0.33);
            List<Map.Entry<Ref, Date>> selectedTags = sortedEntries.subList(0, cutoff);

            int i=0;
            //selectedTags al posto di sortedEntries
            for (Map.Entry<Ref, Date> entry : sortedEntries) {

                Ref tagRef = entry.getKey();
                String fullTagName = tagRef.getName();  // es: refs/tags/release-1.0.0
                String cleanedTag = fullTagName.replace("refs/tags/", "");
                //System.out.println("Processing release: " + cleanedTag + " - Date: " + entry.getValue()+ i);
                git.reset().setMode(ResetCommand.ResetType.HARD).call();
                git.clean().setCleanDirectories(true).setForce(true).call();


                git.checkout().setName(cleanedTag).call();
                //List<Files> listaFileVersione = getFilesFromVersion(percorsoRepo, cleanedTag);
                Versione version = new Versione(cleanedTag,i);
                i++;
                versioni.add(version);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return versioni;
    }





    public List<Files> getFilesFromVersion(String percorsoRepo, String versionId) throws IOException, GitAPIException {
        List<Files> listaFileVersiones = new ArrayList<>();

        // Costruisce il repository
        try (Repository repository = new FileRepositoryBuilder()
                .setGitDir(new File(percorsoRepo, ".git"))
                .readEnvironment()
                .findGitDir()
                .build();
             Git git = new Git(repository)) {

            git.reset().setMode(ResetCommand.ResetType.HARD).call();
            git.clean().setCleanDirectories(true).setForce(true).call();


            git.checkout().setName(versionId).call();


            File rootDir = new File(percorsoRepo);
            for (File fsFile : listAllFiles(rootDir)) {
                if (fsFile.isFile()) {

                    Files fileVersione = new Files(fsFile.getAbsolutePath());

                    if(fsFile.getName().endsWith(".java")) {
                        listaFileVersiones.add(fileVersione);
                        System.out.println("File trovato: " + fsFile.getAbsolutePath());
                       // fileVersione.setMethods(extractMethodsFromFile(fsFile.getAbsolutePath()));
                    }

                }
            }
        }

        return listaFileVersiones;
    }



    public List<File> listAllFiles(File directory) {
        List<File> result = new ArrayList<>();
        File[] fileArray = directory.listFiles();
        if (fileArray != null) {
            for (File fsFile : fileArray) {
                if (fsFile.isDirectory()) {
                    // Scansione ricorsiva se è una directory
                    result.addAll(listAllFiles(fsFile));
                } else {
                    result.add(fsFile);
                }
            }
        }
        return result;
    }



    public List<Methods> extractMethodsFromFile2(String filePath) {

       // System.out.println("filepath: " + filePath);
        List<Methods> methodsList = new ArrayList<>();
        try {
            JavaParser parser = new JavaParser();
            ParseResult<CompilationUnit> result = parser.parse(new File(filePath));

            if (result.isSuccessful() && result.getResult().isPresent()) {
                CompilationUnit cu = result.getResult().get();

                List<MethodDeclaration> methods = cu.findAll(MethodDeclaration.class);

                for (MethodDeclaration method : methods) {
                    String methodName = method.getNameAsString();

                    int startLine = method.getBegin().get().line;
                    int endLine = method.getEnd().get().line;
                    int loc = endLine - startLine + 1;

                    Methods m = new Methods(methodName, loc);
                    m.setLOC(loc);
                    m.setStartLine(startLine);
                    m.setEndLine(endLine);
                    m.setFilePath(filePath); // qui salviamo il path assoluto
                    methodsList.add(m);
                }
            } else {
                System.out.println("Parsing fallito su file: " + filePath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return methodsList;
    }

    public List<Methods> extractMethodsFromFile3(String filePath) {

        List<Methods> methodsList = new ArrayList<>();
        try {
            JavaParser parser = new JavaParser();
            ParseResult<CompilationUnit> result = parser.parse(new File(filePath));

            if (result.isSuccessful() && result.getResult().isPresent()) {
                CompilationUnit cu = result.getResult().get();

                List<MethodDeclaration> methods = cu.findAll(MethodDeclaration.class);

                for (MethodDeclaration method : methods) {
                    String methodName = method.getNameAsString();

                    int startLine = method.getBegin().get().line;
                    int endLine = method.getEnd().get().line;
                    int loc = endLine - startLine + 1;

                    Methods m = new Methods(methodName, loc);
                    m.setLOC(loc);
                    m.setStartLine(startLine);
                    m.setEndLine(endLine);
                    m.setFilePath(filePath); // salva path assoluto

                    // Aggiunte nuove metriche:
                    int parameterCount = method.getParameters().size();
                    m.setParameterCount(parameterCount);

                    // Statements count
                    int statementsCount = method.findAll(Statement.class).size();
                    m.setStatementsCount(statementsCount);

                    // Return statements
                    int returnStatements = method.findAll(ReturnStmt.class).size();
                    m.setReturnStatements(returnStatements);

                    // Method invocations
                    List<MethodCallExpr> calls = method.findAll(MethodCallExpr.class);
                    int methodInvocations = calls.size();
                    m.setMethodInvocations(methodInvocations);

                    // Distinct method invocations
                    Set<String> distinctCalls = calls.stream().map(MethodCallExpr::getNameAsString).collect(Collectors.toSet());
                    int distinctMethodInvocations = distinctCalls.size();
                    m.setDistinctMethodInvocations(distinctMethodInvocations);

                    // Local variable declarations
                    int localVariableDeclarations = method.findAll(VariableDeclarationExpr.class).size();
                    m.setLocalVariableDeclarations(localVariableDeclarations);

                    // Cyclomatic Complexity (decision points + 1)
                    int decisions = method.findAll(IfStmt.class).size() +
                            method.findAll(ForStmt.class).size() +
                            method.findAll(ForEachStmt.class).size() +
                            method.findAll(WhileStmt.class).size() +
                            method.findAll(DoStmt.class).size() +
                            method.findAll(SwitchEntry.class).size() +
                            method.findAll(CatchClause.class).size();
                    int cyclomaticComplexity = decisions + 1;
                    m.setCyclomaticComplexity(cyclomaticComplexity);

                    // Number of branches (simile ai decision points)
                    m.setNumberOfBranches(decisions);

                    // Nesting depth
                    int nestingDepth = computeNestingDepth(method);
                    m.setNestingDepth(nestingDepth);

                    // Has Javadoc
                    boolean hasJavadoc = method.getJavadoc().isPresent();
                    m.setHasJavadoc(hasJavadoc);

                    methodsList.add(m);
                }
            } else {
                System.out.println("Parsing fallito su file: " + filePath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return methodsList;
    }


    private int computeNestingDepth(Node node) {
        return computeNestingDepthRecursive(node, 0);
    }

    private int computeNestingDepthRecursive(Node node, int currentDepth) {
        int maxDepth = currentDepth;
        for (Node child : node.getChildNodes()) {
            if (child instanceof IfStmt || child instanceof ForStmt || child instanceof ForEachStmt ||
                    child instanceof WhileStmt || child instanceof DoStmt || child instanceof SwitchStmt) {
                int childDepth = computeNestingDepthRecursive(child, currentDepth + 1);
                maxDepth = Math.max(maxDepth, childDepth);
            } else {
                int childDepth = computeNestingDepthRecursive(child, currentDepth);
                maxDepth = Math.max(maxDepth, childDepth);
            }
        }
        return maxDepth;
    }









}