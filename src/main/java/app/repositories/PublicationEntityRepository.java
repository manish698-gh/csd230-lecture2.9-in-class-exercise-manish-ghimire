package app.repositories;

import app.entities.PublicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicationEntityRepository extends JpaRepository<PublicationEntity, Long> {
}