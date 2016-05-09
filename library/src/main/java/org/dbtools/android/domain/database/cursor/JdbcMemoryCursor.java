package org.dbtools.android.domain.database.cursor;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JdbcMemoryCursor implements Cursor {
    private int currentPosition = 0;
    private List<List<Object>> data = new ArrayList<List<Object>>();
    private int columnCount;
    private String[] columnNames;
    private boolean closed = false; // state variable

    public JdbcMemoryCursor(@Nonnull ResultSet resultSet) {
        try {
            readMetaData(resultSet);
            readData(resultSet);
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void readMetaData(@Nonnull ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        columnCount = metaData.getColumnCount();
        columnNames = new String[metaData.getColumnCount()];

        for (int i = 0; i < columnCount; i++) {
            int sqlIndex = i + 1;
            columnNames[i] = metaData.getColumnName(sqlIndex);
        }
    }

    private void readData(@Nonnull ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            List<Object> rowData = new ArrayList<Object>();
            for (int i = 0; i < columnCount; i++) {
                int index = i + 1;
                rowData.add(resultSet.getObject(index));
            }
            data.add(rowData);
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public int getPosition() {
        return currentPosition;
    }

    @Override
    public boolean move(int offset) {
        currentPosition += offset;
        return true;
    }

    @Override
    public boolean moveToPosition(int position) {
        if (position >= 0 && position < getCount()) {
            currentPosition = position;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean moveToFirst() {
        return moveToPosition(0);
    }

    @Override
    public boolean moveToLast() {
        return moveToPosition(getCount() - 1);
    }

    @Override
    public boolean moveToNext() {
        return move(1);
    }

    @Override
    public boolean moveToPrevious() {
        return move(-1);
    }

    @Override
    public boolean isFirst() {
        return currentPosition == 0;
    }

    @Override
    public boolean isLast() {
        return currentPosition == (getCount() - 1);
    }

    @Override
    public boolean isBeforeFirst() {
        return currentPosition < 0;
    }

    @Override
    public boolean isAfterLast() {
        return currentPosition > getCount();
    }

    @Override
    public int getColumnIndex(String columnName) {
        for (int i = 0; i < columnCount; i++) {
            if (columnNames[i].equalsIgnoreCase(columnName)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
        int index = getColumnIndex(columnName);
        if (index < 0) {
            throw new IllegalArgumentException("Cannot find column [" + columnName + "]");
        }

        return index;
    }

    @Override
    public String getColumnName(int index) {
        return columnNames[index];
    }

    @Override
    public String[] getColumnNames() {
        return columnNames;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    private List<Object> getRowData() {
        return data.get(currentPosition);
    }

    @Override
    public byte[] getBlob(int i) {
        return (byte[]) getRowData().get(i);
    }

    @Override
    public String getString(int i) {
        return (String) getRowData().get(i);
    }

    @Override
    public void copyStringToBuffer(int i, CharArrayBuffer charArrayBuffer) {

    }

    @Override
    public short getShort(int i) {
        return (Short) getRowData().get(i);
    }

    @Override
    public int getInt(int i) {
        return (Integer) getRowData().get(i);
    }

    @Override
    public long getLong(int i) {
        Object data = getRowData().get(i);
        if (data instanceof Integer) {
            return (Integer) getRowData().get(i);
        } else {
            return (Long) getRowData().get(i);
        }
    }

    @Override
    public float getFloat(int i) {
        Object data = getRowData().get(i);

        if (data instanceof Double) {
            return new BigDecimal((Double) getRowData().get(i)).floatValue();
        } else {
            return (Float) getRowData().get(i);
        }
    }

    @Override
    public double getDouble(int i) {
        return (Double) getRowData().get(i);
    }

    @Override
    public int getType(int i) {
        return getInt(i);
    }

    @Override
    public boolean isNull(int i) {
        return getRowData().get(i) == null;
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
        closed = true;
    }

    @Override
    public boolean isClosed() {
        return closed;
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

    @Nullable
    public Uri getNotificationUri() {
        return null;
    }

    @Override
    public boolean getWantsAllOnMoveCalls() {
        return true;
    }

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
