package my.project.step7gen.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import my.project.step7gen.model.Bn3500DataDb;
import my.project.step7gen.service.FunctionGenerator;
import my.project.step7gen.utility.Bn3500ExportParser;
import my.project.step7gen.utility.Bn3500ExportParserV1;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class Step7Controller {

  private final FunctionGenerator functionGenerator;
  private final Bn3500ExportParserV1 bn3500ExportParserV1;
  private final Bn3500ExportParser bn3500ExportParser;

  @GetMapping("/parse-bn3500-export/{fileName}")
  public List<Bn3500DataDb> parseBn3500Export(@PathVariable String fileName) throws Exception {
    return bn3500ExportParser.createBn3500Db(fileName);
  }

  @GetMapping("/generate-fc155")
  public List<String> generateFc155() throws Exception {
    return functionGenerator.fc155CodeGenerator();
  }
}
