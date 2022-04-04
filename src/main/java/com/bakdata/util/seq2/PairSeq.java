/*
 * Copyright (c), 2019 bakdata GmbH
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bakdata.util.seq2;

import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Random;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.jooq.lambda.Seq;
import org.jooq.lambda.Window;
import org.jooq.lambda.tuple.Tuple2;

@SuppressWarnings("deprecation")
public interface PairSeq<K, V> extends Stream<Tuple2<K, V>>, Iterable<Tuple2<K, V>>, BaseSeq<Tuple2<K, V>> {
    /**
     * @see Seq#empty()
     */
    static <K, V> PairSeq<K, V> empty() {
        final Seq2<Tuple2<K, V>> seq = Seq2.empty();
        return seq(seq);
    }

    /**
     * @see Seq#generate(Supplier)
     */
    static <K, V> PairSeq<K, V> generate(final Supplier<? extends Tuple2<K, V>> s) {
        final Seq2<Tuple2<K, V>> seq = Seq2.generate(s);
        return seq(seq);
    }

    /**
     * @see Seq#generate(Object)
     */
    static <K, V> PairSeq<K, V> generate(final Tuple2<K, V> value) {
        final Seq2<Tuple2<K, V>> seq = Seq2.generate(value);
        return seq(seq);
    }

    /**
     * @see Seq#generate(Object)
     */
    static <K, V> PairSeq<K, V> generate(final K key, final V value) {
        final Seq2<Tuple2<K, V>> seq = Seq2.generate(new Tuple2<>(key, value));
        return seq(seq);
    }

    /**
     * @see Seq#of(Object[])
     */
    static <K, V> PairSeq<K, V> of(final Tuple2<K, V>... values) {
        final Seq2<Tuple2<K, V>> seq = Seq2.of(values);
        return seq(seq);
    }

    /**
     * @see Seq#of(Object)
     */
    static <K, V> PairSeq<K, V> of(final Tuple2<K, V> value) {
        final Seq2<Tuple2<K, V>> seq = Seq2.of(value);
        return seq(seq);
    }

    /**
     * @see Seq#of(Object)
     */
    static <K, V> PairSeq<K, V> of(final K key, final V value) {
        final Seq2<Tuple2<K, V>> seq = Seq2.of(new Tuple2<>(key, value));
        return seq(seq);
    }

    /**
     * @see Seq#seq(Stream)
     */
    static <K, V> PairSeq<K, V> seq(final Stream<? extends Tuple2<K, V>> stream) {
        final Seq2<Tuple2<K, V>> seq = Seq2.seq(stream);
        return seq(seq);
    }

    /**
     * @see Seq#seq(Supplier)
     */
    static <K, V> PairSeq<K, V> seq(final Supplier<? extends Tuple2<K, V>> s) {
        final Seq2<Tuple2<K, V>> seq = Seq2.seq(s);
        return seq(seq);
    }

    /**
     * @see Seq#seq(Iterator)
     */
    static <K, V> PairSeq<K, V> seq(final Iterator<? extends Tuple2<K, V>> iterator) {
        final Seq2<Tuple2<K, V>> seq = Seq2.seq(iterator);
        return seq(seq);
    }

    /**
     * @see Seq#seq(Enumeration)
     */
    static <K, V> PairSeq<K, V> seq(final Enumeration<Tuple2<K, V>> enumeration) {
        final Seq2<Tuple2<K, V>> seq = Seq2.seq(enumeration);
        return seq(seq);
    }

    /**
     * @see Seq#seq(Spliterator)
     */
    static <K, V> PairSeq<K, V> seq(final Spliterator<? extends Tuple2<K, V>> spliterator) {
        final Seq2<Tuple2<K, V>> seq = Seq2.seq(spliterator);
        return seq(seq);
    }

    /**
     * @see Seq#seq(Object[], int, int)
     */
    static <K, V> PairSeq<K, V> seq(final Tuple2<K, V>[] values, final int startIndex, final int endIndex) {
        final Seq2<Tuple2<K, V>> seq = Seq2.seq(values, startIndex, endIndex);
        return seq(seq);
    }

    /**
     * @see Seq#seq(Iterable)
     */
    static <K, V> PairSeq<K, V> seq(final Iterable<? extends Tuple2<K, V>> iterable) {
        final Seq2<Tuple2<K, V>> seq = Seq2.seq(iterable);
        return seq(seq);
    }

    /**
     * @see Seq#seq(Optional)
     */
    static <K, V> PairSeq<K, V> seq(final Optional<? extends Tuple2<K, V>> optional) {
        final Seq2<Tuple2<K, V>> seq = Seq2.seq(optional);
        return seq(seq);
    }

    /**
     * Wrap a {@code Stream} into a {@code PairSeq}.
     */
    static <K, V> PairSeq<K, V> seq(final Seq2<Tuple2<K, V>> seq2) {
        return new PairSeqImpl<>(seq2);
    }

    /**
     * @see Seq#seq(Seq)
     */
    static <K, V> PairSeq<K, V> seq(final Seq<Tuple2<K, V>> seq) {
        final Seq2<Tuple2<K, V>> seq2 = Seq2.seq(seq);
        return seq(seq2);
    }

    /**
     * @see Seq#seq(Map)
     */
    static <K, V> PairSeq<K, V> seq(final Map<? extends K, ? extends V> map) {
        final Seq<Tuple2<K, V>> seq = Seq.seq(map);
        return seq(seq);
    }

    /**
     * @see Stream#allMatch(Predicate)
     */
    default boolean allMatch(final BiPredicate<? super K, ? super V> predicate) {
        return this.allMatch(this.unwrap(predicate));
    }

    /**
     * @deprecated Use {@link #allMatch(BiPredicate)}
     */
    @Override
    @Deprecated
    default boolean allMatch(final Predicate<? super Tuple2<K, V>> predicate) {
        return this.toSeq2().allMatch(predicate);
    }

    /**
     * @see Stream#anyMatch(Predicate)
     */
    default boolean anyMatch(final BiPredicate<? super K, ? super V> predicate) {
        return this.anyMatch(this.unwrap(predicate));
    }

    /**
     * @deprecated Use {@link #anyMatch(BiPredicate)}
     */
    @Override
    @Deprecated
    default boolean anyMatch(final Predicate<? super Tuple2<K, V>> predicate) {
        return this.toSeq2().anyMatch(predicate);
    }

    /**
     * @see Seq#append(Stream)
     */
    default PairSeq<K, V> append(final Stream<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().append(other));
    }

    /**
     * @see Seq#append(Iterable)
     */
    default PairSeq<K, V> append(final Iterable<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().append(other));
    }

    /**
     * @see Seq#append(Seq)
     */
    default PairSeq<K, V> append(final Seq<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().append(other));
    }

    /**
     * @see Seq#append(Seq)
     */
    default PairSeq<K, V> append(final Seq2<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().append(other));
    }

    /**
     * @see Seq#append(Seq)
     */
    default PairSeq<K, V> append(final PairSeq<K, V> other) {
        return this.append(other.toSeq2());
    }

    /**
     * @see Seq#append(Object)
     * @deprecated Use {@link #append(Object, Object)}
     */
    @Deprecated
    default PairSeq<K, V> append(final Tuple2<K, V> other) {
        return seq(this.toSeq2().append(other));
    }

    /**
     * @see Seq#append(Object)
     */
    default PairSeq<K, V> append(final K otherKey, final V otherValue) {
        return this.append(new Tuple2<>(otherKey, otherValue));
    }

    /**
     * @see Seq#append(Object[])
     */
    default PairSeq<K, V> append(final Tuple2<K, V>... other) {
        return seq(this.toSeq2().append(other));
    }

    /**
     * @see Seq#append(Optional)
     */
    default PairSeq<K, V> append(final Optional<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().append(other));
    }

    /**
     * @deprecated Use {@link #avg(BiFunction)}
     */
    @Override
    @Deprecated
    default <U> Optional<U> avg(final Function<? super Tuple2<K, V>, ? extends U> function) {
        return BaseSeq.super.avg(function);
    }

    /**
     * @see Seq#avg(Function)
     */
    default <U> Optional<U> avg(final BiFunction<? super K, ? super V, ? extends U> function) {
        return this.avg(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #avgDouble(ToDoubleBiFunction)}
     */
    @Override
    @Deprecated
    default double avgDouble(final ToDoubleFunction<? super Tuple2<K, V>> function) {
        return BaseSeq.super.avgDouble(function);
    }

    /**
     * @see Seq#avgDouble(ToDoubleFunction)
     */
    default double avgDouble(final ToDoubleBiFunction<? super K, ? super V> function) {
        return this.avgDouble(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #avgInt(ToIntBiFunction)}
     */
    @Override
    @Deprecated
    default double avgInt(final ToIntFunction<? super Tuple2<K, V>> function) {
        return BaseSeq.super.avgInt(function);
    }

    /**
     * @see Seq#avgInt(ToIntFunction)
     */
    default double avgInt(final ToIntBiFunction<? super K, ? super V> function) {
        return this.avgInt(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #avgLong(ToLongBiFunction)}
     */
    @Override
    @Deprecated
    default double avgLong(final ToLongFunction<? super Tuple2<K, V>> function) {
        return BaseSeq.super.avgLong(function);
    }

    /**
     * @see Seq#avgLong(ToLongFunction)
     */
    default double avgLong(final ToLongBiFunction<? super K, ? super V> function) {
        return this.avgLong(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #bitAnd(BiFunction)}
     */
    @Override
    @Deprecated
    default <U> Optional<U> bitAnd(final Function<? super Tuple2<K, V>, ? extends U> function) {
        return BaseSeq.super.bitAnd(function);
    }

    /**
     * @see Seq#bitAnd(Function)
     */
    default <U> Optional<U> bitAnd(final BiFunction<? super K, ? super V, ? extends U> function) {
        return this.bitAnd(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #bitAndInt(ToIntBiFunction)}
     */
    @Override
    @Deprecated
    default int bitAndInt(final ToIntFunction<? super Tuple2<K, V>> function) {
        return BaseSeq.super.bitAndInt(function);
    }

    /**
     * @see Seq#bitAndInt(ToIntFunction)
     */
    default int bitAndInt(final ToIntBiFunction<? super K, ? super V> function) {
        return this.bitAndInt(this.unwrap(function));
    }

    /**
     * @see Seq#bitAndLong(ToLongFunction)
     */
    default long bitAndLong(final ToLongBiFunction<? super K, ? super V> function) {
        return this.bitAndLong(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #bitAndLong(ToLongBiFunction)}
     */
    @Override
    @Deprecated
    default long bitAndLong(final ToLongFunction<? super Tuple2<K, V>> function) {
        return BaseSeq.super.bitAndLong(function);
    }

    /**
     * @deprecated Use {@link #bitOr(BiFunction)}
     */
    @Override
    @Deprecated
    default <U> Optional<U> bitOr(final Function<? super Tuple2<K, V>, ? extends U> function) {
        return BaseSeq.super.bitOr(function);
    }

    /**
     * @see Seq#bitOr(Function)
     */
    default <U> Optional<U> bitOr(final BiFunction<? super K, ? super V, ? extends U> function) {
        return this.bitOr(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #bitOrInt(ToIntBiFunction)}
     */
    @Override
    @Deprecated
    default int bitOrInt(final ToIntFunction<? super Tuple2<K, V>> function) {
        return BaseSeq.super.bitOrInt(function);
    }

    /**
     * @see Seq#bitOrInt(ToIntFunction)
     */
    default int bitOrInt(final ToIntBiFunction<? super K, ? super V> function) {
        return this.bitOrInt(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #bitOrLong(ToLongBiFunction)}
     */
    @Override
    @Deprecated
    default long bitOrLong(final ToLongFunction<? super Tuple2<K, V>> function) {
        return BaseSeq.super.bitOrLong(function);
    }

    /**
     * @see Seq#bitOrLong(ToLongFunction)
     */
    default long bitOrLong(final ToLongBiFunction<? super K, ? super V> function) {
        return this.bitOrLong(this.unwrap(function));
    }

    /**
     * @see Seq#cast(Class)
     */
    default <U> Seq2<U> cast(final Class<? extends U> type) {
        return this.toSeq2().cast(type);
    }

    @Override
    default void close() {
        this.toSeq2().close();
    }

    @Override
    default <R> R collect(final Supplier<R> supplier, final BiConsumer<R, ? super Tuple2<K, V>> accumulator,
            final BiConsumer<R, R> combiner) {
        return this.toSeq2().collect(supplier, accumulator, combiner);
    }

    @Override
    default <R, A> R collect(final Collector<? super Tuple2<K, V>, A, R> collector) {
        return this.toSeq2().collect(collector);
    }

    /**
     * @see Seq#concat(Object)
     */
    default PairSeq<K, V> concat(final K otherKey, final V otherValue) {
        return this.concat(new Tuple2<>(otherKey, otherValue));
    }

    /**
     * @see Seq#concat(Object)
     * @deprecated Use {@link #concat(Object, Object)}
     */
    @Deprecated
    default PairSeq<K, V> concat(final Tuple2<K, V> other) {
        return seq(this.toSeq2().concat(other));
    }

    /**
     * @see Seq#concat(Object[])
     */
    default PairSeq<K, V> concat(final Tuple2<K, V>... other) {
        return seq(this.toSeq2().concat(other));
    }

    /**
     * @see Seq#concat(Iterable)
     */
    default PairSeq<K, V> concat(final Iterable<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().concat(other));
    }

    /**
     * @see Seq#concat(Stream, Stream)
     */
    default PairSeq<K, V> concat(final Stream<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().concat(other));
    }

    /**
     * @see Seq#concat(Seq)
     */
    default PairSeq<K, V> concat(final Seq<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().concat(other));
    }

    /**
     * @see Seq#concat(Seq)
     */
    default PairSeq<K, V> concat(final Seq2<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().concat(other));
    }

    /**
     * @see Seq#concat(Seq)
     */
    default PairSeq<K, V> concat(final PairSeq<K, V> other) {
        return seq(this.toSeq2().concat(other.toSeq2()));
    }

    /**
     * @see Seq#concat(Optional)
     */
    default PairSeq<K, V> concat(final Optional<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().concat(other));
    }

    /**
     * @see Seq#contains(Object)
     */
    default boolean contains(final K otherKey, final V otherValue) {
        return this.contains(new Tuple2<>(otherKey, otherValue));
    }

    /**
     * @deprecated Use {@link #contains(Object, Object)}
     */
    @Override
    @Deprecated
    default boolean contains(final Tuple2<K, V> otherValue) {
        return BaseSeq.super.contains(otherValue);
    }

    /**
     * @see Seq#containsAll(Seq)
     */
    default boolean containsAll(final PairSeq<K, V> other) {
        return this.containsAll(other.toSeq2());
    }

    /**
     * @see Seq#containsAny(Seq)
     */
    default boolean containsAny(final PairSeq<K, V> other) {
        return this.containsAny(other.toSeq2());
    }

    @Override
    default long count() {
        return this.toSeq2().count();
    }

    /**
     * @deprecated Use {@link #count(BiPredicate)}
     */
    @Override
    @Deprecated
    default long count(final Predicate<? super Tuple2<K, V>> predicate) {
        return BaseSeq.super.count(predicate);
    }

    /**
     * @see Seq#count(Predicate)
     */
    default long count(final BiPredicate<? super K, ? super V> predicate) {
        return this.count(this.unwrap(predicate));
    }

    /**
     * @deprecated Use {@link #countDistinct(BiPredicate)}
     */
    @Override
    @Deprecated
    default long countDistinct(final Predicate<? super Tuple2<K, V>> predicate) {
        return BaseSeq.super.countDistinct(predicate);
    }

    /**
     * @see Seq#countDistinct(Predicate)
     */
    default long countDistinct(final BiPredicate<? super K, ? super V> predicate) {
        return this.countDistinct(this.unwrap(predicate));
    }

    /**
     * @deprecated Use {@link #countDistinctBy(BiFunction)}
     */
    @Override
    @Deprecated
    default <U> long countDistinctBy(final Function<? super Tuple2<K, V>, ? extends U> function) {
        return BaseSeq.super.countDistinctBy(function);
    }

    /**
     * @see Seq#countDistinctBy(Function)
     */
    default <U> long countDistinctBy(final BiFunction<? super K, ? super V, ? extends U> function) {
        return this.countDistinctBy(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #countDistinctBy(BiFunction, Predicate)}
     */
    @Override
    @Deprecated
    default <U> long countDistinctBy(final Function<? super Tuple2<K, V>, ? extends U> function,
            final Predicate<? super U> predicate) {
        return BaseSeq.super.countDistinctBy(function, predicate);
    }

    /**
     * @see Seq#countDistinctBy(Function, Predicate)
     */
    default <U> long countDistinctBy(final BiFunction<? super K, ? super V, ? extends U> function,
            final Predicate<? super U> predicate) {
        return this.countDistinctBy(t -> t.map(function), predicate);
    }

    default long countDistinctKeys() {
        return this.keys().countDistinct();
    }

    default long countDistinctKeys(final Predicate<? super K> predicate) {
        return this.keys().countDistinct(predicate);
    }

    default <U> long countDistinctKeysBy(final Function<? super K, ? extends U> function) {
        return this.keys().countDistinctBy(function);
    }

    default <U> long countDistinctKeysBy(final Function<? super K, ? extends U> function,
            final Predicate<? super U> predicate) {
        return this.keys().countDistinctBy(function, predicate);
    }

    default long countDistinctValues() {
        return this.values().countDistinct();
    }

    default long countDistinctValues(final Predicate<? super V> predicate) {
        return this.values().countDistinct(predicate);
    }

    default <U> long countDistinctValuesBy(final Function<? super V, ? extends U> function) {
        return this.values().countDistinctBy(function);
    }

    default <U> long countDistinctValuesBy(final Function<? super V, ? extends U> function,
            final Predicate<? super U> predicate) {
        return this.values().countDistinctBy(function, predicate);
    }

    default long countKeys(final Predicate<? super K> predicate) {
        return this.keys().count(predicate);
    }

    default long countValues(final Predicate<? super V> predicate) {
        return this.values().count(predicate);
    }

    /**
     * @see Seq#crossApply(Function)
     * @deprecated Use {@link #crossApply(BiFunction)}
     */
    @Deprecated
    default <U> PairSeq<Tuple2<K, V>, U> crossApply(
            final Function<? super Tuple2<K, V>, ? extends Iterable<? extends U>> function) {
        return this.toSeq2().crossApply(function);
    }

    /**
     * @see Seq#crossApply(Function)
     */
    default <U> PairSeq<Tuple2<K, V>, U> crossApply(
            final BiFunction<? super K, ? super V, ? extends Iterable<? extends U>> function) {
        return this.crossApply(this.unwrap(function));
    }

    /**
     * @see Seq#crossJoin(Stream)
     */
    default <U> PairSeq<Tuple2<K, V>, U> crossJoin(final Stream<? extends U> other) {
        return this.toSeq2().crossJoin(other);
    }

    /**
     * @see Seq#crossJoin(Iterable)
     */
    default <U> PairSeq<Tuple2<K, V>, U> crossJoin(final Iterable<? extends U> other) {
        return this.toSeq2().crossJoin(other);
    }

    /**
     * @see Seq#crossJoin(Seq)
     */
    default <U> PairSeq<Tuple2<K, V>, U> crossJoin(final Seq<? extends U> other) {
        return this.toSeq2().crossJoin(other);
    }

    /**
     * @see Seq#crossJoin(Seq)
     */
    default <U> PairSeq<Tuple2<K, V>, U> crossJoin(final Seq2<? extends U> other) {
        return this.toSeq2().crossJoin(other);
    }

    /**
     * @see Seq#crossJoin(Seq)
     */
    default <K1, V1> PairSeq<Tuple2<K, V>, Tuple2<K1, V1>> crossJoin(final PairSeq<K1, V1> other) {
        return this.crossJoin(other.toSeq2());
    }

    /**
     * @see Seq#crossSelfJoin()
     */
    default PairSeq<Tuple2<K, V>, Tuple2<K, V>> crossSelfJoin() {
        return this.toSeq2().crossSelfJoin();
    }

    /**
     * @see Seq#cycle()
     */
    default PairSeq<K, V> cycle() {
        return seq(this.toSeq2().cycle());
    }

    /**
     * @see Seq#cycle(long)
     */
    default PairSeq<K, V> cycle(final long times) {
        return seq(this.toSeq2().cycle(times));
    }

    @Override
    default PairSeq<K, V> distinct() {
        return seq(this.toSeq2().distinct());
    }

    /**
     * @see Seq#distinct(Function)
     * @deprecated Use {@link #distinct(BiFunction)}
     */
    @Deprecated
    default <U> PairSeq<K, V> distinct(final Function<? super Tuple2<K, V>, ? extends U> keyExtractor) {
        return seq(this.toSeq2().distinct(keyExtractor));
    }

    /**
     * @see Seq#distinct(Function)
     */
    default <U> PairSeq<K, V> distinct(final BiFunction<? super K, ? super V, ? extends U> keyExtractor) {
        return this.distinct(this.unwrap(keyExtractor));
    }

    /**
     * @see Seq#drop(long)
     */
    default PairSeq<K, V> drop(final long n) {
        return seq(this.toSeq2().drop(n));
    }

    /**
     * @deprecated Use {@link #dropWhile(BiPredicate)}
     */
    @Override
    @Deprecated
    default PairSeq<K, V> dropWhile(final Predicate<? super Tuple2<K, V>> predicate) {
        return seq(this.toSeq2().dropWhile(predicate));
    }

    /**
     * @see Seq#dropWhile(Predicate)
     */
    default PairSeq<K, V> dropWhile(final BiPredicate<? super K, ? super V> predicate) {
        return this.dropWhile(this.unwrap(predicate));
    }

    /**
     * @see Seq#duplicate()
     */
    default Tuple2<PairSeq<K, V>, PairSeq<K, V>> duplicate() {
        return this.toSeq2().duplicate().map1(PairSeq::seq).map2(PairSeq::seq);
    }

    /**
     * @deprecated Use {@link #filter(BiPredicate)}
     */
    @Override
    @Deprecated
    default PairSeq<K, V> filter(final Predicate<? super Tuple2<K, V>> predicate) {
        return seq(this.toSeq2().filter(predicate));
    }

    /**
     * @see Stream#filter(Predicate)
     */
    default PairSeq<K, V> filter(final BiPredicate<? super K, ? super V> predicate) {
        return this.filter(this.unwrap(predicate));
    }

    default PairSeq<K, V> filterKeys(final Predicate<? super K> predicate) {
        return seq(this.toSeq2().filter(t -> predicate.test(t.v1())));
    }

    default PairSeq<K, V> filterKeysNot(final Predicate<? super K> predicate) {
        return seq(this.toSeq2().filterNot(t -> predicate.test(t.v1())));
    }

    default PairSeq<K, V> filterNot(final Predicate<? super Tuple2<K, V>> predicate) {
        return seq(this.toSeq2().filterNot(predicate));
    }

    default PairSeq<K, V> filterNot(final BiPredicate<? super K, ? super V> predicate) {
        return seq(this.toSeq2().filterNot(this.unwrap(predicate)));
    }

    default PairSeq<K, V> filterValues(final Predicate<? super V> predicate) {
        return seq(this.toSeq2().filter(t -> predicate.test(t.v2())));
    }

    default PairSeq<K, V> filterValuesNot(final Predicate<? super V> predicate) {
        return seq(this.toSeq2().filterNot(t -> predicate.test(t.v2())));
    }

    @Override
    default Optional<Tuple2<K, V>> findAny() {
        return this.toSeq2().findAny();
    }

    @Override
    default Optional<Tuple2<K, V>> findFirst() {
        return this.toSeq2().findFirst();
    }

    /**
     * @deprecated Use {@link #findFirst(BiPredicate)}
     */
    @Override
    @Deprecated
    default Optional<Tuple2<K, V>> findFirst(final Predicate<? super Tuple2<K, V>> predicate) {
        return BaseSeq.super.findFirst(predicate);
    }

    /**
     * @see Seq#findFirst(Predicate)
     */
    default Optional<Tuple2<K, V>> findFirst(final BiPredicate<? super K, ? super V> predicate) {
        return this.findFirst(this.unwrap(predicate));
    }

    /**
     * @see Seq#findLast(Predicate)
     */
    default Optional<Tuple2<K, V>> findLast(final BiPredicate<? super K, ? super V> predicate) {
        return this.findLast(this.unwrap(predicate));
    }

    /**
     * @deprecated Use {@link #findLast(BiPredicate)}
     */
    @Override
    @Deprecated
    default Optional<Tuple2<K, V>> findLast(final Predicate<? super Tuple2<K, V>> predicate) {
        return BaseSeq.super.findLast(predicate);
    }

    /**
     * @deprecated Use {@link #flatMap(BiFunction)}
     */
    @Override
    @Deprecated
    default <R> Seq2<R> flatMap(final Function<? super Tuple2<K, V>, ? extends Stream<? extends R>> mapper) {
        return this.toSeq2().flatMap(mapper);
    }

    /**
     * @see Stream#flatMap(Function)
     */
    default <R> Seq2<R> flatMap(final BiFunction<? super K, ? super V, ? extends Stream<? extends R>> mapper) {
        return this.flatMap(this.unwrap(mapper));
    }

    default <K2> PairSeq<K2, V> flatMapKeys(final Function<? super K, ? extends Stream<? extends K2>> function) {
        return this.flatMapToPair((k, v) -> function.apply(k).map(k2 -> new Tuple2<>(k2, v)));
    }

    default <K2, V2> PairSeq<K2, V2> flatMapKeysToPair(
            final Function<? super K, ? extends Stream<? extends Tuple2<K2, V2>>> function) {
        return seq(this.flatMapKeys(function).keys());
    }

    /**
     * @deprecated Use {@link #flatMapToDouble(BiFunction)}
     */
    @Override
    @Deprecated
    default DoubleStream flatMapToDouble(final Function<? super Tuple2<K, V>, ? extends DoubleStream> mapper) {
        return this.toSeq2().flatMapToDouble(mapper);
    }

    /**
     * @see Stream#flatMapToDouble(Function)
     */
    default DoubleStream flatMapToDouble(final BiFunction<? super K, ? super V, ? extends DoubleStream> mapper) {
        return this.flatMapToDouble(t -> t.map(mapper));
    }

    /**
     * @deprecated Use {@link #flatMapToInt(BiFunction)}
     */
    @Override
    @Deprecated
    default IntStream flatMapToInt(final Function<? super Tuple2<K, V>, ? extends IntStream> mapper) {
        return this.toSeq2().flatMapToInt(mapper);
    }

    /**
     * @see Stream#flatMapToInt(Function)
     */
    default IntStream flatMapToInt(final BiFunction<? super K, ? super V, ? extends IntStream> mapper) {
        return this.flatMapToInt(t -> t.map(mapper));
    }

    /**
     * @deprecated Use {@link #flatMapToLong(BiFunction)}
     */
    @Override
    @Deprecated
    default LongStream flatMapToLong(final Function<? super Tuple2<K, V>, ? extends LongStream> mapper) {
        return this.toSeq2().flatMapToLong(mapper);
    }

    /**
     * @see Stream#flatMapToLong(Function)
     */
    default LongStream flatMapToLong(final BiFunction<? super K, ? super V, ? extends LongStream> mapper) {
        return this.flatMapToLong(t -> t.map(mapper));
    }

    default <K2, V2> PairSeq<K2, V2> flatMapToPair(
            final BiFunction<? super K, ? super V, ? extends Stream<? extends Tuple2<K2, V2>>> mapper) {
        final Seq2<Tuple2<K2, V2>> seq2 = this.flatMap(t -> t.map(mapper));
        return seq(seq2);
    }

    default <V2> PairSeq<K, V2> flatMapValues(final Function<? super V, ? extends Stream<? extends V2>> function) {
        return this.flatMapToPair((k, v) -> function.apply(v).map(v2 -> new Tuple2<>(k, v2)));
    }

    default <K2, V2> PairSeq<K2, V2> flatMapValuesToPair(
            final Function<? super V, ? extends Stream<? extends Tuple2<K2, V2>>> function) {
        return seq(this.flatMapValues(function).values());
    }

    /**
     * @see Seq2#flatMapToIterable(Function)
     */
    default <R> Seq2<R> flatMapToIterable(
            final BiFunction<? super K, ? super V, ? extends Iterable<? extends R>> mapper) {
        final Seq2<Iterable<? extends R>> seq2 = this.map(mapper);
        return seq2.flatMapToIterable(Function.identity());
    }

    /**
     * @see Seq2#flatMapToOptional(Function)
     */
    default <R> Seq2<R> flatMapToOptional(
            final BiFunction<? super K, ? super V, ? extends Optional<? extends R>> mapper) {
        final Seq2<Optional<? extends R>> seq2 = this.map(mapper);
        return seq2.flatMapToOptional(Function.identity());
    }

    /**
     * @deprecated Use {@link #forEach(BiConsumer)}
     */
    @Override
    @Deprecated
    default void forEach(final Consumer<? super Tuple2<K, V>> action) {
        this.toSeq2().forEach(action);
    }

    /**
     * @see Stream#forEach(Consumer)
     * @see Iterable#forEach(Consumer)
     */
    default void forEach(final BiConsumer<? super K, ? super V> action) {
        this.toSeq2().forEach(this.unwrap(action));
    }

    /**
     * @deprecated Use {@link #forEachOrdered(BiConsumer)}
     */
    @Override
    @Deprecated
    default void forEachOrdered(final Consumer<? super Tuple2<K, V>> action) {
        this.toSeq2().forEachOrdered(action);
    }

    /**
     * @see Stream#forEachOrdered(Consumer)
     */
    default void forEachOrdered(final BiConsumer<? super K, ? super V> action) {
        this.forEachOrdered(this.unwrap(action));
    }

    /**
     * @see Seq#groupBy(Function)
     */
    default <K1> Map<K1, List<Tuple2<K, V>>> groupBy(final BiFunction<? super K, ? super V, ? extends K1> classifier) {
        return this.groupBy(this.unwrap(classifier));
    }

    /**
     * @deprecated Use {@link #groupBy(BiFunction)}
     */
    @Override
    @Deprecated
    default <K1> Map<K1, List<Tuple2<K, V>>> groupBy(final Function<? super Tuple2<K, V>, ? extends K1> classifier) {
        return BaseSeq.super.groupBy(classifier);
    }

    /**
     * @see Seq#groupBy(Function, Collector)
     */
    default <K1, A, D> Map<K1, D> groupBy(final BiFunction<? super K, ? super V, ? extends K1> classifier,
            final Collector<? super Tuple2<K, V>, A, D> downstream) {
        return this.groupBy(this.unwrap(classifier), downstream);
    }

    /**
     * @deprecated Use {@link #groupBy(BiFunction, Collector)}
     */
    @Override
    @Deprecated
    default <K1, A, D> Map<K1, D> groupBy(final Function<? super Tuple2<K, V>, ? extends K1> classifier,
            final Collector<? super Tuple2<K, V>, A, D> downstream) {
        return BaseSeq.super.groupBy(classifier, downstream);
    }

    /**
     * @see Seq#groupBy(Function, Supplier, Collector)
     */
    default <K1, D, A, M extends Map<K1, D>> M groupBy(final BiFunction<? super K, ? super V, ? extends K1> classifier,
            final Supplier<M> mapFactory, final Collector<? super Tuple2<K, V>, A, D> downstream) {
        return this.groupBy(this.unwrap(classifier), mapFactory, downstream);
    }

    /**
     * @deprecated Use {@link #groupBy(BiFunction, Supplier, Collector)}
     */
    @Override
    @Deprecated
    default <K1, D, A, M extends Map<K1, D>> M groupBy(final Function<? super Tuple2<K, V>, ? extends K1> classifier,
            final Supplier<M> mapFactory, final Collector<? super Tuple2<K, V>, A, D> downstream) {
        return BaseSeq.super.groupBy(classifier, mapFactory, downstream);
    }

    default PairSeq<K, Seq2<V>> groupByKey() {
        final PairSeq<K, Seq2<Tuple2<K, V>>> grouped = this.toSeq2().grouped(Tuple2::v1);
        return grouped.mapValues(s -> s.map(Tuple2::v2));
    }

    /**
     * @see Seq#grouped(Function)
     * @deprecated Use {@link #grouped(BiFunction)}
     */
    @Deprecated
    default <K1> PairSeq<K1, PairSeq<K, V>> grouped(final Function<? super Tuple2<K, V>, K1> classifier) {
        return this.toSeq2().grouped(classifier).mapValues(PairSeq::seq);
    }

    /**
     * @see Seq#grouped(Function)
     */
    default <K1> PairSeq<K1, PairSeq<K, V>> grouped(final BiFunction<? super K, ? super V, K1> classifier) {
        return this.toSeq2().grouped(t -> t.map(classifier)).mapValues(PairSeq::seq);
    }

    /**
     * @see Seq#grouped(Function, Collector)
     * @deprecated Use {@link #grouped(BiFunction, Collector)}
     */
    @Deprecated
    default <K1, A, D> PairSeq<K1, D> grouped(final Function<? super Tuple2<K, V>, ? extends K1> classifier,
            final Collector<? super Tuple2<K, V>, A, D> downstream) {
        return this.toSeq2().grouped(classifier, downstream);
    }

    /**
     * @see Seq#grouped(Function, Collector)
     */
    default <K1, A, D> PairSeq<K1, D> grouped(final BiFunction<? super K, ? super V, ? extends K1> classifier,
            final Collector<? super Tuple2<K, V>, A, D> downstream) {
        return this.toSeq2().grouped(t -> t.map(classifier), downstream);
    }

    /**
     * @deprecated Use {@link #indexOf(Object, Object)}
     */
    @Override
    @Deprecated
    default OptionalLong indexOf(final Tuple2<K, V> element) {
        return BaseSeq.super.indexOf(element);
    }

    /**
     * @see Seq#indexOf(Object)
     */
    default OptionalLong indexOf(final K elementKey, final V elementValue) {
        return this.indexOf(new Tuple2<>(elementKey, elementValue));
    }

    /**
     * @deprecated Use {@link #indexOf(BiPredicate)}
     */
    @Override
    @Deprecated
    default OptionalLong indexOf(final Predicate<? super Tuple2<K, V>> predicate) {
        return BaseSeq.super.indexOf(predicate);
    }

    /**
     * @see Seq#indexOf(Predicate)
     */
    default OptionalLong indexOf(final BiPredicate<? super K, ? super V> predicate) {
        return this.indexOf(this.unwrap(predicate));
    }

    /**
     * @see Seq#innerJoin(Stream, BiPredicate)
     */
    default <U> PairSeq<Tuple2<K, V>, U> innerJoin(final Stream<? extends U> other,
            final BiPredicate<? super Tuple2<K, V>, ? super U> predicate) {
        return this.toSeq2().innerJoin(other, predicate);
    }

    /**
     * @see Seq#innerJoin(Iterable, BiPredicate)
     */
    default <U> PairSeq<Tuple2<K, V>, U> innerJoin(final Iterable<? extends U> other,
            final BiPredicate<? super Tuple2<K, V>, ? super U> predicate) {
        return this.toSeq2().innerJoin(other, predicate);
    }

    /**
     * @see Seq#innerJoin(Seq, BiPredicate)
     */
    default <U> PairSeq<Tuple2<K, V>, U> innerJoin(final Seq<? extends U> other,
            final BiPredicate<? super Tuple2<K, V>, ? super U> predicate) {
        return this.toSeq2().innerJoin(other, predicate);
    }

    /**
     * @see Seq#innerJoin(Seq, BiPredicate)
     */
    default <U> PairSeq<Tuple2<K, V>, U> innerJoin(final Seq2<? extends U> other,
            final BiPredicate<? super Tuple2<K, V>, ? super U> predicate) {
        return this.toSeq2().innerJoin(other, predicate);
    }

    /**
     * @see Seq#innerJoin(Seq, BiPredicate)
     */
    default <K1, V1> PairSeq<Tuple2<K, V>, Tuple2<K1, V1>> innerJoin(final PairSeq<K1, V1> other,
            final BiPredicate<? super Tuple2<K, V>, ? super Tuple2<K1, V1>> predicate) {
        return this.innerJoin(other.toSeq2(), predicate);
    }

    default <V1> PairSeq<K, Tuple2<V, V1>> innerJoinByKey(final PairSeq<K, V1> other) {
        final PairSeq<Tuple2<K, V>, Tuple2<K, V1>> joined = this.innerJoin(other, this.byKey());
        return joined.mapToPair((left, right) -> left.v1(), (k, v) -> new Tuple2<>(k.v2(), v.v2()));
    }

    /**
     * @see Seq#innerSelfJoin(BiPredicate)
     */
    default PairSeq<Tuple2<K, V>, Tuple2<K, V>> innerSelfJoin(
            final BiPredicate<? super Tuple2<K, V>, ? super Tuple2<K, V>> predicate) {
        return this.toSeq2().innerSelfJoin(predicate);
    }

    default PairSeq<K, Tuple2<V, V>> innerSelfJoinByKey() {
        final PairSeq<Tuple2<K, V>, Tuple2<K, V>> joined = this.innerSelfJoin(this.byKey());
        return joined.mapToPair((left, right) -> left.v1(), (k, v) -> new Tuple2<>(k.v2(), v.v2()));
    }

    /**
     * @see Seq#intersperse(Object)
     * @deprecated Use {@link #intersperse(Object, Object)}
     */
    @Deprecated
    default PairSeq<K, V> intersperse(final Tuple2<K, V> value) {
        return seq(this.toSeq2().intersperse(value));
    }

    /**
     * @see Seq#intersperse(Object)
     */
    default PairSeq<K, V> intersperse(final K key, final V value) {
        return this.intersperse(new Tuple2<>(key, value));
    }

    @Override
    default boolean isParallel() {
        return this.toSeq2().isParallel();
    }

    @Override
    default Iterator<Tuple2<K, V>> iterator() {
        return this.toSeq2().iterator();
    }

    default Seq2<K> keys() {
        return this.toSeq2().map(Tuple2::v1);
    }

    /**
     * @see Seq#leftOuterJoin(Stream, BiPredicate)
     */
    default <U> PairSeq<Tuple2<K, V>, U> leftOuterJoin(final Stream<? extends U> other,
            final BiPredicate<? super Tuple2<K, V>, ? super U> predicate) {
        return this.toSeq2().leftOuterJoin(other, predicate);
    }

    /**
     * @see Seq#leftOuterJoin(Iterable, BiPredicate)
     */
    default <U> PairSeq<Tuple2<K, V>, U> leftOuterJoin(final Iterable<? extends U> other,
            final BiPredicate<? super Tuple2<K, V>, ? super U> predicate) {
        return this.toSeq2().leftOuterJoin(other, predicate);
    }

    /**
     * @see Seq#leftOuterJoin(Seq, BiPredicate)
     */
    default <U> PairSeq<Tuple2<K, V>, U> leftOuterJoin(final Seq<? extends U> other,
            final BiPredicate<? super Tuple2<K, V>, ? super U> predicate) {
        return this.toSeq2().leftOuterJoin(other, predicate);
    }

    /**
     * @see Seq#leftOuterJoin(Seq, BiPredicate)
     */
    default <U> PairSeq<Tuple2<K, V>, U> leftOuterJoin(final Seq2<? extends U> other,
            final BiPredicate<? super Tuple2<K, V>, ? super U> predicate) {
        return this.toSeq2().leftOuterJoin(other, predicate);
    }

    /**
     * @see Seq#leftOuterJoin(Seq, BiPredicate)
     */
    default <K1, V1> PairSeq<Tuple2<K, V>, Tuple2<K1, V1>> leftOuterJoin(
            final PairSeq<K1, V1> other,
            final BiPredicate<? super Tuple2<K, V>, ? super Tuple2<K1, V1>> predicate) {
        return this.leftOuterJoin(other.toSeq2(), predicate);
    }

    default <V1> PairSeq<K, Tuple2<V, V1>> leftOuterJoinByKey(final PairSeq<K, V1> other) {
        final PairSeq<Tuple2<K, V>, Tuple2<K, V1>> joined = this.leftOuterJoin(other, this.byKey());
        return joined.mapToPair((left, optionalRight) -> new Tuple2<>(left.v1(),
                new Tuple2<>(left.v2(), Optional.ofNullable(optionalRight).map(Tuple2::v2).orElse(null))));
    }

    /**
     * @see Seq#leftOuterSelfJoin(BiPredicate)
     */
    default PairSeq<Tuple2<K, V>, Tuple2<K, V>> leftOuterSelfJoin(
            final BiPredicate<? super Tuple2<K, V>, ? super Tuple2<K, V>> predicate) {
        return this.toSeq2().leftOuterSelfJoin(predicate);
    }

    @Override
    default PairSeq<K, V> limit(final long maxSize) {
        return seq(this.toSeq2().limit(maxSize));
    }

    /**
     * @see Seq#limitUntil(Predicate)
     * @deprecated Use {@link #limitUntil(BiPredicate)}
     */
    @Deprecated
    default PairSeq<K, V> limitUntil(final Predicate<? super Tuple2<K, V>> predicate) {
        return seq(this.toSeq2().limitUntil(predicate));
    }

    /**
     * @see Seq#limitUntil(Predicate)
     */
    default PairSeq<K, V> limitUntil(final BiPredicate<? super K, ? super V> predicate) {
        return this.limitUntil(this.unwrap(predicate));
    }

    /**
     * @see Seq#limitUntilClosed(Predicate)
     * @deprecated Use {@link #limitUntilClosed(BiPredicate)}
     */
    @Deprecated
    default PairSeq<K, V> limitUntilClosed(final Predicate<? super Tuple2<K, V>> predicate) {
        return seq(this.toSeq2().limitUntilClosed(predicate));
    }

    /**
     * @see Seq#limitUntilClosed(Predicate)
     */
    default PairSeq<K, V> limitUntilClosed(final BiPredicate<? super K, ? super V> predicate) {
        return this.limitUntilClosed(this.unwrap(predicate));
    }

    /**
     * @see Seq#limitWhile(Predicate)
     * @deprecated Use {@link #limitWhile(BiPredicate)}
     */
    @Deprecated
    default PairSeq<K, V> limitWhile(final Predicate<? super Tuple2<K, V>> predicate) {
        return seq(this.toSeq2().limitWhile(predicate));
    }

    /**
     * @see Seq#limitWhile(Predicate)
     */
    default PairSeq<K, V> limitWhile(final BiPredicate<? super K, ? super V> predicate) {
        return this.limitWhile(this.unwrap(predicate));
    }

    /**
     * @see Seq#limitWhileClosed(Predicate)
     * @deprecated Use {@link #limitWhileClosed(BiPredicate)}
     */
    @Deprecated
    default PairSeq<K, V> limitWhileClosed(final Predicate<? super Tuple2<K, V>> predicate) {
        return seq(this.toSeq2().limitWhileClosed(predicate));
    }

    /**
     * @see Seq#limitWhileClosed(Predicate)
     */
    default PairSeq<K, V> limitWhileClosed(final BiPredicate<? super K, ? super V> predicate) {
        return this.limitWhileClosed(this.unwrap(predicate));
    }

    /**
     * @deprecated Use {@link #map(BiFunction)}
     */
    @Override
    @Deprecated
    default <R> Seq2<R> map(final Function<? super Tuple2<K, V>, ? extends R> mapper) {
        return this.toSeq2().map(mapper);
    }

    default <K2, V2> PairSeq<K2, V2> map(final Function<? super K, K2> keyMapper,
            final Function<? super V, V2> valueMapper) {
        return this.mapKeys(keyMapper).mapValues(valueMapper);
    }

    /**
     * @see Stream#map(Function)
     */
    default <T> Seq2<T> map(final BiFunction<? super K, ? super V, ? extends T> function) {
        return this.map(this.unwrap(function));
    }

    default <K2> PairSeq<K2, V> mapKeys(final Function<? super K, K2> function) {
        final Seq2<Tuple2<K2, V>> seq = this.map(t -> t.map1(function));
        return seq(seq);
    }

    /**
     * @deprecated Use {@link #mapToDouble(ToDoubleBiFunction)}
     */
    @Override
    @Deprecated
    default DoubleStream mapToDouble(final ToDoubleFunction<? super Tuple2<K, V>> mapper) {
        return this.toSeq2().mapToDouble(mapper);
    }

    /**
     * @see Stream#mapToDouble(ToDoubleFunction)
     */
    default DoubleStream mapToDouble(final ToDoubleBiFunction<? super K, ? super V> function) {
        return this.mapToDouble(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #mapToInt(ToIntBiFunction)}
     */
    @Override
    @Deprecated
    default IntStream mapToInt(final ToIntFunction<? super Tuple2<K, V>> mapper) {
        return this.toSeq2().mapToInt(mapper);
    }

    /**
     * @see Stream#mapToInt(ToIntFunction)
     */
    default IntStream mapToInt(final ToIntBiFunction<? super K, ? super V> function) {
        return this.mapToInt(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #mapToLong(ToLongBiFunction)}
     */
    @Override
    @Deprecated
    default LongStream mapToLong(final ToLongFunction<? super Tuple2<K, V>> mapper) {
        return this.toSeq2().mapToLong(mapper);
    }

    /**
     * @see Stream#mapToLong(ToLongFunction)
     */
    default LongStream mapToLong(final ToLongBiFunction<? super K, ? super V> function) {
        return this.mapToLong(this.unwrap(function));
    }

    default <K2, V2> PairSeq<K2, V2> mapToPair(final BiFunction<? super K, ? super V, ? extends K2> keyMapper,
            final BiFunction<? super K, ? super V, ? extends V2> valueMapper) {
        return this.mapToPair(this.toTuple(keyMapper, valueMapper));
    }

    default <K2, V2> PairSeq<K2, V2> mapToPair(
            final BiFunction<? super K, ? super V, ? extends Tuple2<K2, V2>> function) {
        return this.mapToPair(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #mapToPair(BiFunction)}
     */
    @Deprecated
    default <K2, V2> PairSeq<K2, V2> mapToPair(
            final Function<? super Tuple2<K, V>, ? extends Tuple2<K2, V2>> function) {
        return seq(this.map(function));
    }

    default <V2> PairSeq<K, V2> mapValues(final Function<? super V, V2> function) {
        final Seq2<Tuple2<K, V2>> seq = this.map(t -> t.map2(function));
        return seq(seq);
    }

    @Override
    default Optional<Tuple2<K, V>> max(final Comparator<? super Tuple2<K, V>> comparator) {
        return this.toSeq2().max(comparator);
    }

    /**
     * @deprecated Use {@link #max(BiFunction)}
     */
    @Override
    @Deprecated
    default <U extends Comparable<U>> Optional<U> max(final Function<? super Tuple2<K, V>, ? extends U> function) {
        return BaseSeq.super.max(function);
    }

    /**
     * @see Seq#max(Function)
     */
    default <U extends Comparable<U>> Optional<U> max(final BiFunction<? super K, ? super V, ? extends U> function) {
        return this.max(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #max(BiFunction, Comparator)}
     */
    @Override
    @Deprecated
    default <U> Optional<U> max(final Function<? super Tuple2<K, V>, ? extends U> function,
            final Comparator<? super U> comparator) {
        return BaseSeq.super.max(function, comparator);
    }

    /**
     * @see Seq#max(Function, Comparator)
     */
    default <U> Optional<U> max(final BiFunction<? super K, ? super V, ? extends U> function,
            final Comparator<? super U> comparator) {
        return this.max(t -> function.apply(t.v1(), t.v2()), comparator);
    }

    /**
     * @see Seq#maxAll()
     */
    default PairSeq<K, V> maxAll() {
        return seq(this.toSeq2().maxAll());
    }

    /**
     * @see Seq#maxAll(Comparator)
     */
    default PairSeq<K, V> maxAll(final Comparator<? super Tuple2<K, V>> comparator) {
        return seq(this.toSeq2().maxAll(comparator));
    }

    /**
     * @see Seq#maxAll(Function)
     * @deprecated Use {@link #maxAll(BiFunction)}
     */
    @Deprecated
    default <U extends Comparable<U>> Seq2<U> maxAll(final Function<? super Tuple2<K, V>, ? extends U> function) {
        return this.toSeq2().maxAll(function);
    }

    /**
     * @see Seq#maxAll(Function)
     */
    default <U extends Comparable<U>> Seq2<U> maxAll(final BiFunction<? super K, ? super V, ? extends U> function) {
        return this.maxAll(this.unwrap(function));
    }

    /**
     * @see Seq#maxAll(Function, Comparator)
     * @deprecated Use {@link #maxAll(BiFunction, Comparator)}
     */
    @Deprecated
    default <U> Seq2<U> maxAll(final Function<? super Tuple2<K, V>, ? extends U> function,
            final Comparator<? super U> comparator) {
        return this.toSeq2().maxAll(function, comparator);
    }

    /**
     * @see Seq#maxAll(Function, Comparator)
     */
    default <U> Seq2<U> maxAll(final BiFunction<? super K, ? super V, ? extends U> function,
            final Comparator<? super U> comparator) {
        return this.maxAll(t -> t.map(function), comparator);
    }

    /**
     * @see Seq#maxAllBy(Function)
     * @deprecated Use {@link #maxAllBy(BiFunction)}
     */
    @Deprecated
    default <U extends Comparable<U>> PairSeq<K, V> maxAllBy(
            final Function<? super Tuple2<K, V>, ? extends U> function) {
        return seq(this.toSeq2().maxAllBy(function));
    }

    /**
     * @see Seq#maxAllBy(Function)
     */
    default <U extends Comparable<U>> PairSeq<K, V> maxAllBy(
            final BiFunction<? super K, ? super V, ? extends U> function) {
        return this.maxAllBy(this.unwrap(function));
    }

    /**
     * @see Seq#maxAllBy(Function, Comparator)
     * @deprecated Use {@link #maxAllBy(BiFunction, Comparator)}
     */
    @Deprecated
    default <U> PairSeq<K, V> maxAllBy(final Function<? super Tuple2<K, V>, ? extends U> function,
            final Comparator<? super U> comparator) {
        return seq(this.toSeq2().maxAllBy(function, comparator));
    }

    /**
     * @see Seq#maxAllBy(Function, Comparator)
     */
    default <U> PairSeq<K, V> maxAllBy(
            final BiFunction<? super K, ? super V, ? extends U> function,
            final Comparator<? super U> comparator) {
        return this.maxAllBy(t -> t.map(function), comparator);
    }

    /**
     * @deprecated Use {@link #maxBy(BiFunction)}
     */
    @Override
    @Deprecated
    default <U extends Comparable<U>> Optional<Tuple2<K, V>> maxBy(
            final Function<? super Tuple2<K, V>, ? extends U> function) {
        return BaseSeq.super.maxBy(function);
    }

    /**
     * @see Seq#maxBy(Function)
     */
    default <U extends Comparable<U>> Optional<Tuple2<K, V>> maxBy(
            final BiFunction<? super K, ? super V, ? extends U> function) {
        return this.maxBy(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #maxBy(BiFunction, Comparator)}
     */
    @Override
    @Deprecated
    default <U> Optional<Tuple2<K, V>> maxBy(final Function<? super Tuple2<K, V>, ? extends U> function,
            final Comparator<? super U> comparator) {
        return BaseSeq.super.maxBy(function, comparator);
    }

    /**
     * @see Seq#maxBy(Function, Comparator)
     */
    default <U> Optional<Tuple2<K, V>> maxBy(final BiFunction<? super K, ? super V, ? extends U> function,
            final Comparator<? super U> comparator) {
        return this.maxBy(t -> t.map(function), comparator);
    }

    /**
     * @deprecated Use {@link #medianBy(BiFunction)}
     */
    @Override
    @Deprecated
    default <U extends Comparable<? super U>> Optional<Tuple2<K, V>> medianBy(
            final Function<? super Tuple2<K, V>, ? extends U> function) {
        return BaseSeq.super.medianBy(function);
    }

    /**
     * @see Seq#medianBy(Function)
     */
    default <U extends Comparable<? super U>> Optional<Tuple2<K, V>> medianBy(
            final BiFunction<? super K, ? super V, ? extends U> function) {
        return this.medianBy(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #medianBy(BiFunction, Comparator)}
     */
    @Override
    @Deprecated
    default <U> Optional<Tuple2<K, V>> medianBy(final Function<? super Tuple2<K, V>, ? extends U> function,
            final Comparator<? super U> comparator) {
        return BaseSeq.super.medianBy(function, comparator);
    }

    /**
     * @see Seq#medianBy(Function, Comparator)
     */
    default <U> Optional<Tuple2<K, V>> medianBy(final BiFunction<? super K, ? super V, ? extends U> function,
            final Comparator<? super U> comparator) {
        return this.medianBy(t -> t.map(function), comparator);
    }

    @Override
    default Optional<Tuple2<K, V>> min(final Comparator<? super Tuple2<K, V>> comparator) {
        return this.toSeq2().min(comparator);
    }

    /**
     * @deprecated Use {@link #min(BiFunction)}
     */
    @Override
    @Deprecated
    default <U extends Comparable<U>> Optional<U> min(final Function<? super Tuple2<K, V>, ? extends U> function) {
        return BaseSeq.super.min(function);
    }

    /**
     * @see Seq#min(Function)
     */
    default <U extends Comparable<U>> Optional<U> min(final BiFunction<? super K, ? super V, ? extends U> function) {
        return this.min(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #min(BiFunction, Comparator)}
     */
    @Override
    @Deprecated
    default <U> Optional<U> min(final Function<? super Tuple2<K, V>, ? extends U> function,
            final Comparator<? super U> comparator) {
        return BaseSeq.super.min(function, comparator);
    }

    /**
     * @see Seq#min(Function, Comparator)
     */
    default <U> Optional<U> min(final BiFunction<? super K, ? super V, ? extends U> function,
            final Comparator<? super U> comparator) {
        return this.min(t -> function.apply(t.v1(), t.v2()), comparator);
    }

    /**
     * @see Seq#minAll()
     */
    default PairSeq<K, V> minAll() {
        return seq(this.toSeq2().minAll());
    }

    /**
     * @see Seq#minAll(Comparator)
     */
    default PairSeq<K, V> minAll(final Comparator<? super Tuple2<K, V>> comparator) {
        return seq(this.toSeq2().minAll(comparator));
    }

    /**
     * @see Seq#minAll(Function)
     * @deprecated Use {@link #minAll(BiFunction)}
     */
    @Deprecated
    default <U extends Comparable<U>> Seq2<U> minAll(final Function<? super Tuple2<K, V>, ? extends U> function) {
        return this.toSeq2().minAll(function);
    }

    /**
     * @see Seq#minAll(Function)
     */
    default <U extends Comparable<U>> Seq2<U> minAll(final BiFunction<? super K, ? super V, ? extends U> function) {
        return this.minAll(this.unwrap(function));
    }

    /**
     * @see Seq#minAll(Function, Comparator)
     * @deprecated Use {@link #minAll(BiFunction, Comparator)}
     */
    @Deprecated
    default <U> Seq2<U> minAll(final Function<? super Tuple2<K, V>, ? extends U> function,
            final Comparator<? super U> comparator) {
        return this.toSeq2().minAll(function, comparator);
    }

    /**
     * @see Seq#minAll(Function, Comparator)
     */
    default <U> Seq2<U> minAll(final BiFunction<? super K, ? super V, ? extends U> function,
            final Comparator<? super U> comparator) {
        return this.toSeq2().minAll(t -> function.apply(t.v1(), t.v2()), comparator);
    }

    /**
     * @see Seq#minAllBy(Function, Comparator)
     * @deprecated Use {@link #minAllBy(BiFunction)}
     */
    @Deprecated
    default <U extends Comparable<U>> PairSeq<K, V> minAllBy(
            final Function<? super Tuple2<K, V>, ? extends U> function) {
        return seq(this.toSeq2().minAllBy(function));
    }

    /**
     * @see Seq#minAllBy(Function)
     */
    default <U extends Comparable<U>> PairSeq<K, V> minAllBy(
            final BiFunction<? super K, ? super V, ? extends U> function) {
        return this.minAllBy(this.unwrap(function));
    }

    /**
     * @see Seq#minAllBy(Function, Comparator)
     * @deprecated Use {@link #minAllBy(BiFunction, Comparator)}
     */
    @Deprecated
    default <U> PairSeq<K, V> minAllBy(final Function<? super Tuple2<K, V>, ? extends U> function,
            final Comparator<? super U> comparator) {
        return seq(this.toSeq2().minAllBy(function, comparator));
    }

    /**
     * @see Seq#minAllBy(Function, Comparator)
     */
    default <U> PairSeq<K, V> minAllBy(final BiFunction<? super K, ? super V, ? extends U> function,
            final Comparator<? super U> comparator) {
        return this.minAllBy(t -> t.map(function), comparator);
    }

    /**
     * @deprecated Use {@link #minBy(BiFunction)}
     */
    @Override
    @Deprecated
    default <U extends Comparable<U>> Optional<Tuple2<K, V>> minBy(
            final Function<? super Tuple2<K, V>, ? extends U> function) {
        return BaseSeq.super.minBy(function);
    }

    /**
     * @see Seq#minBy(Function)
     */
    default <U extends Comparable<U>> Optional<Tuple2<K, V>> minBy(
            final BiFunction<? super K, ? super V, ? extends U> function) {
        return this.minBy(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #minBy(BiFunction, Comparator)}
     */
    @Override
    @Deprecated
    default <U> Optional<Tuple2<K, V>> minBy(final Function<? super Tuple2<K, V>, ? extends U> function,
            final Comparator<? super U> comparator) {
        return BaseSeq.super.minBy(function, comparator);
    }

    /**
     * @see Seq#minBy(Function, Comparator)
     */
    default <U> Optional<Tuple2<K, V>> minBy(final BiFunction<? super K, ? super V, ? extends U> function,
            final Comparator<? super U> comparator) {
        return this.minBy(t -> function.apply(t.v1(), t.v2()), comparator);
    }

    /**
     * @see Seq#modeAll()
     */
    default PairSeq<K, V> modeAll() {
        return seq(this.toSeq2().modeAll());
    }

    /**
     * @see Seq#modeAllBy(Function)
     * @deprecated Use {@link #modeAllBy(BiFunction)}
     */
    @Deprecated
    default <U> PairSeq<K, V> modeAllBy(final Function<? super Tuple2<K, V>, ? extends U> function) {
        return seq(this.toSeq2().modeAllBy(function));
    }

    /**
     * @see Seq#modeAllBy(Function)
     */
    default <U> PairSeq<K, V> modeAllBy(final BiFunction<? super K, ? super V, ? extends U> function) {
        return this.modeAllBy(this.unwrap(function));
    }

    /**
     * @see Seq#modeBy(Function)
     * @deprecated Use {@link #modeBy(BiFunction)}
     */
    @Override
    @Deprecated
    default <U> Optional<Tuple2<K, V>> modeBy(final Function<? super Tuple2<K, V>, ? extends U> function) {
        return BaseSeq.super.modeBy(function);
    }

    /**
     * @see Seq#modeBy(Function)
     */
    default <U> Optional<Tuple2<K, V>> modeBy(final BiFunction<? super K, ? super V, ? extends U> function) {
        return this.modeBy(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #noneMatch(BiPredicate)}
     */
    @Override
    @Deprecated
    default boolean noneMatch(final Predicate<? super Tuple2<K, V>> predicate) {
        return this.toSeq2().noneMatch(predicate);
    }

    /**
     * @see Stream#noneMatch(Predicate)
     */
    default boolean noneMatch(final BiPredicate<? super K, ? super V> predicate) {
        return this.noneMatch(this.unwrap(predicate));
    }

    /**
     * @see Seq#ofType(Class)
     */
    default <U> Seq2<U> ofType(final Class<? extends U> type) {
        return this.toSeq2().ofType(type);
    }

    @Override
    default PairSeq<K, V> onClose(final Runnable closeHandler) {
        return seq(this.toSeq2().onClose(closeHandler));
    }

    /**
     * @see Seq#onEmpty(Object)
     * @deprecated Use {@link #onEmpty(Object, Object)}
     */
    @Deprecated
    default PairSeq<K, V> onEmpty(final Tuple2<K, V> value) {
        return seq(this.toSeq2().onEmpty(value));
    }

    /**
     * @see Seq#onEmpty(Object)
     */
    default PairSeq<K, V> onEmpty(final K key, final V value) {
        return this.onEmpty(new Tuple2<>(key, value));
    }

    /**
     * @see Seq#onEmptyGet(Supplier)
     */
    default PairSeq<K, V> onEmptyGet(final Supplier<? extends Tuple2<K, V>> supplier) {
        return seq(this.toSeq2().onEmptyGet(supplier));
    }

    /**
     * @see Seq#onEmptyThrow(Supplier)
     */
    default PairSeq<K, V> onEmptyThrow(final Supplier<? extends Throwable> supplier) {
        return seq(this.toSeq2().onEmptyThrow(supplier));
    }

    /**
     * @see Seq#outerApply(Function)
     * @deprecated Use {@link #outerApply(BiFunction)}
     */
    @Deprecated
    default <U> PairSeq<Tuple2<K, V>, U> outerApply(
            final Function<? super Tuple2<K, V>, ? extends Iterable<? extends U>> function) {
        return this.toSeq2().outerApply(function);
    }

    /**
     * @see Seq#outerApply(Function)
     */
    default <U> PairSeq<Tuple2<K, V>, U> outerApply(
            final BiFunction<? super K, ? super V, ? extends Iterable<? extends U>> function) {
        return this.outerApply(this.unwrap(function));
    }

    @Override
    default PairSeq<K, V> parallel() {
        return seq(this.toSeq2().parallel());
    }

    /**
     * @see Seq#partition(Predicate)
     * @deprecated Use {@link #partition(BiPredicate)}
     */
    @Deprecated
    default Tuple2<PairSeq<K, V>, PairSeq<K, V>> partition(final Predicate<? super Tuple2<K, V>> predicate) {
        return this.toSeq2().partition(predicate).map1(PairSeq::seq).map2(PairSeq::seq);
    }

    /**
     * @see Seq#partition(Predicate)
     */
    default Tuple2<PairSeq<K, V>, PairSeq<K, V>> partition(final BiPredicate<? super K, ? super V> predicate) {
        return this.partition(this.unwrap(predicate));
    }

    /**
     * @deprecated Use {@link #peek(BiConsumer)}
     */
    @Override
    @Deprecated
    default PairSeq<K, V> peek(final Consumer<? super Tuple2<K, V>> action) {
        return seq(this.toSeq2().peek(action));
    }

    /**
     * @see Stream#peek(Consumer)
     */
    default PairSeq<K, V> peek(final BiConsumer<? super K, ? super V> action) {
        return this.peek(this.unwrap(action));
    }

    default PairSeq<K, V> peekKey(final Consumer<? super K> action) {
        return seq(this.toSeq2().peek(t -> action.accept(t.v1())));
    }

    default PairSeq<K, V> peekValue(final Consumer<? super V> action) {
        return seq(this.toSeq2().peek(t -> action.accept(t.v2())));
    }

    /**
     * @deprecated Use {@link #percentileBy(double, BiFunction)}
     */
    @Override
    @Deprecated
    default <U extends Comparable<? super U>> Optional<Tuple2<K, V>> percentileBy(final double percentile,
            final Function<? super Tuple2<K, V>, ? extends U> function) {
        return BaseSeq.super.percentileBy(percentile, function);
    }

    /**
     * @see Seq#percentileBy(double, Function, Comparator)
     */
    default <U extends Comparable<? super U>> Optional<Tuple2<K, V>> percentileBy(final double percentile,
            final BiFunction<? super K, ? super V, ? extends U> function) {
        return this.percentileBy(percentile, this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #percentileBy(double, BiFunction, Comparator)}
     */
    @Override
    @Deprecated
    default <U> Optional<Tuple2<K, V>> percentileBy(final double percentile,
            final Function<? super Tuple2<K, V>, ? extends U> function, final Comparator<? super U> comparator) {
        return BaseSeq.super.percentileBy(percentile, function, comparator);
    }

    /**
     * @see Seq#percentileBy(double, Function)
     */
    default <U> Optional<Tuple2<K, V>> percentileBy(final double percentile,
            final BiFunction<? super K, ? super V, ? extends U> function, final Comparator<? super U> comparator) {
        return this.percentileBy(percentile, t -> t.map(function), comparator);
    }

    /**
     * @see Seq#prepend(Object)
     */
    default PairSeq<K, V> prepend(final K otherKey, final V otherValue) {
        return this.prepend(new Tuple2<>(otherKey, otherValue));
    }

    /**
     * @see Seq#prepend(Object)
     * @deprecated Use {@link #prepend(Object, Object)}
     */
    @Deprecated
    default PairSeq<K, V> prepend(final Tuple2<K, V> other) {
        return seq(this.toSeq2().prepend(other));
    }

    /**
     * @see Seq#prepend(Object[])
     */
    default PairSeq<K, V> prepend(final Tuple2<K, V>... other) {
        return seq(this.toSeq2().prepend(other));
    }

    /**
     * @see Seq#prepend(Iterable)
     */
    default PairSeq<K, V> prepend(final Iterable<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().prepend(other));
    }

    /**
     * @see Seq#prepend(Stream)
     */
    default PairSeq<K, V> prepend(final Stream<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().prepend(other));
    }

    /**
     * @see Seq#prepend(Seq)
     */
    default PairSeq<K, V> prepend(final Seq<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().prepend(other));
    }

    /**
     * @see Seq#prepend(Seq)
     */
    default PairSeq<K, V> prepend(final Seq2<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().prepend(other));
    }

    /**
     * @see Seq#prepend(Seq)
     */
    default PairSeq<K, V> prepend(final PairSeq<K, V> other) {
        return this.prepend(other.toSeq2());
    }

    /**
     * @see Seq#prepend(Optional)
     */
    default PairSeq<K, V> prepend(final Optional<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().prepend(other));
    }

    @Override
    default Tuple2<K, V> reduce(final Tuple2<K, V> identity, final BinaryOperator<Tuple2<K, V>> accumulator) {
        return this.toSeq2().reduce(identity, accumulator);
    }

    @Override
    default Optional<Tuple2<K, V>> reduce(final BinaryOperator<Tuple2<K, V>> accumulator) {
        return this.toSeq2().reduce(accumulator);
    }

    @Override
    default <U> U reduce(final U identity, final BiFunction<U, ? super Tuple2<K, V>, U> accumulator,
            final BinaryOperator<U> combiner) {
        return this.toSeq2().reduce(identity, accumulator, combiner);
    }

    default PairSeq<K, V> reduceByKey(final V identity, final BinaryOperator<V> accumulator) {
        return this.groupByKey().mapValues(s -> s.reduce(identity, accumulator));
    }

    default PairSeq<K, V> reduceByKey(final BinaryOperator<V> accumulator) {
        return this.groupByKey().flatMapValues(s -> s.reduce(accumulator).stream());
    }

    default <U> PairSeq<K, U> reduceByKey(final U identity, final BiFunction<U, ? super V, U> accumulator,
            final BinaryOperator<U> combiner) {
        return this.groupByKey().mapValues(s -> s.reduce(identity, accumulator, combiner));
    }

    /**
     * @see Seq#remove(Object)
     * @deprecated Use {@link #remove(Object, Object)}
     */
    @Deprecated
    default PairSeq<K, V> remove(final Tuple2<K, V> other) {
        return seq(this.toSeq2().remove(other));
    }

    /**
     * @see Seq#remove(Object)
     */
    default PairSeq<K, V> remove(final K otherKey, final V otherValue) {
        return this.remove(new Tuple2<>(otherKey, otherValue));
    }

    /**
     * @see Seq#removeAll(Seq)
     */
    default PairSeq<K, V> removeAll(final PairSeq<K, V> other) {
        return this.removeAll(other.toSeq2());
    }

    /**
     * @see Seq#removeAll(Object[])
     */
    default PairSeq<K, V> removeAll(final Tuple2<K, V>... other) {
        return seq(this.toSeq2().removeAll(other));
    }

    /**
     * @see Seq#removeAll(Stream)
     */
    default PairSeq<K, V> removeAll(final Stream<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().removeAll(other));
    }

    /**
     * @see Seq#removeAll(Iterable)
     */
    default PairSeq<K, V> removeAll(final Iterable<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().removeAll(other));
    }

    /**
     * @see Seq#removeAll(Seq)
     */
    default PairSeq<K, V> removeAll(final Seq<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().removeAll(other));
    }

    /**
     * @see Seq#removeAll(Seq)
     */
    default PairSeq<K, V> removeAll(final Seq2<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().removeAll(other));
    }

    /**
     * @see Seq#retainAll(Object[])
     */
    default PairSeq<K, V> retainAll(final Tuple2<K, V>... other) {
        return seq(this.toSeq2().retainAll(other));
    }

    /**
     * @see Seq#retainAll(Stream)
     */
    default PairSeq<K, V> retainAll(final Stream<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().retainAll(other));
    }

    /**
     * @see Seq#retainAll(Iterable)
     */
    default PairSeq<K, V> retainAll(final Iterable<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().retainAll(other));
    }

    /**
     * @see Seq#retainAll(Seq)
     */
    default PairSeq<K, V> retainAll(final Seq<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().retainAll(other));
    }

    /**
     * @see Seq#retainAll(Seq)
     */
    default PairSeq<K, V> retainAll(final Seq2<? extends Tuple2<K, V>> other) {
        return seq(this.toSeq2().retainAll(other));
    }

    /**
     * @see Seq#retainAll(Seq)
     */
    default PairSeq<K, V> retainAll(final PairSeq<K, V> other) {
        return this.retainAll(other.toSeq2());
    }

    /**
     * @see Seq#reverse()
     */
    default PairSeq<K, V> reverse() {
        return seq(this.toSeq2().reverse());
    }

    /**
     * @see Seq#rightOuterJoin(Stream, BiPredicate)
     */
    default <U> PairSeq<Tuple2<K, V>, U> rightOuterJoin(final Stream<? extends U> other,
            final BiPredicate<? super Tuple2<K, V>, ? super U> predicate) {
        return this.toSeq2().rightOuterJoin(other, predicate);
    }

    /**
     * @see Seq#rightOuterJoin(Iterable, BiPredicate)
     */
    default <U> PairSeq<Tuple2<K, V>, U> rightOuterJoin(final Iterable<? extends U> other,
            final BiPredicate<? super Tuple2<K, V>, ? super U> predicate) {
        return this.toSeq2().rightOuterJoin(other, predicate);
    }

    /**
     * @see Seq#rightOuterJoin(Seq, BiPredicate)
     */
    default <U> PairSeq<Tuple2<K, V>, U> rightOuterJoin(final Seq<? extends U> other,
            final BiPredicate<? super Tuple2<K, V>, ? super U> predicate) {
        return this.toSeq2().rightOuterJoin(other, predicate);
    }

    /**
     * @see Seq#rightOuterJoin(Seq, BiPredicate)
     */
    default <U> PairSeq<Tuple2<K, V>, U> rightOuterJoin(final Seq2<? extends U> other,
            final BiPredicate<? super Tuple2<K, V>, ? super U> predicate) {
        return this.toSeq2().rightOuterJoin(other, predicate);
    }

    /**
     * @see Seq#rightOuterJoin(Seq, BiPredicate)
     */
    default <K1, V1> PairSeq<Tuple2<K, V>, Tuple2<K1, V1>> rightOuterJoin(
            final PairSeq<K1, V1> other,
            final BiPredicate<? super Tuple2<K, V>, ? super Tuple2<K1, V1>> predicate) {
        return this.rightOuterJoin(other.toSeq2(), predicate);
    }

    default <V1> PairSeq<K, Tuple2<V, V1>> rightOuterJoinByKey(final PairSeq<K, V1> other) {
        final PairSeq<Tuple2<K, V>, Tuple2<K, V1>> joined = this.rightOuterJoin(other, this.byKey());
        return joined.mapToPair((optionalLeft, right) -> new Tuple2<>(right.v1(),
                new Tuple2<>(Optional.ofNullable(optionalLeft).map(Tuple2::v2).orElse(null), right.v2())));
    }

    /**
     * @see Seq#rightOuterSelfJoin(BiPredicate)
     */
    default PairSeq<Tuple2<K, V>, Tuple2<K, V>> rightOuterSelfJoin(
            final BiPredicate<? super Tuple2<K, V>, ? super Tuple2<K, V>> predicate) {
        return this.toSeq2().rightOuterSelfJoin(predicate);
    }

    /**
     * @see Seq#scanLeft(Object, BiFunction)
     */
    default <U> Seq2<U> scanLeft(final U seed,
            final BiFunction<? super U, ? super Tuple2<K, V>, ? extends U> function) {
        return this.toSeq2().scanLeft(seed, function);
    }

    /**
     * @see Seq#scanRight(Object, BiFunction)
     */
    default <U> Seq2<U> scanRight(final U seed,
            final BiFunction<? super Tuple2<K, V>, ? super U, ? extends U> function) {
        return this.toSeq2().scanRight(seed, function);
    }

    @Override
    default PairSeq<K, V> sequential() {
        return seq(this.toSeq2().sequential());
    }

    /**
     * @see Seq#shuffle()
     */
    default PairSeq<K, V> shuffle() {
        return seq(this.toSeq2().shuffle());
    }

    /**
     * @see Seq#shuffle(Random)
     */
    default PairSeq<K, V> shuffle(final Random random) {
        return seq(this.toSeq2().shuffle(random));
    }

    @Override
    default PairSeq<K, V> skip(final long n) {
        return seq(this.toSeq2().skip(n));
    }

    /**
     * @see Seq#skipUntil(Predicate)
     * @deprecated Use {@link #skipUntil(BiPredicate)}
     */
    @Deprecated
    default PairSeq<K, V> skipUntil(final Predicate<? super Tuple2<K, V>> predicate) {
        return seq(this.toSeq2().skipUntil(predicate));
    }

    /**
     * @see Seq#skipUntil(Predicate)
     */
    default PairSeq<K, V> skipUntil(final BiPredicate<? super K, ? super V> predicate) {
        return this.skipUntil(this.unwrap(predicate));
    }

    /**
     * @see Seq#skipUntilClosed(Predicate)
     * @deprecated Use {@link #skipUntilClosed(BiPredicate)}
     */
    @Deprecated
    default PairSeq<K, V> skipUntilClosed(final Predicate<? super Tuple2<K, V>> predicate) {
        return seq(this.toSeq2().skipUntilClosed(predicate));
    }

    /**
     * @see Seq#skipWhileClosed(Predicate)
     */
    default PairSeq<K, V> skipUntilClosed(final BiPredicate<? super K, ? super V> predicate) {
        return this.skipUntilClosed(this.unwrap(predicate));
    }

    /**
     * @see Seq#skipWhile(Predicate)
     * @deprecated Use {@link #skipWhile(BiPredicate)}
     */
    @Deprecated
    default PairSeq<K, V> skipWhile(final Predicate<? super Tuple2<K, V>> predicate) {
        return seq(this.toSeq2().skipWhile(predicate));
    }

    /**
     * @see Seq#skipWhile(Predicate)
     */
    default PairSeq<K, V> skipWhile(final BiPredicate<? super K, ? super V> predicate) {
        return this.skipWhile(this.unwrap(predicate));
    }

    /**
     * @see Seq#skipWhileClosed(Predicate)
     * @deprecated Use {@link #skipWhileClosed(BiPredicate)}
     */
    @Deprecated
    default PairSeq<K, V> skipWhileClosed(final Predicate<? super Tuple2<K, V>> predicate) {
        return seq(this.toSeq2().skipWhileClosed(predicate));
    }

    /**
     * @see Seq#skipWhileClosed(Predicate)
     */
    default PairSeq<K, V> skipWhileClosed(final BiPredicate<? super K, ? super V> predicate) {
        return this.skipWhileClosed(this.unwrap(predicate));
    }

    /**
     * @see Seq#slice(long, long)
     */
    default PairSeq<K, V> slice(final long from, final long to) {
        return seq(this.toSeq2().slice(from, to));
    }

    /**
     * @see Seq#sliding(long)
     */
    default Seq2<PairSeq<K, V>> sliding(final long size) {
        return this.toSeq2().sliding(size).map(PairSeq::seq);
    }

    @Override
    default PairSeq<K, V> sorted() {
        return seq(this.toSeq2().sorted());
    }

    @Override
    default PairSeq<K, V> sorted(final Comparator<? super Tuple2<K, V>> comparator) {
        return seq(this.toSeq2().sorted(comparator));
    }

    /**
     * @see Seq#sorted(Function)
     * @deprecated Use {@link #sorted(BiFunction)}
     */
    @Deprecated
    default <U extends Comparable<? super U>> PairSeq<K, V> sorted(
            final Function<? super Tuple2<K, V>, ? extends U> function) {
        return seq(this.toSeq2().sorted(function));
    }

    /**
     * @see Seq#sorted(Function)
     */
    default <U extends Comparable<? super U>> PairSeq<K, V> sorted(
            final BiFunction<? super K, ? super V, ? extends U> function) {
        return this.sorted(this.unwrap(function));
    }

    /**
     * @see Seq#sorted(Function, Comparator)
     * @deprecated Use {@link #sorted(BiFunction, Comparator)}
     */
    @Deprecated
    default <U> PairSeq<K, V> sorted(final Function<? super Tuple2<K, V>, ? extends U> function,
            final Comparator<? super U> comparator) {
        return seq(this.toSeq2().sorted(function, comparator));
    }

    /**
     * @see Seq#sorted(Function, Comparator)
     */
    default <U> PairSeq<K, V> sorted(final BiFunction<? super K, ? super V, ? extends U> function,
            final Comparator<? super U> comparator) {
        return this.sorted(t -> t.map(function), comparator);
    }

    /**
     * @see Seq#splitAt(long)
     */
    default Tuple2<PairSeq<K, V>, PairSeq<K, V>> splitAt(final long position) {
        return this.toSeq2().splitAt(position).map1(PairSeq::seq).map2(PairSeq::seq);
    }

    /**
     * @see Seq#splitAtHead()
     */
    default Tuple2<Optional<Tuple2<K, V>>, PairSeq<K, V>> splitAtHead() {
        return this.toSeq2().splitAtHead().map2(PairSeq::seq);
    }

    @Override
    default Spliterator<Tuple2<K, V>> spliterator() {
        return this.toSeq2().spliterator();
    }

    /**
     * @deprecated Use {@link #sum(BiFunction)}
     */
    @Override
    @Deprecated
    default <U> Optional<U> sum(final Function<? super Tuple2<K, V>, ? extends U> function) {
        return BaseSeq.super.sum(function);
    }

    /**
     * @see Seq#sum(Function)
     */
    default <U> Optional<U> sum(final BiFunction<? super K, ? super V, ? extends U> function) {
        return this.sum(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #sumDouble(ToDoubleBiFunction)}
     */
    @Override
    @Deprecated
    default double sumDouble(final ToDoubleFunction<? super Tuple2<K, V>> function) {
        return BaseSeq.super.sumDouble(function);
    }

    /**
     * @see Seq#sumDouble(ToDoubleFunction)
     */
    default double sumDouble(final ToDoubleBiFunction<? super K, ? super V> function) {
        return this.sumDouble(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #sumInt(ToIntBiFunction)}
     */
    @Override
    @Deprecated
    default int sumInt(final ToIntFunction<? super Tuple2<K, V>> function) {
        return BaseSeq.super.sumInt(function);
    }

    /**
     * @see Seq#sumInt(ToIntFunction)
     */
    default int sumInt(final ToIntBiFunction<? super K, ? super V> function) {
        return this.sumInt(this.unwrap(function));
    }

    /**
     * @deprecated Use {@link #sumLong(ToLongBiFunction)}
     */
    @Override
    @Deprecated
    default long sumLong(final ToLongFunction<? super Tuple2<K, V>> function) {
        return BaseSeq.super.sumLong(function);
    }

    /**
     * @see Seq#sumLong(ToLongFunction)
     */
    default long sumLong(final ToLongBiFunction<? super K, ? super V> function) {
        return this.sumLong(this.unwrap(function));
    }

    default PairSeq<V, K> swapped() {
        return this.mapToPair(Tuple2::swap);
    }

    /**
     * @see Seq#take(long)
     */
    default PairSeq<K, V> take(final long maxSize) {
        return seq(this.toSeq2().take(maxSize));
    }

    /**
     * @deprecated Use {@link #takeWhile(BiPredicate)}
     */
    @Override
    @Deprecated
    default Stream<Tuple2<K, V>> takeWhile(final Predicate<? super Tuple2<K, V>> predicate) {
        return this.toSeq2().takeWhile(predicate);
    }

    /**
     * @see Seq#takeWhile(Predicate)
     */
    default Stream<Tuple2<K, V>> takeWhile(final BiPredicate<? super K, ? super V> predicate) {
        return this.takeWhile(this.unwrap(predicate));
    }

    @Override
    default Object[] toArray() {
        return this.toSeq2().toArray();
    }

    @Override
    default <A> A[] toArray(final IntFunction<A[]> generator) {
        return this.toSeq2().toArray(generator);
    }

    default Map<K, V> toMap() {
        return this.toSeq2().toMap(Tuple2::v1, Tuple2::v2);
    }

    /**
     * @deprecated Use {@link #toMap(BiFunction, BiFunction)}
     */
    @Override
    @Deprecated
    default <K2, V2> Map<K2, V2> toMap(final Function<? super Tuple2<K, V>, ? extends K2> keyMapper,
            final Function<? super Tuple2<K, V>, ? extends V2> valueMapper) {
        return BaseSeq.super.toMap(keyMapper, valueMapper);
    }

    /**
     * @see Seq#toMap(Function, Function)
     */
    default <K2, V2> Map<K2, V2> toMap(final BiFunction<? super K, ? super V, ? extends K2> keyMapper,
            final BiFunction<? super K, ? super V, ? extends V2> valueMapper) {
        return this.toMap(t -> t.map(keyMapper), t -> t.map(valueMapper));
    }

    /**
     * @deprecated Use {@link #toMap(BiFunction)}
     */
    @Override
    @Deprecated
    default <K2> Map<K2, Tuple2<K, V>> toMap(final Function<? super Tuple2<K, V>, ? extends K2> keyMapper) {
        return BaseSeq.super.toMap(keyMapper);
    }

    /**
     * @see Seq#toMap(Function)
     */
    default <K2> Map<K2, Tuple2<K, V>> toMap(final BiFunction<? super K, ? super V, ? extends K2> keyMapper) {
        return this.toMap(t -> t.map(keyMapper));
    }

    /**
     * @deprecated Only for internal use. If methods of {@link Seq2} are missing, implement them.
     */
    @Deprecated
    Seq2<Tuple2<K, V>> toSeq2();

    /**
     * @see Seq#transform(Function)
     */
    default <U> U transform(final Function<? super PairSeq<K, V>, ? extends U> transformer) {
        return this.toSeq2().transform(s -> transformer.apply(seq(s)));
    }

    @Override
    default PairSeq<K, V> unordered() {
        return seq(this.toSeq2().unordered());
    }

    default Seq2<V> values() {
        return this.toSeq2().map(Tuple2::v2);
    }

    /**
     * @see Seq#window()
     */
    default Seq2<Window<Tuple2<K, V>>> window() {
        return this.toSeq2().window();
    }

    /**
     * @see Seq#window(long, long)
     */
    default Seq2<Window<Tuple2<K, V>>> window(final long lower, final long upper) {
        return this.toSeq2().window(lower, upper);
    }

    /**
     * @see Seq#window(Comparator)
     */
    default Seq2<Window<Tuple2<K, V>>> window(final Comparator<? super Tuple2<K, V>> orderBy) {
        return this.toSeq2().window(orderBy);
    }

    /**
     * @see Seq#window(Comparator, long, long)
     */
    default Seq2<Window<Tuple2<K, V>>> window(final Comparator<? super Tuple2<K, V>> orderBy, final long lower,
            final long upper) {
        return this.toSeq2().window(orderBy, lower, upper);
    }

    /**
     * @see Seq#window(Function)
     * @deprecated Use {@link #window(BiFunction)}
     */
    @Deprecated
    default <U> Seq2<Window<Tuple2<K, V>>> window(final Function<? super Tuple2<K, V>, ? extends U> partitionBy) {
        return this.toSeq2().window(partitionBy);
    }

    /**
     * @see Seq#window(Function)
     */
    default <U> Seq2<Window<Tuple2<K, V>>> window(final BiFunction<? super K, ? super V, ? extends U> partitionBy) {
        return this.window(this.unwrap(partitionBy));
    }

    /**
     * @see Seq#window(Function, long, long)
     * @deprecated Use {@link #window(BiFunction, long, long)}
     */
    @Deprecated
    default <U> Seq2<Window<Tuple2<K, V>>> window(final Function<? super Tuple2<K, V>, ? extends U> partitionBy,
            final long lower,
            final long upper) {
        return this.toSeq2().window(partitionBy, lower, upper);
    }

    /**
     * @see Seq#window(Function, long, long)
     */
    default <U> Seq2<Window<Tuple2<K, V>>> window(final BiFunction<? super K, ? super V, ? extends U> partitionBy,
            final long lower,
            final long upper) {
        return this.window(this.unwrap(partitionBy), lower, upper);
    }

    /**
     * @see Seq#window(Function, Comparator)
     * @deprecated Use {@link #window(BiFunction, Comparator)}
     */
    @Deprecated
    default <U> Seq2<Window<Tuple2<K, V>>> window(final Function<? super Tuple2<K, V>, ? extends U> partitionBy,
            final Comparator<? super Tuple2<K, V>> orderBy) {
        return this.toSeq2().window(partitionBy, orderBy);
    }

    /**
     * @see Seq#window(Function, Comparator)
     */
    default <U> Seq2<Window<Tuple2<K, V>>> window(final BiFunction<? super K, ? super V, ? extends U> partitionBy,
            final Comparator<? super Tuple2<K, V>> orderBy) {
        return this.window(this.unwrap(partitionBy), orderBy);
    }

    /**
     * @see Seq#window(Function, Comparator, long, long)
     * @deprecated Use {@link #window(BiFunction, Comparator, long, long)}
     */
    @Deprecated
    default <U> Seq2<Window<Tuple2<K, V>>> window(final Function<? super Tuple2<K, V>, ? extends U> partitionBy,
            final Comparator<? super Tuple2<K, V>> orderBy, final long lower, final long upper) {
        return this.toSeq2().window(partitionBy, orderBy, lower, upper);
    }

    /**
     * @see Seq#window(Function, Comparator, long, long)
     */
    default <U> Seq2<Window<Tuple2<K, V>>> window(final BiFunction<? super K, ? super V, ? extends U> partitionBy,
            final Comparator<? super Tuple2<K, V>> orderBy, final long lower, final long upper) {
        return this.window(this.unwrap(partitionBy), orderBy, lower, upper);
    }

    private <V1> BiPredicate<Tuple2<K, V>, Tuple2<K, V1>> byKey() {
        return (k, v) -> k.v1().equals(v.v1());
    }

    private Consumer<Tuple2<K, V>> unwrap(final BiConsumer<? super K, ? super V> action) {
        return t -> action.accept(t.v1(), t.v2());
    }

    private <K2, V2> BiFunction<K, V, Tuple2<K2, V2>> toTuple(
            final BiFunction<? super K, ? super V, ? extends K2> keyMapper,
            final BiFunction<? super K, ? super V, ? extends V2> valueMapper) {
        return (k, v) -> new Tuple2<>(keyMapper.apply(k, v), valueMapper.apply(k, v));
    }

    private Predicate<Tuple2<K, V>> unwrap(final BiPredicate<? super K, ? super V> predicate) {
        return t -> predicate.test(t.v1(), t.v2());
    }

    private <U> Function<Tuple2<K, V>, ? extends U> unwrap(
            final BiFunction<? super K, ? super V, ? extends U> function) {
        return t -> t.map(function);
    }

    private ToIntFunction<Tuple2<K, V>> unwrap(final ToIntBiFunction<? super K, ? super V> function) {
        return t -> function.applyAsInt(t.v1(), t.v2());
    }

    private ToLongFunction<Tuple2<K, V>> unwrap(final ToLongBiFunction<? super K, ? super V> function) {
        return t -> function.applyAsLong(t.v1(), t.v2());
    }

    private ToDoubleFunction<Tuple2<K, V>> unwrap(final ToDoubleBiFunction<? super K, ? super V> function) {
        return t -> function.applyAsDouble(t.v1(), t.v2());
    }
}
