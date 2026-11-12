/*
 * Copyright (c), 2026 bakdata GmbH
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jooq.lambda.Seq;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

/**
 * Tests that methods of {@link BaseSeq} delegate to the intended methods of {@link Seq}.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class BaseSeqTest {

    private final Function<Integer, Integer> function = i -> i;
    private final Predicate<Integer> predicate = i -> true;
    private final Comparator<Integer> comparator = Comparator.naturalOrder();
    @Mock
    private Seq<Integer> seq;

    private BaseSeq<Integer> seq2() {
        return Seq2.seq(this.seq);
    }

    @Test
    void shouldAvg() {
        this.seq2().avg();
        verify(this.seq).avg();
    }

    @Test
    void shouldAvgWithFunction() {
        this.seq2().avg(this.function);
        verify(this.seq).avg(this.function);
    }

    @Test
    void shouldAvgDouble() {
        final ToDoubleFunction<Integer> f = i -> i;
        this.seq2().avgDouble(f);
        verify(this.seq).avgDouble(f);
    }

    @Test
    void shouldAvgInt() {
        final ToIntFunction<Integer> f = i -> i;
        this.seq2().avgInt(f);
        verify(this.seq).avgInt(f);
    }

    @Test
    void shouldAvgLong() {
        final ToLongFunction<Integer> f = i -> i;
        this.seq2().avgLong(f);
        verify(this.seq).avgLong(f);
    }

    @Test
    void shouldBitAnd() {
        this.seq2().bitAnd();
        verify(this.seq).bitAnd();
    }

    @Test
    void shouldBitAndWithFunction() {
        this.seq2().bitAnd(this.function);
        verify(this.seq).bitAnd(this.function);
    }

    @Test
    void shouldBitAndInt() {
        final ToIntFunction<Integer> f = i -> i;
        this.seq2().bitAndInt(f);
        verify(this.seq).bitAndInt(f);
    }

    @Test
    void shouldBitAndLong() {
        final ToLongFunction<Integer> f = i -> i;
        this.seq2().bitAndLong(f);
        verify(this.seq).bitAndLong(f);
    }

    @Test
    void shouldBitOr() {
        this.seq2().bitOr();
        verify(this.seq).bitOr();
    }

    @Test
    void shouldBitOrWithFunction() {
        this.seq2().bitOr(this.function);
        verify(this.seq).bitOr(this.function);
    }

    @Test
    void shouldBitOrInt() {
        final ToIntFunction<Integer> f = i -> i;
        this.seq2().bitOrInt(f);
        verify(this.seq).bitOrInt(f);
    }

    @Test
    void shouldBitOrLong() {
        final ToLongFunction<Integer> f = i -> i;
        this.seq2().bitOrLong(f);
        verify(this.seq).bitOrLong(f);
    }

    @Test
    void shouldCommonPrefix() {
        this.seq2().commonPrefix();
        verify(this.seq).commonPrefix();
    }

    @Test
    void shouldCommonSuffix() {
        this.seq2().commonSuffix();
        verify(this.seq).commonSuffix();
    }

    @Test
    void shouldContain() {
        this.seq2().contains(1);
        verify(this.seq).contains(1);
    }

    @Test
    void shouldContainAllArray() {
        this.seq2().containsAll(1, 2);
        verify(this.seq).containsAll(1, 2);
    }

    @Test
    void shouldContainAllStream() {
        final Stream<Integer> other = Stream.of(1);
        this.seq2().containsAll(other);
        verify(this.seq).containsAll(other);
    }

    @Test
    void shouldContainAllIterable() {
        final Iterable<Integer> other = List.of(1);
        this.seq2().containsAll(other);
        verify(this.seq).containsAll(other);
    }

    @Test
    void shouldContainAllSeq() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().containsAll(other);
        verify(this.seq).containsAll(other);
    }

    @Test
    void shouldContainAllSeq2() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().containsAll(Seq2.seq(other));
        verify(this.seq).containsAll(other);
    }

    @Test
    void shouldContainAnyArray() {
        this.seq2().containsAny(1, 2);
        verify(this.seq).containsAny(1, 2);
    }

    @Test
    void shouldContainAnyStream() {
        final Stream<Integer> other = Stream.of(1);
        this.seq2().containsAny(other);
        verify(this.seq).containsAny(other);
    }

    @Test
    void shouldContainAnyIterable() {
        final Iterable<Integer> other = List.of(1);
        this.seq2().containsAny(other);
        verify(this.seq).containsAny(other);
    }

    @Test
    void shouldContainAnySeq() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().containsAny(other);
        verify(this.seq).containsAny(other);
    }

    @Test
    void shouldContainAnySeq2() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().containsAny(Seq2.seq(other));
        verify(this.seq).containsAny(other);
    }

    @Test
    void shouldCountWithPredicate() {
        this.seq2().count(this.predicate);
        verify(this.seq).count(this.predicate);
    }

    @Test
    void shouldCountDistinct() {
        this.seq2().countDistinct();
        verify(this.seq).countDistinct();
    }

    @Test
    void shouldCountDistinctWithPredicate() {
        this.seq2().countDistinct(this.predicate);
        verify(this.seq).countDistinct(this.predicate);
    }

    @Test
    void shouldCountDistinctBy() {
        this.seq2().countDistinctBy(this.function);
        verify(this.seq).countDistinctBy(this.function);
    }

    @Test
    void shouldCountDistinctByWithPredicate() {
        this.seq2().countDistinctBy(this.function, this.predicate);
        verify(this.seq).countDistinctBy(this.function, this.predicate);
    }

    @Test
    void shouldFindFirstWithPredicate() {
        this.seq2().findFirst(this.predicate);
        verify(this.seq).findFirst(this.predicate);
    }

    @Test
    void shouldFindLast() {
        this.seq2().findLast();
        verify(this.seq).findLast();
    }

    @Test
    void shouldFindLastWithPredicate() {
        this.seq2().findLast(this.predicate);
        verify(this.seq).findLast(this.predicate);
    }

    @Test
    void shouldFindSingle() {
        this.seq2().findSingle();
        verify(this.seq).findSingle();
    }

    @Test
    void shouldFoldLeft() {
        final BiFunction<String, Integer, String> f = (s, i) -> s + i;
        this.seq2().foldLeft("", f);
        verify(this.seq).foldLeft("", f);
    }

    @Test
    void shouldFoldRight() {
        final BiFunction<Integer, String, String> f = (i, s) -> s + i;
        this.seq2().foldRight("", f);
        verify(this.seq).foldRight("", f);
    }

    @Test
    void shouldForEach() {
        final Consumer<Integer> action = i -> {};
        this.seq2().forEach(action);
        verify(this.seq).forEach(action);
    }

    @Test
    void shouldFormat() {
        this.seq2().format();
        verify(this.seq).format();
    }

    @Test
    void shouldGet() {
        this.seq2().get(1L);
        verify(this.seq).get(1L);
    }

    @Test
    void shouldGroupBy() {
        this.seq2().groupBy(this.function);
        verify(this.seq).groupBy(this.function);
    }

    @Test
    void shouldGroupByWithCollector() {
        final Collector<Integer, ?, List<Integer>> downstream = Collectors.toList();
        this.seq2().groupBy(this.function, downstream);
        verify(this.seq).groupBy(this.function, downstream);
    }

    @Test
    void shouldGroupByWithMapFactoryAndCollector() {
        final Supplier<Map<Integer, List<Integer>>> mapFactory = TreeMap::new;
        final Collector<Integer, ?, List<Integer>> downstream = Collectors.toList();
        this.seq2().groupBy(this.function, mapFactory, downstream);
        verify(this.seq).groupBy(this.function, mapFactory, downstream);
    }

    @Test
    void shouldIndexOf() {
        final Integer element = 1;
        this.seq2().indexOf(element);
        verify(this.seq).indexOf(element);
    }

    @Test
    void shouldIndexOfWithPredicate() {
        this.seq2().indexOf(this.predicate);
        verify(this.seq).indexOf(this.predicate);
    }

    @Test
    void shouldCheckIsEmpty() {
        this.seq2().isEmpty();
        verify(this.seq).isEmpty();
    }

    @Test
    void shouldCheckIsNotEmpty() {
        this.seq2().isNotEmpty();
        verify(this.seq).isNotEmpty();
    }

    @Test
    void shouldMax() {
        this.seq2().max();
        verify(this.seq).max();
    }

    @Test
    void shouldMaxWithFunction() {
        this.seq2().max(this.function);
        verify(this.seq).max(this.function);
    }

    @Test
    void shouldMaxWithFunctionAndComparator() {
        this.seq2().max(this.function, this.comparator);
        verify(this.seq).max(this.function, this.comparator);
    }

    @Test
    void shouldMaxBy() {
        this.seq2().maxBy(this.function);
        verify(this.seq).maxBy(this.function);
    }

    @Test
    void shouldMaxByWithComparator() {
        this.seq2().maxBy(this.function, this.comparator);
        verify(this.seq).maxBy(this.function, this.comparator);
    }

    @Test
    void shouldMedian() {
        this.seq2().median();
        verify(this.seq).median();
    }

    @Test
    void shouldMedianWithComparator() {
        this.seq2().median(this.comparator);
        verify(this.seq).median(this.comparator);
    }

    @Test
    void shouldMedianBy() {
        this.seq2().medianBy(this.function);
        verify(this.seq).medianBy(this.function);
    }

    @Test
    void shouldMedianByWithComparator() {
        this.seq2().medianBy(this.function, this.comparator);
        verify(this.seq).medianBy(this.function, this.comparator);
    }

    @Test
    void shouldMin() {
        this.seq2().min();
        verify(this.seq).min();
    }

    @Test
    void shouldMinWithFunction() {
        this.seq2().min(this.function);
        verify(this.seq).min(this.function);
    }

    @Test
    void shouldMinWithFunctionAndComparator() {
        this.seq2().min(this.function, this.comparator);
        verify(this.seq).min(this.function, this.comparator);
    }

    @Test
    void shouldMinBy() {
        this.seq2().minBy(this.function);
        verify(this.seq).minBy(this.function);
    }

    @Test
    void shouldMinByWithComparator() {
        this.seq2().minBy(this.function, this.comparator);
        verify(this.seq).minBy(this.function, this.comparator);
    }

    @Test
    void shouldMode() {
        this.seq2().mode();
        verify(this.seq).mode();
    }

    @Test
    void shouldModeBy() {
        this.seq2().modeBy(this.function);
        verify(this.seq).modeBy(this.function);
    }

    @Test
    void shouldPercentile() {
        this.seq2().percentile(0.5);
        verify(this.seq).percentile(0.5);
    }

    @Test
    void shouldPercentileWithComparator() {
        this.seq2().percentile(0.5, this.comparator);
        verify(this.seq).percentile(0.5, this.comparator);
    }

    @Test
    void shouldPercentileBy() {
        this.seq2().percentileBy(0.5, this.function);
        verify(this.seq).percentileBy(0.5, this.function);
    }

    @Test
    void shouldPercentileByWithComparator() {
        this.seq2().percentileBy(0.5, this.function, this.comparator);
        verify(this.seq).percentileBy(0.5, this.function, this.comparator);
    }

    @Test
    void shouldPrintToWriter() {
        final PrintWriter writer = new PrintWriter(new StringWriter());
        this.seq2().print(writer);
        verify(this.seq).print(writer);
    }

    @Test
    void shouldPrintToStream() {
        final PrintStream stream = System.out;
        this.seq2().print(stream);
        verify(this.seq).print(stream);
    }

    @Test
    void shouldPrintErr() {
        this.seq2().printErr();
        verify(this.seq).printErr();
    }

    @Test
    void shouldPrintOut() {
        this.seq2().printOut();
        verify(this.seq).printOut();
    }

    @Test
    void shouldSpliterator() {
        this.seq2().spliterator();
        verify(this.seq).spliterator();
    }

    @Test
    void shouldSum() {
        this.seq2().sum();
        verify(this.seq).sum();
    }

    @Test
    void shouldSumWithFunction() {
        this.seq2().sum(this.function);
        verify(this.seq).sum(this.function);
    }

    @Test
    void shouldSumDouble() {
        final ToDoubleFunction<Integer> f = i -> i;
        this.seq2().sumDouble(f);
        verify(this.seq).sumDouble(f);
    }

    @Test
    void shouldSumInt() {
        final ToIntFunction<Integer> f = i -> i;
        this.seq2().sumInt(f);
        verify(this.seq).sumInt(f);
    }

    @Test
    void shouldSumLong() {
        final ToLongFunction<Integer> f = i -> i;
        this.seq2().sumLong(f);
        verify(this.seq).sumLong(f);
    }

    @Test
    void shouldCollectToCollection() {
        final Supplier<List<Integer>> factory = ArrayList::new;
        this.seq2().toCollection(factory);
        verify(this.seq).toCollection(factory);
    }

    @Test
    void shouldCollectToList() {
        this.seq2().toList();
        verify(this.seq).toList();
    }

    @Test
    void shouldCollectToListWithFactory() {
        final Supplier<List<Integer>> factory = ArrayList::new;
        this.seq2().toList(factory);
        verify(this.seq).toList(factory);
    }

    @Test
    void shouldCollectToMap() {
        this.seq2().toMap(this.function, this.function);
        verify(this.seq).toMap(this.function, this.function);
    }

    @Test
    void shouldCollectToMapWithKeyMapper() {
        this.seq2().toMap(this.function);
        verify(this.seq).toMap(this.function);
    }

    @Test
    void shouldCollectToSet() {
        this.seq2().toSet();
        verify(this.seq).toSet();
    }

    @Test
    void shouldCollectToSetWithFactory() {
        final Supplier<Set<Integer>> factory = HashSet::new;
        this.seq2().toSet(factory);
        verify(this.seq).toSet(factory);
    }

    @Test
    void shouldJoinToString() {
        this.seq2().toString(" ");
        verify(this.seq).toString(" ");
    }

    @Test
    void shouldJoinToStringOrEmpty() {
        when(this.seq.toString(" ")).thenReturn("1 2");
        assertThat(this.seq2().toStringOrEmpty(" ")).hasValue("1 2");
    }

    @Test
    void shouldJoinToStringOrEmptyIfEmpty() {
        when(this.seq.toString(" ")).thenReturn("");
        assertThat(this.seq2().toStringOrEmpty(" ")).isNotPresent();
    }

    @Test
    void shouldJoinToStringWithPrefixAndSuffix() {
        this.seq2().toString(" ", "^", "$");
        verify(this.seq).toString(" ", "^", "$");
    }

    @Test
    void shouldJoinToStringOrEmptyWithPrefixAndSuffix() {
        when(this.seq.toString(" ", "^", "$")).thenReturn("^1 2$");
        assertThat(this.seq2().toStringOrEmpty(" ", "^", "$")).hasValue("^1 2$");
    }

    @Test
    void shouldJoinToStringOrEmptyWithPrefixAndSuffixIfEmpty() {
        when(this.seq.toString(" ", "^", "$")).thenReturn("^$");
        assertThat(this.seq2().toStringOrEmpty(" ", "^", "$")).isNotPresent();
    }

    @Test
    void shouldCollectToUnmodifiableList() {
        this.seq2().toUnmodifiableList();
        verify(this.seq).toUnmodifiableList();
    }

    @Test
    void shouldCollectToUnmodifiableSet() {
        this.seq2().toUnmodifiableSet();
        verify(this.seq).toUnmodifiableSet();
    }
}
