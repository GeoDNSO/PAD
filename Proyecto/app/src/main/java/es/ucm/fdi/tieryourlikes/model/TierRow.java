package es.ucm.fdi.tieryourlikes.model;

import java.util.List;

public class TierRow {

    String tierName;
    List<String> images;

    public TierRow(String tierName, List<String> images) {
        this.tierName = tierName;
        this.images = images;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
