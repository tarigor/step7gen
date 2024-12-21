package my.project.step7gen.service.strategy;

import lombok.AllArgsConstructor;
import my.project.step7gen.model.Bn3500DataModbusTcp;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ParsingService {

  private final CardParserContext cardParserContext;

  public Bn3500DataModbusTcp parseText(String text) {
    CardParserStrategy strategy = cardParserContext.getParserStrategy(text);
    return strategy.parseTextBlock(text);
  }
}
