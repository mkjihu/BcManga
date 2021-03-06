package de.greenrobot.BcComicdao;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "BC_IMAGE_DATA".
 */
@Entity
public class BcImageData {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String HomePK;

    @NotNull
    @Unique
    private String PKUrl;
    private String ComicName;
    private Integer Quantity;
    private String ItemArrayGSON;
    private String ItemReArrayGSON;
    private java.util.Date LastUpdated;

    @Generated(hash = 1095111157)
    public BcImageData() {
    }

    public BcImageData(Long id) {
        this.id = id;
    }

    @Generated(hash = 1527378779)
    public BcImageData(Long id, @NotNull String HomePK, @NotNull String PKUrl, String ComicName, Integer Quantity, String ItemArrayGSON, String ItemReArrayGSON,
            java.util.Date LastUpdated) {
        this.id = id;
        this.HomePK = HomePK;
        this.PKUrl = PKUrl;
        this.ComicName = ComicName;
        this.Quantity = Quantity;
        this.ItemArrayGSON = ItemArrayGSON;
        this.ItemReArrayGSON = ItemReArrayGSON;
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
    public String getPKUrl() {
        return PKUrl;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setPKUrl(@NotNull String PKUrl) {
        this.PKUrl = PKUrl;
    }

    public String getComicName() {
        return ComicName;
    }

    public void setComicName(String ComicName) {
        this.ComicName = ComicName;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public void setQuantity(Integer Quantity) {
        this.Quantity = Quantity;
    }

    public String getItemArrayGSON() {
        return ItemArrayGSON;
    }

    public void setItemArrayGSON(String ItemArrayGSON) {
        this.ItemArrayGSON = ItemArrayGSON;
    }

    public String getItemReArrayGSON() {
        return ItemReArrayGSON;
    }

    public void setItemReArrayGSON(String ItemReArrayGSON) {
        this.ItemReArrayGSON = ItemReArrayGSON;
    }

    public java.util.Date getLastUpdated() {
        return LastUpdated;
    }

    public void setLastUpdated(java.util.Date LastUpdated) {
        this.LastUpdated = LastUpdated;
    }

}
