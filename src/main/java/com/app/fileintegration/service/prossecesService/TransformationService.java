package com.app.fileintegration.service.prossecesService;

import com.app.fileintegration.Implementation.transformation.Mappage;
import com.app.fileintegration.Interface.IDataTransformer;
import com.app.fileintegration.entity.Job;
import com.app.fileintegration.entity.Transformation;
import com.app.fileintegration.repository.JobRepository;
import com.app.fileintegration.repository.TransformationRepository;
import com.app.fileintegration.service.JobService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TransformationService {
    private final JobService jobService;
    private final TransformationRepository transformationRepository;
    private final JobRepository jobRepository;

    
    @Getter
    private String message = "";
    @Getter
    private List<Map<String, Object>> transformedData = null;
    @Getter
    private List<String> extractKeys = null;

    public List<String> extractAllKeys(List<Map<String, Object>> mapList) {
        if (mapList == null || mapList.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> uniqueKeys = new LinkedHashSet<>();
        for (Map<String, Object> map : mapList) {
            uniqueKeys.addAll(map.keySet());
        }
        return new ArrayList<>(uniqueKeys);
    }

    public List<Map<String, Object>> transform(List<Map<String, Object>> data, List<String> newKeys) {
        if (data == null || data.isEmpty()) {
            message = "Les données d'entrée sont nulles ou vides";
            return null;
        }

        if (newKeys == null || newKeys.isEmpty()) {
            message = "Les nouvelles clés sont nulles ou vides";
            return null;
        }

        try {
            List<String> oldKeys = extractAllKeys(data);
            
            // Créer et configurer la transformation
            Transformation transformation = new Transformation();
            transformation.setOldKesys(oldKeys);
            transformation.setNewKesys(newKeys);
            // Créer une nouvelle instance de Mappage
            IDataTransformer transformer = new Mappage();

            // Vérifier la transformation
                message = "Transformation réussie";

                // Sauvegarder d'abord la transformation
                transformation = transformationRepository.save(transformation);

                // Mettre à jour le Job avec la transformation sauvegardée
                Optional<Job> job = jobService.findMostRecentJob();
                if (job.isPresent()) {
                    Job currentJob = job.get();
                    currentJob.setTransformation(transformation);
                    jobRepository.save(currentJob);
                }

                transformedData = transformer.transform(data, newKeys);
                return transformedData;

        } catch (Exception e) {
            message = "Erreur lors de la transformation : " + e.getMessage();
            return null;
        }
    }
}