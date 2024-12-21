package my.project.step7gen.utility.strategy.types;

import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_NULL_AND_BLANK;

import my.project.step7gen.model.SymbolTable;
import my.project.step7gen.utility.strategy.ExcelRowToEntityStrategy;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

@Service
public class SymbolTableToEntityStrategy implements ExcelRowToEntityStrategy<SymbolTable> {

  @Override
  public SymbolTable mapRowToEntity(Row row) {
    SymbolTable symbolTable = new SymbolTable();
    symbolTable.setTag(row.getCell(0).getStringCellValue());
    symbolTable.setAddress(row.getCell(1).getStringCellValue());
    symbolTable.setType(row.getCell(2).getStringCellValue());
    symbolTable.setDescription(
        row.getCell(3, RETURN_NULL_AND_BLANK) == null ? "" : row.getCell(3).toString());
    return symbolTable;
  }
}
