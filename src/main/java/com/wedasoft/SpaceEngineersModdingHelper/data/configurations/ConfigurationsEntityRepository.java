package com.wedasoft.SpaceEngineersModdingHelper.data.configurations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationsEntityRepository extends JpaRepository<ConfigurationsEntity, Long> {
}
