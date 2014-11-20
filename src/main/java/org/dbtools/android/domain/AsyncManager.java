package org.dbtools.android.domain;

import android.content.ContentValues;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface AsyncManager<T extends AndroidBaseRecord> {
    long insert(@Nonnull String databaseName, @Nonnull T e);
    int update(@Nonnull String databaseName, @Nonnull T e);
    int update(@Nonnull String databaseName,  @Nonnull ContentValues contentValues, @Nullable String where, @Nullable String[] whereArgs);
    int delete(@Nonnull String databaseName, @Nonnull T e);
    int delete(@Nonnull String databaseName, @Nullable String where, @Nullable String[] whereArgs);
}
