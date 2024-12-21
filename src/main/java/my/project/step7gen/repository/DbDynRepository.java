package my.project.step7gen.repository;

import my.project.step7gen.model.DbDyn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DbDynRepository extends JpaRepository<DbDyn, Long> {}
