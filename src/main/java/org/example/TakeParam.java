package org.example;

import utils.Methods;
import utils.Versione;

import java.io.File;
import java.util.List;

public interface TakeParam {

    // Metodi per il recupero delle entità base
    List<Versione> getVersions(String projectName);
    List<File> getFiles(String versionId);
    List<Methods> getMethods(String filePath);

    // Metodi per le metriche a livello di metodo
    int getLineOfCode(Methods method);
    int getLineOfCodeTouched(Methods method);
    int getNumberOfRevisions(Methods method);
    int getNumberOfFixes(Methods method);

    // Metodi aggiuntivi per metriche avanzate (implementazione facoltativa)
    default int getNumberOfAuthors(Methods method) {
        // Implementazione di default vuota
        return 0;
    }

    // Altri metodi per le metriche elencate
    int getLocAdded(Methods method);
    int getMaxLocAdded(Methods method);
    double getAvgLocAdded(Methods method);
    int getChurn(Methods method);
    int getMaxChurn(Methods method);
    double getAvgChurn(Methods method);
    int getChgSetSize(Methods method);
    int getMaxChgSet(Methods method);
    double getAvgChgSet(Methods method);
    int getAge(Methods method);
    int getWeightedAge(Methods method);
    boolean isBuggy(Methods method);
}
