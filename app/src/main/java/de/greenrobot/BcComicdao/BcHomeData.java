package de.greenrobot.BcComicdao;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "BC_HOME_DATA".
 */
@Entity
public class BcHomeData {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @Unique
    private String HomePK;

    @NotNull
    @Unique
    private String HomeUrl;
    private String HomePageItemArrayGSON;
    private String HomeItemArrayGSON;
    private java.util.Date LastUpdated;

    @Generated(hash = 1283662041)
    public BcHomeData() {
    }

    public BcHomeData(Long id) {
        this.id = id;
    }

    @Generated(hash = 1365023554)
    public BcHomeData(Long id, @NotNull String HomePK, @NotNull String HomeUrl, String HomePageItemArrayGSON, String HomeItemArrayGSON,
            java.util.Date LastUpdated) {
        this.id = id;
        this.HomePK = HomePK;
        this.HomeUrl = HomeUrl;
        this.HomePageItemArrayGSON = HomePageItemArrayGSON;
        this.HomeItemArrayGSON = HomeItemArrayGSON;
        this.LastUpdated = LastUpdated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getHomePK() {
        return HomePK;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setHomePK(@NotNull String HomePK) {
        this.HomePK = HomePK;
    }

    @NotNull
    public String getHomeUrl() {
        return HomeUrl;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setHomeUrl(@NotNull String HomeUrl) {
        this.HomeUrl = HomeUrl;
    }

    public String getHomePageItemArrayGSON() {
        return HomePageItemArrayGSON;
    }

    public void setHomePageItemArrayGSON(String HomePageItemArrayGSON) {
        this.HomePageItemArrayGSON = HomePageItemArrayGSON;
    }

    public String getHomeItemArrayGSON() {
        return HomeItemArrayGSON;
    }

    public void setHomeItemArrayGSON(String HomeItemArrayGSON) {
        this.HomeItemArrayGSON = HomeItemArrayGSON;
    }

    public java.util.Date getLastUpdated() {
        return LastUpdated;
    }

    public void setLastUpdated(java.util.Date LastUpdated) {
        this.LastUpdated = LastUpdated;
    }

}
