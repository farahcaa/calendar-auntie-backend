package com.calendar_auntie.model.repositories;

import com.calendar_auntie.model.entities.Config;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Config, String> {
}
