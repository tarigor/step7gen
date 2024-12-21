package my.project.step7gen.service.strategy.types;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import my.project.step7gen.constant.BnTypeData;
import my.project.step7gen.model.Bn3500DataDb;
import my.project.step7gen.service.strategy.CardParserStrategy;
import org.springframework.stereotype.Service;

@Service
public class Card3500_62Parser implements CardParserStrategy {

  @Override
  public Bn3500DataDb parseTextBlock(String text) {
    String[] channelInfo = extractChannelInfo(text);
    String tag = channelInfo[0];
    String address = extractProportionalValue(text);
    if (tag == null) {
      tag = "spare_" + address;
    }
    return new Bn3500DataDb(null, tag, BnTypeData.BN_TE_UDT.name(), address, "");
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
