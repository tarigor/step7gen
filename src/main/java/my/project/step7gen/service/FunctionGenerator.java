package my.project.step7gen.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import my.project.step7gen.constant.BnTypeData;
import my.project.step7gen.model.Bn3500DataModbusTcp;
import my.project.step7gen.model.DB_AnalogV;
import my.project.step7gen.model.DbDyn;
import my.project.step7gen.model.DcsDataModbusTcp;
import my.project.step7gen.model.SymbolTable;
import my.project.step7gen.repository.Bn3500DataRepository;
import my.project.step7gen.utility.FileHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FunctionGenerator {

  private final FileHelper fileHelper;
  private final Bn3500DataRepository bn3500DataRepository;
  private final DbService dbService;

  public List<String> fc155CodeGenerator() throws Exception {
    List<String> modifiedList = new ArrayList<>();
    List<Bn3500DataModbusTcp> bn3500DataModbusTcpList = bn3500DataRepository.findAll();
    for (Bn3500DataModbusTcp bn3500DataModbusTcp : bn3500DataModbusTcpList) {
      if (!bn3500DataModbusTcp.getTag().contains("spare")) {
        List<String> lines =
            fileHelper.readTextFile(
                BnTypeData.valueOf(bn3500DataModbusTcp.getType()).getFileName());
        for (String line : lines) {
          line = line.replace("{TAG}", bn3500DataModbusTcp.getTag());
          modifiedList.add(line);
        }
      }
    }
    List<String> fc155Lines = new ArrayList<>();
    List<String> lines = fileHelper.readTextFile("FC155_header.awl");
    fc155Lines.addAll(lines);
    fc155Lines.addAll(modifiedList);
    fc155Lines.add("END_FUNCTION");
    fileHelper.writeTextFile(fc155Lines, "FC155.awl");
    return fc155Lines;
  }

  public List<String> fc200CodeGenerate() throws Exception {
    List<SymbolTable> symbolTable = dbService.getSymbolTable();
    List<DB_AnalogV> dbAnalogV = dbService.getDb10();
    List<DbDyn> dbDyn = dbService.getDbDyn();
    List<DcsDataModbusTcp> dscDataDb = dbService.getDcsDb();

    String code =
        fileHelper.readTextFile("DCS/store_digital.txt").stream().collect(Collectors.joining("\n"));
    String newNetwork =
        new StringBuilder()
            .append("\n")
            .append("NETWORK")
            .append("\n")
            .append("TITLE =")
            .append("\n")
            .toString();
    StringBuilder fcStringBuilder = new StringBuilder();
    fcStringBuilder.append(
        fileHelper.readTextFile("DCS/FC200_Header.txt").stream().collect(Collectors.joining("\n")));
    int lineCounter = 0;
    for (DcsDataModbusTcp record : dscDataDb) {
      if (record.getType().equals("BOOL")) {
        // copy alarms and trip
        String modifiedCode;
        String tag1 = findAlarmTag(record.getTag(), symbolTable, dbDyn);
        modifiedCode =
            code.replace("{TAG1}", tag1)
                .replace("{TAG2}", "DCS_DATA_MODBUS_TCP." + record.getTag());
        if (lineCounter == 8) {
          lineCounter = 0;
        }
        if (lineCounter == 0) {
          fcStringBuilder.append(newNetwork).append("\n");
        }
        fcStringBuilder.append(modifiedCode).append("\n");
        lineCounter = lineCounter + 1;
      } else if (record.getType().equals("INT")) {
        System.out.println("");
      }
    }
    return null;
  }

  private String findAlarmTag(String tag, List<SymbolTable> symbolTable, List<DbDyn> dbDyn) {
    for (DbDyn dbdyn : dbDyn) {
      if (tag.equals(dbdyn.getDescription().split(" ")[0].trim())) {
        return "Db_Dyn." + dbdyn.getTag();
      }
    }
    for (SymbolTable st : symbolTable) {
      if (tag.equals(st.getTag())) {
        return st.getAddress();
      }
    }
    System.out.println("no link found for -> " + tag);
    return "M40.0";
  }
}
