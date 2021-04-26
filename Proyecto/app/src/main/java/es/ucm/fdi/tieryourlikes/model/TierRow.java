package es.ucm.fdi.tieryourlikes.model;

import java.util.List;

public class TierRow {

    String row_name;
    List<String> image_urls;

    public TierRow(String tierName, List<String> images) {
        this.row_name = tierName;
        this.image_urls = images;
    }

    public String getRowName() {
        return row_name;
    }

    public void setRowName(String row_name) {
        this.row_name = row_name;
    }

    public List<String> getImageUrls() {
        return image_urls;
    }

    public void setImageUrls(List<String> image_urls) {
        this.image_urls = image_urls;
    }
}
