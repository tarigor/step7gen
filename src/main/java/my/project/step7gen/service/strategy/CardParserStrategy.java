package my.project.step7gen.service.strategy;

import my.project.step7gen.model.Bn3500DataDb;

public interface CardParserStrategy {
  Bn3500DataDb parseTextBlock(String text);
}
