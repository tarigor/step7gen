package my.project.step7gen.repository;

import my.project.step7gen.model.Bn3500DataModbusTcp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Bn3500DataRepository extends JpaRepository<Bn3500DataModbusTcp, Long> {}
