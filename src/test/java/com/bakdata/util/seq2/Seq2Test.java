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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class Seq2Test {

    private final Function<Integer, Integer> function = i -> i;
    private final Predicate<Integer> predicate = i -> true;
    private final BiPredicate<Integer, Integer> biPredicate = (i, j) -> true;
    private final Comparator<Integer> comparator = Comparator.naturalOrder();
    private final BiFunction<Integer, Integer, Integer> zipper = Integer::sum;
    @Mock
    private Seq<Integer> seq;

    private Seq2<Integer> seq2() {
        return Seq2.seq(this.seq);
    }

    @Test
    void shouldCollectToList() {
        assertThat(Seq2.seq(List.of(1, 2)).toList())
                .hasSize(2)
                .containsExactly(1, 2);
    }

    @Test
    void shouldFlatMapToIterable() {
        assertThat((Stream<Integer>) Seq2.of(1, 2).flatMapToIterable(List::of))
                .hasSize(2)
                .containsExactlyInAnyOrder(1, 2);
    }

    @Test
    void shouldFlatMapToOptional() {
        assertThat((Stream<Integer>) Seq2.of(1, 2).flatMapToOptional(Optional::of))
                .hasSize(2)
                .containsExactlyInAnyOrder(1, 2);
        assertThat((Stream<Integer>) Seq2.of(1, null).flatMapToOptional(Optional::ofNullable))
                .hasSize(1)
                .containsExactlyInAnyOrder(1);
    }

    @Test
    void shouldJoinToString() {
        assertThat(Seq2.seq(List.of(1, 2)).toStringOrEmpty(" ")).hasValue("1 2");
        assertThat(Seq2.empty().toStringOrEmpty(" ")).isNotPresent();
    }

    @Test
    void shouldJoinToStringWithPrefixAndSuffix() {
        assertThat(Seq2.seq(List.of(1, 2, 3)).toStringOrEmpty(" ", "^", "$")).hasValue("^1 2 3$");
        assertThat(Seq2.empty().toStringOrEmpty(" ", "^", "$")).isNotPresent();
    }

    @Test
    void shouldSelectKey() {
        assertThat((Stream<Tuple2<Integer, Integer>>) Seq2.seq(List.of(1, 2))
                .selectKey(i -> i + 1))
                .hasSize(2)
                .containsExactlyInAnyOrder(new Tuple2<>(2, 1), new Tuple2<>(3, 2));
    }

    @Test
    void shouldSelectValue() {
        assertThat((Stream<Tuple2<Integer, Integer>>) Seq2.seq(List.of(1, 2))
                .selectValue(i -> i + 1))
                .hasSize(2)
                .containsExactlyInAnyOrder(new Tuple2<>(1, 2), new Tuple2<>(2, 3));
    }

    @Test
    void shouldFlatMapToOptionalPair() {
        assertThat((Stream<Tuple2<Integer, Integer>>) Seq2.seq(List.of(1, 2))
                .flatMapToOptionalPair(i -> i == 1 ? Optional.empty() : Optional.of(new Tuple2<>(i + 1, i))))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(3, 2));
    }

    @Test
    void shouldFlatMapToIterablePair() {
        assertThat((Stream<Tuple2<Integer, Integer>>) Seq2.seq(List.of(1, 2))
                .flatMapToIterablePair(i -> List.of(new Tuple2<>(i, i), new Tuple2<>(i + 1, i + 1))))
                .hasSize(4)
                .containsExactlyInAnyOrder(new Tuple2<>(1, 1), new Tuple2<>(2, 2),
                        new Tuple2<>(2, 2), new Tuple2<>(3, 3));
    }

    @Test
    void shouldZipWithIndex() {
        assertThat((Stream<Tuple2<Integer, Long>>) Seq2.seq(List.of(10, 11, 12))
                .zipWithIndex()
        ).containsExactlyInAnyOrder(
                new Tuple2<>(10, 0L),
                new Tuple2<>(11, 1L),
                new Tuple2<>(12, 2L)
        );
    }

    @Test
    void shouldPairWithIndexKey() {
        assertThat((Stream<Tuple2<Long, Integer>>) Seq2.seq(List.of(10, 11, 12))
                .keyedByIndex()
        ).containsExactlyInAnyOrder(
                new Tuple2<>(0L, 10),
                new Tuple2<>(1L, 11),
                new Tuple2<>(2L, 12)
        );
    }

    @Test
    void shouldCreateEmpty() {
        assertThat((Stream<Integer>) Seq2.<Integer>empty()).isEmpty();
    }

    @Test
    void shouldGenerateFromSupplier() {
        final Supplier<Integer> supplier = () -> 1;
        assertThat((Stream<Integer>) Seq2.generate(supplier).limit(2)).containsExactly(1, 1);
    }

    @Test
    void shouldGenerateFromValue() {
        assertThat((Stream<Integer>) Seq2.generate(1).limit(2)).containsExactly(1, 1);
    }

    @Test
    void shouldGenerate() {
        assertThat((Stream<Void>) Seq2.generate().limit(2)).containsExactly(null, null);
    }

    @Test
    void shouldCreateOfValues() {
        assertThat((Stream<Integer>) Seq2.of(1, 2)).containsExactly(1, 2);
    }

    @Test
    void shouldCreateOfValue() {
        assertThat((Stream<Integer>) Seq2.of(1)).containsExactly(1);
    }

    @Test
    void shouldCreateFromStream() {
        assertThat((Stream<Integer>) Seq2.seq(Stream.of(1, 2))).containsExactly(1, 2);
    }

    @Test
    void shouldCreateFromSupplier() {
        final Supplier<Integer> supplier = () -> 1;
        assertThat((Stream<Integer>) Seq2.seq(supplier)).containsExactly(1);
    }

    @Test
    void shouldCreateFromIterator() {
        assertThat((Stream<Integer>) Seq2.seq(List.of(1, 2).iterator())).containsExactly(1, 2);
    }

    @Test
    void shouldCreateFromEnumeration() {
        assertThat((Stream<Integer>) Seq2.seq(Collections.enumeration(List.of(1, 2)))).containsExactly(1, 2);
    }

    @Test
    void shouldCreateFromSpliterator() {
        final Spliterator<Integer> spliterator = List.of(1, 2).spliterator();
        assertThat((Stream<Integer>) Seq2.seq(spliterator)).containsExactly(1, 2);
    }

    @Test
    void shouldCreateFromArray() {
        assertThat((Stream<Integer>) Seq2.seq(new Integer[]{1, 2, 3, 4}, 1, 3)).containsExactly(2, 3);
    }

    @Test
    void shouldCreateFromIterable() {
        assertThat((Stream<Integer>) Seq2.seq((Iterable<Integer>) List.of(1, 2))).containsExactly(1, 2);
    }

    @Test
    void shouldCreateFromOptional() {
        assertThat((Stream<Integer>) Seq2.seq(Optional.of(1))).containsExactly(1);
        assertThat((Stream<Integer>) Seq2.seq(Optional.<Integer>empty())).isEmpty();
    }

    @Test
    void shouldCreateFromSeq2() {
        assertThat((Object) Seq2.seq(Seq2.seq(this.seq)).toSeq()).isSameAs(this.seq);
    }

    @Test
    void shouldCreateFromSeq() {
        assertThat((Object) Seq2.seq(this.seq).toSeq()).isSameAs(this.seq);
    }

    @Test
    void shouldCreateFromMap() {
        assertThat((Stream<Tuple2<Integer, String>>) Seq2.seq(Map.of(1, "a")))
                .containsExactly(new Tuple2<>(1, "a"));
    }

    @Test
    void shouldAllMatch() {
        this.seq2().allMatch(this.predicate);
        verify(this.seq).allMatch(this.predicate);
    }

    @Test
    void shouldAnyMatch() {
        this.seq2().anyMatch(this.predicate);
        verify(this.seq).anyMatch(this.predicate);
    }

    @Test
    void shouldAppendStream() {
        final Stream<Integer> other = Stream.of(1);
        this.seq2().append(other);
        verify(this.seq).append(other);
    }

    @Test
    void shouldAppendIterable() {
        final Iterable<Integer> other = List.of(1);
        this.seq2().append(other);
        verify(this.seq).append(other);
    }

    @Test
    void shouldAppendSeq2() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().append(Seq2.seq(other));
        verify(this.seq).append(other);
    }

    @Test
    void shouldAppendSeq() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().append(other);
        verify(this.seq).append(other);
    }

    @Test
    void shouldAppendValue() {
        final Integer other = 1;
        this.seq2().append(other);
        verify(this.seq).append(other);
    }

    @Test
    void shouldAppendValues() {
        this.seq2().append(1, 2);
        verify(this.seq).append(1, 2);
    }

    @Test
    void shouldAppendOptional() {
        final Optional<Integer> other = Optional.of(1);
        this.seq2().append(other);
        verify(this.seq).append(other);
    }

    @Test
    void shouldCast() {
        this.seq2().cast(Integer.class);
        verify(this.seq).cast(Integer.class);
    }

    @Test
    void shouldClose() {
        this.seq2().close();
        verify(this.seq).close();
    }

    @Test
    void shouldCollectWithSupplierAccumulatorAndCombiner() {
        final Supplier<List<Integer>> supplier = ArrayList::new;
        final BiConsumer<List<Integer>, Integer> accumulator = List::add;
        final BiConsumer<List<Integer>, List<Integer>> combiner = List::addAll;
        this.seq2().collect(supplier, accumulator, combiner);
        verify(this.seq).collect(supplier, accumulator, combiner);
    }

    @Test
    void shouldCollect() {
        final Collector<Integer, ?, List<Integer>> collector = Collectors.toList();
        this.seq2().collect(collector);
        verify(this.seq).collect(collector);
    }

    @Test
    void shouldConcatValue() {
        final Integer other = 1;
        this.seq2().concat(other);
        verify(this.seq).concat(other);
    }

    @Test
    void shouldConcatValues() {
        this.seq2().concat(1, 2);
        verify(this.seq).concat(1, 2);
    }

    @Test
    void shouldConcatIterable() {
        final Iterable<Integer> other = List.of(1);
        this.seq2().concat(other);
        verify(this.seq).concat(other);
    }

    @Test
    void shouldConcatStream() {
        final Stream<Integer> other = Stream.of(1);
        this.seq2().concat(other);
        verify(this.seq).concat(other);
    }

    @Test
    void shouldConcatSeq() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().concat(other);
        verify(this.seq).concat(other);
    }

    @Test
    void shouldConcatSeq2() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().concat(Seq2.seq(other));
        verify(this.seq).concat(other);
    }

    @Test
    void shouldConcatOptional() {
        final Optional<Integer> other = Optional.of(1);
        this.seq2().concat(other);
        verify(this.seq).concat(other);
    }

    @Test
    void shouldCount() {
        this.seq2().count();
        verify(this.seq).count();
    }

    @Test
    void shouldCrossApply() {
        final Function<Integer, List<Integer>> f = List::of;
        this.seq2().crossApply(f);
        verify(this.seq).crossApply(f);
    }

    @Test
    void shouldCrossJoinIterable() {
        final Iterable<Integer> other = List.of(1);
        this.seq2().crossJoin(other);
        verify(this.seq).crossJoin(other);
    }

    @Test
    void shouldCrossJoinStream() {
        final Stream<Integer> other = Stream.of(1);
        this.seq2().crossJoin(other);
        verify(this.seq).crossJoin(other);
    }

    @Test
    void shouldCrossJoinSeq() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().crossJoin(other);
        verify(this.seq).crossJoin(other);
    }

    @Test
    void shouldCrossJoinSeq2() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().crossJoin(Seq2.seq(other));
        verify(this.seq).crossJoin(other);
    }

    @Test
    void shouldCrossSelfJoin() {
        this.seq2().crossSelfJoin();
        verify(this.seq).crossSelfJoin();
    }

    @Test
    void shouldCycle() {
        this.seq2().cycle();
        verify(this.seq).cycle();
    }

    @Test
    void shouldCycleTimes() {
        this.seq2().cycle(2L);
        verify(this.seq).cycle(2L);
    }

    @Test
    void shouldDistinctByKeyExtractor() {
        this.seq2().distinct(this.function);
        verify(this.seq).distinct(this.function);
    }

    @Test
    void shouldDistinct() {
        this.seq2().distinct();
        verify(this.seq).distinct();
    }

    @Test
    void shouldDrop() {
        this.seq2().drop(1L);
        verify(this.seq).drop(1L);
    }

    @Test
    void shouldDropWhile() {
        this.seq2().dropWhile(this.predicate);
        verify(this.seq).dropWhile(this.predicate);
    }

    @Test
    void shouldDuplicate() {
        when(this.seq.duplicate()).thenReturn(new Tuple2<>(Seq.of(1), Seq.of(2)));
        final Tuple2<Seq2<Integer>, Seq2<Integer>> duplicate = this.seq2().duplicate();
        assertThat(duplicate.v1().toList()).containsExactly(1);
        assertThat(duplicate.v2().toList()).containsExactly(2);
    }

    @Test
    void shouldFilter() {
        this.seq2().filter(this.predicate);
        verify(this.seq).filter(this.predicate);
    }

    @Test
    void shouldFilterNot() {
        assertThat((Stream<Integer>) Seq2.of(1, 2, 3).filterNot(i -> i > 1)).containsExactly(1);
    }

    @Test
    void shouldFindAny() {
        this.seq2().findAny();
        verify(this.seq).findAny();
    }

    @Test
    void shouldFindFirst() {
        this.seq2().findFirst();
        verify(this.seq).findFirst();
    }

    @Test
    void shouldFlatMap() {
        final Function<Integer, Stream<Integer>> mapper = Stream::of;
        this.seq2().flatMap(mapper);
        verify(this.seq).flatMap(mapper);
    }

    @Test
    void shouldFlatMapToDouble() {
        final Function<Integer, DoubleStream> mapper = DoubleStream::of;
        this.seq2().flatMapToDouble(mapper);
        verify(this.seq).flatMapToDouble(mapper);
    }

    @Test
    void shouldFlatMapToInt() {
        final Function<Integer, IntStream> mapper = IntStream::of;
        this.seq2().flatMapToInt(mapper);
        verify(this.seq).flatMapToInt(mapper);
    }

    @Test
    void shouldFlatMapToLong() {
        final Function<Integer, LongStream> mapper = LongStream::of;
        this.seq2().flatMapToLong(mapper);
        verify(this.seq).flatMapToLong(mapper);
    }

    @Test
    void shouldFlatMapToPair() {
        final Function<Integer, Stream<Tuple2<Integer, Integer>>> mapper = i -> Stream.of(new Tuple2<>(i, i));
        this.seq2().flatMapToPair(mapper);
        verify(this.seq).flatMap(mapper);
    }

    @Test
    void shouldForEachOrdered() {
        final Consumer<Integer> action = i -> {};
        this.seq2().forEachOrdered(action);
        verify(this.seq).forEachOrdered(action);
    }

    @Test
    void shouldGrouped() {
        when(this.seq.grouped(this.function)).thenReturn(Seq.of(new Tuple2<>(1, Seq.of(1, 1))));
        final List<Tuple2<Integer, List<Integer>>> grouped = this.seq2().grouped(this.function)
                .mapValues(Seq2::toList)
                .toList();
        assertThat(grouped).containsExactly(new Tuple2<>(1, List.of(1, 1)));
    }

    @Test
    void shouldGroupedWithCollector() {
        final Collector<Integer, ?, List<Integer>> downstream = Collectors.toList();
        this.seq2().grouped(this.function, downstream);
        verify(this.seq).grouped(this.function, downstream);
    }

    @Test
    void shouldInnerJoinStream() {
        final Stream<Integer> other = Stream.of(1);
        this.seq2().innerJoin(other, this.biPredicate);
        verify(this.seq).innerJoin(other, this.biPredicate);
    }

    @Test
    void shouldInnerJoinIterable() {
        final Iterable<Integer> other = List.of(1);
        this.seq2().innerJoin(other, this.biPredicate);
        verify(this.seq).innerJoin(other, this.biPredicate);
    }

    @Test
    void shouldInnerJoinSeq() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().innerJoin(other, this.biPredicate);
        verify(this.seq).innerJoin(other, this.biPredicate);
    }

    @Test
    void shouldInnerJoinSeq2() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().innerJoin(Seq2.seq(other), this.biPredicate);
        verify(this.seq).innerJoin(other, this.biPredicate);
    }

    @Test
    void shouldInnerSelfJoin() {
        this.seq2().innerSelfJoin(this.biPredicate);
        verify(this.seq).innerSelfJoin(this.biPredicate);
    }

    @Test
    void shouldIntersperse() {
        this.seq2().intersperse(0);
        verify(this.seq).intersperse(0);
    }

    @Test
    void shouldCheckIsParallel() {
        this.seq2().isParallel();
        verify(this.seq).isParallel();
    }

    @Test
    void shouldIterate() {
        this.seq2().iterator();
        verify(this.seq).iterator();
    }

    @Test
    void shouldLeftOuterJoinStream() {
        final Stream<Integer> other = Stream.of(1);
        this.seq2().leftOuterJoin(other, this.biPredicate);
        verify(this.seq).leftOuterJoin(other, this.biPredicate);
    }

    @Test
    void shouldLeftOuterJoinIterable() {
        final Iterable<Integer> other = List.of(1);
        this.seq2().leftOuterJoin(other, this.biPredicate);
        verify(this.seq).leftOuterJoin(other, this.biPredicate);
    }

    @Test
    void shouldLeftOuterJoinSeq() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().leftOuterJoin(other, this.biPredicate);
        verify(this.seq).leftOuterJoin(other, this.biPredicate);
    }

    @Test
    void shouldLeftOuterJoinSeq2() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().leftOuterJoin(Seq2.seq(other), this.biPredicate);
        verify(this.seq).leftOuterJoin(other, this.biPredicate);
    }

    @Test
    void shouldLeftOuterSelfJoin() {
        this.seq2().leftOuterSelfJoin(this.biPredicate);
        verify(this.seq).leftOuterSelfJoin(this.biPredicate);
    }

    @Test
    void shouldLimit() {
        this.seq2().limit(1L);
        verify(this.seq).limit(1L);
    }

    @Test
    void shouldLimitUntil() {
        this.seq2().limitUntil(this.predicate);
        verify(this.seq).limitUntil(this.predicate);
    }

    @Test
    void shouldLimitUntilClosed() {
        this.seq2().limitUntilClosed(this.predicate);
        verify(this.seq).limitUntilClosed(this.predicate);
    }

    @Test
    void shouldLimitWhile() {
        this.seq2().limitWhile(this.predicate);
        verify(this.seq).limitWhile(this.predicate);
    }

    @Test
    void shouldLimitWhileClosed() {
        this.seq2().limitWhileClosed(this.predicate);
        verify(this.seq).limitWhileClosed(this.predicate);
    }

    @Test
    void shouldMap() {
        this.seq2().map(this.function);
        verify(this.seq).map(this.function);
    }

    @Test
    void shouldMapToDouble() {
        final ToDoubleFunction<Integer> mapper = i -> i;
        this.seq2().mapToDouble(mapper);
        verify(this.seq).mapToDouble(mapper);
    }

    @Test
    void shouldMapToInt() {
        final ToIntFunction<Integer> mapper = i -> i;
        this.seq2().mapToInt(mapper);
        verify(this.seq).mapToInt(mapper);
    }

    @Test
    void shouldMapToLong() {
        final ToLongFunction<Integer> mapper = i -> i;
        this.seq2().mapToLong(mapper);
        verify(this.seq).mapToLong(mapper);
    }

    @Test
    void shouldMapToPair() {
        final Function<Integer, Tuple2<Integer, Integer>> mapper = i -> new Tuple2<>(i, i);
        this.seq2().mapToPair(mapper);
        verify(this.seq).map(mapper);
    }

    @Test
    void shouldMapToPairWithKeyAndValueMapper() {
        assertThat((Stream<Tuple2<Integer, String>>) Seq2.of(1, 2).mapToPair(i -> i + 1, i -> "v" + i))
                .containsExactly(new Tuple2<>(2, "v1"), new Tuple2<>(3, "v2"));
    }

    @Test
    void shouldMaxWithComparator() {
        this.seq2().max(this.comparator);
        verify(this.seq).max(this.comparator);
    }

    @Test
    void shouldMaxAll() {
        this.seq2().maxAll();
        verify(this.seq).maxAll();
    }

    @Test
    void shouldMaxAllWithComparator() {
        this.seq2().maxAll(this.comparator);
        verify(this.seq).maxAll(this.comparator);
    }

    @Test
    void shouldMaxAllWithFunction() {
        this.seq2().maxAll(this.function);
        verify(this.seq).maxAll(this.function);
    }

    @Test
    void shouldMaxAllWithFunctionAndComparator() {
        this.seq2().maxAll(this.function, this.comparator);
        verify(this.seq).maxAll(this.function, this.comparator);
    }

    @Test
    void shouldMaxAllBy() {
        this.seq2().maxAllBy(this.function);
        verify(this.seq).maxAllBy(this.function);
    }

    @Test
    void shouldMaxAllByWithComparator() {
        this.seq2().maxAllBy(this.function, this.comparator);
        verify(this.seq).maxAllBy(this.function, this.comparator);
    }

    @Test
    void shouldMinWithComparator() {
        this.seq2().min(this.comparator);
        verify(this.seq).min(this.comparator);
    }

    @Test
    void shouldMinAll() {
        this.seq2().minAll();
        verify(this.seq).minAll();
    }

    @Test
    void shouldMinAllWithComparator() {
        this.seq2().minAll(this.comparator);
        verify(this.seq).minAll(this.comparator);
    }

    @Test
    void shouldMinAllWithFunction() {
        this.seq2().minAll(this.function);
        verify(this.seq).minAll(this.function);
    }

    @Test
    void shouldMinAllWithFunctionAndComparator() {
        this.seq2().minAll(this.function, this.comparator);
        verify(this.seq).minAll(this.function, this.comparator);
    }

    @Test
    void shouldMinAllBy() {
        this.seq2().minAllBy(this.function);
        verify(this.seq).minAllBy(this.function);
    }

    @Test
    void shouldMinAllByWithComparator() {
        this.seq2().minAllBy(this.function, this.comparator);
        verify(this.seq).minAllBy(this.function, this.comparator);
    }

    @Test
    void shouldModeAll() {
        this.seq2().modeAll();
        verify(this.seq).modeAll();
    }

    @Test
    void shouldModeAllBy() {
        this.seq2().modeAllBy(this.function);
        verify(this.seq).modeAllBy(this.function);
    }

    @Test
    void shouldNoneMatch() {
        this.seq2().noneMatch(this.predicate);
        verify(this.seq).noneMatch(this.predicate);
    }

    @Test
    void shouldFilterOfType() {
        this.seq2().ofType(Integer.class);
        verify(this.seq).ofType(Integer.class);
    }

    @Test
    void shouldRegisterOnClose() {
        final Runnable closeHandler = () -> {};
        this.seq2().onClose(closeHandler);
        verify(this.seq).onClose(closeHandler);
    }

    @Test
    void shouldReturnOnEmpty() {
        this.seq2().onEmpty(1);
        verify(this.seq).onEmpty(1);
    }

    @Test
    void shouldReturnOnEmptyGet() {
        final Supplier<Integer> supplier = () -> 1;
        this.seq2().onEmptyGet(supplier);
        verify(this.seq).onEmptyGet(supplier);
    }

    @Test
    void shouldThrowOnEmpty() {
        final Supplier<RuntimeException> supplier = RuntimeException::new;
        this.seq2().onEmptyThrow(supplier);
        verify(this.seq).onEmptyThrow(supplier);
    }

    @Test
    void shouldOuterApply() {
        final Function<Integer, List<Integer>> f = List::of;
        this.seq2().outerApply(f);
        verify(this.seq).outerApply(f);
    }

    @Test
    void shouldParallel() {
        this.seq2().parallel();
        verify(this.seq).parallel();
    }

    @Test
    void shouldPartition() {
        when(this.seq.partition(this.predicate)).thenReturn(new Tuple2<>(Seq.of(1), Seq.of(2)));
        final Tuple2<Seq2<Integer>, Seq2<Integer>> partition = this.seq2().partition(this.predicate);
        assertThat(partition.v1().toList()).containsExactly(1);
        assertThat(partition.v2().toList()).containsExactly(2);
    }

    @Test
    void shouldPeek() {
        final Consumer<Integer> action = i -> {};
        this.seq2().peek(action);
        verify(this.seq).peek(action);
    }

    @Test
    void shouldPrependValue() {
        final Integer other = 1;
        this.seq2().prepend(other);
        verify(this.seq).prepend(other);
    }

    @Test
    void shouldPrependValues() {
        this.seq2().prepend(1, 2);
        verify(this.seq).prepend(1, 2);
    }

    @Test
    void shouldPrependIterable() {
        final Iterable<Integer> other = List.of(1);
        this.seq2().prepend(other);
        verify(this.seq).prepend(other);
    }

    @Test
    void shouldPrependStream() {
        final Stream<Integer> other = Stream.of(1);
        this.seq2().prepend(other);
        verify(this.seq).prepend(other);
    }

    @Test
    void shouldPrependSeq() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().prepend(other);
        verify(this.seq).prepend(other);
    }

    @Test
    void shouldPrependSeq2() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().prepend(Seq2.seq(other));
        verify(this.seq).prepend(other);
    }

    @Test
    void shouldPrependOptional() {
        final Optional<Integer> other = Optional.of(1);
        this.seq2().prepend(other);
        verify(this.seq).prepend(other);
    }

    @Test
    void shouldReduceWithIdentity() {
        final BinaryOperator<Integer> accumulator = Integer::sum;
        this.seq2().reduce(0, accumulator);
        verify(this.seq).reduce(0, accumulator);
    }

    @Test
    void shouldReduce() {
        final BinaryOperator<Integer> accumulator = Integer::sum;
        this.seq2().reduce(accumulator);
        verify(this.seq).reduce(accumulator);
    }

    @Test
    void shouldReduceWithIdentityAndCombiner() {
        final BiFunction<Long, Integer, Long> accumulator = (l, i) -> l + i;
        final BinaryOperator<Long> combiner = Long::sum;
        this.seq2().reduce(0L, accumulator, combiner);
        verify(this.seq).reduce(0L, accumulator, combiner);
    }

    @Test
    void shouldRemove() {
        this.seq2().remove(1);
        verify(this.seq).remove(1);
    }

    @Test
    void shouldRemoveAllValues() {
        this.seq2().removeAll(1, 2);
        verify(this.seq).removeAll(1, 2);
    }

    @Test
    void shouldRemoveAllStream() {
        final Stream<Integer> other = Stream.of(1);
        this.seq2().removeAll(other);
        verify(this.seq).removeAll(other);
    }

    @Test
    void shouldRemoveAllIterable() {
        final Iterable<Integer> other = List.of(1);
        this.seq2().removeAll(other);
        verify(this.seq).removeAll(other);
    }

    @Test
    void shouldRemoveAllSeq() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().removeAll(other);
        verify(this.seq).removeAll(other);
    }

    @Test
    void shouldRemoveAllSeq2() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().removeAll(Seq2.seq(other));
        verify(this.seq).removeAll(other);
    }

    @Test
    void shouldRetainAllValues() {
        this.seq2().retainAll(1, 2);
        verify(this.seq).retainAll(1, 2);
    }

    @Test
    void shouldRetainAllStream() {
        final Stream<Integer> other = Stream.of(1);
        this.seq2().retainAll(other);
        verify(this.seq).retainAll(other);
    }

    @Test
    void shouldRetainAllIterable() {
        final Iterable<Integer> other = List.of(1);
        this.seq2().retainAll(other);
        verify(this.seq).retainAll(other);
    }

    @Test
    void shouldRetainAllSeq() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().retainAll(other);
        verify(this.seq).retainAll(other);
    }

    @Test
    void shouldRetainAllSeq2() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().retainAll(Seq2.seq(other));
        verify(this.seq).retainAll(other);
    }

    @Test
    void shouldReverse() {
        this.seq2().reverse();
        verify(this.seq).reverse();
    }

    @Test
    void shouldRightOuterJoinStream() {
        final Stream<Integer> other = Stream.of(1);
        this.seq2().rightOuterJoin(other, this.biPredicate);
        verify(this.seq).rightOuterJoin(other, this.biPredicate);
    }

    @Test
    void shouldRightOuterJoinIterable() {
        final Iterable<Integer> other = List.of(1);
        this.seq2().rightOuterJoin(other, this.biPredicate);
        verify(this.seq).rightOuterJoin(other, this.biPredicate);
    }

    @Test
    void shouldRightOuterJoinSeq() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().rightOuterJoin(other, this.biPredicate);
        verify(this.seq).rightOuterJoin(other, this.biPredicate);
    }

    @Test
    void shouldRightOuterJoinSeq2() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().rightOuterJoin(Seq2.seq(other), this.biPredicate);
        verify(this.seq).rightOuterJoin(other, this.biPredicate);
    }

    @Test
    void shouldRightOuterSelfJoin() {
        this.seq2().rightOuterSelfJoin(this.biPredicate);
        verify(this.seq).rightOuterSelfJoin(this.biPredicate);
    }

    @Test
    void shouldScanLeft() {
        final BiFunction<String, Integer, String> f = (s, i) -> s + i;
        this.seq2().scanLeft("", f);
        verify(this.seq).scanLeft("", f);
    }

    @Test
    void shouldScanRight() {
        final BiFunction<Integer, String, String> f = (i, s) -> s + i;
        this.seq2().scanRight("", f);
        verify(this.seq).scanRight("", f);
    }

    @Test
    void shouldSequential() {
        this.seq2().sequential();
        verify(this.seq).sequential();
    }

    @Test
    void shouldShuffle() {
        this.seq2().shuffle();
        verify(this.seq).shuffle();
    }

    @Test
    void shouldShuffleWithRandom() {
        final Random random = new Random();
        this.seq2().shuffle(random);
        verify(this.seq).shuffle(random);
    }

    @Test
    void shouldSkip() {
        this.seq2().skip(1L);
        verify(this.seq).skip(1L);
    }

    @Test
    void shouldSkipUntil() {
        this.seq2().skipUntil(this.predicate);
        verify(this.seq).skipUntil(this.predicate);
    }

    @Test
    void shouldSkipUntilClosed() {
        this.seq2().skipUntilClosed(this.predicate);
        verify(this.seq).skipUntilClosed(this.predicate);
    }

    @Test
    void shouldSkipWhile() {
        this.seq2().skipWhile(this.predicate);
        verify(this.seq).skipWhile(this.predicate);
    }

    @Test
    void shouldSkipWhileClosed() {
        this.seq2().skipWhileClosed(this.predicate);
        verify(this.seq).skipWhileClosed(this.predicate);
    }

    @Test
    void shouldSlice() {
        this.seq2().slice(1L, 2L);
        verify(this.seq).slice(1L, 2L);
    }

    @Test
    void shouldSlide() {
        when(this.seq.sliding(2L)).thenReturn(Seq.of(Seq.of(1, 2)));
        assertThat(this.seq2().sliding(2L).map(Seq2::toList).toList()).containsExactly(List.of(1, 2));
    }

    @Test
    void shouldSort() {
        this.seq2().sorted();
        verify(this.seq).sorted();
    }

    @Test
    void shouldSortWithComparator() {
        this.seq2().sorted(this.comparator);
        verify(this.seq).sorted(this.comparator);
    }

    @Test
    void shouldSortWithFunction() {
        this.seq2().sorted(this.function);
        verify(this.seq).sorted(this.function);
    }

    @Test
    void shouldSortWithFunctionAndComparator() {
        this.seq2().sorted(this.function, this.comparator);
        verify(this.seq).sorted(this.function, this.comparator);
    }

    @Test
    void shouldSplitAt() {
        when(this.seq.splitAt(1L)).thenReturn(new Tuple2<>(Seq.of(1), Seq.of(2)));
        final Tuple2<Seq2<Integer>, Seq2<Integer>> split = this.seq2().splitAt(1L);
        assertThat(split.v1().toList()).containsExactly(1);
        assertThat(split.v2().toList()).containsExactly(2);
    }

    @Test
    void shouldSplitAtHead() {
        when(this.seq.splitAtHead()).thenReturn(new Tuple2<>(Optional.of(1), Seq.of(2)));
        final Tuple2<Optional<Integer>, Seq2<Integer>> split = this.seq2().splitAtHead();
        assertThat(split.v1()).hasValue(1);
        assertThat(split.v2().toList()).containsExactly(2);
    }

    @Test
    void shouldTake() {
        this.seq2().take(1L);
        verify(this.seq).take(1L);
    }

    @Test
    void shouldTakeWhile() {
        this.seq2().takeWhile(this.predicate);
        verify(this.seq).takeWhile(this.predicate);
    }

    @Test
    void shouldConvertToArray() {
        this.seq2().toArray();
        verify(this.seq).toArray();
    }

    @Test
    void shouldConvertToArrayWithGenerator() {
        final IntFunction<Integer[]> generator = Integer[]::new;
        this.seq2().toArray(generator);
        verify(this.seq).toArray(generator);
    }

    @Test
    void shouldReturnWrappedSeq() {
        assertThat((Object) this.seq2().toSeq()).isSameAs(this.seq);
    }

    @Test
    void shouldTransform() {
        final Function<Seq<Integer>, Long> transformer = Seq::count;
        this.seq2().transform(transformer);
        verify(this.seq).transform(transformer);
    }

    @Test
    void shouldUnordered() {
        this.seq2().unordered();
        verify(this.seq).unordered();
    }

    @Test
    void shouldWindow() {
        this.seq2().window();
        verify(this.seq).window();
    }

    @Test
    void shouldWindowWithBounds() {
        this.seq2().window(-1L, 1L);
        verify(this.seq).window(-1L, 1L);
    }

    @Test
    void shouldWindowWithOrder() {
        this.seq2().window(this.comparator);
        verify(this.seq).window(this.comparator);
    }

    @Test
    void shouldWindowWithOrderAndBounds() {
        this.seq2().window(this.comparator, -1L, 1L);
        verify(this.seq).window(this.comparator, -1L, 1L);
    }

    @Test
    void shouldWindowWithPartition() {
        this.seq2().window(this.function);
        verify(this.seq).window(this.function);
    }

    @Test
    void shouldWindowWithPartitionAndBounds() {
        this.seq2().window(this.function, -1L, 1L);
        verify(this.seq).window(this.function, -1L, 1L);
    }

    @Test
    void shouldWindowWithPartitionAndOrder() {
        this.seq2().window(this.function, this.comparator);
        verify(this.seq).window(this.function, this.comparator);
    }

    @Test
    void shouldWindowWithPartitionOrderAndBounds() {
        this.seq2().window(this.function, this.comparator, -1L, 1L);
        verify(this.seq).window(this.function, this.comparator, -1L, 1L);
    }

    @Test
    void shouldZipStreamWithZipper() {
        final Stream<Integer> other = Stream.of(1);
        this.seq2().zip(other, this.zipper);
        verify(this.seq).zip(other, this.zipper);
    }

    @Test
    void shouldZipIterableWithZipper() {
        final Iterable<Integer> other = List.of(1);
        this.seq2().zip(other, this.zipper);
        verify(this.seq).zip(other, this.zipper);
    }

    @Test
    void shouldZipSeqWithZipper() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().zip(other, this.zipper);
        verify(this.seq).zip(other, this.zipper);
    }

    @Test
    void shouldZipSeq2WithZipper() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().zip(Seq2.seq(other), this.zipper);
        verify(this.seq).zip(other, this.zipper);
    }

    @Test
    void shouldZipStream() {
        final Stream<Integer> other = Stream.of(1);
        this.seq2().zip(other);
        verify(this.seq).zip(other);
    }

    @Test
    void shouldZipIterable() {
        final Iterable<Integer> other = List.of(1);
        this.seq2().zip(other);
        verify(this.seq).zip(other);
    }

    @Test
    void shouldZipSeq() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().zip(other);
        verify(this.seq).zip(other);
    }

    @Test
    void shouldZipSeq2() {
        final Seq<Integer> other = Seq.of(1);
        this.seq2().zip(Seq2.seq(other));
        verify(this.seq).zip(other);
    }

    @Test
    void shouldZipWithIndexDelegate() {
        this.seq2().zipWithIndex();
        verify(this.seq).zipWithIndex();
    }

    @Test
    void shouldZipWithIndexAndZipper() {
        final BiFunction<Integer, Long, String> indexZipper = (i, l) -> i + ":" + l;
        this.seq2().zipWithIndex(indexZipper);
        verify(this.seq).zipWithIndex(indexZipper);
    }

}
