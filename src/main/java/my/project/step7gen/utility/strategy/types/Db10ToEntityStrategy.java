package my.project.step7gen.utility.strategy.types;

import my.project.step7gen.model.DB_AnalogV;
import my.project.step7gen.utility.strategy.ExcelRowToEntityStrategy;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

@Service
public class Db10ToEntityStrategy implements ExcelRowToEntityStrategy<DB_AnalogV> {

  @Override
  public DB_AnalogV mapRowToEntity(Row row) {
    DB_AnalogV DBAnalogV = new DB_AnalogV();
    DBAnalogV.setTag(row.getCell(0).getStringCellValue()); // Column 1
    DBAnalogV.setDescription(row.getCell(1).getStringCellValue());
    return DBAnalogV;
  }
}
