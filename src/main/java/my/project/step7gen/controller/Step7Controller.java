package my.project.step7gen.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import my.project.step7gen.model.Bn3500DataModbusTcp;
import my.project.step7gen.model.DB_AnalogV;
import my.project.step7gen.service.DbService;
import my.project.step7gen.service.FunctionGenerator;
import my.project.step7gen.utility.Bn3500ExportParser;
import my.project.step7gen.utility.ParseExcelFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class Step7Controller {

  private final FunctionGenerator functionGenerator;
  private final Bn3500ExportParser bn3500ExportParser;
  private final ParseExcelFile parseExcelFile;
  private final DbService dbService;

  @GetMapping("/parse-bn3500-export/{fileName}")
  public List<Bn3500DataModbusTcp> parseBn3500Export(@PathVariable String fileName)
      throws Exception {
    List<DB_AnalogV> DBAnalogV_C3101 = dbService.getDb10();
    return bn3500ExportParser.createBn3500Db(fileName);
  }

  @GetMapping("/generate-fc155")
  public List<String> generateFc155() throws Exception {
    return functionGenerator.fc155CodeGenerator();
  }

  @GetMapping("/generate-fc200")
  public List<String> generateFc200() throws Exception {
    return functionGenerator.fc200CodeGenerate();
  }
}
