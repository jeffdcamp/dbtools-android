package org.dbtools.android.domain;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import org.dbtools.android.domain.database.DatabaseWrapper;
import org.dbtools.android.domain.dbtype.DatabaseValue;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")
public abstract class RxAndroidBaseManager<T extends AndroidBaseRecord> extends AndroidBaseManager<T> {

    public Observable<Cursor> findCursorAllRx() {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Nullable
            @Override
            public Cursor call() {
                return findCursorAll();
            }
        });
    }

    @Nonnull
    public Observable<Cursor> findCursorByRawQueryRx(@Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Nullable
            @Override
            public Cursor call() {
                return findCursorByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
            }
        });
    }

    @Nonnull
    public Observable<Cursor> findCursorByRawQueryRx(@Nonnull final String databaseName, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return getReadableDatabase(databaseName).rawQuery(rawQuery, selectionArgs);
            }
        });
    }

    @Nonnull
    public Observable<Cursor> findCursorBySelectionRx(@Nullable final String selection, @Nullable final String orderBy) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Nullable
            @Override
            public Cursor call() {
                return findCursorBySelection(selection, new String[]{}, orderBy);
            }
        });
    }

    @Nonnull
    public Observable<Cursor> findCursorBySelectionRx(@Nonnull final String databaseName, @Nullable final String selection, @Nullable final String orderBy) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Nullable
            @Override
            public Cursor call() {
                return findCursorBySelection(databaseName, selection, null, orderBy);
            }
        });
    }

    @Nonnull
    public Observable<Cursor> findCursorBySelectionRx(@Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Nullable
            @Override
            public Cursor call() {
                return findCursorBySelection(getDatabaseName(), selection, selectionArgs, orderBy);
            }
        });
    }

    @Nonnull
    public Observable<Cursor> findCursorBySelectionRx(@Nonnull final String databaseName, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Nullable
            @Override
            public Cursor call() {
                return findCursorBySelection(databaseName, true, getTableName(), getAllColumns(), selection, selectionArgs, null, null, orderBy, null);
            }
        });
    }

    @Nonnull
    public Observable<Cursor> findCursorBySelectionRx(final boolean distinct, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String groupBy, @Nullable final String having, @Nullable final String orderBy, @Nullable final String limit) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Nullable
            @Override
            public Cursor call() {
                return findCursorBySelection(getDatabaseName(), distinct, getTableName(), getAllColumns(), selection, selectionArgs, groupBy, having, orderBy, limit);
            }
        });
    }

    @Nonnull
    public Observable<Cursor> findCursorBySelectionRx(@Nonnull final String databaseName, final boolean distinct, @Nonnull final String table, @Nonnull final String[] columns, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String groupBy, @Nullable final String having, @Nullable final String orderBy, @Nullable final String limit) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Nullable
            @Override
            public Cursor call() {
                return findCursorBySelection(databaseName, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            }
        });
    }

    @Nonnull
    public Observable<Cursor> findCursorByRowIdRx(final long rowId) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Nullable
            @Override
            public Cursor call() {
                return findCursorBySelection(getPrimaryKey() + "= ?", new String[]{String.valueOf(rowId)}, null);
            }
        });
    }

    @Nonnull
    public Observable<Cursor> findCursorByRowIdRx(@Nonnull final String databaseName, final long rowId) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Nullable
            @Override
            public Cursor call() {
                return findCursorBySelection(databaseName, getPrimaryKey() + "= ?", new String[]{String.valueOf(rowId)}, null);
            }
        });
    }

    @Nonnull
    public Observable<T> findAllRx() {
        return findBySelectionRx(null, null, null);
    }

    @Nonnull
    public Observable<T> findAllRx(@Nonnull final String databaseName) {
        return findBySelectionRx(databaseName, null, null, null);
    }

    @Nonnull
    public Observable<T> findAllOrderByRx(@Nullable final String orderBy) {
        return findBySelectionRx(null, null, orderBy);
    }

    @Nonnull
    public Observable<T> findAllOrderByRx(@Nonnull final String databaseName, @Nullable final String orderBy) {
        return findBySelectionRx(databaseName, null, null, orderBy);
    }

    @Nonnull
    public Observable<T> findByRowIdRx(final long rowId) {
        return findBySelectionRx(getPrimaryKey() + "= ?", new String[]{String.valueOf(rowId)}, null);
    }

    @Nonnull
    public Observable<T> findByRowIdRx(@Nonnull final String databaseName, final long rowId) {
        return findBySelectionRx(databaseName, getPrimaryKey() + "= ?", new String[]{String.valueOf(rowId)}, null);
    }

    @Nonnull
    public Observable<T> findBySelectionRx(@Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy) {
        return findBySelectionRx(getDatabaseName(), selection, selectionArgs, orderBy);
    }

    @Nonnull
    public Observable<T> findBySelectionRx(@Nonnull final String databaseName, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy) {
        return DBToolsRxUtil.from(new Func0<Iterable<T>>() {
            @Override
            public Iterable<T> call() {
                return findAllBySelection(databaseName, selection, selectionArgs, orderBy);
            }
        });
    }

    @Nonnull
    public Observable<T> findByRawQueryRx(@Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return findByRawQueryRx(getDatabaseName(), rawQuery, selectionArgs);
    }

    @Nonnull
    public Observable<T> findByRawQueryRx(@Nonnull final String databaseName, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.from(new Func0<Iterable<T>>() {
            @Override
            public Iterable<T> call() {
                return findAllByRawQuery(databaseName, rawQuery, selectionArgs);
            }
        });
    }

    @Nonnull
    public Observable<T> findAllByRowIdsRx(final long[] rowIds) {
        return findAllByRowIdsRx(rowIds, null);
    }

    @Nonnull
    public Observable<T> findAllByRowIdsRx(final long[] rowIds, @Nullable final String orderBy) {
        return findAllByRowIdsRx(getDatabaseName(), rowIds, orderBy);
    }

    @Nonnull
    public Observable<T> findAllByRowIdsRx(@Nonnull final String databaseName, final long[] rowIds, @Nullable final String orderBy) {
        StringBuilder sb = new StringBuilder();
        for (long rowId : rowIds) {
            if (sb.length() > 0) {
                sb.append(" OR ");
            }
            sb.append(getPrimaryKey()).append(" = ").append(rowId);
        }

        return findBySelectionRx(databaseName, sb.toString(), null, orderBy);
    }

    @Nonnull
    public Observable<Long> findCountRx() {
        return DBToolsRxUtil.just(new Func0<Long>() {
            @Override
            public Long call() {
                return findCountBySelection(null, null);
            }
        });
    }

    @Nonnull
    public Observable<Long> findCountRx(@Nonnull final String databaseName) {
        return DBToolsRxUtil.just(new Func0<Long>() {
            @Override
            public Long call() {
                return findCountBySelection(databaseName, null, null);
            }
        });
    }

    @Nonnull
    public Observable<Long> findCountBySelectionRx(@Nullable final String selection, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<Long>() {
            @Override
            public Long call() {
                return findCountBySelection(getDatabaseName(), selection, selectionArgs);
            }
        });
    }

    @Nonnull
    public Observable<Long> findCountBySelectionRx(@Nonnull final String databaseName, @Nullable final String selection, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<Long>() {
            @Override
            public Long call() {
                return findCountBySelection(getReadableDatabase(databaseName), getTableName(), selection, selectionArgs);
            }
        });

    }

    @Nonnull
    public static Observable<Long> findCountBySelectionRx(@Nonnull final DatabaseWrapper database, @Nonnull final String tableName, @Nullable final String selection, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<Long>() {
            @Override
            public Long call() {
                return findCountBySelection(database, tableName, selection, selectionArgs);
            }
        });
    }

    /**
     * Find count by raw query.  Raw query assumes first SELECT param is count(1).
     *
     * @param rawQuery Query
     * @return total count
     */
    @Nonnull
    public Observable<Long> findCountByRawQueryRx(@Nonnull final String rawQuery) {
        return DBToolsRxUtil.just(new Func0<Long>() {
            @Override
            public Long call() {
                return findCountByRawQuery(getDatabaseName(), rawQuery, null);
            }
        });
    }

    /**
     * Find count by raw query.  Raw query assumes first SELECT param is count(1).
     *
     * @param databaseName name of database to use
     * @param rawQuery     Query
     * @return total count
     */
    @Nonnull
    public Observable<Long> findCountByRawQueryRx(@Nonnull final String databaseName, @Nonnull final String rawQuery) {
        return DBToolsRxUtil.just(new Func0<Long>() {
            @Override
            public Long call() {
                return findCountByRawQuery(databaseName, rawQuery, null);
            }
        });
    }

    /**
     * Find count by raw query.  Raw query assumes first SELECT param is count(1).
     *
     * @param rawQuery      Query
     * @param selectionArgs Selection args
     * @return total count
     */
    @Nonnull
    public Observable<Long> findCountByRawQueryRx(@Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<Long>() {
            @Override
            public Long call() {
                return findCountByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
            }
        });
    }

    @Nonnull
    public Observable<Long> findCountByRawQueryRx(@Nonnull final String databaseName, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {

        return DBToolsRxUtil.just(new Func0<Long>() {
            @Override
            public Long call() {
                return findCountByRawQuery(getReadableDatabase(databaseName), rawQuery, selectionArgs);
            }
        });
    }

    @Nonnull
    public static Observable<Long> findCountByRawQueryRx(@Nonnull final DatabaseWrapper database, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<Long>() {
            @Override
            public Long call() {
                return findCountByRawQuery(database, rawQuery, selectionArgs);
            }
        });
    }

    /**
     * Return the first column and first row value as the value for given rawQuery and selectionArgs.
     *
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param rawQuery      Query contain first column which is the needed value
     * @param selectionArgs Query parameters
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    @Nonnull
    public <I> Observable<I> findValueByRawQueryRx(@Nonnull final Class<I> valueType, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return findValueByRawQueryRx(getDatabaseName(), valueType, rawQuery, selectionArgs);
    }

    /**
     * Return the first column and first row value as the value for given rawQuery and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param rawQuery      Query contain first column which is the needed value
     * @param selectionArgs Query parameters
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    @Nonnull
    public <I> Observable<I> findValueByRawQueryRx(@Nonnull final String databaseName, @Nonnull final Class<I> valueType, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return findValueByRawQueryRx(getReadableDatabase(databaseName), valueType, 0, rawQuery, selectionArgs);
    }

    /**
     * Return the first column and first row value as a Date for given rawQuery and selectionArgs.
     *
     * @param database      DatabaseWrapper of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param columnIndex   Index of column with value
     * @param rawQuery      Query contain first column which is the needed value
     * @param selectionArgs Query parameters
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    @Nonnull
    public static <I> Observable<I> findValueByRawQueryRx(@Nonnull final DatabaseWrapper database, @Nonnull final Class<I> valueType, final int columnIndex, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.from(new Func0<Iterable<I>>() {
            @Override
            public Iterable<I> call() {
                return findAllValuesByRawQuery(database, valueType, columnIndex, rawQuery, selectionArgs);
            }
        });
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    @Nonnull
    public <I> Observable<I> findValueBySelectionRx(@Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, final I defaultValue) {
        return findValueBySelectionRx(getDatabaseName(), valueType, column, selection, selectionArgs, defaultValue);
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param orderBy       Order by value(s)
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    @Nonnull
    public <I> Observable<I> findValueBySelectionRx(@Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, final String orderBy, final I defaultValue) {
        return findValueBySelectionRx(getDatabaseName(), valueType, column, selection, selectionArgs, orderBy, defaultValue);
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    @Nonnull
    public <I> Observable<I> findValueBySelectionRx(@Nonnull final String databaseName, @Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, final I defaultValue) {
        return findValueBySelectionRx(databaseName, valueType, column, selection, selectionArgs, null, defaultValue);
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param orderBy       Order by value(s)
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    @Nonnull
    public <I> Observable<I> findValueBySelectionRx(@Nonnull final String databaseName, @Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy, @Nullable final I defaultValue) {
        return findValueBySelectionRx(getReadableDatabase(databaseName), getTableName(), valueType, column, selection, selectionArgs, orderBy, defaultValue);
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @param database      DatabaseWrapper of database to query
     * @param tableName     Table to run query against
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param orderBy       Order by value(s)
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    @Nonnull
    public static <I> Observable<I> findValueBySelectionRx(@Nonnull final DatabaseWrapper database, @Nonnull final String tableName, @Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy, @Nullable final I defaultValue) {
        return DBToolsRxUtil.from(new Func0<Iterable<I>>() {
            @Override
            public Iterable<I> call() {
                return findAllValuesBySelection(database, tableName, valueType, column, selection, selectionArgs, orderBy);
            }
        });
    }

    @Nonnull
    public Observable<Boolean> tableExistsRx(@Nonnull final String tableName) {
        return DBToolsRxUtil.just(new Func0<Boolean>() {
            @Override
            public Boolean call() {
                return tableExists(getDatabaseName(), tableName);
            }
        });
    }

    @Nonnull
    public Observable<Boolean> tableExistsRx(@Nonnull final String databaseName, @Nonnull final String tableName) {
        return DBToolsRxUtil.just(new Func0<Boolean>() {
            @Override
            public Boolean call() {
                return tableExists(getReadableDatabase(databaseName), tableName);
            }
        });
    }

    @Nonnull
    public static Observable<Boolean> tableExistsRx(@Nonnull final AndroidDatabase androidDatabase, @Nonnull final String tableName) {
        return DBToolsRxUtil.just(new Func0<Boolean>() {
            @Override
            public Boolean call() {
                return tableExists(androidDatabase.getDatabaseWrapper(), tableName);
            }
        });
    }

    @Nonnull
    public static Observable<Boolean> tableExistsRx(@Nullable final DatabaseWrapper db, @Nullable final String tableName) {
        return DBToolsRxUtil.just(new Func0<Boolean>() {
            @Override
            public Boolean call() {
                return tableExists(db, tableName);
            }
        });
    }

    @Nonnull
    public Observable<MatrixCursor> toMatrixCursorRx(@Nonnull final T record) {
        return DBToolsRxUtil.just(new Func0<MatrixCursor>() {
            @Nullable
            @Override
            public MatrixCursor call() {
                return toMatrixCursor(record);
            }
        });
    }

    @Nonnull
    public Observable<MatrixCursor> toMatrixCursorRx(@Nonnull final T... records) {
        return DBToolsRxUtil.just(new Func0<MatrixCursor>() {
            @Nullable
            @Override
            public MatrixCursor call() {
                return toMatrixCursor(Arrays.asList(records));
            }
        });
    }

    @Nonnull
    public Observable<MatrixCursor> toMatrixCursorRx(@Nonnull final List<T> records) {
        return DBToolsRxUtil.just(new Func0<MatrixCursor>() {
            @Nullable
            @Override
            public MatrixCursor call() {
                return toMatrixCursor(records);
            }
        });
    }

    @Nonnull
    public Observable<MatrixCursor> toMatrixCursorRx(final String[] columns, final List<T> records) {
        return DBToolsRxUtil.just(new Func0<MatrixCursor>() {
            @Override
            public MatrixCursor call() {
                return toMatrixCursor(columns, records);
            }
        });
    }

    @Nonnull
    public Observable<Cursor> mergeCursorsRx(final Cursor... cursors) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return new MergeCursor(cursors);
            }
        });
    }

    @Nonnull
    public Observable<Cursor> addAllToCursorTopRx(final Cursor cursor, final List<T> records) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return mergeCursors(toMatrixCursor(records), cursor);
            }
        });
    }

    @Nonnull
    public Observable<Cursor> addAllToCursorTopRx(final Cursor cursor, final T... records) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return mergeCursors(toMatrixCursor(records), cursor);
            }
        });
    }

    @Nonnull
    public Observable<Cursor> addAllToCursorBottomRx(final Cursor cursor, final List<T> records) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return mergeCursors(cursor, toMatrixCursor(records));
            }
        });
    }

    @Nonnull
    public Observable<Cursor> addAllToCursorBottomRx(final Cursor cursor, final T... records) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return mergeCursors(cursor, toMatrixCursor(records));
            }
        });
    }
}
