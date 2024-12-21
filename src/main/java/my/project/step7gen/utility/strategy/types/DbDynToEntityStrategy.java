package my.project.step7gen.utility.strategy.types;

import my.project.step7gen.model.DbDyn;
import my.project.step7gen.utility.strategy.ExcelRowToEntityStrategy;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

@Service
public class DbDynToEntityStrategy implements ExcelRowToEntityStrategy<DbDyn> {

  @Override
  public DbDyn mapRowToEntity(Row row) {
    DbDyn dbDyn = new DbDyn();
    dbDyn.setTag(row.getCell(0).getStringCellValue());
    dbDyn.setType(row.getCell(1).getStringCellValue()); // Column 1
    dbDyn.setDescription(row.getCell(2).getStringCellValue());
    return dbDyn;
  }
}
