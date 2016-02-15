package org.dbtools.android.domain;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;


/**
 *
 * @author jcampbell
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class AndroidBaseRecord implements Serializable {


    public abstract void setPrimaryKeyId(long id);
    public abstract long getPrimaryKeyId();
    public abstract String getIdColumnName();

    public abstract String[] getAllColumns();


    /**
     * getContentValues.
     * @return values for database (DO NOT INCLUDE THE ID COLUMN)
     */
    public abstract ContentValues getContentValues();
    public abstract Object[] getValues();
    public abstract void setContent(Cursor cursor);
    public abstract boolean isNewRecord();

    public static int booleanToInt(boolean b) {
        return b ? 1 : 0;
    }

    public static int booleanToLong(boolean b) {
        return b ? 1 : 0;
    }

    public static boolean intToBoolean(int b) {
        return b != 0;
    }

    public boolean longToBoolean(long b) {
        return b != 0;
    }

}
