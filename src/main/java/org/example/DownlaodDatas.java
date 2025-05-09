package org.example;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import java.io.File;

import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import utils.Files;
import utils.Versione;
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
                }
            }
        }

        return listaFileVersiones;
    }



    private List<File> listAllFiles(File directory) {
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

}