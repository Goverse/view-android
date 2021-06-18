package com.goverse.customview.pickview;

public class PickItemBean {

    private int id;
    private String preferenceName;
    private int preferenceWeight;

    public PickItemBean(int id, String preferenceName, int preferenceWeight) {
        this.id = id;
        this.preferenceName = preferenceName;
        this.preferenceWeight = preferenceWeight;
    }

    public int getId() {
        return id;
    }

    public int getPreferenceWeight() {
        return preferenceWeight;
    }

    public String getPreferenceName() {
        return preferenceName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPreferenceName(String preferenceName) {
        this.preferenceName = preferenceName;
    }

    public void setPreferenceWeight(int preferenceWeight) {
        this.preferenceWeight = preferenceWeight;
    }

    @Override
    public String toString() {
        return "PickItemBean{" +
                "id=" + id +
                ", preferenceName='" + preferenceName + '\'' +
                ", preferenceWeight=" + preferenceWeight +
                '}';
    }
}
