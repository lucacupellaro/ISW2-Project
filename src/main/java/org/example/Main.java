package org.example;

import utils.Files;
import utils.JiraIssue;
import utils.Methods;
import utils.Versione;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {

        DownlaodDatas datas= new DownlaodDatas();
        //datas.initialized();
        
        String path="/home/luca/ISW2/bookkeeper";

        List<Versione>versiones=datas.getVersioni2(path);

        CsvUtils csv=new CsvUtils();


        for(Versione versione:versiones){

            List<Files> listaFileVersione = datas.getFilesFromVersion(path, versione.getVersione());

            for(Files file:listaFileVersione){
               List<Methods> methods= datas.extractMethodsFromFile(file.getFileName());

               for(Methods method:methods){
                   csv.writeData(versione.getid(), file.getFileName(), method.getName(), method.getLOC());
               }
            }
        }



        /*
         CsvUtils csvUtils = new CsvUtils();

        csvUtils.createCsv("src/main/resources/metrica.csv");

        Date data = new SimpleDateFormat("yyyy MM dd").parse("2020 12 10");

         */




        /*
        *
        * JiraIssueFetcher fetcher = new JiraIssueFetcher();
        List<JiraIssue> issues = fetcher.fetchIssues();
        fetcher.printIssues(issues);
        System.out.println("Numero di issue: " + issues.size());
       // System.out.println("issue key: " + issues.returnKey());
        * */
        //jira



        //jGit



       // datas.getVersioni(path, data,issues);


    }
}