package com.app.fileintegration.controller;

import com.app.fileintegration.entity.DataSource;
import com.app.fileintegration.entity.DataTarget;
import com.app.fileintegration.service.prossecesService.ExtractionService;
import com.app.fileintegration.service.prossecesService.LoadService;
import com.app.fileintegration.service.prossecesService.TransformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prosseces")
@RequiredArgsConstructor
public class ProssecesController {

    private final ExtractionService extractionService;
    private final TransformationService transformationService;
    private final LoadService loadService;
    private List<Map<String, Object>> data;

    // Suppression des champs data et endData pour éviter les états partagés entre requêtes
    // Ces données seront maintenant passées directement entre les services

    @PostMapping("/datasource")
    public ResponseEntity<?> datasource(@RequestBody DataSource dataSource) {
        try {
            // Validation des entrées
            if (dataSource == null) {
                return ResponseEntity.badRequest().body("La source de données ne peut pas être nulle");
            }

            // Extraction des données
            List<Map<String, Object>> extractedData = extractionService.extract(dataSource);

            if (extractedData == null || extractedData.isEmpty()) {
                return ResponseEntity.badRequest().body(extractionService.getMessage() != null ?
                        extractionService.getMessage() : "Aucune donnée n'a pu être extraite");
            }

            data = extractedData;
            return ResponseEntity.ok(extractionService.getExtractKeys());

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erreur lors de l'extraction des données: " + e.getMessage());
        }
    }

    @GetMapping("/oldKeys")
    public ResponseEntity<List<String>> getOldKeys() {
        try {
            List<String> keys = extractionService.getExtractKeys();

            if (keys == null || keys.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(keys);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/transformation")
    public ResponseEntity<?> transform(@RequestBody List<String> newKeys) {

        try {
            // Validation des entrées
            if (newKeys == null || newKeys.isEmpty()) {
                return ResponseEntity.badRequest().body("La liste des nouvelles clés ne peut pas être vide");
            }


            if (data == null || data.isEmpty()) {
                return ResponseEntity.badRequest().body("Aucune donnée à transformer. Effectuez d'abord une extraction.");
            }

            // Transformation des données
            data = transformationService.transform(data, newKeys);

            if (data == null || data.isEmpty()) {
                return ResponseEntity.badRequest().body(transformationService.getMessage() != null ?
                        transformationService.getMessage() : "La transformation a échoué");
            }

            return ResponseEntity.ok()
                    .header("X-Transformed-Records", String.valueOf(data.size()))
                    .body(transformationService.getMessage());

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erreur lors de la transformation: " + e.getMessage());
        }
    }

    @PostMapping("/datatarget")
    public ResponseEntity<?> load(@RequestBody DataTarget dataTarget) {
        try {
            // Validation des entrées
            if (dataTarget == null) {
                return ResponseEntity.badRequest().body("La cible de données ne peut pas être nulle");
            }

            if (data == null || data.isEmpty()) {
                return ResponseEntity.badRequest().body("Aucune donnée à charger. Effectuez d'abord une transformation.");
            }

            // Chargement des données
            boolean loadSuccess = loadService.load(dataTarget, data);

            if (!loadSuccess) {
                return ResponseEntity.badRequest().body(loadService.getMessage() != null ?
                        loadService.getMessage() : "Le chargement des données a échoué");
            }

            return ResponseEntity.ok()
                    .header("X-Loaded-Records", String.valueOf(data.size()))
                    .body(loadService.getMessage());

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erreur lors du chargement: " + e.getMessage());
        }
    }
}