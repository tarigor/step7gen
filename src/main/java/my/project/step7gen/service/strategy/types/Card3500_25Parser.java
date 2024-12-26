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
public class Card3500_25Parser implements CardParserStrategy {

  private final DbService dbService;

  @Override
  public Bn3500DataModbusTcp parseTextBlock(String text) {
    String tag = extractTagFromChannelInfo(text);
    String address = extractProportionalValue(text);

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
    return new Bn3500DataModbusTcp(null, tag, BnTypeData.BN_ST_UDT.name(), address, description);
  }

  // Helper method to extract the tag from the "Channel" line
  private String extractTagFromChannelInfo(String text) {
    String pattern = "Channel \\d+: (\\S+)\\s*";
    return extractFirstGroupFromRegex(text, pattern);
  }

  // Helper method to extract the address from "Current Proportional Values: Kph Ppl"
  private String extractProportionalValue(String text) {
    String pattern = "Current Proportional Values: Kph Ppl\\s*(\\d+)";
    return extractFirstGroupFromRegex(text, pattern);
  }

  // Generic method to extract the first group from a regex pattern
  private String extractFirstGroupFromRegex(String text, String pattern) {
    Pattern regex = Pattern.compile(pattern);
    Matcher matcher = regex.matcher(text);

    if (matcher.find()) {
      return matcher.group(1);
    }
    return null;
  }
}
