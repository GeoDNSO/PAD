package es.ucm.fdi.tieryourlikes.model;

import java.util.ArrayList;
import java.util.List;

public class TierRow {

    public static final String DEFAULT_COLOR = "NO_COLOR";
    private String row_name;
    private String color;
    private List<String> image_urls;

    public TierRow(String tierName, String color, List<String> images) {
        this.row_name = tierName;
        this.color = color;
        this.image_urls = images;
    }

    public static List<TierRow> getListFromString(List<String> tierListString){
        List<TierRow> tierRowList = new ArrayList<>();
        for(String title : tierListString)
            tierRowList.add(new TierRow(title, DEFAULT_COLOR, new ArrayList<>()));
        return tierRowList;
    }

    public boolean hasDefaultColor(){
        return this.color.equals(DEFAULT_COLOR);
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "TierRow{" +
                "row_name='" + row_name + '\'' +
                "color='" + color + '\'' +
                ", image_urls=" + image_urls +
                '}';
    }
}
