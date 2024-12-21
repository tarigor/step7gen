package my.project.step7gen.repository;

import my.project.step7gen.model.DB_AnalogV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Db10Repository extends JpaRepository<DB_AnalogV, Long> {}
