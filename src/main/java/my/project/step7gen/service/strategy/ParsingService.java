package my.project.step7gen.service.strategy;

import lombok.AllArgsConstructor;
import my.project.step7gen.model.Bn3500DataDb;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ParsingService {

  private final CardParserContext cardParserContext;

  public Bn3500DataDb parseText(String text) {
    CardParserStrategy strategy = cardParserContext.getParserStrategy(text);
    return strategy.parseTextBlock(text);
  }
}
