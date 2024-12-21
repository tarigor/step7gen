package my.project.step7gen;

import jakarta.annotation.PostConstruct;
import my.project.step7gen.model.DB_AnalogV;
import my.project.step7gen.model.DbDyn;
import my.project.step7gen.model.DcsDataModbusTcp;
import my.project.step7gen.model.SymbolTable;
import my.project.step7gen.service.DbService;
import my.project.step7gen.utility.ParseExcelFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Step7GenApplication {

  @Autowired private ParseExcelFile parseExcelFile;
  @Autowired private DbService dbService;

  public static void main(String[] args) {
    SpringApplication.run(Step7GenApplication.class, args);
  }

  @PostConstruct
  void init() {
    dbService.saveDb10(
        parseExcelFile.parseExcelFile("/DB10/C3101_DB_AnalogV.xlsx", DB_AnalogV.class));
    dbService.saveSymbolTable(
        parseExcelFile.parseExcelFile("/DB10/C3101_SymbolTable.xlsx", SymbolTable.class));
    dbService.saveDcsDb(
        parseExcelFile.parseExcelFile(
            "/DB10/C3101_DCS_DATA_MODBUS_TCP.xlsx", DcsDataModbusTcp.class));
    dbService.saveDbDyn(parseExcelFile.parseExcelFile("/DB10/C3101_DB_Dyn.xlsx", DbDyn.class));
  }
}
