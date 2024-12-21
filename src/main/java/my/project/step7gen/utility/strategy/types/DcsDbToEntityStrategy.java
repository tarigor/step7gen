package my.project.step7gen.utility.strategy.types;

import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_NULL_AND_BLANK;

import my.project.step7gen.model.DcsDataModbusTcp;
import my.project.step7gen.utility.strategy.ExcelRowToEntityStrategy;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

@Service
public class DcsDbToEntityStrategy implements ExcelRowToEntityStrategy<DcsDataModbusTcp> {

  @Override
  public DcsDataModbusTcp mapRowToEntity(Row row) {
    DcsDataModbusTcp dcsDataModbusTcp = new DcsDataModbusTcp();
    dcsDataModbusTcp.setTag(row.getCell(0).getStringCellValue());
    dcsDataModbusTcp.setType(row.getCell(1).getStringCellValue());
    dcsDataModbusTcp.setDescription(
        row.getCell(3, RETURN_NULL_AND_BLANK) == null ? "" : row.getCell(3).toString());
    return dcsDataModbusTcp;
  }
}
