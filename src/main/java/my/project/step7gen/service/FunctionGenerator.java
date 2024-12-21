package my.project.step7gen.service;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import my.project.step7gen.constant.BnTypeData;
import my.project.step7gen.model.Bn3500DataDb;
import my.project.step7gen.repository.Bn3500DataRepository;
import my.project.step7gen.utility.FileHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FunctionGenerator {

  private final FileHelper fileHelper;
  private final Bn3500DataRepository bn3500DataRepository;

  public List<String> fc155CodeGenerator() throws Exception {
    List<String> modifiedList = new ArrayList<>();
    List<Bn3500DataDb> bn3500DataDbList = bn3500DataRepository.findAll();
    for (Bn3500DataDb bn3500DataDb : bn3500DataDbList) {
      if (!bn3500DataDb.getTag().contains("spare")) {
        List<String> lines =
            fileHelper.readTextFile(BnTypeData.valueOf(bn3500DataDb.getType()).getFileName());
        for (String line : lines) {
          line = line.replace("{TAG}", bn3500DataDb.getTag());
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
}
