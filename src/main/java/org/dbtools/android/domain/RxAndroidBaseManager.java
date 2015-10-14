package org.dbtools.android.domain;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import org.dbtools.android.domain.database.DatabaseWrapper;
import org.dbtools.android.domain.dbtype.DatabaseValue;
import org.dbtools.android.domain.dbtype.DatabaseValueUtil;
import rx.Observable;
import rx.functions.Func0;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")
public abstract class RxAndroidBaseManager<T extends AndroidBaseRecord> extends AndroidBaseManager<T> {

    public Observable<Cursor> findCursorAllRx() {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return findCursorAll();
            }
        });
    }

    @Nullable
    public Observable<Cursor> findCursorByRawQueryRx(@Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return findCursorByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
            }
        });
    }

    @Nullable
    public Observable<Cursor> findCursorByRawQueryRx(@Nonnull final String databaseName, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return getReadableDatabase(databaseName).rawQuery(rawQuery, selectionArgs);
            }
        });
    }

    @Nullable
    public Observable<Cursor> findCursorBySelectionRx(@Nullable final String selection, @Nullable final String orderBy) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return findCursorBySelection(selection, new String[]{}, orderBy);
            }
        });
    }

    @Nullable
    public Observable<Cursor> findCursorBySelectionRx(@Nonnull final String databaseName, @Nullable final String selection, @Nullable final String orderBy) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return findCursorBySelection(databaseName, selection, null, orderBy);
            }
        });
    }

    @Nullable
    public Observable<Cursor> findCursorBySelectionRx(@Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return findCursorBySelection(getDatabaseName(), selection, selectionArgs, orderBy);
            }
        });
    }

    @Nullable
    public Observable<Cursor> findCursorBySelectionRx(@Nonnull final String databaseName, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return findCursorBySelection(databaseName, true, getTableName(), getAllKeys(), selection, selectionArgs, null, null, orderBy, null);
            }
        });
    }

    @Nullable
    public Observable<Cursor> findCursorBySelectionRx(final boolean distinct, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String groupBy, @Nullable final String having, @Nullable final String orderBy, @Nullable final String limit) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return findCursorBySelection(getDatabaseName(), distinct, getTableName(), getAllKeys(), selection, selectionArgs, groupBy, having, orderBy, limit);
            }
        });
    }

    @Nullable
    public Observable<Cursor> findCursorBySelectionRx(@Nonnull final String databaseName, final boolean distinct, @Nonnull final String table, @Nonnull final String[] columns, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String groupBy, @Nullable final String having, @Nullable final String orderBy, @Nullable final String limit) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return findCursorBySelection(databaseName, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            }
        });
    }

    @Nullable
    public Observable<Cursor> findCursorByRowIdRx(final long rowId) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return findCursorBySelection(getPrimaryKey() + "= ?", new String[]{String.valueOf(rowId)}, null);
            }
        });
    }

    @Nullable
    public Observable<Cursor> findCursorByRowIdRx(@Nonnull final String databaseName, final long rowId) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return findCursorBySelection(databaseName, getPrimaryKey() + "= ?", new String[]{String.valueOf(rowId)}, null);
            }
        });
    }

    @Nonnull
    public Observable<List<T>> findAllRx() {
        return DBToolsRxUtil.just(new Func0<List<T>>() {
            @Override
            public List<T> call() {
                return findAllBySelection(null, null, null);
            }
        });
    }

    @Nonnull
    public Observable<List<T>> findAllRx(@Nonnull final String databaseName) {
        return DBToolsRxUtil.just(new Func0<List<T>>() {
            @Override
            public List<T> call() {
                return findAllBySelection(databaseName, null, null, null);
            }
        });
    }

    @Nonnull
    public Observable<List<T>> findAllOrderByRx(@Nullable final String orderBy) {
        return DBToolsRxUtil.just(new Func0<List<T>>() {
            @Override
            public List<T> call() {
                return findAllBySelection(null, null, orderBy);
            }
        });
    }

    @Nonnull
    public Observable<List<T>> findAllOrderByRx(@Nonnull final String databaseName, @Nullable final String orderBy) {
        return DBToolsRxUtil.just(new Func0<List<T>>() {
            @Override
            public List<T> call() {
                return findAllBySelection(databaseName, null, null, orderBy);
            }
        });
    }

    @Nonnull
    public Observable<List<T>> findAllBySelectionRx(@Nullable final String selection, @Nonnull final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<List<T>>() {
            @Override
            public List<T> call() {
                return findAllBySelection(selection, selectionArgs, null);
            }
        });
    }

    @Nonnull
    public Observable<List<T>> findAllBySelectionRx(@Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy) {
        return DBToolsRxUtil.just(new Func0<List<T>>() {
            @Override
            public List<T> call() {
                Cursor cursor = findCursorBySelection(selection, selectionArgs, orderBy);
                return getAllItemsFromCursor(cursor);
            }
        });
    }

    @Nonnull
    public Observable<List<T>> findAllBySelectionRx(@Nonnull final String databaseName, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy) {
        return DBToolsRxUtil.just(new Func0<List<T>>() {
            @Override
            public List<T> call() {
                Cursor cursor = findCursorBySelection(databaseName, selection, selectionArgs, orderBy);
                return getAllItemsFromCursor(cursor);
            }
        });
    }

    /**
     * Populate of List from a rawQuery.  The raw query must contain all of the columns names for the object
     *
     * @param rawQuery Custom query
     * @return List of object T
     */
    @Nonnull
    public Observable<List<T>> findAllByRawQueryRx(@Nonnull final String rawQuery) {
        return DBToolsRxUtil.just(new Func0<List<T>>() {
            @Override
            public List<T> call() {
                return getAllItemsFromCursor(findCursorByRawQuery(getDatabaseName(), rawQuery, null));
            }
        });
    }

    /**
     * Populate of List from a rawQuery.  The raw query must contain all of the columns names for the object
     *
     * @param rawQuery      Custom query
     * @param selectionArgs query arguments
     * @return List of object T
     */
    @Nonnull
    public Observable<List<T>> findAllByRawQueryRx(@Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<List<T>>() {
            @Override
            public List<T> call() {
                return getAllItemsFromCursor(findCursorByRawQuery(getDatabaseName(), rawQuery, selectionArgs));
            }
        });
    }

    /**
     * Populate of List from a rawQuery.  The raw query must contain all of the columns names for the object
     *
     * @param databaseName  Name of database
     * @param rawQuery      Custom query
     * @param selectionArgs query arguments
     * @return List of object T
     */
    @Nonnull
    public Observable<List<T>> findAllByRawQueryRx(@Nonnull final String databaseName, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<List<T>>() {
            @Override
            public List<T> call() {
                return getAllItemsFromCursor(findCursorByRawQuery(databaseName, rawQuery, selectionArgs));
            }
        });
    }

    @Nonnull
    public Observable<List<T>> getAllItemsFromCursorRx(@Nullable final Cursor cursor) {
        return DBToolsRxUtil.just(new Func0<List<T>>() {
            @Override
            public List<T> call() {
                return getAllItemsFromCursor(cursor);
            }
        });
    }

    @Nullable
    public Observable<T> findByRowIdRx(final long rowId) {
        return DBToolsRxUtil.just(new Func0<T>() {
            @Override
            public T call() {
                return findBySelection(getPrimaryKey() + "= ?", new String[]{String.valueOf(rowId)}, null);
            }
        });
    }

    @Nullable
    public Observable<T> findByRowIdRx(@Nonnull final String databaseName, final long rowId) {
        return DBToolsRxUtil.just(new Func0<T>() {
            @Override
            public T call() {
                return findBySelection(databaseName, getPrimaryKey() + "= ?", new String[]{String.valueOf(rowId)}, null);
            }
        });

    }

    @Nullable
    public Observable<T> findBySelectionRx(@Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy) {
        return DBToolsRxUtil.just(new Func0<T>() {
            @Override
            public T call() {
                return findBySelection(selection, selectionArgs, orderBy);
            }
        });
    }

    @Nullable
    public Observable<T> findBySelectionRx(@Nonnull final String databaseName, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy) {
        return DBToolsRxUtil.just(new Func0<T>() {
            @Override
            public T call() {
                Cursor cursor = findCursorBySelection(databaseName, selection, selectionArgs, orderBy);
                return createRecordFromCursor(cursor);
            }
        });
    }

    @Nullable
    public Observable<T> findByRawQueryRx(@Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<T>() {
            @Override
            public T call() {
                return findByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
            }
        });
    }

    @Nullable
    public Observable<T> findByRawQueryRx(@Nonnull final String databaseName, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<T>() {
            @Override
            public T call() {
                return findByRawQuery(databaseName, rawQuery, selectionArgs);
            }
        });
    }

    @Nullable
    private Observable<T> createRecordFromCursorRx(@Nullable final Cursor cursor) {
        return DBToolsRxUtil.just(new Func0<T>() {
            @Override
            public T call() {
                return createRecordFromCursor(cursor);
            }
        });
    }

    @Nonnull
    public Observable<List<T>> findAllByRowIdsRx(final long[] rowIds) {
        return DBToolsRxUtil.just(new Func0<List<T>>() {
            @Override
            public List<T> call() {
                return findAllByRowIds(rowIds, null);
            }
        });
    }

    @Nonnull
    public Observable<List<T>> findAllByRowIdsRx(final long[] rowIds, @Nullable final String orderBy) {
        return DBToolsRxUtil.just(new Func0<List<T>>() {
            @Override
            public List<T> call() {
                return findAllByRowIds(getDatabaseName(), rowIds, orderBy);
            }
        });
    }

    @Nonnull
    public Observable<List<T>> findAllByRowIdsRx(@Nonnull final String databaseName, final long[] rowIds, @Nullable final String orderBy) {
        return DBToolsRxUtil.just(new Func0<List<T>>() {
            @Override
            public List<T> call() {
                return findAllByRowIds(databaseName, rowIds, orderBy);
            }
        });
    }

    public Observable<Long> findCountRx() {
        return DBToolsRxUtil.just(new Func0<Long>() {
            @Override
            public Long call() {
                return findCountBySelection(null, null);
            }
        });
    }

    public Observable<Long> findCountRx(@Nonnull final String databaseName) {
        return DBToolsRxUtil.just(new Func0<Long>() {
            @Override
            public Long call() {
                return findCountBySelection(databaseName, null, null);
            }
        });
    }

    public Observable<Long> findCountBySelectionRx(@Nullable final String selection, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<Long>() {
            @Override
            public Long call() {
                return findCountBySelection(getDatabaseName(), selection, selectionArgs);
            }
        });
    }

    public Observable<Long> findCountBySelectionRx(@Nonnull final String databaseName, @Nullable final String selection, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<Long>() {
            @Override
            public Long call() {
                return findCountBySelection(getReadableDatabase(databaseName), getTableName(), selection, selectionArgs);
            }
        });

    }

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
    public Observable<Long> findCountByRawQueryRx(@Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {

        return DBToolsRxUtil.just(new Func0<Long>() {
            @Override
            public Long call() {
                return findCountByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
            }
        });
    }

    public Observable<Long> findCountByRawQueryRx(@Nonnull final String databaseName, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {

        return DBToolsRxUtil.just(new Func0<Long>() {
            @Override
            public Long call() {
                return findCountByRawQuery(getReadableDatabase(databaseName), rawQuery, selectionArgs);
            }
        });
    }

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
     * @param defaultValue  Value returned if nothing is found
     * @return query results value or defaultValue if no data was returned
     */
    public <I> Observable<I> findValueByRawQueryRx(@Nonnull final Class<I> valueType, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs, final I defaultValue) {
        return DBToolsRxUtil.just(new Func0<I>() {
            @Override
            public I call() {
                return findValueByRawQuery(getDatabaseName(), valueType, rawQuery, selectionArgs, defaultValue);
            }
        });
    }

    /**
     * Return the first column and first row value as the value for given rawQuery and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param rawQuery      Query contain first column which is the needed value
     * @param selectionArgs Query parameters
     * @param defaultValue  Value returned if nothing is found
     * @return query results value or defaultValue if no data was returned
     */
    public <I> Observable<I> findValueByRawQueryRx(@Nonnull final String databaseName, @Nonnull final Class<I> valueType, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs, final I defaultValue) {
        return DBToolsRxUtil.just(new Func0<I>() {
            @Override
            public I call() {
                return findValueByRawQuery(getReadableDatabase(databaseName), valueType, rawQuery, selectionArgs, defaultValue);
            }
        });
    }

    /**
     * Return the first column and first row value as a Date for given rawQuery and selectionArgs.
     *
     * @param database      DatabaseWrapper of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param rawQuery      Query contain first column which is the needed value
     * @param selectionArgs Query parameters
     * @param defaultValue  Value returned if nothing is found
     * @return query results value or defaultValue if no data was returned
     */
    public static <I> Observable<I> findValueByRawQueryRx(@Nonnull final DatabaseWrapper database, @Nonnull final Class<I> valueType, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs, final I defaultValue) {
        return DBToolsRxUtil.just(new Func0<I>() {
            @Override
            public I call() {
                return findValueByRawQuery(database, valueType, rawQuery, selectionArgs, defaultValue);
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
     * @return query results value or defaultValue if no data was returned
     */
    public <I> Observable<I> findValueBySelectionRx(@Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, final I defaultValue) {
        return DBToolsRxUtil.just(new Func0<I>() {
            @Override
            public I call() {
                return findValueBySelection(getDatabaseName(), valueType, column, selection, selectionArgs, defaultValue);
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
     * @param orderBy       Order by value(s)
     * @param defaultValue  Value returned if nothing is found
     * @return query results value or defaultValue if no data was returned
     */
    public <I> Observable<I> findValueBySelectionRx(@Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, final String orderBy, final I defaultValue) {
        return DBToolsRxUtil.just(new Func0<I>() {
            @Override
            public I call() {
                return findValueBySelection(getDatabaseName(), valueType, column, selection, selectionArgs, orderBy, defaultValue);
            }
        });
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
     * @return query results value or defaultValue if no data was returned
     */
    public <I> Observable<I> findValueBySelectionRx(@Nonnull final String databaseName, @Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, final I defaultValue) {
        return DBToolsRxUtil.just(new Func0<I>() {
            @Override
            public I call() {
                return findValueBySelection(databaseName, valueType, column, selection, selectionArgs, null, defaultValue);
            }
        });
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
     * @return query results value or defaultValue if no data was returned
     */
    public <I> Observable<I> findValueBySelectionRx(@Nonnull final String databaseName, @Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, final String orderBy, final I defaultValue) {
        return DBToolsRxUtil.just(new Func0<I>() {
            @Override
            public I call() {
                return findValueBySelection(getReadableDatabase(databaseName), getTableName(), valueType, column, selection, selectionArgs, orderBy, defaultValue);
            }
        });
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
     * @return query results value or defaultValue if no data was returned
     */
    public static <I> Observable<I> findValueBySelectionRx(@Nonnull final DatabaseWrapper database, @Nonnull final String tableName, @Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, final String orderBy, final I defaultValue) {
        return DBToolsRxUtil.just(new Func0<I>() {
            @Override
            public I call() {
                return findValueBySelection(database, tableName, valueType, column, selection, selectionArgs, orderBy, defaultValue);
            }
        });
    }

    /**
     * Return a list of all of the first column values as a List for given rawQuery and selectionArgs.
     *
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param rawQuery      Query contain first column contains value
     * @param selectionArgs Query parameters
     * @return query results List or empty List returned
     */
    @Nonnull
    public <I> Observable<List<I>> findAllValuesByRawQueryRx(@Nonnull final Class<I> valueType, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<List<I>>() {
            @Override
            public List<I> call() {
                return findAllValuesByRawQuery(getDatabaseName(), valueType, rawQuery, selectionArgs);
            }
        });
    }

    /**
     * Return a list of all of the first column values as a List for given rawQuery and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param rawQuery      Query contain first column contains value
     * @param selectionArgs Query parameters
     * @return query results List or empty List returned
     */
    @Nonnull
    public <I> Observable<List<I>> findAllValuesByRawQueryRx(@Nonnull final String databaseName, @Nonnull final Class<I> valueType, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<List<I>>() {
            @Override
            public List<I> call() {
                return findAllValuesByRawQuery(databaseName, valueType, 0, rawQuery, selectionArgs);
            }
        });
    }

    /**
     * Return a list of all of the first column values as a List for given rawQuery and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param rawQuery      Query contain first column contains value
     * @param selectionArgs Query parameters
     * @return query results List or empty List returned
     */
    @Nonnull
    public <I> Observable<List<I>> findAllValuesByRawQueryRx(@Nonnull final String databaseName, @Nonnull final Class<I> valueType, final int columnIndex, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<List<I>>() {
            @Override
            public List<I> call() {
                return findAllValuesByRawQuery(getReadableDatabase(databaseName), valueType, columnIndex, rawQuery, selectionArgs);
            }
        });

    }

    /**
     * Return a list of all of the first column values as a List for given rawQuery and selectionArgs.
     *
     * @param database      DatabaseWrapper of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param rawQuery      Query contain first column contains value
     * @param selectionArgs Query parameters
     * @return query results List or empty List returned
     */
    public static <I> Observable<List<I>> findAllValuesByRawQueryRx(@Nonnull final DatabaseWrapper database, @Nonnull final Class<I> valueType, final int columnIndex, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<List<I>>() {
            @Override
            public List<I> call() {
                return findAllValuesByRawQuery(database, valueType, columnIndex, rawQuery, selectionArgs);
            }
        });
    }

    /**
     * Return a list of all values for the specified column for given selection and selectionArgs.
     *
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @return query results List or empty List returned
     */
    @Nonnull
    public <I> Observable<List<I>> findAllValuesBySelectionRx(@Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs) {
        return DBToolsRxUtil.just(new Func0<List<I>>() {
            @Override
            public List<I> call() {
                return findAllValuesBySelection(getDatabaseName(), valueType, column, selection, selectionArgs, null);
            }
        });
    }

    /**
     * Return a list of all values for the specified column for given selection and selectionArgs.
     *
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param orderBy       Query order by
     * @return query results List or empty List returned
     */
    @Nonnull
    public <I> Observable<List<I>> findAllValuesBySelectionRx(@Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy) {
        return DBToolsRxUtil.just(new Func0<List<I>>() {
            @Override
            public List<I> call() {
                return findAllValuesBySelection(getDatabaseName(), valueType, column, selection, selectionArgs, orderBy);
            }
        });
    }

    /**
     * Return a list of all values for the specified column for given selection and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param orderBy       Query order by
     * @return query results List or empty List returned
     */
    @Nonnull
    public <I> Observable<List<I>> findAllValuesBySelectionRx(@Nonnull final String databaseName, @Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy) {
        return DBToolsRxUtil.just(new Func0<List<I>>() {
            @Override
            public List<I> call() {
                return findAllValuesBySelection(getReadableDatabase(databaseName), getTableName(), valueType, column, selection, selectionArgs, orderBy);
            }
        });
    }

    /**
     * Return a list of all values for the specified column for given selection and selectionArgs.
     *
     * @param database      DatabaseWrapper of database to query
     * @param tableName     Table to run query against
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param orderBy       Query order by
     * @return query results List or empty List returned
     */
    @Nonnull
    public static <I> Observable<List<I>> findAllValuesBySelectionRx(@Nonnull final DatabaseWrapper database, @Nonnull final String tableName, @Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy) {
        return DBToolsRxUtil.just(new Func0<List<I>>() {
            @Override
            public List<I> call() {
                return findAllValuesBySelection(database, tableName, valueType, column, selection, selectionArgs, orderBy);
            }
        });
    }

    private static <I> Observable<DatabaseValue<I>> getDatabaseValueRx(final Class<I> type) {
        return DBToolsRxUtil.just(new Func0<DatabaseValue<I>>() {
            @Override
            public DatabaseValue<I> call() {
                return DatabaseValueUtil.getDatabaseValue(type);
            }
        });
    }

    public Observable<Boolean> tableExistsRx(@Nonnull final String tableName) {
        return DBToolsRxUtil.just(new Func0<Boolean>() {
            @Override
            public Boolean call() {
                return tableExists(getDatabaseName(), tableName);
            }
        });
    }

    public Observable<Boolean> tableExistsRx(@Nonnull final String databaseName, @Nonnull final String tableName) {
        return DBToolsRxUtil.just(new Func0<Boolean>() {
            @Override
            public Boolean call() {
                return tableExists(getReadableDatabase(databaseName), tableName);
            }
        });
    }

    public static Observable<Boolean> tableExistsRx(@Nonnull final AndroidDatabase androidDatabase, @Nonnull final String tableName) {
        return DBToolsRxUtil.just(new Func0<Boolean>() {
            @Override
            public Boolean call() {
                return tableExists(androidDatabase.getDatabaseWrapper(), tableName);
            }
        });
    }

    public static Observable<Boolean> tableExistsRx(@Nullable final DatabaseWrapper db, @Nullable final String tableName) {
        return DBToolsRxUtil.just(new Func0<Boolean>() {
            @Override
            public Boolean call() {
                return tableExists(db, tableName);
            }
        });
    }

    @Nullable
    public Observable<MatrixCursor> toMatrixCursorRx(@Nonnull final T record) {
        return DBToolsRxUtil.just(new Func0<MatrixCursor>() {
            @Override
            public MatrixCursor call() {
                return toMatrixCursor(record);
            }
        });
    }

    @Nullable
    public Observable<MatrixCursor> toMatrixCursorRx(@Nonnull final T... records) {
        return DBToolsRxUtil.just(new Func0<MatrixCursor>() {
            @Override
            public MatrixCursor call() {
                return toMatrixCursor(Arrays.asList(records));
            }
        });
    }

    @Nullable
    public Observable<MatrixCursor> toMatrixCursorRx(@Nonnull final List<T> records) {
        return DBToolsRxUtil.just(new Func0<MatrixCursor>() {
            @Override
            public MatrixCursor call() {
                return toMatrixCursor(records);
            }
        });
    }

    public Observable<MatrixCursor> toMatrixCursorRx(final String[] columns, final List<T> records) {
        return DBToolsRxUtil.just(new Func0<MatrixCursor>() {
            @Override
            public MatrixCursor call() {
                return toMatrixCursor(columns, records);
            }
        });
    }

    public Observable<Cursor> mergeCursorsRx(final Cursor... cursors) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return new MergeCursor(cursors);
            }
        });
    }

    public Observable<Cursor> addAllToCursorTopRx(final Cursor cursor, final List<T> records) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return mergeCursors(toMatrixCursor(records), cursor);
            }
        });
    }

    public Observable<Cursor> addAllToCursorTopRx(final Cursor cursor, final T... records) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return mergeCursors(toMatrixCursor(records), cursor);
            }
        });
    }

    public Observable<Cursor> addAllToCursorBottomRx(final Cursor cursor, final List<T> records) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return mergeCursors(cursor, toMatrixCursor(records));
            }
        });
    }

    public Observable<Cursor> addAllToCursorBottomRx(final Cursor cursor, final T... records) {
        return DBToolsRxUtil.just(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return mergeCursors(cursor, toMatrixCursor(records));
            }
        });
    }
}
