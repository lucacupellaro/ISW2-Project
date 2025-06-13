package org.example;
import com.github.javaparser.ParseResult;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.BufferedReader;
import java.io.File;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.TagOpt;
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




    public void getVersioni(String percorsoRepo, Date dataLimite, List<JiraIssue> issues) {
        List<Versione> versioni = new ArrayList<>();
        Set<String> issueKeys = issues.stream()
                .map(JiraIssue::returnKey)
                .collect(Collectors.toSet());

        FileRepositoryBuilder builder = new FileRepositoryBuilder();

        try (Repository repository = builder
                .setGitDir(new File(percorsoRepo, ".git"))
                .readEnvironment()
                .findGitDir()
                .build();
             Git git = new Git(repository)) {

            RevWalk revWalk = new RevWalk(repository);
            ObjectId head = repository.resolve("HEAD");
            RevCommit headCommit = revWalk.parseCommit(head);
            revWalk.markStart(headCommit);

            // Regex per tutte le chiavi Jira
            Pattern keyPattern = Pattern.compile(
                    "\\b(" + String.join("|", issueKeys) + ")\\b"
            );

            for (RevCommit commit : revWalk) {
                String commitMessage = commit.getFullMessage();
                Matcher matcher = keyPattern.matcher(commitMessage);
                System.out.println("match find: " +matcher.find());
                // 1. Controllo chiave Jira nel messaggio
                if (matcher.find()) {
                    Date commitDate = new Date(commit.getCommitTime() * 1000L);
                    System.out.println("Ciao");
                    // 2. Controllo data commit
                    if (commitDate.after(dataLimite)) {
                        System.out.println("Commit valido: " + commit.getId().getName()
                                + " - Data: " + commitDate
                                + " - Issue: " + matcher.group(1));

                        // 3. Recupero file modificati
                        List<Files> files = getFilesFromVersion(
                                percorsoRepo,
                                commit.getId().getName()
                        );

                        // 4. Creazione oggetto Versione
                       /* versioni.add(new Versione(
                                commit.getId().getName(),
                                files,
                                commit.getAuthorIdent().getName(),
                                commitDate,
                                matcher.group(1)  // Chiave Jira
                        ));

                        */
                    }
                }
            }
            revWalk.dispose();
        } catch (Exception e) {
            throw new RuntimeException("Errore nell'analisi del repository", e);
        }
       // return versioni;
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

            git.checkout().setName(versionId).call();


            File rootDir = new File(percorsoRepo);
            for (File fsFile : listAllFiles(rootDir)) {
                if (fsFile.isFile()) {

                    Files fileVersione = new Files(fsFile.getName());

                    if(fsFile.getName().endsWith(".java")) {
                        listaFileVersiones.add(fileVersione);
                       // System.out.println("File trovato: " + fsFile.getAbsolutePath());
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


    public List<Methods> extractMethodsFromFile(String filePath) {
        List<Methods> methodsList = new ArrayList<>();
        try {
            JavaParser parser = new JavaParser();
            ParseResult<CompilationUnit> result = parser.parse(new File(filePath));

            if (result.isSuccessful() && result.getResult().isPresent()) {
                CompilationUnit cu = result.getResult().get();

                List<MethodDeclaration> methods = cu.findAll(MethodDeclaration.class);

                for (MethodDeclaration method : methods) {
                    String methodName = method.getNameAsString();
                    //int numParams = method.getParameters().size();

                    int startLine = method.getBegin().get().line;
                    int endLine = method.getEnd().get().line;
                    int loc = endLine - startLine + 1;

                    //int numIf = method.findAll(IfStmt.class).size();
                    //int numFor = method.findAll(ForStmt.class).size();
                    //int numWhile = method.findAll(WhileStmt.class).size();
                    //int numSwitch = method.findAll(SwitchStmt.class).size();
                    //int ciclomatica = 1 + numIf + numFor + numWhile + numSwitch;

                    /*
                     Methods m = new Methods(methodName, loc);
                    m.setLOC(loc);
                    m.setNumParameters(numParams);
                    m.setNumIf(numIf);
                    m.setNumFor(numFor);
                    m.setNumWhile(numWhile);
                    m.setCyclomatic(ciclomatica);

                    methodsList.add(m);
                     */
                    // DEBUG
                    //System.out.println("Metodo: " + methodName + " LOC: " + loc );

                    Methods m = new Methods(methodName, loc);
                    m.setLOC(loc);
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





    public List<Methods> getMethodsFromFile(String file) {
        List<Methods> methods = new ArrayList<>();
        List<String> lines = new ArrayList<>();

        // Lettura del file
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(String.valueOf(file)))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return methods;
        }

        for (int i = 0; i < lines.size(); i++) {
            String trimmedLine = lines.get(i).trim();

            if (isMethodDeclaration(trimmedLine)) {
                int startLine = i + 1; // Linea di inizio (1-based per compatibilità con git blame)
                int endLine = findMethodEnd(lines, i);
                int loc = endLine - i; // Calcolo LOC

                // Estrazione firma metodo
                String signature = extractMethodSignature(trimmedLine);
                Methods method = new Methods(signature,loc);

                // Impostazione metrica LOC
                method.setLOC(loc);

                // Aggiungi altre metriche qui...

                methods.add(method);

                System.out.println("Metodo: "+signature+" - LOC: "+loc);
                // Salta le linee già processate
                i = endLine - 1; // -1 perché il ciclo incrementa i
            }
        }
        return methods;
    }

    // Metodi di supporto
    public boolean isMethodDeclaration(String line) {
        return (line.startsWith("public ") || line.startsWith("private ") ||
                line.startsWith("protected ") || line.startsWith("static "))
                && line.contains("(") && line.contains(")") && line.contains("{");
    }

    public int findMethodEnd(List<String> lines, int startIndex) {
        int braceCount = 1;
        for (int j = startIndex + 1; j < lines.size(); j++) {
            braceCount += countBraces(lines.get(j));
            if (braceCount == 0) return j + 1; // Restituisce linea 1-based
        }
        return lines.size(); // Fallback per metodi malformati
    }

    public int countBraces(String line) {
        int count = 0;
        for (char c : line.toCharArray()) {
            if (c == '{') count++;
            if (c == '}') count--;
        }
        return count;
    }

    public String extractMethodSignature(String declaration) {
        int paramStart = declaration.indexOf('(');
        int paramEnd = declaration.indexOf(')');
        return declaration.substring(0, paramStart).trim() +
                declaration.substring(paramStart, paramEnd + 1);
    }

    public int get_LOC_touched(String percorsoRepo, String versionId, Methods metodo){
        int i=0;
        return i;
    }

}