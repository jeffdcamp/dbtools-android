package org.dbtools.android.domain.database.cursor;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class JdbcCursor implements Cursor {
    private ResultSet resultSet;

    public JdbcCursor(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public int getCount() {
        int count;
        try {
            resultSet.last();
            count = resultSet.getRow();
            resultSet.beforeFirst();
        } catch (Exception ex) {
            return 0;
        }
        return count;
    }

    @Override
    public int getPosition() {
        try {
            return resultSet.getRow();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean move(int offset) {
        try {
            for (int i = 0; i < offset; i++) {
                resultSet.next();
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean moveToPosition(int position) {
        try {
            resultSet.absolute(position);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean moveToFirst() {
        try {
            resultSet.first();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean moveToLast() {
        try {
            resultSet.last();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean moveToNext() {
        try {
            resultSet.next();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean moveToPrevious() {
        try {
            resultSet.previous();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isFirst() {
        try {
            resultSet.isFirst();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isLast() {
        try {
            resultSet.isLast();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isBeforeFirst() {
        try {
            resultSet.isBeforeFirst();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isAfterLast() {
        try {
            resultSet.isAfterLast();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getColumnIndex(String columnName) {
        try {
            return resultSet.findColumn(columnName);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
        try {
            return resultSet.findColumn(columnName);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Cannot find column [" + columnName + "]");
        }
    }

    @Override
    public String getColumnName(int index) {
        try {
            return resultSet.getMetaData().getColumnName(index);
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String[] getColumnNames() {
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            String[] columnNames = new String[metaData.getColumnCount()];

            for (int i = 0; i < metaData.getColumnCount(); i++) {
                columnNames[i] = metaData.getColumnName(i);
            }
            return columnNames;
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[]{};
        }
    }

    @Override
    public int getColumnCount() {
        try {
            return resultSet.getMetaData().getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public byte[] getBlob(int i) {
        try {
            return resultSet.getBytes(i);
        } catch (SQLException e) {
            e.printStackTrace();
            return new byte[]{};
        }
    }

    @Override
    public String getString(int i) {
        try {
            return resultSet.getString(i);
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void copyStringToBuffer(int i, CharArrayBuffer charArrayBuffer) {

    }

    @Override
    public short getShort(int i) {
        try {
            return resultSet.getShort(i);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int getInt(int i) {
        try {
            return resultSet.getInt(i);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public long getLong(int i) {
        try {
            return resultSet.getLong(i);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public float getFloat(int i) {
        try {
            return resultSet.getFloat(i);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public double getDouble(int i) {
        try {
            return resultSet.getDouble(i);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int getType(int i) {
        try {
            return resultSet.getInt(i);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean isNull(int i) {
        try {
            return resultSet.wasNull();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void deactivate() {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public boolean requery() {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public void close() {
        try {
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isClosed() {
        try {
            return resultSet.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    @Override
    public void registerContentObserver(ContentObserver contentObserver) {

    }

    @Override
    public void unregisterContentObserver(ContentObserver contentObserver) {

    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void setNotificationUri(ContentResolver contentResolver, Uri uri) {

    }

    @Override
    public Uri getNotificationUri() {
        return null;
    }

    @Override
    public boolean getWantsAllOnMoveCalls() {
        return true;
    }

    @Override
    public void setExtras(Bundle extras) {

    }

    @Override
    public Bundle getExtras() {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public Bundle respond(Bundle bundle) {
        throw new UnsupportedOperationException("unsupported");
    }
}
