package org.example;

import utils.JiraIssue;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        
        String path="/home/luca/ISW2/zookeeper";

        CsvUtils csvUtils = new CsvUtils();
        csvUtils.createCsv("src/main/resources/metrica.csv");
        
        Date data = new SimpleDateFormat("yyyy MM dd").parse("2020 12 10");

        DownlaodDatas datas= new DownlaodDatas();

        JiraIssueFetcher fetcher = new JiraIssueFetcher();
        List<JiraIssue> issues = fetcher.fetchIssues();
        fetcher.printIssues(issues);


        //datas.initialized();
        datas.getVersioni(path, data);
    }
}