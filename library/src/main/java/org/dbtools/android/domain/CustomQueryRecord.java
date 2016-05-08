package org.dbtools.android.domain;

public interface CustomQueryRecord {
    /**
     * Used to populate data in CustomQueryRecord instances
     * @param rowData Object[] of data. (array elements based on getColumnTypes())
     */
    void setRowData(Object[] rowData);

    /**
     * Definition of column types for the query for the CustomQueryRecord instance
     * @return Class[] of classes
     */
    Class[] getColumnTypes();
}
