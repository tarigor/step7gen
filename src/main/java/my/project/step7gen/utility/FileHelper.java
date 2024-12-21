package my.project.step7gen.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class FileHelper {

  public List<String> readTextFile(String fileName) throws Exception {
    List<String> lines = new ArrayList<>();

    try (BufferedReader reader =
        new BufferedReader(
            new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/input/" + fileName)),
                StandardCharsets.UTF_8))) {
      String line;
      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }
    }

    return lines;
  }

  public void writeTextFile(List<String> lines, String fileName) throws Exception {
    // Determine the output path for the file
    Path outputPath = Paths.get("src/main/resources/output", fileName);

    // Ensure the output directory exists
    File outputDir = outputPath.getParent().toFile();
    if (!outputDir.exists()) {
      outputDir.mkdirs();
    }

    // Write the lines to the file
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath.toFile()))) {
      for (String line : lines) {
        writer.write(line);
        writer.newLine(); // Add a newline after each line
      }
    }
  }
}
