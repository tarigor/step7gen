package my.project.step7gen.constant;

import lombok.Getter;

@Getter
public enum BnTypeData {
  BN_VE_UDT("BN_VE_TE_EXT.txt", "Radial Vibration"),
  BN_TE_UDT("BN_VE_TE_EXT.txt", "Process Variable"),
  BN_XE_UDT("BN_ZE_EXT.txt", "Thrust Position"),
  BN_ST_UDT("BN_ST.txt", "");

  private final String fileName;
  private final String type;

  BnTypeData(String fileName, String type) {
    this.fileName = fileName;
    this.type = type;
  }
}
