package my.project.step7gen.service.strategy;

import lombok.AllArgsConstructor;
import my.project.step7gen.service.strategy.types.Card3500_25Parser;
import my.project.step7gen.service.strategy.types.Card3500_40Parser;
import my.project.step7gen.service.strategy.types.Card3500_42Parser;
import my.project.step7gen.service.strategy.types.Card3500_62Parser;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CardParserContext {
  private final Card3500_42Parser card3500_42Parser;
  private final Card3500_25Parser card3500_25Parser;
  private final Card3500_40Parser card3500_40Parser;
  private final Card3500_62Parser card3500_62Parser;

  public CardParserStrategy getParserStrategy(String text) {
    if (text.contains("3500/42")) {
      return card3500_42Parser;
    } else if (text.contains("3500/25")) {
      return card3500_25Parser;
    } else if (text.contains("3500/40")) {
      return card3500_40Parser;
    } else if (text.contains("3500/62")) {
      return card3500_62Parser;
    }
    throw new IllegalArgumentException("Unknown card type in text.");
  }
}
