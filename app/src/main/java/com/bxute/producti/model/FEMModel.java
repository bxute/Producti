/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti.model;

public class FEMModel {
  private int focus;
  private int energy;
  private int motivation;
  private String remarks;
  private int dataID;
  private String createdAt;
  private String modifiedAt;

  public FEMModel() {
  }

  public FEMModel(int focus, int energy, int motivation, String remarks) {
    this.focus = focus;
    this.energy = energy;
    this.motivation = motivation;
    this.remarks = remarks;
  }

  public int getDataID() {
    return dataID;
  }

  public void setDataID(int dataID) {
    this.dataID = dataID;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public int getFocus() {
    return focus;
  }

  public void setFocus(int focus) {
    this.focus = focus;
  }

  public int getEnergy() {
    return energy;
  }

  public void setEnergy(int energy) {
    this.energy = energy;
  }

  public int getMotivation() {
    return motivation;
  }

  public void setMotivation(int motivation) {
    this.motivation = motivation;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getModifiedAt() {
    return modifiedAt;
  }

  public void setModifiedAt(String modified_at) {
    this.modifiedAt = modified_at;
  }
}
