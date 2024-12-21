package my.project.step7gen.utility.strategy;

import org.apache.poi.ss.usermodel.Row;

public interface ExcelRowToEntityStrategy<T> {
  T mapRowToEntity(Row row);
}
