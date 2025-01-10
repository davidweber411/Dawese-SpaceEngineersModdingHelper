package com.wedasoft.SpaceEngineersModdingHelper.data.configurations;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ConfigurationsEntity")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "pathToModsWorkspace")
    private String pathToModsWorkspace;

    @Column(name = "pathToAppdataModsDirectory")
    private String pathToAppdataModsDirectory;

}
