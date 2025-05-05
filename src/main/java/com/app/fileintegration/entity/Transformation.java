package com.app.fileintegration.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Transformation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(hidden = true)
    private Long id;
    private String type;

    @OneToOne(mappedBy = "transformation")
    @JsonIgnore
    private Job job;
}
