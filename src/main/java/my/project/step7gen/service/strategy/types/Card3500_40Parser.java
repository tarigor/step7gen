package my.project.step7gen.service.strategy.types;

import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import my.project.step7gen.constant.BnTypeData;
import my.project.step7gen.model.Bn3500DataModbusTcp;
import my.project.step7gen.service.DbService;
import my.project.step7gen.service.strategy.CardParserStrategy;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Card3500_40Parser implements CardParserStrategy {
  private final DbService dbService;

  @Override
  public Bn3500DataModbusTcp parseTextBlock(String text) {
    String tag = extractTag(text);
    String address = extractProportionalValue(text);
    String type = determineType(text);
    if (tag == null) {
      tag = "spare_" + address;
    }
    String finalTag = tag;
    String description = "";
    if (!tag.contains("spare")) {
      description =
          dbService.getDb10().stream()
              .filter(d -> d.getTag().equals(finalTag))
              .findFirst()
              .orElseThrow(() -> new NoSuchElementException(finalTag + " was not found in DB10"))
              .getDescription();
    } else {
      description = tag;
    }
    return new Bn3500DataModbusTcp(null, tag, type, address, description);
  }

  // Extracts the tag from the "Channel" line
  private String extractTag(String text) {
    String pattern = "Channel \\d+: (\\S+)\\s+: (.+?)\\s*";
    Pattern regex = Pattern.compile(pattern);
    Matcher matcher = regex.matcher(text);

    if (matcher.find()) {
      return matcher.group(1); // e.g., VE0051Y
    }
    return null;
  }

  // Extracts the address from the "Current Proportional Values: Direct" line
  private String extractProportionalValue(String text) {
    String pattern = "Current Proportional Values: Direct\\s*(\\d+)";
    Pattern regex = Pattern.compile(pattern);
    Matcher matcher = regex.matcher(text);

    if (matcher.find()) {
      return matcher.group(1); // e.g., 46150
    }
    return null;
  }

  // Determines the type based on the content of the text
  private String determineType(String text) {
    if (text.contains("Radial Vibration")) {
      return BnTypeData.BN_VE_UDT.name();
    } else if (text.contains("Thrust Position")) {
      return BnTypeData.BN_XE_UDT.name();
    }
    return "";
  }
}
