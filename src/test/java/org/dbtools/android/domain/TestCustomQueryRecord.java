package org.dbtools.android.domain;

/**
 * User: jcampbell
 * Date: 1/31/14
 */
public class TestCustomQueryRecord implements CustomQueryRecord {
    private long id;
    private String name;
    private boolean enable;

    @Override
    public void setRowData(Object[] rowData) {
        for (int i = 0; i < rowData.length; i++) {
            switch (i) {
                case 0:
                    id = (Integer) rowData[i];
                    break;
                case 1:
                    name = (String) rowData[i];
                    break;
                case 2:
                    enable = (Integer) rowData[i] != 0;
                default:
                    throw new IllegalStateException("Unexpected column index [" + i + "]");
            }
        }
    }
}
