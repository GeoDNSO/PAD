package es.ucm.fdi.tieryourlikes.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Template implements Parcelable {
    private String _id;
    private String title;
    private String category;
    private String creator_username;
    private List<String> container;
    private List<String> tier_rows;
    private String cover;
    private String creation_time;

    public Template(String id, String title, String category, String creator_username, List<String> container, List<String> tier_rows, String cover_image, String creation_time) {
        this._id = id;
        this.title = title;
        this.category = category;
        this.creator_username = creator_username;
        this.container = container;
        this.tier_rows = tier_rows;
        this.cover = cover_image;
        this.creation_time = creation_time;
    }

    public Template(String title, String category, String creator_username, String cover_image, List<String> container, List<String> tier_rows) {
        _id = "-1";
        this.title = title;
        this.category = category;
        this.cover = cover_image;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public String toString() {
        return "Template{" +
                "_id='" + _id + '\'' +
                "title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", creator_username='" + creator_username + '\'' +
                ", cover_image='" + cover + '\'' +
                ", container=" + container +
                ", tier_rows=" + tier_rows +
                '}';
    }



    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getCreationTime() {
        return creation_time;
    }

    public void setCreationTime(String creation_time) {
        this.creation_time = creation_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.title);
        dest.writeString(this.category);
        dest.writeString(this.creator_username);
        dest.writeStringList(this.container);
        dest.writeStringList(this.tier_rows);
        dest.writeString(this.cover);
        dest.writeString(this.creation_time);
    }

    public void readFromParcel(Parcel source) {
        this._id = source.readString();
        this.title = source.readString();
        this.category = source.readString();
        this.creator_username = source.readString();
        this.container = source.createStringArrayList();
        this.tier_rows = source.createStringArrayList();
        this.cover = source.readString();
        this.creation_time = source.readString();
    }

    protected Template(Parcel in) {
        this._id = in.readString();
        this.title = in.readString();
        this.category = in.readString();
        this.creator_username = in.readString();
        this.container = in.createStringArrayList();
        this.tier_rows = in.createStringArrayList();
        this.cover = in.readString();
        this.creation_time = in.readString();
    }

    public static final Parcelable.Creator<Template> CREATOR = new Parcelable.Creator<Template>() {
        @Override
        public Template createFromParcel(Parcel source) {
            return new Template(source);
        }

        @Override
        public Template[] newArray(int size) {
            return new Template[size];
        }
    };
}
