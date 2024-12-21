package my.project.step7gen.utility;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import my.project.step7gen.utility.strategy.ExcelRowToEntityStrategy;
import my.project.step7gen.utility.strategy.ExcelRowToEntityStrategyRegistry;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ParseExcelFile {

  public <T> List<T> parseExcelFile(String fileName, Class<T> entityClass) {
    List<T> entities = new ArrayList<>();

    // Get the strategy automatically based on the entity class
    ExcelRowToEntityStrategy<T> strategy =
        ExcelRowToEntityStrategyRegistry.getStrategy(entityClass);

    if (strategy == null) {
      throw new IllegalArgumentException("No strategy found for class: " + entityClass.getName());
    }

    try (InputStream inputStream = ParseExcelFile.class.getResourceAsStream("/input" + fileName)) {
      try (Workbook workbook = new XSSFWorkbook(inputStream)) {

        Sheet sheet = workbook.getSheetAt(0); // Reading the first sheet.
        for (int rowIndex = 0;
            rowIndex <= sheet.getLastRowNum();
            rowIndex++) { // Skipping header row.
          Row row = sheet.getRow(rowIndex);
          if (row == null) continue;

          // Use the strategy to map the row to the entity
          T entity = strategy.mapRowToEntity(row);
          entities.add(entity);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return entities;
  }
}
