/*
 * Copyright (c), 2024 bakdata GmbH
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
import java.util.Map;
import java.util.Optional;
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
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
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
public interface Seq2<T> extends Iterable<T>, BaseSeq<T> {
    /**
     * @see Seq#empty()
     */
    static <T> Seq2<T> empty() {
        final Seq<T> seq = Seq.empty();
        return seq(seq);
    }

    /**
     * @see Seq#generate(Supplier)
     */
    static <T> Seq2<T> generate(final Supplier<? extends T> s) {
        final Seq<T> seq = Seq.generate(s);
        return seq(seq);
    }

    /**
     * @see Seq#generate(Object)
     */
    static <T> Seq2<T> generate(final T value) {
        final Seq<T> seq = Seq.generate(value);
        return seq(seq);
    }

    /**
     * @see Seq#generate()
     */
    static Seq2<Void> generate() {
        final Seq<Void> seq = Seq.generate();
        return seq(seq);
    }

    /**
     * @see Seq#of(Object[])
     */
    static <T> Seq2<T> of(final T... values) {
        final Seq<T> seq = Seq.of(values);
        return seq(seq);
    }

    /**
     * @see Seq#of(Object)
     */
    static <T> Seq2<T> of(final T value) {
        final Seq<T> seq = Seq.of(value);
        return seq(seq);
    }

    /**
     * @see Seq#seq(Stream)
     */
    static <T> Seq2<T> seq(final Stream<? extends T> stream) {
        final Seq<T> seq = Seq.seq(stream);
        return seq(seq);
    }

    /**
     * @see Seq#seq(Supplier)
     */
    static <T> Seq2<T> seq(final Supplier<? extends T> s) {
        final Seq<T> seq = Seq.seq(s);
        return seq(seq);
    }

    /**
     * @see Seq#seq(Iterator)
     */
    static <T> Seq2<T> seq(final Iterator<? extends T> iterator) {
        final Seq<T> seq = Seq.seq(iterator);
        return seq(seq);
    }

    /**
     * @see Seq#seq(Enumeration)
     */
    static <T> Seq2<T> seq(final Enumeration<T> enumeration) {
        final Seq<T> seq = Seq.seq(enumeration);
        return seq(seq);
    }

    /**
     * @see Seq#seq(Spliterator)
     */
    static <T> Seq2<T> seq(final Spliterator<? extends T> spliterator) {
        final Seq<T> seq = Seq.seq(spliterator);
        return seq(seq);
    }

    /**
     * @see Seq#seq(Object[], int, int)
     */
    static <T> Seq2<T> seq(final T[] values, final int startIndex, final int endIndex) {
        final Seq<T> seq = Seq.seq(values, startIndex, endIndex);
        return seq(seq);
    }

    /**
     * @see Seq#seq(Iterable)
     */
    static <T> Seq2<T> seq(final Iterable<? extends T> iterable) {
        final Seq<T> seq = Seq.seq(iterable);
        return seq(seq);
    }

    /**
     * @see Seq#seq(Optional)
     */
    static <T> Seq2<T> seq(final Optional<? extends T> optional) {
        final Seq<T> seq = Seq.seq(optional);
        return seq(seq);
    }

    /**
     * Wrap a {@code Stream} into a {@code Seq}.
     */
    static <T> Seq2<T> seq(final Seq2<T> seq2) {
        return seq(seq2.toSeq());
    }

    /**
     * Wrap a {@code Stream} into a {@code Seq}.
     */
    static <T> Seq2<T> seq(final Seq<T> seq) {
        return new Seq2Impl<>(seq);
    }

    /**
     * @see Seq#seq(Map)
     */
    static <V, K> Seq2<Tuple2<K, V>> seq(final Map<? extends K, ? extends V> map) {
        final Seq<Tuple2<K, V>> seq = Seq.seq(map);
        return seq(seq);
    }

    @Override
    default boolean allMatch(final Predicate<? super T> predicate) {
        return this.toSeq().allMatch(predicate);
    }

    @Override
    default boolean anyMatch(final Predicate<? super T> predicate) {
        return this.toSeq().anyMatch(predicate);
    }

    /**
     * @see Seq#append(Stream)
     */
    default Seq2<T> append(final Stream<? extends T> other) {
        return seq(this.toSeq().append(other));
    }

    /**
     * @see Seq#append(Iterable)
     */
    default Seq2<T> append(final Iterable<? extends T> other) {
        return seq(this.toSeq().append(other));
    }

    /**
     * @see Seq#append(Seq)
     */
    default Seq2<T> append(final Seq2<? extends T> other) {
        return this.append(other.toSeq());
    }

    /**
     * @see Seq#append(Seq)
     */
    default Seq2<T> append(final Seq<? extends T> other) {
        return seq(this.toSeq().append(other));
    }

    /**
     * @see Seq#append(Object)
     */
    default Seq2<T> append(final T other) {
        return seq(this.toSeq().append(other));
    }

    /**
     * @see Seq#append(Object[])
     */
    default Seq2<T> append(final T... other) {
        return seq(this.toSeq().append(other));
    }

    /**
     * @see Seq#append(Optional)
     */
    default Seq2<T> append(final Optional<? extends T> other) {
        return seq(this.toSeq().append(other));
    }

    /**
     * @see Seq#cast(Class)
     */
    default <U> Seq2<U> cast(final Class<? extends U> type) {
        return seq(this.toSeq().cast(type));
    }

    @Override
    default void close() {
        this.toSeq().close();
    }

    @Override
    default <R> R collect(final Supplier<R> supplier, final BiConsumer<R, ? super T> accumulator,
            final BiConsumer<R, R> combiner) {
        return this.toSeq().collect(supplier, accumulator, combiner);
    }

    @Override
    default <R, A> R collect(final Collector<? super T, A, R> collector) {
        return this.toSeq().collect(collector);
    }

    /**
     * @see Seq#concat(Object)
     */
    default Seq2<T> concat(final T other) {
        return seq(this.toSeq().concat(other));
    }

    /**
     * @see Seq#concat(Object[])
     */
    default Seq2<T> concat(final T... other) {
        return seq(this.toSeq().concat(other));
    }

    /**
     * @see Seq#concat(Iterable)
     */
    default Seq2<T> concat(final Iterable<? extends T> other) {
        return seq(this.toSeq().concat(other));
    }

    /**
     * @see Seq#concat(Stream)
     */
    default Seq2<T> concat(final Stream<? extends T> other) {
        return seq(this.toSeq().concat(other));
    }

    /**
     * @see Seq#concat(Seq)
     */
    default Seq2<T> concat(final Seq<? extends T> other) {
        return seq(this.toSeq().concat(other));
    }

    /**
     * @see Seq#concat(Seq)
     */
    default Seq2<T> concat(final Seq2<? extends T> other) {
        return this.concat(other.toSeq());
    }

    /**
     * @see Seq#concat(Optional)
     */
    default Seq2<T> concat(final Optional<? extends T> other) {
        return seq(this.toSeq().concat(other));
    }

    @Override
    default long count() {
        return this.toSeq().count();
    }

    /**
     * @see Seq#crossApply(Function)
     */
    default <U> PairSeq<T, U> crossApply(final Function<? super T, ? extends Iterable<? extends U>> function) {
        return PairSeq.seq(this.toSeq().crossApply(function));
    }

    /**
     * @see Seq#crossJoin(Iterable)
     */
    default <U> PairSeq<T, U> crossJoin(final Iterable<? extends U> other) {
        return PairSeq.seq(this.toSeq().crossJoin(other));
    }

    /**
     * @see Seq#crossJoin(Stream)
     */
    default <U> PairSeq<T, U> crossJoin(final Stream<? extends U> other) {
        return PairSeq.seq(this.toSeq().crossJoin(other));
    }

    /**
     * @see Seq#crossJoin(Seq)
     */
    default <U> PairSeq<T, U> crossJoin(final Seq<? extends U> other) {
        return PairSeq.seq(this.toSeq().crossJoin(other));
    }

    /**
     * @see Seq#crossJoin(Seq)
     */
    default <U> PairSeq<T, U> crossJoin(final Seq2<? extends U> other) {
        return this.crossJoin(other.toSeq());
    }

    /**
     * @see Seq#crossSelfJoin()
     */
    default PairSeq<T, T> crossSelfJoin() {
        return PairSeq.seq(this.toSeq().crossSelfJoin());
    }

    /**
     * @see Seq#cycle()
     */
    default Seq2<T> cycle() {
        return seq(this.toSeq().cycle());
    }

    /**
     * @see Seq#cycle(long)
     */
    default Seq2<T> cycle(final long times) {
        return seq(this.toSeq().cycle(times));
    }

    /**
     * @see Seq#distinct(Function)
     */
    default <U> Seq2<T> distinct(final Function<? super T, ? extends U> keyExtractor) {
        return seq(this.toSeq().distinct(keyExtractor));
    }

    @Override
    default Seq2<T> distinct() {
        return seq(this.toSeq().distinct());
    }

    /**
     * @see Seq#drop(long)
     */
    default Seq2<T> drop(final long n) {
        return seq(this.toSeq().drop(n));
    }

    @Override
    default Seq2<T> dropWhile(final Predicate<? super T> predicate) {
        return seq(this.toSeq().dropWhile(predicate));
    }

    /**
     * @see Seq#duplicate()
     */
    default Tuple2<Seq2<T>, Seq2<T>> duplicate() {
        return this.toSeq().duplicate().map1(Seq2::seq).map2(Seq2::seq);
    }

    @Override
    default Seq2<T> filter(final Predicate<? super T> predicate) {
        return seq(this.toSeq().filter(predicate));
    }

    /**
     * Same as {@link Seq2#filter(Predicate)} but with negated predicate.
     *
     * @see Seq#map(Function)
     */
    default Seq2<T> filterNot(final Predicate<? super T> predicate) {
        return seq(this.filter(predicate.negate()));
    }

    @Override
    default Optional<T> findAny() {
        return this.toSeq().findAny();
    }

    @Override
    default Optional<T> findFirst() {
        return this.toSeq().findFirst();
    }

    @Override
    default <R> Seq2<R> flatMap(final Function<? super T, ? extends Stream<? extends R>> mapper) {
        return seq(this.toSeq().flatMap(mapper));
    }

    @Override
    default DoubleStream flatMapToDouble(final Function<? super T, ? extends DoubleStream> mapper) {
        return this.toSeq().flatMapToDouble(mapper);
    }

    @Override
    default IntStream flatMapToInt(final Function<? super T, ? extends IntStream> mapper) {
        return this.toSeq().flatMapToInt(mapper);
    }

    /**
     * Flat map this stream. Instead of returning a stream, the passed function can return an {@code Iterable}.
     *
     * @see Seq#flatMap(Function)
     */
    default <R> Seq2<R> flatMapToIterable(final Function<? super T, ? extends Iterable<? extends R>> mapper) {
        return seq(this.toSeq().flatMap(t -> seq(mapper.apply(t))));
    }

    /**
     * Flat map this stream. Instead of returning a stream, the passed function can return an {@code Optional}.
     *
     * @see Seq#flatMap(Function)
     */
    default <R> Seq2<R> flatMapToOptional(final Function<? super T, ? extends Optional<? extends R>> mapper) {
        return seq(this.toSeq().flatMap(t -> mapper.apply(t).stream()));
    }

    @Override
    default LongStream flatMapToLong(final Function<? super T, ? extends LongStream> mapper) {
        return this.toSeq().flatMapToLong(mapper);
    }

    /**
     * Flat map a {@code Seq2} to a {@code PairSeq}
     */
    default <K, V> PairSeq<K, V> flatMapToPair(
            final Function<? super T, ? extends Stream<? extends Tuple2<K, V>>> mapper) {
        final Seq2<Tuple2<K, V>> seq = this.flatMap(mapper);
        return PairSeq.seq(seq);
    }

    /**
     * Flat map a {@code Seq2} to a {@code PairSeq}
     */
    default <K, V> PairSeq<K, V> flatMapToOptionalPair(
            final Function<? super T, ? extends Optional<? extends Tuple2<K, V>>> mapper) {
        return this.flatMapToPair(t -> mapper.apply(t).stream());
    }

    /**
     * Flat map a {@code Seq2} to a {@code PairSeq}
     */
    default <K, V> PairSeq<K, V> flatMapToIterablePair(
            final Function<? super T, ? extends Iterable<? extends Tuple2<K, V>>> mapper) {
        return this.flatMapToPair(t -> seq(mapper.apply(t)));
    }

    @Override
    default void forEach(final Consumer<? super T> action) {
        this.toSeq().forEach(action);
    }

    @Override
    default void forEachOrdered(final Consumer<? super T> action) {
        this.toSeq().forEachOrdered(action);
    }

    /**
     * @see Seq#grouped(Function)
     */
    default <K> PairSeq<K, Seq2<T>> grouped(final Function<? super T, ? extends K> classifier) {
        final Seq<Tuple2<K, Seq<T>>> seq = this.toSeq().grouped(classifier);
        final PairSeq<K, Seq<T>> pairSeq = PairSeq.seq(seq);
        return pairSeq.mapValues(Seq2::seq);
    }

    /**
     * @see Seq#grouped(Function, Collector)
     */
    default <K, A, D> PairSeq<K, D> grouped(final Function<? super T, ? extends K> classifier,
            final Collector<? super T, A, D> downstream) {
        return PairSeq.seq(this.toSeq().grouped(classifier, downstream));
    }

    /**
     * @see Seq#innerJoin(Stream, BiPredicate)
     */
    default <U> PairSeq<T, U> innerJoin(final Stream<? extends U> other,
            final BiPredicate<? super T, ? super U> predicate) {
        return PairSeq.seq(this.toSeq().innerJoin(other, predicate));
    }

    /**
     * @see Seq#innerJoin(Iterable, BiPredicate)
     */
    default <U> PairSeq<T, U> innerJoin(final Iterable<? extends U> other,
            final BiPredicate<? super T, ? super U> predicate) {
        return PairSeq.seq(this.toSeq().innerJoin(other, predicate));
    }

    /**
     * @see Seq#innerJoin(Seq, BiPredicate)
     */
    default <U> PairSeq<T, U> innerJoin(final Seq<? extends U> other,
            final BiPredicate<? super T, ? super U> predicate) {
        return PairSeq.seq(this.toSeq().innerJoin(other, predicate));
    }

    /**
     * @see Seq#innerJoin(Seq, BiPredicate)
     */
    default <U> PairSeq<T, U> innerJoin(final Seq2<? extends U> other,
            final BiPredicate<? super T, ? super U> predicate) {
        return this.innerJoin(other.toSeq(), predicate);
    }

    /**
     * @see Seq#innerSelfJoin(BiPredicate)
     */
    default PairSeq<T, T> innerSelfJoin(final BiPredicate<? super T, ? super T> predicate) {
        return PairSeq.seq(this.toSeq().innerSelfJoin(predicate));
    }

    /**
     * @see Seq#intersperse(Object)
     */
    default Seq2<T> intersperse(final T value) {
        return seq(this.toSeq().intersperse(value));
    }

    @Override
    default boolean isParallel() {
        return this.toSeq().isParallel();
    }

    @Override
    default Iterator<T> iterator() {
        return this.toSeq().iterator();
    }

    /**
     * @see Seq#leftOuterJoin(Stream, BiPredicate)
     */
    default <U> PairSeq<T, U> leftOuterJoin(final Stream<? extends U> other,
            final BiPredicate<? super T, ? super U> predicate) {
        return PairSeq.seq(this.toSeq().leftOuterJoin(other, predicate));
    }

    /**
     * @see Seq#leftOuterJoin(Iterable, BiPredicate)
     */
    default <U> PairSeq<T, U> leftOuterJoin(final Iterable<? extends U> other,
            final BiPredicate<? super T, ? super U> predicate) {
        return PairSeq.seq(this.toSeq().leftOuterJoin(other, predicate));
    }

    /**
     * @see Seq#leftOuterJoin(Seq, BiPredicate)
     */
    default <U> PairSeq<T, U> leftOuterJoin(final Seq<? extends U> other,
            final BiPredicate<? super T, ? super U> predicate) {
        return PairSeq.seq(this.toSeq().leftOuterJoin(other, predicate));
    }

    /**
     * @see Seq#leftOuterJoin(Seq, BiPredicate)
     */
    default <U> PairSeq<T, U> leftOuterJoin(final Seq2<? extends U> other,
            final BiPredicate<? super T, ? super U> predicate) {
        return this.leftOuterJoin(other.toSeq(), predicate);
    }

    /**
     * @see Seq#leftOuterSelfJoin(BiPredicate)
     */
    default PairSeq<T, T> leftOuterSelfJoin(final BiPredicate<? super T, ? super T> predicate) {
        return PairSeq.seq(this.toSeq().leftOuterSelfJoin(predicate));
    }

    @Override
    default Seq2<T> limit(final long maxSize) {
        return seq(this.toSeq().limit(maxSize));
    }

    /**
     * @see Seq#limitUntil(Predicate)
     */
    default Seq2<T> limitUntil(final Predicate<? super T> predicate) {
        return seq(this.toSeq().limitUntil(predicate));
    }

    /**
     * @see Seq#limitUntilClosed(Predicate)
     */
    default Seq2<T> limitUntilClosed(final Predicate<? super T> predicate) {
        return seq(this.toSeq().limitUntilClosed(predicate));
    }

    /**
     * @see Seq#limitWhile(Predicate)
     */
    default Seq2<T> limitWhile(final Predicate<? super T> predicate) {
        return seq(this.toSeq().limitWhile(predicate));
    }

    /**
     * @see Seq#limitWhileClosed(Predicate)
     */
    default Seq2<T> limitWhileClosed(final Predicate<? super T> predicate) {
        return seq(this.toSeq().limitWhileClosed(predicate));
    }

    @Override
    default <U> Seq2<U> map(final Function<? super T, ? extends U> function) {
        return seq(this.toSeq().map(function));
    }

    @Override
    default DoubleStream mapToDouble(final ToDoubleFunction<? super T> mapper) {
        return this.toSeq().mapToDouble(mapper);
    }

    @Override
    default IntStream mapToInt(final ToIntFunction<? super T> function) {
        return this.toSeq().mapToInt(function);
    }

    @Override
    default LongStream mapToLong(final ToLongFunction<? super T> mapper) {
        return this.toSeq().mapToLong(mapper);
    }

    /**
     * Map a {@code Seq2} to a {@code PairSeq}
     *
     * @see Seq#map(Function)
     */
    default <K, V> PairSeq<K, V> mapToPair(final Function<? super T, ? extends Tuple2<K, V>> mapper) {
        final Seq<Tuple2<K, V>> seq = this.toSeq().map(mapper);
        return PairSeq.seq(seq);
    }

    /**
     * Map a {@code Seq2} to a {@code PairSeq}
     *
     * @param keyMapper function to extract key from input
     * @param valueMapper function to extract value from input
     * @see Seq#map(Function)
     */
    default <K, V> PairSeq<K, V> mapToPair(final Function<? super T, ? extends K> keyMapper,
            final Function<? super T, ? extends V> valueMapper) {
        return this.mapToPair(this.toTuple(keyMapper, valueMapper));
    }

    @Override
    default Optional<T> max(final Comparator<? super T> comparator) {
        return this.toSeq().max(comparator);
    }

    /**
     * @see Seq#maxAll()
     */
    default Seq2<T> maxAll() {
        return seq(this.toSeq().maxAll());
    }

    /**
     * @see Seq#maxAll(Comparator)
     */
    default Seq2<T> maxAll(final Comparator<? super T> comparator) {
        return seq(this.toSeq().maxAll(comparator));
    }

    /**
     * @see Seq#maxAll(Function)
     */
    default <U extends Comparable<U>> Seq2<U> maxAll(final Function<? super T, ? extends U> function) {
        return seq(this.toSeq().maxAll(function));
    }

    /**
     * @see Seq#maxAll(Function, Comparator)
     */
    default <U> Seq2<U> maxAll(final Function<? super T, ? extends U> function,
            final Comparator<? super U> comparator) {
        return seq(this.toSeq().maxAll(function, comparator));
    }

    /**
     * @see Seq#maxAllBy(Function)
     */
    default <U extends Comparable<U>> Seq2<T> maxAllBy(final Function<? super T, ? extends U> function) {
        return seq(this.toSeq().maxAllBy(function));
    }

    /**
     * @see Seq#maxAllBy(Function, Comparator)
     */
    default <U> Seq2<T> maxAllBy(final Function<? super T, ? extends U> function,
            final Comparator<? super U> comparator) {
        return seq(this.toSeq().maxAllBy(function, comparator));
    }

    @Override
    default Optional<T> min(final Comparator<? super T> comparator) {
        return this.toSeq().min(comparator);
    }

    /**
     * @see Seq#minAll()
     */
    default Seq2<T> minAll() {
        return seq(this.toSeq().minAll());
    }

    /**
     * @see Seq#minAll(Comparator)
     */
    default Seq2<T> minAll(final Comparator<? super T> comparator) {
        return seq(this.toSeq().minAll(comparator));
    }

    /**
     * @see Seq#minAll(Function)
     */
    default <U extends Comparable<U>> Seq2<U> minAll(final Function<? super T, ? extends U> function) {
        return seq(this.toSeq().minAll(function));
    }

    /**
     * @see Seq#minAll(Function, Comparator)
     */
    default <U> Seq2<U> minAll(final Function<? super T, ? extends U> function,
            final Comparator<? super U> comparator) {
        return seq(this.toSeq().minAll(function, comparator));
    }

    /**
     * @see Seq#minAllBy(Function)
     */
    default <U extends Comparable<U>> Seq2<T> minAllBy(final Function<? super T, ? extends U> function) {
        return seq(this.toSeq().minAllBy(function));
    }

    /**
     * @see Seq#minAllBy(Function, Comparator)
     */
    default <U> Seq2<T> minAllBy(final Function<? super T, ? extends U> function,
            final Comparator<? super U> comparator) {
        return seq(this.toSeq().minAllBy(function, comparator));
    }

    /**
     * @see Seq#modeAll()
     */
    default Seq2<T> modeAll() {
        return seq(this.toSeq().modeAll());
    }

    /**
     * @see Seq#modeAllBy(Function)
     */
    default <U> Seq2<T> modeAllBy(final Function<? super T, ? extends U> function) {
        return seq(this.toSeq().modeAllBy(function));
    }

    @Override
    default boolean noneMatch(final Predicate<? super T> predicate) {
        return this.toSeq().noneMatch(predicate);
    }

    /**
     * @see Seq#ofType(Class)
     */
    default <U> Seq2<U> ofType(final Class<? extends U> type) {
        return seq(this.toSeq().ofType(type));
    }

    @Override
    default Seq2<T> onClose(final Runnable closeHandler) {
        return seq(this.toSeq().onClose(closeHandler));
    }

    /**
     * @see Seq#onEmpty(Object)
     */
    default Seq2<T> onEmpty(final T value) {
        return seq(this.toSeq().onEmpty(value));
    }

    /**
     * @see Seq#onEmptyGet(Supplier)
     */
    default Seq2<T> onEmptyGet(final Supplier<? extends T> supplier) {
        return seq(this.toSeq().onEmptyGet(supplier));
    }

    /**
     * @see Seq#onEmptyThrow(Supplier)
     */
    default Seq2<T> onEmptyThrow(final Supplier<? extends Throwable> supplier) {
        return seq(this.toSeq().onEmptyThrow(supplier));
    }

    /**
     * @see Seq#outerApply(Function)
     */
    default <U> PairSeq<T, U> outerApply(final Function<? super T, ? extends Iterable<? extends U>> function) {
        return PairSeq.seq(this.toSeq().outerApply(function));
    }

    @Override
    default Seq2<T> parallel() {
        return seq(this.toSeq().parallel());
    }

    /**
     * @see Seq#partition(Predicate)
     */
    default Tuple2<Seq2<T>, Seq2<T>> partition(final Predicate<? super T> predicate) {
        return this.toSeq().partition(predicate).map1(Seq2::seq).map2(Seq2::seq);
    }

    @Override
    default Seq2<T> peek(final Consumer<? super T> action) {
        return seq(this.toSeq().peek(action));
    }

    /**
     * @see Seq#prepend(Object)
     */
    default Seq2<T> prepend(final T other) {
        return seq(this.toSeq().prepend(other));
    }

    /**
     * @see Seq#prepend(Object[])
     */
    default Seq2<T> prepend(final T... other) {
        return seq(this.toSeq().prepend(other));
    }

    /**
     * @see Seq#prepend(Iterable)
     */
    default Seq2<T> prepend(final Iterable<? extends T> other) {
        return seq(this.toSeq().prepend(other));
    }

    /**
     * @see Seq#prepend(Stream)
     */
    default Seq2<T> prepend(final Stream<? extends T> other) {
        return seq(this.toSeq().prepend(other));
    }

    /**
     * @see Seq#prepend(Seq)
     */
    default Seq2<T> prepend(final Seq<? extends T> other) {
        return seq(this.toSeq().prepend(other));
    }

    /**
     * @see Seq#prepend(Seq)
     */
    default Seq2<T> prepend(final Seq2<? extends T> other) {
        return this.prepend(other.toSeq());
    }

    /**
     * @see Seq#prepend(Optional)
     */
    default Seq2<T> prepend(final Optional<? extends T> other) {
        return seq(this.toSeq().prepend(other));
    }

    @Override
    default T reduce(final T identity, final BinaryOperator<T> accumulator) {
        return this.toSeq().reduce(identity, accumulator);
    }

    @Override
    default Optional<T> reduce(final BinaryOperator<T> accumulator) {
        return this.toSeq().reduce(accumulator);
    }

    @Override
    default <U> U reduce(final U identity, final BiFunction<U, ? super T, U> accumulator,
            final BinaryOperator<U> combiner) {
        return this.toSeq().reduce(identity, accumulator, combiner);
    }

    /**
     * @see Seq#remove(Object)
     */
    default Seq2<T> remove(final T other) {
        return seq(this.toSeq().remove(other));
    }

    /**
     * @see Seq#removeAll(Object[])
     */
    default Seq2<T> removeAll(final T... other) {
        return seq(this.toSeq().removeAll(other));
    }

    /**
     * @see Seq#removeAll(Stream)
     */
    default Seq2<T> removeAll(final Stream<? extends T> other) {
        return seq(this.toSeq().removeAll(other));
    }

    /**
     * @see Seq#removeAll(Iterable)
     */
    default Seq2<T> removeAll(final Iterable<? extends T> other) {
        return seq(this.toSeq().removeAll(other));
    }

    /**
     * @see Seq#removeAll(Seq)
     */
    default Seq2<T> removeAll(final Seq<? extends T> other) {
        return seq(this.toSeq().removeAll(other));
    }

    /**
     * @see Seq#removeAll(Seq)
     */
    default Seq2<T> removeAll(final Seq2<? extends T> other) {
        return this.removeAll(other.toSeq());
    }

    /**
     * @see Seq#retainAll(Object[])
     */
    default Seq2<T> retainAll(final T... other) {
        return seq(this.toSeq().retainAll(other));
    }

    /**
     * @see Seq#retainAll(Stream)
     */
    default Seq2<T> retainAll(final Stream<? extends T> other) {
        return seq(this.toSeq().retainAll(other));
    }

    /**
     * @see Seq#retainAll(Iterable)
     */
    default Seq2<T> retainAll(final Iterable<? extends T> other) {
        return seq(this.toSeq().retainAll(other));
    }

    /**
     * @see Seq#retainAll(Seq)
     */
    default Seq2<T> retainAll(final Seq<? extends T> other) {
        return seq(this.toSeq().retainAll(other));
    }

    /**
     * @see Seq#retainAll(Seq)
     */
    default Seq2<T> retainAll(final Seq2<? extends T> other) {
        return this.retainAll(other.toSeq());
    }

    /**
     * @see Seq#reverse()
     */
    default Seq2<T> reverse() {
        return seq(this.toSeq().reverse());
    }

    /**
     * @see Seq#rightOuterJoin(Stream, BiPredicate)
     */
    default <U> PairSeq<T, U> rightOuterJoin(final Stream<? extends U> other,
            final BiPredicate<? super T, ? super U> predicate) {
        return PairSeq.seq(this.toSeq().rightOuterJoin(other, predicate));
    }

    /**
     * @see Seq#rightOuterJoin(Iterable, BiPredicate)
     */
    default <U> PairSeq<T, U> rightOuterJoin(final Iterable<? extends U> other,
            final BiPredicate<? super T, ? super U> predicate) {
        return PairSeq.seq(this.toSeq().rightOuterJoin(other, predicate));
    }

    /**
     * @see Seq#rightOuterJoin(Seq, BiPredicate)
     */
    default <U> PairSeq<T, U> rightOuterJoin(final Seq<? extends U> other,
            final BiPredicate<? super T, ? super U> predicate) {
        return PairSeq.seq(this.toSeq().rightOuterJoin(other, predicate));
    }

    /**
     * @see Seq#rightOuterJoin(Seq, BiPredicate)
     */
    default <U> PairSeq<T, U> rightOuterJoin(final Seq2<? extends U> other,
            final BiPredicate<? super T, ? super U> predicate) {
        return this.rightOuterJoin(other.toSeq(), predicate);
    }

    /**
     * @see Seq#rightOuterSelfJoin(BiPredicate)
     */
    default PairSeq<T, T> rightOuterSelfJoin(final BiPredicate<? super T, ? super T> predicate) {
        return PairSeq.seq(this.toSeq().rightOuterSelfJoin(predicate));
    }

    /**
     * @see Seq#scanLeft(Object, BiFunction)
     */
    default <U> Seq2<U> scanLeft(final U seed, final BiFunction<? super U, ? super T, ? extends U> function) {
        return seq(this.toSeq().scanLeft(seed, function));
    }

    /**
     * @see Seq#scanRight(Object, BiFunction)
     */
    default <U> Seq2<U> scanRight(final U seed, final BiFunction<? super T, ? super U, ? extends U> function) {
        return seq(this.toSeq().scanRight(seed, function));
    }

    /**
     * Map a {@code Seq2} to a {@code PairSeq}
     *
     * @see Seq#map(Function)
     */
    default <K> PairSeq<K, T> selectKey(final Function<? super T, ? extends K> keyMapper) {
        return this.mapToPair(keyMapper, Function.identity());
    }

    @Override
    default Seq2<T> sequential() {
        return seq(this.toSeq().sequential());
    }

    /**
     * @see Seq#shuffle()
     */
    default Seq2<T> shuffle() {
        return seq(this.toSeq().shuffle());
    }

    /**
     * @see Seq#shuffle(Random)
     */
    default Seq2<T> shuffle(final Random random) {
        return seq(this.toSeq().shuffle(random));
    }

    @Override
    default Seq2<T> skip(final long n) {
        return seq(this.toSeq().skip(n));
    }

    /**
     * @see Seq#skipUntil(Predicate)
     */
    default Seq2<T> skipUntil(final Predicate<? super T> predicate) {
        return seq(this.toSeq().skipUntil(predicate));
    }

    /**
     * @see Seq#skipUntilClosed(Predicate)
     */
    default Seq2<T> skipUntilClosed(final Predicate<? super T> predicate) {
        return seq(this.toSeq().skipUntilClosed(predicate));
    }

    /**
     * @see Seq#skipWhile(Predicate)
     */
    default Seq2<T> skipWhile(final Predicate<? super T> predicate) {
        return seq(this.toSeq().skipWhile(predicate));
    }

    /**
     * @see Seq#skipWhileClosed(Predicate)
     */
    default Seq2<T> skipWhileClosed(final Predicate<? super T> predicate) {
        return seq(this.toSeq().skipWhileClosed(predicate));
    }

    /**
     * @see Seq#slice(long, long)
     */
    default Seq2<T> slice(final long from, final long to) {
        return seq(this.toSeq().slice(from, to));
    }

    /**
     * @see Seq#sliding(long)
     */
    default Seq2<Seq2<T>> sliding(final long size) {
        return seq(this.toSeq().sliding(size)).map(Seq2::seq);
    }

    @Override
    default Seq2<T> sorted() {
        return seq(this.toSeq().sorted());
    }

    @Override
    default Seq2<T> sorted(final Comparator<? super T> comparator) {
        return seq(this.toSeq().sorted(comparator));
    }

    /**
     * @see Seq#sorted(Function)
     */
    default <U extends Comparable<? super U>> Seq2<T> sorted(final Function<? super T, ? extends U> function) {
        return seq(this.toSeq().sorted(function));
    }

    /**
     * @see Seq#sorted(Function, Comparator)
     */
    default <U> Seq2<T> sorted(final Function<? super T, ? extends U> function,
            final Comparator<? super U> comparator) {
        return seq(this.toSeq().sorted(function, comparator));
    }

    /**
     * @see Seq#splitAt(long)
     */
    default Tuple2<Seq2<T>, Seq2<T>> splitAt(final long position) {
        return this.toSeq().splitAt(position).map1(Seq2::seq).map2(Seq2::seq);
    }

    /**
     * @see Seq#splitAtHead()
     */
    default Tuple2<Optional<T>, Seq2<T>> splitAtHead() {
        return this.toSeq().splitAtHead().map2(Seq2::seq);
    }

    @Override
    default Spliterator<T> spliterator() {
        return this.toSeq().spliterator();
    }

    /**
     * @see Seq#take(long)
     */
    default Seq2<T> take(final long maxSize) {
        return seq(this.toSeq().take(maxSize));
    }

    @Override
    default Stream<T> takeWhile(final Predicate<? super T> predicate) {
        return this.toSeq().takeWhile(predicate);
    }

    @Override
    default Object[] toArray() {
        return this.toSeq().toArray();
    }

    @Override
    default <A> A[] toArray(final IntFunction<A[]> generator) {
        return this.toSeq().toArray(generator);
    }

    /**
     * @see Seq#transform(Function)
     */
    default <U> U transform(final Function<? super Seq<T>, ? extends U> transformer) {
        return this.toSeq().transform(transformer);
    }

    @Override
    default Seq2<T> unordered() {
        return seq(this.toSeq().unordered());
    }

    /**
     * @see Seq#window()
     */
    default Seq2<Window<T>> window() {
        return seq(this.toSeq().window());
    }

    /**
     * @see Seq#window(long, long)
     */
    default Seq2<Window<T>> window(final long lower, final long upper) {
        return seq(this.toSeq().window(lower, upper));
    }

    /**
     * @see Seq#window(Comparator)
     */
    default Seq2<Window<T>> window(final Comparator<? super T> orderBy) {
        return seq(this.toSeq().window(orderBy));
    }

    /**
     * @see Seq#window(Comparator, long, long)
     */
    default Seq2<Window<T>> window(final Comparator<? super T> orderBy, final long lower, final long upper) {
        return seq(this.toSeq().window(orderBy, lower, upper));
    }

    /**
     * @see Seq#window(Function)
     */
    default <U> Seq2<Window<T>> window(final Function<? super T, ? extends U> partitionBy) {
        return seq(this.toSeq().window(partitionBy));
    }

    /**
     * @see Seq#window(Function, long, long)
     */
    default <U> Seq2<Window<T>> window(final Function<? super T, ? extends U> partitionBy, final long lower,
            final long upper) {
        return seq(this.toSeq().window(partitionBy, lower, upper));
    }

    /**
     * @see Seq#window(Function, Comparator)
     */
    default <U> Seq2<Window<T>> window(final Function<? super T, ? extends U> partitionBy,
            final Comparator<? super T> orderBy) {
        return seq(this.toSeq().window(partitionBy, orderBy));
    }

    /**
     * @see Seq#window(Function, Comparator, long, long)
     */
    default <U> Seq2<Window<T>> window(final Function<? super T, ? extends U> partitionBy,
            final Comparator<? super T> orderBy,
            final long lower, final long upper) {
        return seq(this.toSeq().window(partitionBy, orderBy, lower, upper));
    }

    /**
     * @see Seq#zip(Stream, BiFunction)
     */
    default <U, R> Seq2<R> zip(final Stream<? extends U> other,
            final BiFunction<? super T, ? super U, ? extends R> zipper) {
        return seq(this.toSeq().zip(other, zipper));
    }

    /**
     * @see Seq#zip(Iterable, BiFunction)
     */
    default <U, R> Seq2<R> zip(final Iterable<? extends U> other,
            final BiFunction<? super T, ? super U, ? extends R> zipper) {
        return seq(this.toSeq().zip(other, zipper));
    }

    /**
     * @see Seq#zip(Seq, BiFunction)
     */
    default <U, R> Seq2<R> zip(final Seq<? extends U> other,
            final BiFunction<? super T, ? super U, ? extends R> zipper) {
        return seq(this.toSeq().zip(other, zipper));
    }

    /**
     * @see Seq#zip(Seq, BiFunction)
     */
    default <U, R> Seq2<R> zip(final Seq2<? extends U> other,
            final BiFunction<? super T, ? super U, ? extends R> zipper) {
        return this.zip(other.toSeq(), zipper);
    }

    /**
     * @see Seq#zip(Stream)
     */
    default <U> PairSeq<T, U> zip(final Stream<? extends U> other) {
        return PairSeq.seq(this.toSeq().zip(other));
    }

    /**
     * @see Seq#zip(Iterable)
     */
    default <U> PairSeq<T, U> zip(final Iterable<? extends U> other) {
        return PairSeq.seq(this.toSeq().zip(other));
    }

    /**
     * @see Seq#zip(Seq)
     */
    default <U> PairSeq<T, U> zip(final Seq<? extends U> other) {
        return PairSeq.seq(this.toSeq().zip(other));
    }

    /**
     * @see Seq#zip(Seq)
     */
    default <U> PairSeq<T, U> zip(final Seq2<? extends U> other) {
        return this.zip(other.toSeq());
    }

    default PairSeq<T, Long> zipWithIndex() {
        return PairSeq.seq(this.toSeq().zipWithIndex());
    }

    /**
     * @see Seq#zipWithIndex()
     */
    default <R> Seq2<R> zipWithIndex(final BiFunction<? super T, ? super Long, ? extends R> zipper) {
        return seq(this.toSeq().zipWithIndex(zipper));
    }

    private <K, V> Function<T, Tuple2<K, V>> toTuple(final Function<? super T, ? extends K> keyMapper,
            final Function<? super T, ? extends V> valueMapper) {
        return t -> new Tuple2<>(keyMapper.apply(t), valueMapper.apply(t));
    }
}
