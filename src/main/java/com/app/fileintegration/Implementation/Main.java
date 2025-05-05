package com.app.fileintegration.Implementation;


import com.app.fileintegration.Implementation.source.ApiSource;
import com.app.fileintegration.Implementation.source.CsvSource;
import com.app.fileintegration.Implementation.source.ExcelSource;
import com.app.fileintegration.Implementation.source.JsonSource;
import com.app.fileintegration.Implementation.target.ApiTarget;
import com.app.fileintegration.Implementation.target.CsvTarget;
import com.app.fileintegration.Implementation.target.ExcelTarget;
import com.app.fileintegration.Implementation.target.JsonTarget;
import com.app.fileintegration.Implementation.transformation.Mappage;
import com.app.fileintegration.Interface.IDataSource;
import com.app.fileintegration.Interface.IDataTarget;
import com.app.fileintegration.Interface.IDataTransformer;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // --- Source ---
            System.out.println("Choisissez votre type de source (api, csv, json, excel) : ");
            String sourceType = scanner.nextLine().trim().toLowerCase();

            IDataSource dataSource = null;

            switch (sourceType) {
                case "api":
                    System.out.print("Entrez l'URL de l'API source : ");
                    String apiUrl = scanner.nextLine();
                    dataSource = new ApiSource(apiUrl);
                    break;
                case "csv":
                    System.out.print("Entrez le chemin du fichier CSV source : ");
                    String csvPath = scanner.nextLine().trim();
                    dataSource = new CsvSource(csvPath);
                    break;
                case "json":
                    System.out.print("Entrez le chemin du fichier JSON source : ");
                    String jsonPath = scanner.nextLine().trim();
                    dataSource = new JsonSource(jsonPath);
                    break;
                case "excel":
                    System.out.print("Entrez le chemin du fichier Excel source : ");
                    String excelPath = scanner.nextLine().trim();
                    dataSource = new ExcelSource(excelPath);
                    break;
                default:
                    System.out.println("Type de source non reconnu !");
                    return;
            }

            // --- Extraction ---
            List<Map<String, Object>> extractedData = dataSource.extract();
            System.out.println("Extraction terminée. Nombre d'enregistrements extraits : " + extractedData.size());

            List<String> newKeys = List.of("nom","email","none","none","none");
            // --- Transformation ---
            IDataTransformer transformer = new Mappage();
            List<Map<String, Object>> transformedData = transformer.transform(extractedData, newKeys);
            System.out.println("Transformation terminée.");

            //System.out.println(transformedData);
            // --- Target ---
            System.out.println("Choisissez votre type de cible (api, csv, json, excel) : ");
            String targetType = scanner.nextLine().trim().toLowerCase();

            IDataTarget dataTarget = null;

            switch (targetType) {
                case "api":
                    System.out.print("Entrez l'URL de l'API cible : ");
                    String targetApiUrl = scanner.nextLine().trim();
                    dataTarget = new ApiTarget(targetApiUrl);
                    break;
                case "csv":
                    System.out.print("Entrez le chemin du fichier CSV cible : ");
                    String targetCsvPath = scanner.nextLine().trim();
                    dataTarget = new CsvTarget(targetCsvPath);
                    break;
                case "json":
                    System.out.print("Entrez le chemin du fichier JSON cible : ");
                    String targetJsonPath = scanner.nextLine().trim();
                    dataTarget = new JsonTarget(targetJsonPath);
                    break;
                case "excel":
                    System.out.print("Entrez le chemin du fichier Excel cible : ");
                    String targetExcelPath = scanner.nextLine().trim();
                    dataTarget = new ExcelTarget(targetExcelPath);
                    break;
                default:
                    System.out.println("Type de cible non reconnu !");
                    return;
            }

            // --- Loading ---
            dataTarget.load(transformedData);
            System.out.println("Chargement terminé.");

        } catch (Exception e) {
            System.out.println("Erreur dans le processus ETL : " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    // Méthodes utilitaires pour setter les chemins d'accès aux fichiers ou URL
    private static void setFilePath(Object source, String filePath) {
        try {
            source.getClass().getDeclaredField("filePath").setAccessible(true);
            source.getClass().getDeclaredField("filePath").set(source, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setConnexionUrl(ApiSource apiSource, String url) {
        try {
            apiSource.getClass().getDeclaredField("connexionUrl").setAccessible(true);
            apiSource.getClass().getDeclaredField("connexionUrl").set(apiSource, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
