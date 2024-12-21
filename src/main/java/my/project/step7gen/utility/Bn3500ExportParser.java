package my.project.step7gen.utility;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import my.project.step7gen.constant.BnTypeData;
import my.project.step7gen.model.Bn3500DataDb;
import my.project.step7gen.repository.Bn3500DataRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Bn3500ExportParser {

  private final FileHelper fileHelper;
  private final Bn3500DataRepository repository;

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
    List<Bn3500DataDb> bn3500DataDb = new ArrayList<>();
    boolean skipTillEmptyRow = false;

    for (int i = 0; i < updatedExport.size(); i++) {
      String currentRow = updatedExport.get(i);

      if (!skipTillEmptyRow && isChannelRow(currentRow)) {
        if (checkKeyphasorCard(i, updatedExport, bn3500DataDb)) {
          skipTillEmptyRow = true;
          continue;
        }
        bn3500DataDb.add(parseChannelRow(i, updatedExport));
        skipTillEmptyRow = true;
      }

      if (currentRow.isEmpty()) {
        skipTillEmptyRow = false;
      }

      if (currentRow.trim().contains("ModbusRegister")) {
        break;
      }
    }
    return bn3500DataDb;
  }

  private boolean isChannelRow(String row) {
    return row.contains(":") && row.contains("Channel");
  }

  private Bn3500DataDb parseChannelRow(int index, List<String> updatedExport) {
    String[] elements = updatedExport.get(index).split(":");
    String tag =
        checkIfEmpty(elements[1].trim(), updatedExport.get(index + 1).split("Direct")[1].trim());
    String type = defineType(elements[2].trim());
    String address = updatedExport.get(index + 1).split("Direct")[1].trim();

    return new Bn3500DataDb().toBuilder().tag(tag).type(type).address(address).build();
  }

  private String checkIfEmpty(String tag, String address) {
    return tag.isEmpty() ? "spare_" + address : tag;
  }

  private String defineType(String type) {
    for (BnTypeData bnTypeData : BnTypeData.values()) {
      if (bnTypeData.getType().equalsIgnoreCase(type)) {
        return bnTypeData.name();
      }
    }
    return "";
  }

  private boolean checkKeyphasorCard(
      int index, List<String> updatedExport, List<Bn3500DataDb> bn3500DataDb) {
    if (updatedExport.get(index).contains("3500/25")) {
      bn3500DataDb.add(parseKeyphasorCard(index, updatedExport));
      return true;
    }
    return false;
  }

  private Bn3500DataDb parseKeyphasorCard(int index, List<String> updatedExport) {
    String tag = updatedExport.get(index + 1).split(":")[1].split("\\s+")[1].trim();
    String address = updatedExport.get(index - 1).split("Ppl")[1].trim();
    return new Bn3500DataDb().toBuilder().tag(tag).address(address).type("BN_ST_UDT").build();
  }
}
