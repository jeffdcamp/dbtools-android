package org.dbtools.android.domain;

import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface AsyncManager<T extends AndroidBaseRecord> {
    long insert(@Nonnull String databaseName, @Nonnull T e);
    int update(@Nonnull String databaseName, @Nonnull T e);
    int update(@Nonnull String databaseName, @Nonnull DBToolsContentValues contentValues, @Nullable String where, @Nullable String[] whereArgs);
    int delete(@Nonnull String databaseName, @Nonnull T e);
    int delete(@Nonnull String databaseName, @Nullable String where, @Nullable String[] whereArgs);
}
