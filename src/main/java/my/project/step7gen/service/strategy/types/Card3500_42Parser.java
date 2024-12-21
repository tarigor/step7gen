package my.project.step7gen.service.strategy.types;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import my.project.step7gen.model.Bn3500DataModbusTcp;
import my.project.step7gen.service.DbService;
import my.project.step7gen.service.strategy.CardParserStrategy;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Card3500_42Parser implements CardParserStrategy {
  private final DbService dbService;
  private static final Pattern CHANNEL_INFO_PATTERN =
      Pattern.compile("Channel \\d+: (\\S+)\\s+: (.+?)\\s*");
  private static final Pattern PROPORTIONAL_VALUE_PATTERN =
      Pattern.compile("Current Proportional Values: Direct\\s*(\\d+)");

  @Override
  public Bn3500DataModbusTcp parseTextBlock(String text) {
    // Extract channel information
    String[] channelInfo =
        extractChannelInfo(text)
            .orElseThrow(
                () -> new IllegalArgumentException("Invalid text format: Channel info not found"));
    String tag = channelInfo[0]; // e.g., VE0055
    String type = channelInfo[1]; // e.g., Acceleration 2

    // Extract proportional value
    String address =
        extractProportionalValue(text)
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Invalid text format: Proportional value not found"));
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
    return new Bn3500DataModbusTcp(null, tag, type, address, description);
  }

  // Helper method to extract tag and type from "Channel" line
  private Optional<String[]> extractChannelInfo(String text) {
    Matcher matcher = CHANNEL_INFO_PATTERN.matcher(text);
    if (matcher.find()) {
      String tag = matcher.group(1); // e.g., VE0055
      String type = matcher.group(2).trim(); // e.g., Acceleration 2
      return Optional.of(new String[] {tag, type});
    }
    return Optional.empty();
  }

  // Helper method to extract address from "Current Proportional Values: Direct"
  private Optional<String> extractProportionalValue(String text) {
    Matcher matcher = PROPORTIONAL_VALUE_PATTERN.matcher(text);
    if (matcher.find()) {
      return Optional.of(matcher.group(1)); // e.g., 46179
    }
    return Optional.empty();
  }
}
