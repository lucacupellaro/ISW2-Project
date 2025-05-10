package org.example;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.BufferedReader;
import java.io.File;

import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import utils.Files;
import utils.Methods;
import utils.Versione;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import java.util.Date;

public class DownlaodDatas {

    List<Versione> versioni = new ArrayList<>();

    public void initialized() throws IOException {
        try {
            Git git = Git.cloneRepository()
                    .setURI("https://github.com/lucacupellaro/zookeeper")
                    .setDirectory(new File("/home/luca/ISW2/zookeeper"))
                    .setCloneAllBranches(true)
                    .call();
        } catch (GitAPIException ex) {
            throw new RuntimeException(ex);
        }


        Repository repository = new FileRepositoryBuilder()
                .setGitDir(new File("/home/luca/ISW2/zookeeper"))
                .build();
        Git git = new Git(repository);

    }

    public void getVersioni(String percorsoRepo, Date dataLimite) {
        // Inizializza il repository
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        List<Files> listaFileVersione = new ArrayList<>();
        try (Repository repository = builder
                .setGitDir(new File(percorsoRepo, ".git"))
                .readEnvironment()
                .findGitDir()
                .build();
             Git git = new Git(repository)) {

            // RevWalk consente di scorrere i commit in ordine cronologico
            RevWalk revWalk = new RevWalk(repository);
            ObjectId head = repository.resolve("HEAD"); // Ultimo commit disponibile
            // Convertiamo l’ObjectId del commit in RevCommit
            RevCommit headCommit = revWalk.parseCommit(head);

            // Iniziamo a percorrere i commit a partire dal più recente (HEAD)
            revWalk.markStart(headCommit);
            System.out.println("Data inserita"+dataLimite);

            int version_=0;
            for (RevCommit commit : revWalk) {


                // Data del commit (in secondi, convertita in millisecondi)
                Date commitDate = new Date(commit.getCommitTime() * 1000L);

                // Verifica se la data del commit è precedente (o uguale) alla dataLimite
                if (commitDate.after(dataLimite)) {
                    // Qui aggiungi la logica di “scaricamento/estrazione”
                    // ad esempio fare un checkout o archiviare la directory, ecc.
                    System.out.println("Trovato commit valido: " + commit.getId().getName()
                            + " - Data: " + commitDate);

                    // Se devi scaricare/estrarre la versione corrispondente:
                    // git.checkout().setName(commit.getName()).call();
                    // ...esegui operazioni necessarie... String.valueOf(commit.getId())
                    //System.out.println("Questo è il commit: "+);
                    listaFileVersione=getFilesFromVersion(percorsoRepo, commit.getId().getName());

                    Versione version = new Versione(String.valueOf(commit.getId()),listaFileVersione);
                }

                version_++;

                System.out.println("Versione: "+version_);
            }

            revWalk.dispose();
        } catch (AmbiguousObjectException e) {
            throw new RuntimeException(e);
        } catch (IncorrectObjectTypeException e) {
            throw new RuntimeException(e);
        } catch (MissingObjectException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
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

            // Invece di ...
            //git.checkout().setName(versionId).call();

            // ... usa ...
            git.checkout().setName(versionId).call();

            // Recupera la lista di file dall'intero repository
            File rootDir = new File(percorsoRepo);
            for (File fsFile : listAllFiles(rootDir)) {
                if (fsFile.isFile()) {
                    // Qui puoi calcolare LOC o altre metriche secondo le tue necessità
                    Files fileVersione = new Files(fsFile.getName());

                    // Aggiunge il file alla lista
                    listaFileVersiones.add(fileVersione);

                    // Stampa il percorso del file
                    System.out.println("File trovato: " + fsFile.getAbsolutePath());

                    fileVersione.setMethods(getMethodsFromFile(fsFile.getAbsolutePath()));
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


    /*

    public List<Methods> getMethodsFromFile(File file) {
        List<Methods> methods = new ArrayList<>();


        // 1. Lettura del contenuto del file (già passato come parametro).
        List<String> lines = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return methods;
        }

        // 2. Esempio di riconoscimento semplice di un metodo in Java:
        //    cerchiamo righe che inizino con un modificatore e contengano parentesi tonde + parentesi graffa.
        for (String line : lines) {
            String trimmedLine = line.trim();
            // Controlliamo se la riga inizia con un modificatore classico (public, private, protected, static).
            if (trimmedLine.startsWith("public ")
                    || trimmedLine.startsWith("private ")
                    || trimmedLine.startsWith("protected ")
                    || trimmedLine.startsWith("static ")) {

                // Se contiene "(", ")" e "{" potrebbe essere (molto rudimentalmente) la definizione di un metodo.
                if (trimmedLine.contains("(") && trimmedLine.contains(")") && trimmedLine.contains("{")) {
                    // Ricaviamo la firma fino alla parentesi aperta.
                    int indexParenthesis = trimmedLine.indexOf("(");
                    String signature = trimmedLine.substring(0, indexParenthesis).trim();

                    // Crea un nuovo oggetto Methods con la stringa della firma (o ciò che serve).
                    Methods method = new Methods(signature);
                    methods.add(method);
                }
            }
        }

        return methods;
    }
    */


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