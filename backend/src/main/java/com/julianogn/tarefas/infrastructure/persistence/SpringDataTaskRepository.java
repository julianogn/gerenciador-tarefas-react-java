package com.julianogn.tarefas.infrastructure.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataTaskRepository extends JpaRepository<TaskJpaEntity, UUID> {
}
