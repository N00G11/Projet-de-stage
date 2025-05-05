package com.app.fileintegration.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DataSource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(hidden = true)
    private Long id;
    private String lien;
    private String cle;
    private String type;

    @OneToOne(mappedBy = "dataSource")
    @JsonIgnore
    private Job job;
}
