package my.project.step7gen.service.strategy.types;

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
public class Card3500_62Parser implements CardParserStrategy {
  private final DbService dbService;

  @Override
  public Bn3500DataModbusTcp parseTextBlock(String text) {
    String[] channelInfo = extractChannelInfo(text);
    String tag = channelInfo[0];
    String address = extractProportionalValue(text);
    if (tag == null) {
      tag = "spare_" + address;
    }
    String finalTag = tag;
    String description =
        dbService.getDb10().stream()
            .filter(d -> d.getTag().equals(finalTag))
            .findFirst()
            .orElseThrow()
            .getDescription();
    return new Bn3500DataModbusTcp(null, tag, BnTypeData.BN_TE_UDT.name(), address, description);
  }

  private String[] extractChannelInfo(String text) {
    String pattern = "Channel \\d+: (\\S+)\\s+: (.+?)\\s*";
    Pattern regex = Pattern.compile(pattern);
    Matcher matcher = regex.matcher(text);
    if (matcher.find()) {
      String tag = matcher.group(1); // e.g., TE1138
      String type = matcher.group(2).trim(); // e.g., Process Variable
      return new String[] {tag, type};
    }
    return new String[0];
  }

  private String extractProportionalValue(String text) {
    String pattern = "Current Proportional Values: Direct\\s*(\\d+)";
    Pattern regex = Pattern.compile(pattern);
    Matcher matcher = regex.matcher(text);
    if (matcher.find()) {
      return matcher.group(1); // e.g., 45133
    }
    return null;
  }
}
