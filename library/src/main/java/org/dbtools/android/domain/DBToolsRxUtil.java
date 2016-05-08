package org.dbtools.android.domain;

import rx.Observable;
import rx.functions.Func0;

public final class DBToolsRxUtil {
    private DBToolsRxUtil() {
    }

    public static <T> Observable<T> just(final Func0<T> func) {
        return Observable.defer(new Func0<Observable<T>>() {
            @Override
            public Observable<T> call() {
                return Observable.just(func.call());
            }
        });
    }

    public static <T> Observable<T> from(final Func0<Iterable<T>> func) {
        return Observable.defer(new Func0<Observable<T>>() {
            @Override
            public Observable<T> call() {
                return Observable.from(func.call());
            }
        });
    }
}
