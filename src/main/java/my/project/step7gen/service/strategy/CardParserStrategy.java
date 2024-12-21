package my.project.step7gen.service.strategy;

import my.project.step7gen.model.Bn3500DataModbusTcp;

public interface CardParserStrategy {
  Bn3500DataModbusTcp parseTextBlock(String text);
}
