package com.demo.cleanspringboot.infrastructure.adapter.out.persistence.repository;

import com.demo.cleanspringboot.domain.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

}
