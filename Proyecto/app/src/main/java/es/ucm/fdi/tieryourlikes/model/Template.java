package es.ucm.fdi.tieryourlikes.model;

import java.util.List;

public class Template {
    private String title;
    private String category;
    private String creator_username;
    private List<String> container;
    private List<String> tier_rows;

    public Template(String title, String category, String creator_username, List<String> container, List<String> tier_rows) {
        this.title = title;
        this.category = category;
        this.creator_username = creator_username;
        this.container = container;
        this.tier_rows = tier_rows;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreator_username() {
        return creator_username;
    }

    public void setCreator_username(String creator_username) {
        this.creator_username = creator_username;
    }

    public List<String> getContainer() {
        return container;
    }

    public void setContainer(List<String> container) {
        this.container = container;
    }

    public List<String> getTier_rows() {
        return tier_rows;
    }

    public void setTier_rows(List<String> tier_rows) {
        this.tier_rows = tier_rows;
    }

    @Override
    public String toString() {
        return "Template{" +
                "title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", creator_username='" + creator_username + '\'' +
                ", container=" + container +
                ", tier_rows=" + tier_rows +
                '}';
    }
}
