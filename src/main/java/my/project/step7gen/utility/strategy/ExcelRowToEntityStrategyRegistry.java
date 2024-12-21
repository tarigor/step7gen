package my.project.step7gen.utility.strategy;

import java.util.HashMap;
import java.util.Map;
import my.project.step7gen.model.DB_AnalogV;
import my.project.step7gen.model.DbDyn;
import my.project.step7gen.model.DcsDataModbusTcp;
import my.project.step7gen.model.SymbolTable;
import my.project.step7gen.utility.strategy.types.Db10ToEntityStrategy;
import my.project.step7gen.utility.strategy.types.DbDynToEntityStrategy;
import my.project.step7gen.utility.strategy.types.DcsDbToEntityStrategy;
import my.project.step7gen.utility.strategy.types.SymbolTableToEntityStrategy;

public class ExcelRowToEntityStrategyRegistry {

  private static final Map<Class<?>, ExcelRowToEntityStrategy<?>> strategyMap = new HashMap<>();

  static {
    // Register the strategies for different entity types
    strategyMap.put(DB_AnalogV.class, new Db10ToEntityStrategy());
    strategyMap.put(SymbolTable.class, new SymbolTableToEntityStrategy());
    strategyMap.put(DcsDataModbusTcp.class, new DcsDbToEntityStrategy());
    strategyMap.put(DbDyn.class, new DbDynToEntityStrategy());
  }

  public static <T> ExcelRowToEntityStrategy<T> getStrategy(Class<T> entityClass) {
    return (ExcelRowToEntityStrategy<T>) strategyMap.get(entityClass);
  }
}
