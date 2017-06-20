package de.greenrobot.BcComicdao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BC_IMAGE_DATA".
*/
public class BcImageDataDao extends AbstractDao<BcImageData, Long> {

    public static final String TABLENAME = "BC_IMAGE_DATA";

    /**
     * Properties of entity BcImageData.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property HomePK = new Property(1, String.class, "HomePK", false, "HOME_PK");
        public final static Property PKUrl = new Property(2, String.class, "PKUrl", false, "PKURL");
        public final static Property ComicName = new Property(3, String.class, "ComicName", false, "COMIC_NAME");
        public final static Property Quantity = new Property(4, Integer.class, "Quantity", false, "QUANTITY");
        public final static Property ItemArrayGSON = new Property(5, String.class, "ItemArrayGSON", false, "ITEM_ARRAY_GSON");
        public final static Property ItemReArrayGSON = new Property(6, String.class, "ItemReArrayGSON", false, "ITEM_RE_ARRAY_GSON");
        public final static Property LastUpdated = new Property(7, java.util.Date.class, "LastUpdated", false, "LAST_UPDATED");
    }


    public BcImageDataDao(DaoConfig config) {
        super(config);
    }
    
    public BcImageDataDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BC_IMAGE_DATA\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"HOME_PK\" TEXT NOT NULL ," + // 1: HomePK
                "\"PKURL\" TEXT NOT NULL UNIQUE ," + // 2: PKUrl
                "\"COMIC_NAME\" TEXT," + // 3: ComicName
                "\"QUANTITY\" INTEGER," + // 4: Quantity
                "\"ITEM_ARRAY_GSON\" TEXT," + // 5: ItemArrayGSON
                "\"ITEM_RE_ARRAY_GSON\" TEXT," + // 6: ItemReArrayGSON
                "\"LAST_UPDATED\" INTEGER);"); // 7: LastUpdated
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BC_IMAGE_DATA\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, BcImageData entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getHomePK());
        stmt.bindString(3, entity.getPKUrl());
 
        String ComicName = entity.getComicName();
        if (ComicName != null) {
            stmt.bindString(4, ComicName);
        }
 
        Integer Quantity = entity.getQuantity();
        if (Quantity != null) {
            stmt.bindLong(5, Quantity);
        }
 
        String ItemArrayGSON = entity.getItemArrayGSON();
        if (ItemArrayGSON != null) {
            stmt.bindString(6, ItemArrayGSON);
        }
 
        String ItemReArrayGSON = entity.getItemReArrayGSON();
        if (ItemReArrayGSON != null) {
            stmt.bindString(7, ItemReArrayGSON);
        }
 
        java.util.Date LastUpdated = entity.getLastUpdated();
        if (LastUpdated != null) {
            stmt.bindLong(8, LastUpdated.getTime());
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, BcImageData entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getHomePK());
        stmt.bindString(3, entity.getPKUrl());
 
        String ComicName = entity.getComicName();
        if (ComicName != null) {
            stmt.bindString(4, ComicName);
        }
 
        Integer Quantity = entity.getQuantity();
        if (Quantity != null) {
            stmt.bindLong(5, Quantity);
        }
 
        String ItemArrayGSON = entity.getItemArrayGSON();
        if (ItemArrayGSON != null) {
            stmt.bindString(6, ItemArrayGSON);
        }
 
        String ItemReArrayGSON = entity.getItemReArrayGSON();
        if (ItemReArrayGSON != null) {
            stmt.bindString(7, ItemReArrayGSON);
        }
 
        java.util.Date LastUpdated = entity.getLastUpdated();
        if (LastUpdated != null) {
            stmt.bindLong(8, LastUpdated.getTime());
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public BcImageData readEntity(Cursor cursor, int offset) {
        BcImageData entity = new BcImageData( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // HomePK
            cursor.getString(offset + 2), // PKUrl
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // ComicName
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // Quantity
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // ItemArrayGSON
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // ItemReArrayGSON
            cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)) // LastUpdated
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, BcImageData entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setHomePK(cursor.getString(offset + 1));
        entity.setPKUrl(cursor.getString(offset + 2));
        entity.setComicName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setQuantity(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setItemArrayGSON(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setItemReArrayGSON(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setLastUpdated(cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(BcImageData entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(BcImageData entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(BcImageData entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}