package my.project.step7gen.repository;

import my.project.step7gen.model.DcsDataModbusTcp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DcsDbRepository extends JpaRepository<DcsDataModbusTcp, Long> {}
