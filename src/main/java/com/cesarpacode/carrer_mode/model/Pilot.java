package com.cesarpacode.carrer_mode.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Pilot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Ej: "Cesar Palacios"

    @Column
    private String country;

    @Column
    private Long experience;

    @Column
    private String licence;

    @Column
    private String image;

    @OneToOne
    private Garage garage;
}