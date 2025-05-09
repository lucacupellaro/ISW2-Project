package org.example;

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
                "LOC_touched",
                "NR",
                "NFix",
                "NAuth",
                "LOC_added",
                "MAX_LOC_added",
                "AVG_LOC_added",
                "Churn",
                "MAX_Churn",
                "AVG_Churn",
                "ChgSetSize",
                "MAX_ChgSet",
                "AVG_ChgSet",
                "Age",
                "WeightedAge",
                "Buggy"
        );

        // Scrivi solo la riga di intestazione
        try (FileWriter writer = new FileWriter(file)) {
            String headerLine = String.join(",", header);
            writer.write(headerLine + "\n");
        }
    }
}
