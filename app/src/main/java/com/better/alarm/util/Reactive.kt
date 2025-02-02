package com.better.alarm.util

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

fun <T, O> Observable<T>.mapNotNull(func: (T) -> O?): Observable<O> {
    return map { Optional.fromNullable<O>(func(it)) }
        .filter { it.isPresent() }
        .map { it.get() }
}

fun <T : Any> BehaviorSubject<T>.modify(func: (T).(T) -> T) {
    value?.let {
        onNext(func(it, it))
    }
}

fun <T : Any> BehaviorSubject<T>.requireValue(): T {
    return requireNotNull(value)
}

fun <T : Any> Observable<T>.subscribeWith(disposable: CompositeDisposable, func: (T) -> Unit) {
    disposable.add(subscribe(func))
}

fun <T : Any> Single<T>.subscribeWith(disposable: CompositeDisposable, func: (T) -> Unit) {
    disposable.add(subscribe(func))
}

fun <T : Any> Observable<T>.firstOrError(predicate: (T) -> Boolean): Single<T> {
    return filter(predicate).firstOrError()
}

fun <T : Any> Observable<T>.subscribeForever(consumer: (T) -> Unit) {
    subscribe { consumer(it) }.apply { }
}
