/*
 * MIT License
 *
 * Copyright (c) 2023 bakdata
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bakdata.util.seq2;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.jooq.lambda.Seq;
import org.jooq.lambda.exception.TooManyElementsException;

public interface BaseSeq<T> {
    /**
     * @see Seq#avg()
     */
    default Optional<T> avg() {
        return this.toSeq().avg();
    }

    /**
     * @see Seq#avg(Function)
     */
    default <U> Optional<U> avg(final Function<? super T, ? extends U> function) {
        return this.toSeq().avg(function);
    }

    /**
     * @see Seq#avgDouble(ToDoubleFunction)
     */
    default double avgDouble(final ToDoubleFunction<? super T> function) {
        return this.toSeq().avgDouble(function);
    }

    /**
     * @see Seq#avgInt(ToIntFunction)
     */
    default double avgInt(final ToIntFunction<? super T> function) {
        return this.toSeq().avgInt(function);
    }

    /**
     * @see Seq#avgLong(ToLongFunction)
     */
    default double avgLong(final ToLongFunction<? super T> function) {
        return this.toSeq().avgLong(function);
    }

    /**
     * @see Seq#bitAnd()
     */
    default Optional<T> bitAnd() {
        return this.toSeq().bitAnd();
    }

    /**
     * @see Seq#bitAnd(Function)
     */
    default <U> Optional<U> bitAnd(final Function<? super T, ? extends U> function) {
        return this.toSeq().bitAnd(function);
    }

    /**
     * @see Seq#bitAndInt(ToIntFunction)
     */
    default int bitAndInt(final ToIntFunction<? super T> function) {
        return this.toSeq().bitAndInt(function);
    }

    /**
     * @see Seq#bitAndLong(ToLongFunction)
     */
    default long bitAndLong(final ToLongFunction<? super T> function) {
        return this.toSeq().bitAndLong(function);
    }

    /**
     * @see Seq#bitOr()
     */
    default Optional<T> bitOr() {
        return this.toSeq().bitOr();
    }

    /**
     * @see Seq#bitOr(Function)
     */
    default <U> Optional<U> bitOr(final Function<? super T, ? extends U> function) {
        return this.toSeq().bitOr(function);
    }

    /**
     * @see Seq#bitOrInt(ToIntFunction)
     */
    default int bitOrInt(final ToIntFunction<? super T> function) {
        return this.toSeq().bitOrInt(function);
    }

    /**
     * @see Seq#bitOrLong(ToLongFunction)
     */
    default long bitOrLong(final ToLongFunction<? super T> function) {
        return this.toSeq().bitAndLong(function);
    }

    /**
     * @see Seq#commonPrefix()
     */
    default String commonPrefix() {
        return this.toSeq().commonPrefix();
    }

    /**
     * @see Seq#commonSuffix()
     */
    default String commonSuffix() {
        return this.toSeq().commonSuffix();
    }

    /**
     * @see Seq#contains(Object)
     */
    default boolean contains(final T other) {
        return this.toSeq().contains(other);
    }

    /**
     * @see Seq#containsAll(Object[])
     */
    default boolean containsAll(final T... other) {
        return this.toSeq().containsAll(other);
    }

    /**
     * @see Seq#containsAll(Stream)
     */
    default boolean containsAll(final Stream<? extends T> other) {
        return this.toSeq().containsAll(other);
    }

    /**
     * @see Seq#containsAll(Iterable)
     */
    default boolean containsAll(final Iterable<? extends T> other) {
        return this.toSeq().containsAll(other);
    }

    /**
     * @see Seq#containsAll(Seq)
     */
    default boolean containsAll(final Seq<? extends T> other) {
        return this.toSeq().containsAll(other);
    }

    /**
     * @see Seq#containsAll(Seq)
     */
    default boolean containsAll(final Seq2<? extends T> other) {
        return this.containsAll(other.toSeq());
    }

    /**
     * @see Seq#containsAny(Object[])
     */
    default boolean containsAny(final T... other) {
        return this.toSeq().containsAny(other);
    }

    /**
     * @see Seq#containsAny(Stream)
     */
    default boolean containsAny(final Stream<? extends T> other) {
        return this.toSeq().containsAny(other);
    }

    /**
     * @see Seq#containsAny(Iterable)
     */
    default boolean containsAny(final Iterable<? extends T> other) {
        return this.toSeq().containsAny(other);
    }

    /**
     * @see Seq#containsAny(Seq)
     */
    default boolean containsAny(final Seq<? extends T> other) {
        return this.toSeq().containsAny(other);
    }

    /**
     * @see Seq#containsAny(Seq)
     */
    default boolean containsAny(final Seq2<? extends T> other) {
        return this.containsAny(other.toSeq());
    }

    /**
     * @see Seq#count(Predicate)
     */
    default long count(final Predicate<? super T> predicate) {
        return this.toSeq().count(predicate);
    }

    /**
     * @see Seq#countDistinct()
     */
    default long countDistinct() {
        return this.toSeq().countDistinct();
    }

    /**
     * @see Seq#countDistinct(Predicate)
     */
    default long countDistinct(final Predicate<? super T> predicate) {
        return this.toSeq().countDistinct(predicate);
    }

    /**
     * @see Seq#countDistinctBy(Function)
     */
    default <U> long countDistinctBy(final Function<? super T, ? extends U> function) {
        return this.toSeq().countDistinctBy(function);
    }

    /**
     * @see Seq#countDistinctBy(Function, Predicate)
     */
    default <U> long countDistinctBy(final Function<? super T, ? extends U> function,
            final Predicate<? super U> predicate) {
        return this.toSeq().countDistinctBy(function, predicate);
    }

    /**
     * @see Seq#findFirst(Predicate)
     */
    default Optional<T> findFirst(final Predicate<? super T> predicate) {
        return this.toSeq().findFirst(predicate);
    }

    /**
     * @see Seq#findLast()
     */
    default Optional<T> findLast() {
        return this.toSeq().findLast();
    }

    /**
     * @see Seq#findLast(Predicate)
     */
    default Optional<T> findLast(final Predicate<? super T> predicate) {
        return this.toSeq().findLast(predicate);
    }

    /**
     * @see Seq#findSingle()
     */
    default Optional<T> findSingle() throws TooManyElementsException {
        return this.toSeq().findSingle();
    }

    /**
     * @see Seq#foldLeft(Object, BiFunction)
     */
    default <U> U foldLeft(final U seed, final BiFunction<? super U, ? super T, ? extends U> function) {
        return this.toSeq().foldLeft(seed, function);
    }

    /**
     * @see Seq#foldRight(Object, BiFunction)
     */
    default <U> U foldRight(final U seed, final BiFunction<? super T, ? super U, ? extends U> function) {
        return this.toSeq().foldRight(seed, function);
    }

    /**
     * @see Seq#format()
     */
    default String format() {
        return this.toSeq().format();
    }

    /**
     * @see Seq#get(long)
     */
    default Optional<T> get(final long index) {
        return this.toSeq().get(index);
    }

    /**
     * @see Seq#groupBy(Function)
     */
    default <K> Map<K, List<T>> groupBy(final Function<? super T, ? extends K> classifier) {
        return this.toSeq().groupBy(classifier);
    }

    /**
     * @see Seq#groupBy(Function, Collector)
     */
    default <K, A, D> Map<K, D> groupBy(final Function<? super T, ? extends K> classifier,
            final Collector<? super T, A, D> downstream) {
        return this.toSeq().groupBy(classifier, downstream);
    }

    /**
     * @see Seq#groupBy(Function, Supplier, Collector)
     */
    default <K, D, A, M extends Map<K, D>> M groupBy(final Function<? super T, ? extends K> classifier,
            final Supplier<M> mapFactory, final Collector<? super T, A, D> downstream) {
        return this.toSeq().groupBy(classifier, mapFactory, downstream);
    }

    /**
     * @see Seq#indexOf(Object)
     */
    default OptionalLong indexOf(final T element) {
        return this.toSeq().indexOf(element);
    }

    /**
     * @see Seq#indexOf(Predicate)
     */
    default OptionalLong indexOf(final Predicate<? super T> predicate) {
        return this.toSeq().indexOf(predicate);
    }

    /**
     * @see Seq#isEmpty()
     */
    default boolean isEmpty() {
        return this.toSeq().isEmpty();
    }

    /**
     * @see Seq#isNotEmpty()
     */
    default boolean isNotEmpty() {
        return this.toSeq().isNotEmpty();
    }

    /**
     * @see Seq#max()
     */
    default Optional<T> max() {
        return this.toSeq().max();
    }

    /**
     * @see Seq#max(Function)
     */
    default <U extends Comparable<U>> Optional<U> max(final Function<? super T, ? extends U> function) {
        return this.toSeq().max(function);
    }

    /**
     * @see Seq#max(Function, Comparator)
     */
    default <U> Optional<U> max(final Function<? super T, ? extends U> function,
            final Comparator<? super U> comparator) {
        return this.toSeq().max(function, comparator);
    }

    /**
     * @see Seq#maxBy(Function)
     */
    default <U extends Comparable<U>> Optional<T> maxBy(final Function<? super T, ? extends U> function) {
        return this.toSeq().maxBy(function);
    }

    /**
     * @see Seq#maxBy(Function, Comparator)
     */
    default <U> Optional<T> maxBy(final Function<? super T, ? extends U> function,
            final Comparator<? super U> comparator) {
        return this.toSeq().maxBy(function, comparator);
    }

    /**
     * @see Seq#median()
     */
    default Optional<T> median() {
        return this.toSeq().median();
    }

    /**
     * @see Seq#median(Comparator)
     */
    default Optional<T> median(final Comparator<? super T> comparator) {
        return this.toSeq().median(comparator);
    }

    /**
     * @see Seq#medianBy(Function)
     */
    default <U extends Comparable<? super U>> Optional<T> medianBy(final Function<? super T, ? extends U> function) {
        return this.toSeq().medianBy(function);
    }

    /**
     * @see Seq#medianBy(Function, Comparator)
     */
    default <U> Optional<T> medianBy(final Function<? super T, ? extends U> function,
            final Comparator<? super U> comparator) {
        return this.toSeq().medianBy(function, comparator);
    }

    /**
     * @see Seq#min()
     */
    default Optional<T> min() {
        return this.toSeq().min();
    }

    /**
     * @see Seq#min(Function)
     */
    default <U extends Comparable<U>> Optional<U> min(final Function<? super T, ? extends U> function) {
        return this.toSeq().min(function);
    }

    /**
     * @see Seq#min(Function, Comparator)
     */
    default <U> Optional<U> min(final Function<? super T, ? extends U> function,
            final Comparator<? super U> comparator) {
        return this.toSeq().min(function, comparator);
    }

    /**
     * @see Seq#minBy(Function)
     */
    default <U extends Comparable<U>> Optional<T> minBy(final Function<? super T, ? extends U> function) {
        return this.toSeq().minBy(function);
    }

    /**
     * @see Seq#minBy(Function, Comparator)
     */
    default <U> Optional<T> minBy(final Function<? super T, ? extends U> function,
            final Comparator<? super U> comparator) {
        return this.toSeq().minBy(function, comparator);
    }

    /**
     * @see Seq#mode()
     */
    default Optional<T> mode() {
        return this.toSeq().mode();
    }

    /**
     * @see Seq#modeBy(Function)
     */
    default <U> Optional<T> modeBy(final Function<? super T, ? extends U> function) {
        return this.toSeq().modeBy(function);
    }

    /**
     * @see Seq#percentile(double)
     */
    default Optional<T> percentile(final double percentile) {
        return this.toSeq().percentile(percentile);
    }

    /**
     * @see Seq#percentile(double, Comparator)
     */
    default Optional<T> percentile(final double percentile, final Comparator<? super T> comparator) {
        return this.toSeq().percentile(percentile, comparator);
    }

    /**
     * @see Seq#percentileBy(double, Function)
     */
    default <U extends Comparable<? super U>> Optional<T> percentileBy(final double percentile,
            final Function<? super T, ? extends U> function) {
        return this.toSeq().percentileBy(percentile, function);
    }

    /**
     * @see Seq#percentileBy(double, Function, Comparator)
     */
    default <U> Optional<T> percentileBy(final double percentile, final Function<? super T, ? extends U> function,
            final Comparator<? super U> comparator) {
        return this.toSeq().percentileBy(percentile, function, comparator);
    }

    /**
     * @see Seq#print(PrintWriter)
     */
    default void print(final PrintWriter writer) {
        this.toSeq().print(writer);
    }

    /**
     * @see Seq#print(PrintStream)
     */
    default void print(final PrintStream stream) {
        this.toSeq().print(stream);
    }

    /**
     * @see Seq#printErr()
     */
    default void printErr() {
        this.toSeq().printErr();
    }

    /**
     * @see Seq#printOut()
     */
    default void printOut() {
        this.toSeq().printOut();
    }

    /**
     * @see Seq#sum()
     */
    default Optional<T> sum() {
        return this.toSeq().sum();
    }

    /**
     * @see Seq#sum(Function)
     */
    default <U> Optional<U> sum(final Function<? super T, ? extends U> function) {
        return this.toSeq().sum(function);
    }

    /**
     * @see Seq#sumDouble(ToDoubleFunction)
     */
    default double sumDouble(final ToDoubleFunction<? super T> function) {
        return this.toSeq().sumDouble(function);
    }

    /**
     * @see Seq#sumInt(ToIntFunction)
     */
    default int sumInt(final ToIntFunction<? super T> function) {
        return this.toSeq().sumInt(function);
    }

    /**
     * @see Seq#sumLong(ToLongFunction)
     */
    default long sumLong(final ToLongFunction<? super T> function) {
        return this.toSeq().sumLong(function);
    }

    /**
     * @see Seq#toCollection(Supplier)
     */
    default <C extends Collection<T>> C toCollection(final Supplier<C> factory) {
        return this.toSeq().toCollection(factory);
    }

    /**
     * @see Seq#toList()
     */
    List<T> toList(); // having a default implementation conflicts with Java 16+

    /**
     * @see Seq#toList(Supplier)
     */
    default <L extends List<T>> L toList(final Supplier<L> factory) {
        return this.toSeq().toList(factory);
    }

    /**
     * @see Seq#toMap(Function, Function)
     */
    default <K, V> Map<K, V> toMap(final Function<? super T, ? extends K> keyMapper,
            final Function<? super T, ? extends V> valueMapper) {
        return this.toSeq().toMap(keyMapper, valueMapper);
    }

    /**
     * @see Seq#toMap(Function)
     */
    default <K> Map<K, T> toMap(final Function<? super T, ? extends K> keyMapper) {
        return this.toSeq().toMap(keyMapper);
    }

    /**
     * Fallback to {@code Seq}. If methods of {@link Seq} are missing, implement them.
     */
    @Deprecated
    Seq<T> toSeq();

    /**
     * @see Seq#toSet()
     */
    default Set<T> toSet() {
        return this.toSeq().toSet();
    }

    /**
     * @see Seq#toSet(Supplier)
     */
    default <S extends Set<T>> S toSet(final Supplier<S> factory) {
        return this.toSeq().toSet(factory);
    }

    /**
     * @see Seq#toString(CharSequence)
     */
    default String toString(final String delimiter) {
        return this.toSeq().toString(delimiter);
    }

    /**
     * Consume a stream and concatenate all elements using a separator or return an empty Optional if the stream is
     * empty.
     *
     * @param delimiter the delimiter to be used between each element prefix – the sequence of characters to be used at
     * the
     * @return concatenated elements or empty Optional
     * @see #toString(String)
     */
    default Optional<String> toStringOrEmpty(final String delimiter) {
        final String seq = this.toString(delimiter);
        return seq.isEmpty() ? Optional.empty() : Optional.of(seq);
    }

    /**
     * @see Seq#toString(CharSequence, CharSequence, CharSequence)
     */
    default String toString(final String delimiter, final String prefix, final String suffix) {
        return this.toSeq().toString(delimiter, prefix, suffix);
    }

    /**
     * Consume a stream and concatenate all elements using a separator or return an empty Optional if the stream is
     * empty. Additionally, the string is prefixed and suffixed.
     *
     * @param delimiter the delimiter to be used between each element prefix – the sequence of characters to be used at
     * the
     * @param prefix the sequence of characters to be used at the beginning of the joined result
     * @param suffix the sequence of characters to be used at the end of the joined result
     * @return concatenated elements or empty Optional
     * @see #toString(String)
     */
    default Optional<String> toStringOrEmpty(final String delimiter, final String prefix, final String suffix) {
        final String seq = this.toString(delimiter, prefix, suffix);
        return seq.equals(prefix + suffix) ? Optional.empty() : Optional.of(seq);
    }

    /**
     * @see Seq#toUnmodifiableList()
     */
    default List<T> toUnmodifiableList() {
        return this.toSeq().toUnmodifiableList();
    }

    /**
     * @see Seq#toUnmodifiableSet()
     */
    default Set<T> toUnmodifiableSet() {
        return this.toSeq().toUnmodifiableSet();
    }
}
