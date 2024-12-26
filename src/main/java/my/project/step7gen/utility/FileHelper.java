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
    } catch (Exception ex) {
      System.out.printf(
          "Exception occurred %s during getting information about file -> %s%n", ex, fileName);
    }

    return lines;
  }

  public void writeTextFile(List<String> lines, String fileName) throws Exception {
    String projectRoot = Paths.get("").toAbsolutePath().toString();
    // Determine the output path for the file
    Path outputPath =
        Paths.get(projectRoot + "\\step7-gen\\src\\main\\resources\\output\\", fileName);

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

    //        Path path = Paths.get("resources/output/" + fileName); // Path to the file
    //
    //        try {
    //            // Create the parent directories if they do not exist
    //            Files.createDirectories(path.getParent());
    //
    //            // Write lines to the file, appending if the file already exists
    //            Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    //
    //            System.out.println("File written successfully.");
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
  }
}
