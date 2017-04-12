package org.dbtools.android.domain;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;

import org.dbtools.android.domain.database.DatabaseWrapper;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;

@SuppressWarnings("UnusedDeclaration")
public abstract class RxAndroidBaseManager<T extends AndroidBaseRecord> extends AndroidBaseManager<T> {

    public RxAndroidBaseManager(AndroidDatabaseManager androidDatabaseManager) {
        super(androidDatabaseManager);
    }

    public Single<Cursor> findCursorAllRx() {
        return Single.create(new SingleOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Cursor> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.findCursorAll());
            }
        });
    }

    @Nonnull
    public Single<Cursor> findCursorByRawQueryRx(@Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return Single.create(new SingleOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Cursor> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.findCursorByRawQuery(getDatabaseName(), rawQuery, selectionArgs));
            }
        });
    }

    @Nonnull
    public Single<Cursor> findCursorByRawQueryRx(@Nonnull final String databaseName, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return Single.create(new SingleOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Cursor> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.getReadableDatabase(databaseName).rawQuery(rawQuery, selectionArgs));
            }
        });
    }

    @Nonnull
    public Single<Cursor> findCursorBySelectionRx(@Nullable final String selection, @Nullable final String orderBy) {
        return Single.create(new SingleOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Cursor> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.findCursorBySelection(selection, new String[]{}, orderBy));
            }
        });
    }

    @Nonnull
    public Single<Cursor> findCursorBySelectionRx(@Nonnull final String databaseName, @Nullable final String selection, @Nullable final String orderBy) {
        return Single.create(new SingleOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Cursor> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.findCursorBySelection(databaseName, selection, null, orderBy));
            }
        });
    }

    @Nonnull
    public Single<Cursor> findCursorBySelectionRx(@Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy) {
        return Single.create(new SingleOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Cursor> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.findCursorBySelection(getDatabaseName(), selection, selectionArgs, orderBy));
            }
        });
    }

    @Nonnull
    public Single<Cursor> findCursorBySelectionRx(@Nonnull final String databaseName, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy) {
        return Single.create(new SingleOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Cursor> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.findCursorBySelection(databaseName, true, getTableName(), getAllColumns(), selection, selectionArgs, null, null, orderBy, null));
            }
        });
    }

    @Nonnull
    public Single<Cursor> findCursorBySelectionRx(final boolean distinct, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String groupBy, @Nullable final String having, @Nullable final String orderBy, @Nullable final String limit) {
        return Single.create(new SingleOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Cursor> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.findCursorBySelection(getDatabaseName(), distinct, getTableName(), getAllColumns(), selection, selectionArgs, groupBy, having, orderBy, limit));
            }
        });
    }

    @Nonnull
    public Single<Cursor> findCursorBySelectionRx(@Nonnull final String databaseName, final boolean distinct, @Nonnull final String table, @Nonnull final String[] columns, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String groupBy, @Nullable final String having, @Nullable final String orderBy, @Nullable final String limit) {
        return Single.create(new SingleOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Cursor> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.findCursorBySelection(databaseName, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit));
            }
        });
    }

    @Nonnull
    public Single<Cursor> findCursorByRowIdRx(final long rowId) {
        return Single.create(new SingleOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Cursor> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.findCursorBySelection(getPrimaryKey() + "= ?", new String[]{String.valueOf(rowId)}, null));
            }
        });
    }

    @Nonnull
    public Single<Cursor> findCursorByRowIdRx(@Nonnull final String databaseName, final long rowId) {
        return Single.create(new SingleOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Cursor> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.findCursorBySelection(databaseName, getPrimaryKey() + "= ?", new String[]{String.valueOf(rowId)}, null));
            }
        });
    }

    // ==========

    @Nonnull
    public Single<List<T>> findAllRx() {
        return findAllBySelectionRx(null, null, null);
    }

    @Nonnull
    public Single<List<T>> findAllRx(@Nonnull final String databaseName) {
        return findAllBySelectionRx(databaseName, null, null, null);
    }

    @Nonnull
    public Single<List<T>> findAllOrderByRx(@Nullable final String orderBy) {
        return findAllBySelectionRx(null, null, orderBy);
    }

    @Nonnull
    public Single<List<T>> findAllOrderByRx(@Nonnull final String databaseName, @Nullable final String orderBy) {
        return findAllBySelectionRx(databaseName, null, null, orderBy);
    }

    // **********

    @Nonnull
    public Observable<T> findAllRxStream() {
        return findAllBySelectionRxStream(null, null, null);
    }

    @Nonnull
    public Observable<T> findAllRxStream(@Nonnull final String databaseName) {
        return findAllBySelectionRxStream(databaseName, null, null, null);
    }

    @Nonnull
    public Observable<T> findAllOrderByRxStream(@Nullable final String orderBy) {
        return findAllBySelectionRxStream(null, null, orderBy);
    }
    @Nonnull
    public Observable<T> findAllOrderByRxStream(@Nonnull final String databaseName, @Nullable final String orderBy) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                RxAndroidBaseManager.this.emitAllItemsFromCursor(e, findCursorBySelection(databaseName, null, null, orderBy));
            }
        });
    }


    // ==========

    @Nonnull
    public Maybe<T> findByRowIdRx(final long rowId) {
        return findBySelectionRx(getPrimaryKey() + "= ?", new String[]{String.valueOf(rowId)}, null);
    }

    @Nonnull
    public Maybe<T> findByRowIdRx(@Nonnull final String databaseName, final long rowId) {
        return findBySelectionRx(databaseName, getPrimaryKey() + "= ?", new String[]{String.valueOf(rowId)}, null);
    }

    @Nonnull
    public Maybe<T> findBySelectionRx(@Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy) {
        return findBySelectionRx(selection, selectionArgs, null, null, orderBy, null);
    }

    @Nonnull
    public Maybe<T> findBySelectionRx(@Nonnull final String databaseName, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy) {
        return findBySelectionRx(getDatabaseName(), true, getTableName(), getAllColumns(), selection, selectionArgs, null, null, orderBy, null);
    }

    @Nonnull
    public Maybe<T> findBySelectionRx(@Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy, @Nullable String limit) {
        return findBySelectionRx(getDatabaseName(), true, getTableName(), getAllColumns(), selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    @Nonnull
    public Maybe<T> findBySelectionRx(@Nonnull final String databaseName, final boolean distinct, @Nonnull final String table, @Nonnull final String[] columns, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String groupBy, @Nullable final String having, @Nullable final String orderBy, @Nullable final String limit) {
        return Maybe.create(new MaybeOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<T> e) throws Exception {
                T result = RxAndroidBaseManager.this.findBySelection(databaseName, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy);
                if (result != null) {
                    e.onSuccess(result);
                } else {
                    e.onComplete();
                }
            }
        });
    }

    // ==========

    @Nonnull
    public Single<List<T>> findAllBySelectionRx(@Nullable final String selection, @Nullable final String[] selectionArgs) {
        return findAllBySelectionRx(selection, selectionArgs, null, null, null, null);
    }

    @Nonnull
    public Single<List<T>> findAllBySelectionRx(@Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy) {
        return findAllBySelectionRx(selection, selectionArgs, null, null, orderBy, null);
    }

    @Nonnull
    public Single<List<T>> findAllBySelectionRx(@Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy, @Nullable String limit) {
        return findAllBySelectionRx(getDatabaseName(), true, getTableName(), getAllColumns(), selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    @Nonnull
    public Single<List<T>> findAllBySelectionRx(@Nonnull String databaseName, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        return findAllBySelectionRx(databaseName, true, getTableName(), getAllColumns(), selection, selectionArgs, null, null, orderBy, null);
    }

    @Nonnull
    public Single<List<T>> findAllBySelectionRx(@Nonnull final String databaseName, final boolean distinct, @Nonnull final String table, @Nonnull final String[] columns, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String groupBy, @Nullable final String having, @Nullable final String orderBy, @Nullable final String limit) {
        return Single.create(new SingleOnSubscribe<List<T>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<List<T>> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.findAllBySelection(databaseName, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit));
            }
        });
    }

    // **********

    @Nonnull
    public Observable<T> findAllBySelectionRxStream(@Nullable final String selection, @Nullable final String[] selectionArgs) {
        return findAllBySelectionRxStream(selection, selectionArgs, null, null, null, null);
    }

    @Nonnull
    public Observable<T> findAllBySelectionRxStream(@Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy) {
        return findAllBySelectionRxStream(selection, selectionArgs, null, null, orderBy, null);
    }

    @Nonnull
    public Observable<T> findAllBySelectionRxStream(@Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy, @Nullable String limit) {
        return findAllBySelectionRxStream(getDatabaseName(), true, getTableName(), getAllColumns(), selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    @Nonnull
    public Observable<T> findAllBySelectionRxStream(@Nonnull String databaseName, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        return findAllBySelectionRxStream(databaseName, true, getTableName(), getAllColumns(), selection, selectionArgs, null, null, orderBy, null);
    }

    @Nonnull
    public Observable<T> findAllBySelectionRxStream(@Nonnull final String databaseName, final boolean distinct, @Nonnull final String table, @Nonnull final String[] columns, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String groupBy, @Nullable final String having, @Nullable final String orderBy, @Nullable final String limit) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                RxAndroidBaseManager.this.emitAllItemsFromCursor(e, findCursorBySelection(databaseName, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit));
            }
        });
    }

    // ==========

    @Nonnull
    public Maybe<T> findByRawQueryRx(@Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return findByRawQueryRx(getDatabaseName(), rawQuery, selectionArgs);
    }

    @Nonnull
    public Maybe<T> findByRawQueryRx(@Nonnull final String databaseName, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return Maybe.create(new MaybeOnSubscribe<T>() {
                                @Override
                                public void subscribe(@NonNull MaybeEmitter<T> e) throws Exception {
                                    T result = RxAndroidBaseManager.this.findByRawQuery(databaseName, rawQuery, selectionArgs);
                                    if (result != null) {
                                        e.onSuccess(result);
                                    } else {
                                        e.onComplete();
                                    }
                                }
                            }
        );
    }

    // ==========

    @Nonnull
    public Single<List<T>> findAllByRawQueryRx(@Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return findAllByRawQueryRx(getDatabaseName(), rawQuery, selectionArgs);
    }

    @Nonnull
    public Single<List<T>> findAllByRawQueryRx(@Nonnull final String databaseName, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return Single.create(new SingleOnSubscribe<List<T>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<List<T>> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.findAllByRawQuery(databaseName, rawQuery, selectionArgs));
            }
        });
    }

    // **********

    @Nonnull
    public Observable<T> findAllByRawQueryRxStream(@Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return findAllByRawQueryRxStream(getDatabaseName(), rawQuery, selectionArgs);
    }

    @Nonnull
    public Observable<T> findAllByRawQueryRxStream(@Nonnull final String databaseName, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                RxAndroidBaseManager.this.emitAllItemsFromCursor(e, findCursorByRawQuery(databaseName, rawQuery, selectionArgs));
            }
        });
    }

    // ==========

    @Nonnull
    public Single<List<T>> findAllByRowIdsRx(final long[] rowIds) {
        return findAllByRowIdsRx(rowIds, null);
    }

    @Nonnull
    public Single<List<T>> findAllByRowIdsRx(final long[] rowIds, @Nullable final String orderBy) {
        return findAllByRowIdsRx(getDatabaseName(), rowIds, orderBy);
    }

    @Nonnull
    public Single<List<T>> findAllByRowIdsRx(@Nonnull final String databaseName, final long[] rowIds, @Nullable final String orderBy) {
        StringBuilder sb = new StringBuilder();
        for (long rowId : rowIds) {
            if (sb.length() > 0) {
                sb.append(" OR ");
            }
            sb.append(getPrimaryKey()).append(" = ").append(rowId);
        }

        return findAllBySelectionRx(databaseName, sb.toString(), null, orderBy);
    }

    // **********

    @Nonnull
    public Observable<T> findAllByRowIdsRxStream(final long[] rowIds) {
        return findAllByRowIdsRxStream(rowIds, null);
    }

    @Nonnull
    public Observable<T> findAllByRowIdsRxStream(final long[] rowIds, @Nullable final String orderBy) {
        return findAllByRowIdsRxStream(getDatabaseName(), rowIds, orderBy);
    }

    @Nonnull
    public Observable<T> findAllByRowIdsRxStream(@Nonnull final String databaseName, final long[] rowIds, @Nullable final String orderBy) {
        final StringBuilder sb = new StringBuilder();
        for (long rowId : rowIds) {
            if (sb.length() > 0) {
                sb.append(" OR ");
            }
            sb.append(getPrimaryKey()).append(" = ").append(rowId);
        }

        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                RxAndroidBaseManager.this.emitAllItemsFromCursor(e, findCursorBySelection(databaseName, sb.toString(), null, orderBy));
            }
        });
    }

    // ==========

    @Nonnull
    public Single<Long> findCountRx() {
        return Single.create(new SingleOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Long> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.findCountBySelection(null, null));
            }
        });
    }

    @Nonnull
    public Single<Long> findCountRx(@Nonnull final String databaseName) {
        return Single.create(new SingleOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Long> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.findCountBySelection(databaseName, null, null));
            }
        });
    }

    @Nonnull
    public Single<Long> findCountBySelectionRx(@Nullable final String selection, @Nullable final String[] selectionArgs) {
        return Single.create(new SingleOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Long> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.findCountBySelection(getDatabaseName(), selection, selectionArgs));
            }
        });
    }

    @Nonnull
    public Single<Long> findCountBySelectionRx(@Nonnull final String databaseName, @Nullable final String selection, @Nullable final String[] selectionArgs) {
        return Single.create(new SingleOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Long> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.findCountBySelection(databaseName, selection, selectionArgs));
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
    public Single<Long> findCountByRawQueryRx(@Nonnull final String rawQuery) {
        return Single.create(new SingleOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Long> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.findCountByRawQuery(getDatabaseName(), rawQuery, null));
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
    public Single<Long> findCountByRawQueryRx(@Nonnull final String databaseName, @Nonnull final String rawQuery) {
        return Single.create(new SingleOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Long> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.findCountByRawQuery(databaseName, rawQuery, null));
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
    public Single<Long> findCountByRawQueryRx(@Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {
        return Single.create(new SingleOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Long> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.findCountByRawQuery(getDatabaseName(), rawQuery, selectionArgs));
            }
        });
    }

    @Nonnull
    public Single<Long> findCountByRawQueryRx(@Nonnull final String databaseName, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs) {

        return Single.create(new SingleOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Long> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.findCountByRawQuery(databaseName, rawQuery, selectionArgs));
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
    public <I> Single<I> findValueByRawQueryRx(@Nonnull final Class<I> valueType, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs, I defaultValue) {
        return findValueByRawQueryRx(getDatabaseName(), valueType, rawQuery, selectionArgs, defaultValue);
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
    public <I> Single<I> findValueByRawQueryRx(@Nonnull final String databaseName, @Nonnull final Class<I> valueType, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs, final I defaultValue) {
        return findValueByRawQueryRx(getReadableDatabase(databaseName), valueType, rawQuery, selectionArgs, defaultValue);
    }

    /**
     * Return the first column and first row value as a Date for given rawQuery and selectionArgs.
     *
     * @param database      DatabaseWrapper of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param rawQuery      Query contain first column which is the needed value
     * @param selectionArgs Query parameters
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    @Nonnull
    public static <I> Single<I> findValueByRawQueryRx(@Nonnull final DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> database, @Nonnull final Class<I> valueType, @Nonnull final String rawQuery, @Nullable final String[] selectionArgs, final I defaultValue) {
        return Single.create(new SingleOnSubscribe<I>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<I> e) throws Exception {
                e.onSuccess(findValueByRawQuery(database, valueType, rawQuery, selectionArgs, defaultValue));
            }
        });
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    @Nonnull
    public <I> Single<I> findValueByRowIdRx(@Nonnull final Class<I> valueType, @Nonnull final String column, long rowId, final I defaultValue) {
        return findValueBySelectionRx(getDatabaseName(), valueType, column, getPrimaryKey() + " = " + rowId, null, defaultValue);
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
    public <I> Single<I> findValueBySelectionRx(@Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, final I defaultValue) {
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
    public <I> Single<I> findValueBySelectionRx(@Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, final String orderBy, final I defaultValue) {
        return findValueBySelectionRx(valueType, column, selection, selectionArgs, null, null, orderBy, defaultValue);
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
    public <I> Single<I> findValueBySelectionRx(@Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String groupBy, @Nullable final String having, final String orderBy, final I defaultValue) {
        return findValueBySelectionRx(getDatabaseName(), valueType, column, selection, selectionArgs, groupBy, having, orderBy, defaultValue);
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
    public <I> Single<I> findValueBySelectionRx(@Nonnull final String databaseName, @Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, final I defaultValue) {
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
    public <I> Single<I> findValueBySelectionRx(@Nonnull final String databaseName, @Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy, @Nullable final I defaultValue) {
        return findValueBySelectionRx(databaseName, valueType, column, selection, selectionArgs, null, null, orderBy, defaultValue);
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
    public <I> Single<I> findValueBySelectionRx(@Nonnull final String databaseName, @Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String groupBy, @Nullable final String having, @Nullable final String orderBy, @Nullable final I defaultValue) {
        return findValueBySelectionRx(getReadableDatabase(databaseName), getTableName(), valueType, column, selection, selectionArgs, groupBy, having, orderBy, defaultValue);
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
    public static <I> Single<I> findValueBySelectionRx(@Nonnull final DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> database, @Nonnull final String tableName, @Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy, @Nullable final I defaultValue) {
        return findValueBySelectionRx(database, tableName, valueType, column, selection, selectionArgs, null, null, orderBy, defaultValue);
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
    public static <I> Single<I> findValueBySelectionRx(@Nonnull final DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> database, @Nonnull final String tableName, @Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String groupBy, @Nullable final String having, @Nullable final String orderBy, final I defaultValue) {
        return Single.create(new SingleOnSubscribe<I>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<I> e) throws Exception {
                e.onSuccess(findValueBySelection(database, tableName, valueType, column, selection, selectionArgs, groupBy, having, orderBy, defaultValue));
            }
        });
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @return query results List or empty List returned
     */
    @Nonnull
    public <I> Single<List<I>> findAllValuesBySelectionRx(@Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs) {
        return findAllValuesBySelectionRx(getDatabaseName(), valueType, column, selection, selectionArgs, null, null, null, null);
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @return query results List or empty List returned
     */
    @Nonnull
    public <I> Single<List<I>> findAllValuesBySelectionRx(@Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs,  @Nullable final String orderBy) {
        return findAllValuesBySelectionRx(getDatabaseName(), valueType, column, selection, selectionArgs, null, null, orderBy, null);
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @return query results List or empty List returned
     */
    @Nonnull
    public <I> Single<List<I>> findAllValuesBySelectionRx(@Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String groupBy, @Nullable final String having, @Nullable final String orderBy, @Nullable final String limit) {
        return findAllValuesBySelectionRx(getDatabaseName(), valueType, column, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @return query results List or empty List returned
     */
    @Nonnull
    public <I> Single<List<I>> findAllValuesBySelectionRx(@Nonnull String databaseName, @Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String groupBy, @Nullable final String having, @Nullable final String orderBy, @Nullable final String limit) {
        return findAllValuesBySelectionRx(getReadableDatabase(databaseName), getTableName(), valueType, column, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @return query results List or empty List returned
     */
    @Nonnull
    public static <I> Single<List<I>> findAllValuesBySelectionRx(@Nonnull final DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> database, @Nonnull final String tableName, @Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String orderBy) {
        return findAllValuesBySelectionRx(database, tableName, valueType, column, selection, selectionArgs, null, null, orderBy, null);
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @return query results List or empty List returned
     */
    @Nonnull
    public static <I> Single<List<I>> findAllValuesBySelectionRx(@Nonnull final DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> database, @Nonnull final String tableName, @Nonnull final Class<I> valueType, @Nonnull final String column, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable final String groupBy, @Nullable final String having, @Nullable final String orderBy, @Nullable final String limit) {
        return Single.create(new SingleOnSubscribe<List<I>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<List<I>> e) throws Exception {
                e.onSuccess(findAllValuesBySelection(database, tableName, valueType, column, selection, selectionArgs, groupBy, having, orderBy, limit));
            }
        });
    }

    @Nonnull
    public Single<Boolean> tableExistsRx(@Nonnull final String tableName) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Boolean> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.tableExists(getDatabaseName(), tableName));
            }
        });
    }

    @Nonnull
    public Single<Boolean> tableExistsRx(@Nonnull final String databaseName, @Nonnull final String tableName) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Boolean> e) throws Exception {
                e.onSuccess(tableExists(RxAndroidBaseManager.this.getReadableDatabase(databaseName), tableName));
            }
        });
    }

    @Nonnull
    public static Single<Boolean> tableExistsRx(@Nonnull final AndroidDatabase androidDatabase, @Nonnull final String tableName) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Boolean> e) throws Exception {
                e.onSuccess(tableExists(androidDatabase.getDatabaseWrapper(), tableName));
            }
        });
    }

    @Nonnull
    public static Single<Boolean> tableExistsRx(@Nullable final DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db, @Nullable final String tableName) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Boolean> e) throws Exception {
                e.onSuccess(tableExists(db, tableName));
            }
        });
    }

    @Nonnull
    public Single<MatrixCursor> toMatrixCursorRx(@Nonnull final T record) {
        return Single.create(new SingleOnSubscribe<MatrixCursor>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<MatrixCursor> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.toMatrixCursor(record));
            }
        });
    }

    @Nonnull
    public Single<MatrixCursor> toMatrixCursorRx(@Nonnull final T... records) {
        return Single.create(new SingleOnSubscribe<MatrixCursor>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<MatrixCursor> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.toMatrixCursor(Arrays.asList(records)));
            }
        });
    }

    @Nonnull
    public Single<MatrixCursor> toMatrixCursorRx(@Nonnull final List<T> records) {
        return Single.create(new SingleOnSubscribe<MatrixCursor>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<MatrixCursor> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.toMatrixCursor(records));
            }
        });
    }

    @Nonnull
    public Single<MatrixCursor> toMatrixCursorRx(final String[] columns, final List<T> records) {
        return Single.create(new SingleOnSubscribe<MatrixCursor>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<MatrixCursor> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.toMatrixCursor(columns, records));
            }
        });
    }

    @Nonnull
    public Single<Cursor> mergeCursorsRx(final Cursor... cursors) {
        return Single.create(new SingleOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Cursor> e) throws Exception {
                e.onSuccess(new MergeCursor(cursors));
            }
        });
    }

    @Nonnull
    public Single<Cursor> addAllToCursorTopRx(final Cursor cursor, final List<T> records) {
        return Single.create(new SingleOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Cursor> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.mergeCursors(toMatrixCursor(records), cursor));
            }
        });
    }

    @Nonnull
    public Single<Cursor> addAllToCursorTopRx(final Cursor cursor, final T... records) {
        return Single.create(new SingleOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Cursor> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.mergeCursors(toMatrixCursor(records), cursor));
            }
        });
    }

    @Nonnull
    public Single<Cursor> addAllToCursorBottomRx(final Cursor cursor, final List<T> records) {
        return Single.create(new SingleOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Cursor> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.mergeCursors(cursor, toMatrixCursor(records)));
            }
        });
    }

    @Nonnull
    public Single<Cursor> addAllToCursorBottomRx(final Cursor cursor, final T... records) {
        return Single.create(new SingleOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Cursor> e) throws Exception {
                e.onSuccess(RxAndroidBaseManager.this.mergeCursors(cursor, toMatrixCursor(records)));
            }
        });
    }

    private void emitAllItemsFromCursor(ObservableEmitter<T> e, Cursor cursor) {
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    T record = newRecord();
                    record.setContent(cursor);
                    e.onNext(record);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        e.onComplete();
    }
}
