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
public class Bn3500ExportParserV1 {

  private final FileHelper fileHelper;
  private final Bn3500DataRepository repository;

  public List<Bn3500DataDb> createBn3500Db(String fileName) throws Exception {
    List<Bn3500DataDb> list = getBnTags(fileName);
    return repository.saveAll(list);
  }

  public List<Bn3500DataDb> getBnTags(String fileName) throws Exception {
    List<String> rows = fileHelper.readTextFile(fileName);
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
        emptyCount = emptyCount + 1;
        endIndex = i;
        if (emptyCount > 2) {
          updatedExport.addAll(rows.subList(startIndex, endIndex));
          break;
        }
      }
    }
    List<Bn3500DataDb> bn3500DataDb = new ArrayList<>();
    boolean skipTillEmptyRow = false;
    for (int i = 0; i < updatedExport.size(); i++) {

      if (!skipTillEmptyRow
          && updatedExport.get(i).contains(":")
          && updatedExport.get(i).contains("Channel")) {
        if (checkKeyphasorCard(i, updatedExport, bn3500DataDb)) {
          skipTillEmptyRow = true;
          continue;
        }
        String[] elements = updatedExport.get(i).split(":");
        bn3500DataDb.add(
            new Bn3500DataDb()
                .toBuilder()
                    .tag(
                        checkIfEmpty(
                            elements[1].trim(), updatedExport.get(i + 1).split("Direct")[1].trim()))
                    .type(defineType(elements[2].trim()))
                    .address(updatedExport.get(i + 1).split("Direct")[1].trim())
                    .build());

        skipTillEmptyRow = true;
      }
      if (updatedExport.get(i).isEmpty()) {
        skipTillEmptyRow = false;
      }
      if (updatedExport.get(i).trim().contains("ModbusRegister")) {
        break;
      }
    }
    return bn3500DataDb;
  }

  private String checkIfEmpty(String tag, String address) {
    if (tag.isEmpty()) {
      return "spare_" + address;
    }
    return tag;
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
      bn3500DataDb.add(
          new Bn3500DataDb()
              .toBuilder()
                  .tag(updatedExport.get(index + 1).split(":")[1].split("\\s+")[1].trim())
                  .address(updatedExport.get(index - 1).split("Ppl")[1].trim())
                  .type("BN_ST_UDT")
                  .build());
      return true;
    }
    return false;
  }
}
