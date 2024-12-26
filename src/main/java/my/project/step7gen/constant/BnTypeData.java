package my.project.step7gen.constant;

import lombok.Getter;

@Getter
public enum BnTypeData {
  BN_VE_UDT("BN_VE_EXT.txt", "Radial Vibration"),
  BN_TE_UDT("BN_TE_EXT.txt", "Process Variable"),
  BN_XE_UDT("BN_ZE_EXT.txt", "Thrust Position"),
  BN_ACC_UDT("BN_ACC_EXT.txt", "Acceleration 2"),
  BN_ST_UDT("BN_SE.txt", "");

  private final String fileName;
  private final String type;

  BnTypeData(String fileName, String type) {
    this.fileName = fileName;
    this.type = type;
  }
}
