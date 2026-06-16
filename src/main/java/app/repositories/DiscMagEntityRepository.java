package app.repositories;

import app.entities.DiscMagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscMagEntityRepository extends JpaRepository<DiscMagEntity, Long> {
}