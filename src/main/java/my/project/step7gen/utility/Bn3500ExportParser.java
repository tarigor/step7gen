package my.project.step7gen.utility;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import my.project.step7gen.model.Bn3500DataDb;
import my.project.step7gen.repository.Bn3500DataRepository;
import my.project.step7gen.service.strategy.ParsingService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Bn3500ExportParser {

  private final FileHelper fileHelper;
  private final Bn3500DataRepository repository;
  private final ParsingService parsingService;

  public List<Bn3500DataDb> createBn3500Db(String fileName) throws Exception {
    List<Bn3500DataDb> list = getBnTags(fileName);
    return repository.saveAll(list);
  }

  public List<Bn3500DataDb> getBnTags(String fileName) throws Exception {
    List<String> rows = fileHelper.readTextFile(fileName);
    List<String> updatedExport = extractRelevantRows(rows);
    return parseBn3500Data(updatedExport);
  }

  private List<String> extractRelevantRows(List<String> rows) {
    List<String> updatedExport = new ArrayList<>();
    int emptyCount = 0;
    int startIndex = 0;
    int endIndex;

    for (int i = 0; i < rows.size(); i++) {
      if (!rows.get(i).isEmpty()) {
        emptyCount = 0;
      }
      if (rows.get(i).contains("Configurable Modbus Registers")) {
        startIndex = i;
      }
      if (rows.get(i).isEmpty()) {
        emptyCount++;
        endIndex = i;
        if (emptyCount > 2) {
          updatedExport.addAll(rows.subList(startIndex, endIndex));
          break;
        }
      }
    }
    return updatedExport;
  }

  private List<Bn3500DataDb> parseBn3500Data(List<String> updatedExport) {
    StringBuilder stringBuilder = new StringBuilder();
    for (String line : updatedExport) {
      stringBuilder.append(line).append("\n");
    }
    String[] textBlocks = stringBuilder.toString().split("\n\n");
    List<Bn3500DataDb> parsedData = new ArrayList<>();
    for (String textBlock : textBlocks) {
      try {
        parsedData.add(parsingService.parseText(textBlock));
      } catch (Exception ex) {
        System.out.println("No any type card found");
        continue;
      }
    }
    return parsedData;
  }
}
