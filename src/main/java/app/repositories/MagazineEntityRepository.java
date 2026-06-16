package app.repositories;

import app.entities.MagazineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MagazineEntityRepository extends JpaRepository<MagazineEntity, Long> {
}