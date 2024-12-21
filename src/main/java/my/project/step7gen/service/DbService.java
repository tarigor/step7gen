package my.project.step7gen.service;

import java.util.List;
import lombok.AllArgsConstructor;
import my.project.step7gen.model.DB_AnalogV;
import my.project.step7gen.model.DbDyn;
import my.project.step7gen.model.DcsDataModbusTcp;
import my.project.step7gen.model.SymbolTable;
import my.project.step7gen.repository.Db10Repository;
import my.project.step7gen.repository.DbDynRepository;
import my.project.step7gen.repository.DcsDbRepository;
import my.project.step7gen.repository.SymbolTableRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DbService {
  private final Db10Repository db10Repository;
  private final SymbolTableRepository symbolTableRepository;
  private final DcsDbRepository dcsDbRepository;
  private final DbDynRepository dbDynRepository;

  public List<DB_AnalogV> getDb10() {
    return db10Repository.findAll();
  }

  public List<DB_AnalogV> saveDb10(List<DB_AnalogV> DBAnalogV) {
    return db10Repository.saveAll(DBAnalogV);
  }

  public List<SymbolTable> getSymbolTable() {
    return symbolTableRepository.findAll();
  }

  public List<SymbolTable> saveSymbolTable(List<SymbolTable> symbolTable) {
    return symbolTableRepository.saveAll(symbolTable);
  }

  public List<DcsDataModbusTcp> saveDcsDb(List<DcsDataModbusTcp> dcsDataModbusTcps) {
    return dcsDbRepository.saveAll(dcsDataModbusTcps);
  }

  public List<DcsDataModbusTcp> getDcsDb() {
    return dcsDbRepository.findAll();
  }

  public List<DbDyn> saveDbDyn(List<DbDyn> dbDyns) {
    return dbDynRepository.saveAll(dbDyns);
  }

  public List<DbDyn> getDbDyn() {
    return dbDynRepository.findAll();
  }
}
