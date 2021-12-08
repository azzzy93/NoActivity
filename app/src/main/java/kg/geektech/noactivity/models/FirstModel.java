package kg.geektech.noactivity.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class FirstModel implements Serializable {
    private String name;
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String imgStringUri;
    private String userId;
    private String imgStringUrl;

    public FirstModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgStringUri() {
        return imgStringUri;
    }

    public void setImgStringUri(String imgStringUri) {
        this.imgStringUri = imgStringUri;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setImgStringUrl(String imgStringUrl) {
        this.imgStringUrl = imgStringUrl;
    }

    public String getImgStringUrl() {
        return imgStringUrl;
    }
}
