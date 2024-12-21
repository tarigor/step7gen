package my.project.step7gen.repository;

import my.project.step7gen.model.SymbolTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SymbolTableRepository extends JpaRepository<SymbolTable, Long> {}
