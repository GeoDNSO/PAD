package es.ucm.fdi.tieryourlikes.model;

import java.util.List;

public class Tier {
    private String _id;
    private String template_id;
    private String creator_username;
    private List<String> container;
    private List<TierRow> tier_rows;

    public Tier(String _id, String template_id, String creator_username, List<String> container, List<TierRow> tier_rows) {
        this._id = _id;
        this.template_id = template_id;
        this.creator_username = creator_username;
        this.container = container;
        this.tier_rows = tier_rows;
    }

    public String getId() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTemplateId() {
        return template_id;
    }

    public void setTemplateId(String template_id) {
        this.template_id = template_id;
    }

    public String getCreatorUsername() {
        return creator_username;
    }

    public void setCreatorUsername(String creator_username) {
        this.creator_username = creator_username;
    }

    public List<String> getContainer() {
        return container;
    }

    public void setContainer(List<String> container) {
        this.container = container;
    }

    public List<TierRow> getTierRows() {
        return tier_rows;
    }

    public void setTierRows(List<TierRow> tier_rows) {
        this.tier_rows = tier_rows;
    }
}
