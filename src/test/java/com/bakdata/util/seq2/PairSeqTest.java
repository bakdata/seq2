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
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
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
import org.jooq.lambda.Window;
import org.jooq.lambda.tuple.Tuple2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@SuppressWarnings("deprecation")
class PairSeqTest {

    private static final Tuple2<Integer, String> A = new Tuple2<>(1, "a");
    private static final Tuple2<Integer, String> B = new Tuple2<>(2, "b");
    private static final Tuple2<Integer, String> C = new Tuple2<>(3, "c");
    private final Predicate<Tuple2<Integer, String>> predicate = t -> true;
    private final Function<Tuple2<Integer, String>, Integer> function = Tuple2::v1;
    private final Comparator<Tuple2<Integer, String>> comparator = Comparator.comparing(Tuple2::v1);
    private final Comparator<Integer> reverse = Comparator.reverseOrder();
    private final BiFunction<Integer, String, Integer> keyFunction = (k, v) -> k;
    private final BiFunction<Integer, String, String> valueFunction = (k, v) -> v;
    private final BiFunction<Integer, String, Integer> parityFunction = (k, v) -> k % 2;
    private final BiPredicate<Tuple2<Integer, String>, Integer> otherPredicate = (t, i) -> true;
    private final BiPredicate<Tuple2<Integer, String>, Tuple2<Integer, String>> joinPredicate = (t1, t2) -> true;
    @Mock
    private Seq<Tuple2<Integer, String>> seq;

    private static PairSeq<Integer, String> data() {
        return PairSeq.seq(List.of(A, B, C));
    }

    private PairSeq<Integer, String> pairSeq() {
        return PairSeq.seq(this.seq);
    }


    @Test
    void shouldConstructFromMap() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b")))
                .hasSize(2)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "a"), new Tuple2<>(2, "b"));
    }

    @Test
    void shouldCollectToMap() {
        assertThat(PairSeq.seq(Map.of(1, "a", 2, "b")).toMap())
                .hasSize(2)
                .containsEntry(1, "a")
                .containsEntry(2, "b");
    }

    @Test
    void shouldCollectToList() {
        assertThat(PairSeq.seq(Map.of(1, "a", 2, "b")).toList())
                .hasSize(2)
                .contains(new Tuple2<>(1, "a"))
                .contains(new Tuple2<>(2, "b"));
    }

    @Test
    void shouldMap() {
        assertThat((Stream<String>) PairSeq.seq(Map.of(1, "a", 2, "b")).map((k, v) -> k + v))
                .hasSize(2)
                .containsExactlyInAnyOrder("1a", "2b");
    }

    @Test
    void shouldMapToLong() {
        assertThat(PairSeq.seq(Map.of(1, "a", 2, "b")).mapToLong((k, v) -> k))
                .hasSize(2)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void shouldMapToInt() {
        assertThat(PairSeq.seq(Map.of(1, "a", 2, "b")).mapToInt((k, v) -> k))
                .hasSize(2)
                .containsExactlyInAnyOrder(1, 2);
    }

    @Test
    void shouldMapToDouble() {
        assertThat(PairSeq.seq(Map.of(1, "a", 2, "b")).mapToDouble((k, v) -> k))
                .hasSize(2)
                .containsExactlyInAnyOrder(1.0, 2.0);
    }

    @Test
    void shouldFlatMap() {
        assertThat((Stream<String>) PairSeq.seq(Map.of(1, "ab", 2, "bc"))
                .flatMap((k, v) -> v.chars().mapToObj(i -> k + String.valueOf((char) i))))
                .hasSize(4)
                .containsExactlyInAnyOrder("1a", "1b", "2b", "2c");
    }

    @Test
    void shouldFlatMapToLong() {
        assertThat(PairSeq.seq(Map.of(1, "ab", 2, "bc"))
                .flatMapToLong((k, v) -> LongStream.range(0, k)))
                .hasSize(3)
                .containsExactlyInAnyOrder(0L, 0L, 1L);
    }

    @Test
    void shouldFlatMapToInt() {
        assertThat(PairSeq.seq(Map.of(1, "ab", 2, "bc"))
                .flatMapToInt((k, v) -> IntStream.range(0, k)))
                .hasSize(3)
                .containsExactlyInAnyOrder(0, 0, 1);
    }

    @Test
    void shouldFlatMapToDouble() {
        assertThat(PairSeq.seq(Map.of(1, "ab", 2, "bc"))
                .flatMapToDouble((k, v) -> DoubleStream.of(k)))
                .hasSize(2)
                .containsExactlyInAnyOrder(1.0, 2.0);
    }

    @Test
    void shouldFlatMapToPair() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapToPair((k, v) -> PairSeq.seq(Map.of(k, v, k + 1, v + v))))
                .hasSize(4)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "a"), new Tuple2<>(2, "aa"),
                        new Tuple2<>(2, "b"), new Tuple2<>(3, "bb"));
    }

    @Test
    void shouldFlatMapToOptionalPair() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapToOptionalPair((k, v) -> k == 1 ? Optional.empty() : Optional.of(new Tuple2<>(k + 1, v + v))))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(3, "bb"));
    }

    @Test
    void shouldFlatMapToIterablePair() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapToIterablePair((k, v) -> List.of(new Tuple2<>(k, v), new Tuple2<>(k + 1, v + v))))
                .hasSize(4)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "a"), new Tuple2<>(2, "aa"),
                        new Tuple2<>(2, "b"), new Tuple2<>(3, "bb"));
    }

    @Test
    void shouldFlatMapValues() {
        assertThat((Stream<Tuple2<Integer, Character>>) PairSeq.seq(Map.of(1, "ab", 2, "bc"))
                .flatMapValues(v -> v.chars().mapToObj(i -> (char) i)))
                .hasSize(4)
                .containsExactlyInAnyOrder(new Tuple2<>(1, 'a'), new Tuple2<>(1, 'b'), new Tuple2<>(2, 'b'),
                        new Tuple2<>(2, 'c'));
    }

    @Test
    void shouldFlatMapKeysToPair() {
        assertThat((Stream<Tuple2<Integer, Integer>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapKeysToPair(k -> PairSeq.seq(Map.of(k, k, k + 1, k + 1))))
                .hasSize(4)
                .containsExactlyInAnyOrder(new Tuple2<>(1, 1), new Tuple2<>(2, 2),
                        new Tuple2<>(2, 2), new Tuple2<>(3, 3));
    }

    @Test
    void shouldFlatMapKeysToOptionalPair() {
        assertThat((Stream<Tuple2<Integer, Integer>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapKeysToOptionalPair(k -> k == 1 ? Optional.empty() : Optional.of(new Tuple2<>(k + 1, k))))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(3, 2));
    }

    @Test
    void shouldFlatMapKeysToIterablePair() {
        assertThat((Stream<Tuple2<Integer, Integer>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapKeysToIterablePair(k -> List.of(new Tuple2<>(k, k), new Tuple2<>(k + 1, k + 1))))
                .hasSize(4)
                .containsExactlyInAnyOrder(new Tuple2<>(1, 1), new Tuple2<>(2, 2),
                        new Tuple2<>(2, 2), new Tuple2<>(3, 3));
    }

    @Test
    void shouldFlatMapValuesToPair() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, Map.of(3, "a"), 2, Map.of(4, "b", 5, "c")))
                .flatMapValuesToPair(PairSeq::seq))
                .hasSize(3)
                .containsExactlyInAnyOrder(new Tuple2<>(3, "a"), new Tuple2<>(4, "b"), new Tuple2<>(5, "c"));
    }

    @Test
    void shouldFlatMapValuesToOptionalPair() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(
                        Map.<Integer, Optional<Tuple2<Integer, String>>>of(1, Optional.of(new Tuple2<>(3, "a")), 2,
                                Optional.empty()))
                .flatMapValuesToOptionalPair(Function.identity()))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(3, "a"));
    }

    @Test
    void shouldFlatMapValuesToIterablePair() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(
                        Map.of(1, List.of(new Tuple2<>(3, "a")), 2, List.of(new Tuple2<>(4, "b"), new Tuple2<>(5,
                                "c"))))
                .flatMapValuesToIterablePair(Function.identity()))
                .hasSize(3)
                .containsExactlyInAnyOrder(new Tuple2<>(3, "a"), new Tuple2<>(4, "b"), new Tuple2<>(5, "c"));
    }

    @Test
    void shouldFlatMapKeys() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapKeys(k -> IntStream.range(0, k).boxed()))
                .hasSize(3)
                .containsExactlyInAnyOrder(new Tuple2<>(0, "a"), new Tuple2<>(0, "b"), new Tuple2<>(1, "b"));
    }

    @Test
    void shouldMapValues() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b")).mapValues(v -> v + v))
                .hasSize(2)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "aa"), new Tuple2<>(2, "bb"));
    }

    @Test
    void shouldMapKeys() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b")).mapKeys(v -> v + 1))
                .hasSize(2)
                .containsExactlyInAnyOrder(new Tuple2<>(2, "a"), new Tuple2<>(3, "b"));
    }

    @Test
    void shouldReturnValues() {
        assertThat((Stream<String>) PairSeq.seq(Map.of(1, "a", 2, "b")).values())
                .hasSize(2)
                .containsExactlyInAnyOrder("a", "b");
    }

    @Test
    void shouldReturnKeys() {
        assertThat((Stream<Integer>) PairSeq.seq(Map.of(1, "a", 2, "b")).keys())
                .hasSize(2)
                .containsExactlyInAnyOrder(1, 2);
    }

    @Test
    void shouldGroupByKey() {
        assertThat((Stream<Tuple2<Integer, List<String>>>) PairSeq
                .seq(Seq.of(new Tuple2<>(1, "a"), new Tuple2<>(1, "b"), new Tuple2<>(2, "c")))
                .groupByKey()
                .mapValues(Seq2::toList))
                .hasSize(2)
                .anySatisfy(t -> {
                    assertThat(t.v1()).isEqualTo(1);
                    assertThat(t.v2())
                            .hasSize(2)
                            .containsExactlyInAnyOrder("a", "b");
                })
                .anySatisfy(t -> {
                    assertThat(t.v1()).isEqualTo(2);
                    assertThat(t.v2())
                            .hasSize(1)
                            .containsExactlyInAnyOrder("c");
                });
    }

    @Test
    void shouldFilter() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .filter((k, v) -> k > 1))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(2, "b"));
    }

    @Test
    void shouldFilterNot() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .filterNot((k, v) -> k > 1))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "a"));
    }

    @Test
    void shouldFilterKeys() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .filterKeys(k -> k > 1))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(2, "b"));
    }

    @Test
    void shouldFilterKeysNot() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .filterKeysNot(k -> k > 1))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "a"));
    }

    @Test
    void shouldFilterValues() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .filterValues("a"::equals))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "a"));
    }

    @Test
    void shouldFilterValuesNot() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .filterValuesNot("a"::equals))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(2, "b"));
    }

    @Test
    void shouldGroupBy() {
        final Map<String, List<Tuple2<Integer, String>>> actual = PairSeq.seq(Map.of(1, "a", 2, "a"))
                .groupBy((k, v) -> v);
        assertThat(actual)
                .hasSize(1)
                .hasEntrySatisfying("a", value -> assertThat(value)
                        .containsExactlyInAnyOrder(new Tuple2<>(1, "a"), new Tuple2<>(2, "a")));
    }

    @Test
    void shouldFlatMapToIterable() {
        assertThat((Stream<Object>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapToIterable((k, v) -> List.of(k, (Object) v)))
                .hasSize(4)
                .containsExactlyInAnyOrder(1, "a", 2, "b");
    }

    @Test
    void shouldFlatMapToOptional() {
        assertThat((Stream<String>) PairSeq.seq(Map.of(1, "a", 2, "b")).flatMapToOptional((k, v) -> Optional.of(v)))
                .hasSize(2)
                .containsExactlyInAnyOrder("a", "b");
        assertThat((Stream<Integer>) PairSeq.seq(Map.of(1, "a", 2, "b")).flatMapToOptional((k, v) -> Optional.of(k)))
                .hasSize(2)
                .containsExactlyInAnyOrder(1, 2);
        final Map<Integer, String> map = new HashMap<>();
        map.put(1, "a");
        map.put(2, null);
        assertThat((Stream<String>) PairSeq.seq(map).flatMapToOptional((k, v) -> Optional.ofNullable(v)))
                .hasSize(1)
                .containsExactlyInAnyOrder("a");
    }

    @Test
    void shouldFlatMapValuesToIterable() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapValuesToIterable(v -> List.of(v, v + 1)))
                .hasSize(4)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "a"), new Tuple2<>(1, "a1"), new Tuple2<>(2, "b"),
                        new Tuple2<>(2, "b1"));
    }

    @Test
    void shouldFlatMapValuesToOptional() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapValuesToOptional(v -> "a".equals(v) ? Optional.of(v) : Optional.empty()))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "a"));
    }

    @Test
    void shouldFlatMapKeysToIterable() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapKeysToIterable(k -> List.of(k, k + 1)))
                .hasSize(4)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "a"), new Tuple2<>(2, "a"), new Tuple2<>(2, "b"),
                        new Tuple2<>(3, "b"));
    }

    @Test
    void shouldFlatMapKeysToOptional() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapKeysToOptional(k -> 1 == k ? Optional.of(k) : Optional.empty()))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "a"));
    }

    @Test
    void shouldInnerJoinByKey() {
        final Map<Integer, Tuple2<String, Long>> actual = PairSeq.seq(Map.of(1, "a", 2, "b"))
                .innerJoinByKey(PairSeq.seq(Map.of(1, 1L, 2, 2L)))
                .toMap();
        assertThat(actual)
                .hasSize(2)
                .hasEntrySatisfying(1, value -> {
                    assertThat(value.v1()).isEqualTo("a");
                    assertThat(value.v2()).isEqualTo(1L);
                })
                .hasEntrySatisfying(2, value -> {
                    assertThat(value.v1()).isEqualTo("b");
                    assertThat(value.v2()).isEqualTo(2L);
                });
    }

    @Test
    void shouldInnerSelfJoinByKey() {
        final Map<Integer, Tuple2<String, String>> actual = PairSeq.seq(Map.of(1, "a", 2, "b"))
                .innerSelfJoinByKey()
                .toMap();
        assertThat(actual)
                .hasSize(2)
                .hasEntrySatisfying(1, value -> {
                    assertThat(value.v1()).isEqualTo("a");
                    assertThat(value.v2()).isEqualTo("a");
                })
                .hasEntrySatisfying(2, value -> {
                    assertThat(value.v1()).isEqualTo("b");
                    assertThat(value.v2()).isEqualTo("b");
                });
    }

    @Test
    void shouldLeftOuterJoinByKey() {
        final Map<Integer, Tuple2<String, Long>> actual = PairSeq.seq(Map.of(1, "a", 2, "b"))
                .leftOuterJoinByKey(PairSeq.seq(Map.of(1, 1L, 3, 2L)))
                .toMap();
        assertThat(actual)
                .hasSize(2)
                .hasEntrySatisfying(1, value -> {
                    assertThat(value.v1()).isEqualTo("a");
                    assertThat(value.v2()).isEqualTo(1L);
                })
                .hasEntrySatisfying(2, value -> {
                    assertThat(value.v1()).isEqualTo("b");
                    assertThat(value.v2()).isNull();
                });
    }

    @Test
    void shouldRightOuterJoinByKey() {
        final Map<Integer, Tuple2<String, Long>> actual = PairSeq.seq(Map.of(3, "a", 2, "b"))
                .rightOuterJoinByKey(PairSeq.seq(Map.of(1, 1L, 2, 2L)))
                .toMap();
        assertThat(actual)
                .hasSize(2)
                .hasEntrySatisfying(1, value -> {
                    assertThat(value.v1()).isNull();
                    assertThat(value.v2()).isEqualTo(1L);
                })
                .hasEntrySatisfying(2, value -> {
                    assertThat(value.v1()).isEqualTo("b");
                    assertThat(value.v2()).isEqualTo(2L);
                });
    }

    @Test
    void shouldJoinToStringOrEmpty() {
        assertThat(PairSeq.seq(List.of(new Tuple2<>(1, "a"), new Tuple2<>(2, "b"))).toStringOrEmpty(" "))
                .hasValue("(1, a) (2, b)");
        assertThat(PairSeq.empty().toStringOrEmpty(" ")).isNotPresent();
    }

    @Test
    void shouldJoinToStringOrEmptyWithPrefixAndSuffix() {
        assertThat(PairSeq.seq(List.of(new Tuple2<>(1, "a"), new Tuple2<>(2, "b"), new Tuple2<>(3, "c")))
                .toStringOrEmpty(" ", "^", "$"))
                .hasValue("^(1, a) (2, b) (3, c)$");
        assertThat(PairSeq.empty().toStringOrEmpty(" ", "^", "$")).isNotPresent();
    }

    @Test
    void shouldSelectKey() {
        assertThat((Stream<Tuple2<String, Tuple2<Integer, String>>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .selectKey((k, v) -> k + v))
                .hasSize(2)
                .containsExactlyInAnyOrder(new Tuple2<>("1a", new Tuple2<>(1, "a")),
                        new Tuple2<>("2b", new Tuple2<>(2, "b")));
    }

    @Test
    void shouldCreateEmpty() {
        assertThat(PairSeq.empty().toList()).isEmpty();
    }

    @Test
    void shouldGenerateFromSupplier() {
        final Supplier<Tuple2<Integer, String>> supplier = () -> A;
        assertThat(PairSeq.generate(supplier).limit(2).toList()).containsExactly(A, A);
    }

    @Test
    void shouldGenerateFromTuple() {
        assertThat(PairSeq.generate(A).limit(2).toList()).containsExactly(A, A);
    }

    @Test
    void shouldGenerateFromKeyAndValue() {
        assertThat(PairSeq.generate(1, "a").limit(2).toList()).containsExactly(A, A);
    }

    @Test
    void shouldCreateOfTuples() {
        assertThat(PairSeq.<Integer, String>of(A, B).toList()).containsExactly(A, B);
    }

    @Test
    void shouldCreateOfTuple() {
        assertThat(PairSeq.of(A).toList()).containsExactly(A);
    }

    @Test
    void shouldCreateOfKeyAndValue() {
        assertThat(PairSeq.of(1, "a").toList()).containsExactly(A);
    }

    @Test
    void shouldConstructFromStream() {
        assertThat(PairSeq.seq(Stream.of(A, B)).toList()).containsExactly(A, B);
    }

    @Test
    void shouldConstructFromSupplier() {
        final Supplier<Tuple2<Integer, String>> supplier = () -> A;
        assertThat(PairSeq.seq(supplier).toList()).containsExactly(A);
    }

    @Test
    void shouldConstructFromIterator() {
        assertThat(PairSeq.seq(List.of(A, B).iterator()).toList()).containsExactly(A, B);
    }

    @Test
    void shouldConstructFromEnumeration() {
        assertThat(PairSeq.seq(Collections.enumeration(List.of(A, B))).toList()).containsExactly(A, B);
    }

    @Test
    void shouldConstructFromSpliterator() {
        assertThat(PairSeq.seq(List.of(A, B).spliterator()).toList()).containsExactly(A, B);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldConstructFromArray() {
        final Tuple2<Integer, String>[] values = new Tuple2[]{A, B, C};
        assertThat(PairSeq.seq(values, 1, 3).toList()).containsExactly(B, C);
    }

    @Test
    void shouldConstructFromIterable() {
        final Iterable<Tuple2<Integer, String>> iterable = List.of(A, B);
        assertThat(PairSeq.seq(iterable).toList()).containsExactly(A, B);
    }

    @Test
    void shouldConstructFromOptional() {
        assertThat(PairSeq.seq(Optional.of(A)).toList()).containsExactly(A);
    }

    @Test
    void shouldConstructFromSeq2() {
        assertThat(PairSeq.seq(Seq2.of(A, B)).toList()).containsExactly(A, B);
    }

    @Test
    void shouldConstructFromSeq() {
        assertThat(PairSeq.seq(Seq.of(A, B)).toList()).containsExactly(A, B);
    }

    @Test
    void shouldAllMatchWithPredicate() {
        this.pairSeq().allMatch(this.predicate);
        verify(this.seq).allMatch(this.predicate);
    }

    @Test
    void shouldAllMatchWithBiPredicate() {
        assertThat(data().allMatch((k, v) -> k > 0)).isTrue();
        assertThat(data().allMatch((k, v) -> k > 1)).isFalse();
    }

    @Test
    void shouldAnyMatchWithPredicate() {
        this.pairSeq().anyMatch(this.predicate);
        verify(this.seq).anyMatch(this.predicate);
    }

    @Test
    void shouldAnyMatchWithBiPredicate() {
        assertThat(data().anyMatch((k, v) -> k > 2)).isTrue();
        assertThat(data().anyMatch((k, v) -> k > 3)).isFalse();
    }

    @Test
    void shouldAppendStream() {
        final Stream<Tuple2<Integer, String>> other = Stream.of(A);
        this.pairSeq().append(other);
        verify(this.seq).append(other);
    }

    @Test
    void shouldAppendIterable() {
        final List<Tuple2<Integer, String>> other = List.of(A);
        this.pairSeq().append(other);
        verify(this.seq).append(other);
    }

    @Test
    void shouldAppendSeq() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().append(other);
        verify(this.seq).append(other);
    }

    @Test
    void shouldAppendSeq2() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().append(Seq2.seq(other));
        verify(this.seq).append(other);
    }

    @Test
    void shouldAppendPairSeq() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().append(PairSeq.seq(other));
        verify(this.seq).append(other);
    }

    @Test
    void shouldAppendTuple() {
        this.pairSeq().append(A);
        verify(this.seq).append(A);
    }

    @Test
    void shouldAppendKeyAndValue() {
        this.pairSeq().append(1, "a");
        verify(this.seq).append(A);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldAppendTuples() {
        this.pairSeq().append(A, B);
        verify(this.seq).append(A, B);
    }

    @Test
    void shouldAppendOptional() {
        final Optional<Tuple2<Integer, String>> other = Optional.of(A);
        this.pairSeq().append(other);
        verify(this.seq).append(other);
    }

    @Test
    void shouldAvgWithFunction() {
        this.pairSeq().avg(this.function);
        verify(this.seq).avg(this.function);
    }

    @Test
    void shouldAvgWithBiFunction() {
        assertThat(data().avg(this.keyFunction)).hasValue(2);
    }

    @Test
    void shouldAvgDoubleWithFunction() {
        final ToDoubleFunction<Tuple2<Integer, String>> f = Tuple2::v1;
        this.pairSeq().avgDouble(f);
        verify(this.seq).avgDouble(f);
    }

    @Test
    void shouldAvgDoubleWithBiFunction() {
        assertThat(data().avgDouble((k, v) -> k)).isEqualTo(2.0);
    }

    @Test
    void shouldAvgIntWithFunction() {
        final ToIntFunction<Tuple2<Integer, String>> f = Tuple2::v1;
        this.pairSeq().avgInt(f);
        verify(this.seq).avgInt(f);
    }

    @Test
    void shouldAvgIntWithBiFunction() {
        assertThat(data().avgInt((k, v) -> k)).isEqualTo(2.0);
    }

    @Test
    void shouldAvgLongWithFunction() {
        final ToLongFunction<Tuple2<Integer, String>> f = Tuple2::v1;
        this.pairSeq().avgLong(f);
        verify(this.seq).avgLong(f);
    }

    @Test
    void shouldAvgLongWithBiFunction() {
        assertThat(data().avgLong((k, v) -> k)).isEqualTo(2.0);
    }

    @Test
    void shouldBitAndWithFunction() {
        this.pairSeq().bitAnd(this.function);
        verify(this.seq).bitAnd(this.function);
    }

    @Test
    void shouldBitAndWithBiFunction() {
        assertThat(PairSeq.seq(List.of(new Tuple2<>(3, "a"), new Tuple2<>(6, "b"))).bitAnd(this.keyFunction))
                .hasValue(2);
    }

    @Test
    void shouldBitAndIntWithFunction() {
        final ToIntFunction<Tuple2<Integer, String>> f = Tuple2::v1;
        this.pairSeq().bitAndInt(f);
        verify(this.seq).bitAndInt(f);
    }

    @Test
    void shouldBitAndIntWithBiFunction() {
        assertThat(PairSeq.seq(List.of(new Tuple2<>(3, "a"), new Tuple2<>(6, "b"))).bitAndInt((k, v) -> k))
                .isEqualTo(2);
    }

    @Test
    void shouldBitAndLongWithFunction() {
        final ToLongFunction<Tuple2<Integer, String>> f = Tuple2::v1;
        this.pairSeq().bitAndLong(f);
        verify(this.seq).bitAndLong(f);
    }

    @Test
    void shouldBitAndLongWithBiFunction() {
        assertThat(PairSeq.seq(List.of(new Tuple2<>(3, "a"), new Tuple2<>(6, "b"))).bitAndLong((k, v) -> k))
                .isEqualTo(2L);
    }

    @Test
    void shouldBitOrWithFunction() {
        this.pairSeq().bitOr(this.function);
        verify(this.seq).bitOr(this.function);
    }

    @Test
    void shouldBitOrWithBiFunction() {
        assertThat(PairSeq.seq(List.of(new Tuple2<>(3, "a"), new Tuple2<>(6, "b"))).bitOr(this.keyFunction))
                .hasValue(7);
    }

    @Test
    void shouldBitOrIntWithFunction() {
        final ToIntFunction<Tuple2<Integer, String>> f = Tuple2::v1;
        this.pairSeq().bitOrInt(f);
        verify(this.seq).bitOrInt(f);
    }

    @Test
    void shouldBitOrIntWithBiFunction() {
        assertThat(PairSeq.seq(List.of(new Tuple2<>(3, "a"), new Tuple2<>(6, "b"))).bitOrInt((k, v) -> k))
                .isEqualTo(7);
    }

    @Test
    void shouldBitOrLongWithFunction() {
        final ToLongFunction<Tuple2<Integer, String>> f = Tuple2::v1;
        this.pairSeq().bitOrLong(f);
        verify(this.seq).bitOrLong(f);
    }

    @Test
    void shouldBitOrLongWithBiFunction() {
        assertThat(PairSeq.seq(List.of(new Tuple2<>(3, "a"), new Tuple2<>(6, "b"))).bitOrLong((k, v) -> k))
                .isEqualTo(7L);
    }

    @Test
    void shouldCast() {
        final Class<Integer> type = Integer.class;
        this.pairSeq().cast(type);
        verify(this.seq).cast(type);
    }

    @Test
    void shouldClose() {
        this.pairSeq().close();
        verify(this.seq).close();
    }

    @Test
    void shouldCollectWithSupplier() {
        final Supplier<List<Tuple2<Integer, String>>> supplier = ArrayList::new;
        final BiConsumer<List<Tuple2<Integer, String>>, Tuple2<Integer, String>> accumulator = List::add;
        final BiConsumer<List<Tuple2<Integer, String>>, List<Tuple2<Integer, String>>> combiner = List::addAll;
        this.pairSeq().collect(supplier, accumulator, combiner);
        verify(this.seq).collect(supplier, accumulator, combiner);
    }

    @Test
    void shouldCollectWithCollector() {
        final Collector<Tuple2<Integer, String>, ?, Long> collector = Collectors.counting();
        this.pairSeq().collect(collector);
        verify(this.seq).collect(collector);
    }

    @Test
    void shouldConcatKeyAndValue() {
        this.pairSeq().concat(1, "a");
        verify(this.seq).concat(A);
    }

    @Test
    void shouldConcatTuple() {
        this.pairSeq().concat(A);
        verify(this.seq).concat(A);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldConcatTuples() {
        this.pairSeq().concat(A, B);
        verify(this.seq).concat(A, B);
    }

    @Test
    void shouldConcatIterable() {
        final List<Tuple2<Integer, String>> other = List.of(A);
        this.pairSeq().concat(other);
        verify(this.seq).concat(other);
    }

    @Test
    void shouldConcatStream() {
        final Stream<Tuple2<Integer, String>> other = Stream.of(A);
        this.pairSeq().concat(other);
        verify(this.seq).concat(other);
    }

    @Test
    void shouldConcatSeq() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().concat(other);
        verify(this.seq).concat(other);
    }

    @Test
    void shouldConcatSeq2() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().concat(Seq2.seq(other));
        verify(this.seq).concat(other);
    }

    @Test
    void shouldConcatPairSeq() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().concat(PairSeq.seq(other));
        verify(this.seq).concat(other);
    }

    @Test
    void shouldConcatOptional() {
        final Optional<Tuple2<Integer, String>> other = Optional.of(A);
        this.pairSeq().concat(other);
        verify(this.seq).concat(other);
    }

    @Test
    void shouldContainKeyAndValue() {
        this.pairSeq().contains(1, "a");
        verify(this.seq).contains(A);
    }

    @Test
    void shouldContainTuple() {
        this.pairSeq().contains(A);
        verify(this.seq).contains(A);
    }

    @Test
    void shouldContainAllPairSeq() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().containsAll(PairSeq.seq(other));
        verify(this.seq).containsAll(other);
    }

    @Test
    void shouldContainAnyPairSeq() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().containsAny(PairSeq.seq(other));
        verify(this.seq).containsAny(other);
    }

    @Test
    void shouldCount() {
        this.pairSeq().count();
        verify(this.seq).count();
    }

    @Test
    void shouldCountWithPredicate() {
        this.pairSeq().count(this.predicate);
        verify(this.seq).count(this.predicate);
    }

    @Test
    void shouldCountWithBiPredicate() {
        assertThat(data().count((k, v) -> k > 1)).isEqualTo(2L);
    }

    @Test
    void shouldCountDistinctWithPredicate() {
        this.pairSeq().countDistinct(this.predicate);
        verify(this.seq).countDistinct(this.predicate);
    }

    @Test
    void shouldCountDistinctWithBiPredicate() {
        assertThat(PairSeq.seq(List.of(A, A, B, C)).countDistinct((k, v) -> k < 3)).isEqualTo(2L);
    }

    @Test
    void shouldCountDistinctByWithFunction() {
        this.pairSeq().countDistinctBy(this.function);
        verify(this.seq).countDistinctBy(this.function);
    }

    @Test
    void shouldCountDistinctByWithBiFunction() {
        assertThat(PairSeq.seq(List.of(A, new Tuple2<>(2, "a"), C)).countDistinctBy(this.valueFunction))
                .isEqualTo(2L);
    }

    @Test
    void shouldCountDistinctByWithFunctionAndPredicate() {
        final Predicate<Integer> p = i -> true;
        this.pairSeq().countDistinctBy(this.function, p);
        verify(this.seq).countDistinctBy(this.function, p);
    }

    @Test
    void shouldCountDistinctByWithBiFunctionAndPredicate() {
        assertThat(PairSeq.seq(List.of(A, new Tuple2<>(2, "a"), C)).countDistinctBy(this.valueFunction, "a"::equals))
                .isEqualTo(1L);
    }

    @Test
    void shouldCountDistinctKeys() {
        assertThat(PairSeq.seq(List.of(A, new Tuple2<>(1, "b"), C)).countDistinctKeys()).isEqualTo(2L);
    }

    @Test
    void shouldCountDistinctKeysWithPredicate() {
        assertThat(PairSeq.seq(List.of(A, new Tuple2<>(1, "b"), C)).countDistinctKeys(k -> k < 3)).isEqualTo(1L);
    }

    @Test
    void shouldCountDistinctKeysBy() {
        assertThat(data().countDistinctKeysBy(k -> k % 2)).isEqualTo(2L);
    }

    @Test
    void shouldCountDistinctKeysByWithPredicate() {
        assertThat(data().countDistinctKeysBy(k -> k % 2, i -> i == 1)).isEqualTo(1L);
    }

    @Test
    void shouldCountDistinctValues() {
        assertThat(PairSeq.seq(List.of(A, new Tuple2<>(2, "a"), C)).countDistinctValues()).isEqualTo(2L);
    }

    @Test
    void shouldCountDistinctValuesWithPredicate() {
        assertThat(PairSeq.seq(List.of(A, new Tuple2<>(2, "a"), C)).countDistinctValues("a"::equals))
                .isEqualTo(1L);
    }

    @Test
    void shouldCountDistinctValuesBy() {
        assertThat(data().countDistinctValuesBy(String::length)).isEqualTo(1L);
    }

    @Test
    void shouldCountDistinctValuesByWithPredicate() {
        assertThat(data().countDistinctValuesBy(v -> v.charAt(0) < 'c', b -> b)).isEqualTo(1L);
    }

    @Test
    void shouldCountKeys() {
        assertThat(data().countKeys(k -> k > 1)).isEqualTo(2L);
    }

    @Test
    void shouldCountValues() {
        assertThat(data().countValues("a"::equals)).isEqualTo(1L);
    }

    @Test
    void shouldCrossApplyWithFunction() {
        final Function<Tuple2<Integer, String>, Iterable<Integer>> f = t -> List.of(1);
        this.pairSeq().crossApply(f);
        verify(this.seq).crossApply(f);
    }

    @Test
    void shouldCrossApplyWithBiFunction() {
        final BiFunction<Integer, String, Iterable<Integer>> f = (k, v) -> List.of(k, k + 1);
        assertThat(PairSeq.of(A).crossApply(f).toList())
                .containsExactly(new Tuple2<>(A, 1), new Tuple2<>(A, 2));
    }

    @Test
    void shouldCrossJoinStream() {
        final Stream<Integer> other = Stream.of(1);
        this.pairSeq().crossJoin(other);
        verify(this.seq).crossJoin(other);
    }

    @Test
    void shouldCrossJoinIterable() {
        final List<Integer> other = List.of(1);
        this.pairSeq().crossJoin(other);
        verify(this.seq).crossJoin(other);
    }

    @Test
    void shouldCrossJoinSeq() {
        final Seq<Integer> other = Seq.of(1);
        this.pairSeq().crossJoin(other);
        verify(this.seq).crossJoin(other);
    }

    @Test
    void shouldCrossJoinSeq2() {
        final Seq<Integer> other = Seq.of(1);
        this.pairSeq().crossJoin(Seq2.seq(other));
        verify(this.seq).crossJoin(other);
    }

    @Test
    void shouldCrossJoinPairSeq() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().crossJoin(PairSeq.seq(other));
        verify(this.seq).crossJoin(other);
    }

    @Test
    void shouldCrossSelfJoin() {
        this.pairSeq().crossSelfJoin();
        verify(this.seq).crossSelfJoin();
    }

    @Test
    void shouldCycle() {
        this.pairSeq().cycle();
        verify(this.seq).cycle();
    }

    @Test
    void shouldCycleTimes() {
        this.pairSeq().cycle(2);
        verify(this.seq).cycle(2);
    }

    @Test
    void shouldDistinct() {
        this.pairSeq().distinct();
        verify(this.seq).distinct();
    }

    @Test
    void shouldDistinctWithFunction() {
        this.pairSeq().distinct(this.function);
        verify(this.seq).distinct(this.function);
    }

    @Test
    void shouldDistinctWithBiFunction() {
        assertThat(PairSeq.seq(List.of(A, new Tuple2<>(1, "b"), C)).distinct(this.keyFunction).toList())
                .containsExactly(A, C);
    }

    @Test
    void shouldDrop() {
        this.pairSeq().drop(1);
        verify(this.seq).drop(1);
    }

    @Test
    void shouldDropWhileWithPredicate() {
        this.pairSeq().dropWhile(this.predicate);
        verify(this.seq).dropWhile(this.predicate);
    }

    @Test
    void shouldDropWhileWithBiPredicate() {
        assertThat((Stream<Tuple2<Integer, String>>) data().dropWhile((k, v) -> k < 2)).containsExactly(B, C);
    }

    @Test
    void shouldDuplicate() {
        when(this.seq.duplicate()).thenReturn(new Tuple2<>(Seq.empty(), Seq.empty()));
        final Tuple2<PairSeq<Integer, String>, PairSeq<Integer, String>> result = this.pairSeq().duplicate();
        assertThat(result.v1().toList()).isEmpty();
        assertThat(result.v2().toList()).isEmpty();
    }

    @Test
    void shouldFilterWithPredicate() {
        this.pairSeq().filter(this.predicate);
        verify(this.seq).filter(this.predicate);
    }

    @Test
    void shouldFilterNotWithPredicate() {
        final Predicate<Tuple2<Integer, String>> p = t -> t.v1() > 1;
        assertThat(data().filterNot(p).toList()).containsExactly(A);
    }

    @Test
    void shouldFindAny() {
        this.pairSeq().findAny();
        verify(this.seq).findAny();
    }

    @Test
    void shouldFindFirst() {
        this.pairSeq().findFirst();
        verify(this.seq).findFirst();
    }

    @Test
    void shouldFindFirstWithPredicate() {
        this.pairSeq().findFirst(this.predicate);
        verify(this.seq).findFirst(this.predicate);
    }

    @Test
    void shouldFindFirstWithBiPredicate() {
        assertThat(data().findFirst((k, v) -> k > 1)).hasValue(B);
    }

    @Test
    void shouldFindLastWithPredicate() {
        this.pairSeq().findLast(this.predicate);
        verify(this.seq).findLast(this.predicate);
    }

    @Test
    void shouldFindLastWithBiPredicate() {
        assertThat(data().findLast((k, v) -> k < 3)).hasValue(B);
    }

    @Test
    void shouldFlatMapWithFunction() {
        final Function<Tuple2<Integer, String>, Stream<Integer>> mapper = t -> Stream.of(t.v1());
        this.pairSeq().flatMap(mapper);
        verify(this.seq).flatMap(mapper);
    }

    @Test
    void shouldFlatMapToDoubleWithFunction() {
        final Function<Tuple2<Integer, String>, DoubleStream> mapper = t -> DoubleStream.of(t.v1());
        this.pairSeq().flatMapToDouble(mapper);
        verify(this.seq).flatMapToDouble(mapper);
    }

    @Test
    void shouldFlatMapToIntWithFunction() {
        final Function<Tuple2<Integer, String>, IntStream> mapper = t -> IntStream.of(t.v1());
        this.pairSeq().flatMapToInt(mapper);
        verify(this.seq).flatMapToInt(mapper);
    }

    @Test
    void shouldFlatMapToLongWithFunction() {
        final Function<Tuple2<Integer, String>, LongStream> mapper = t -> LongStream.of(t.v1());
        this.pairSeq().flatMapToLong(mapper);
        verify(this.seq).flatMapToLong(mapper);
    }

    @Test
    void shouldForEachWithConsumer() {
        final Consumer<Tuple2<Integer, String>> action = t -> {};
        this.pairSeq().forEach(action);
        verify(this.seq).forEach(action);
    }

    @Test
    void shouldForEachWithBiConsumer() {
        final List<String> result = new ArrayList<>();
        data().forEach((k, v) -> result.add(k + v));
        assertThat(result).containsExactly("1a", "2b", "3c");
    }

    @Test
    void shouldForEachOrderedWithConsumer() {
        final Consumer<Tuple2<Integer, String>> action = t -> {};
        this.pairSeq().forEachOrdered(action);
        verify(this.seq).forEachOrdered(action);
    }

    @Test
    void shouldForEachOrderedWithBiConsumer() {
        final List<String> result = new ArrayList<>();
        data().forEachOrdered((k, v) -> result.add(k + v));
        assertThat(result).containsExactly("1a", "2b", "3c");
    }

    @Test
    void shouldGroupByWithFunction() {
        this.pairSeq().groupBy(this.function);
        verify(this.seq).groupBy(this.function);
    }

    @Test
    void shouldGroupByWithFunctionAndCollector() {
        final Collector<Tuple2<Integer, String>, ?, Long> downstream = Collectors.counting();
        this.pairSeq().groupBy(this.function, downstream);
        verify(this.seq).groupBy(this.function, downstream);
    }

    @Test
    void shouldGroupByWithBiFunctionAndCollector() {
        assertThat(data().groupBy((k, v) -> k % 2, Collectors.counting()))
                .containsOnly(entry(0, 1L), entry(1, 2L));
    }

    @Test
    void shouldGroupByWithFunctionAndMapFactory() {
        final Collector<Tuple2<Integer, String>, ?, Long> downstream = Collectors.counting();
        final Supplier<Map<Integer, Long>> mapFactory = TreeMap::new;
        this.pairSeq().groupBy(this.function, mapFactory, downstream);
        verify(this.seq).groupBy(this.function, mapFactory, downstream);
    }

    @Test
    void shouldGroupByWithBiFunctionAndMapFactory() {
        final Map<Integer, Long> actual = data().groupBy((k, v) -> k % 2, TreeMap::new, Collectors.counting());
        assertThat(actual)
                .isInstanceOf(TreeMap.class)
                .containsOnly(entry(0, 1L), entry(1, 2L));
    }

    @Test
    void shouldGroupedWithFunction() {
        when(this.seq.grouped(this.function)).thenReturn(Seq.empty());
        assertThat(this.pairSeq().grouped(this.function).toList()).isEmpty();
    }

    @Test
    void shouldGroupedWithBiFunction() {
        assertThat(data().grouped((k, v) -> k % 2).mapValues(PairSeq::toList).toMap())
                .containsOnly(entry(1, List.of(A, C)), entry(0, List.of(B)));
    }

    @Test
    void shouldGroupedWithFunctionAndCollector() {
        final Collector<Tuple2<Integer, String>, ?, Long> downstream = Collectors.counting();
        this.pairSeq().grouped(this.function, downstream);
        verify(this.seq).grouped(this.function, downstream);
    }

    @Test
    void shouldGroupedWithBiFunctionAndCollector() {
        assertThat(data().grouped((k, v) -> k % 2, Collectors.counting()).toMap())
                .containsOnly(entry(1, 2L), entry(0, 1L));
    }

    @Test
    void shouldIndexOfTuple() {
        this.pairSeq().indexOf(A);
        verify(this.seq).indexOf(A);
    }

    @Test
    void shouldIndexOfKeyAndValue() {
        this.pairSeq().indexOf(1, "a");
        verify(this.seq).indexOf(A);
    }

    @Test
    void shouldIndexOfWithPredicate() {
        this.pairSeq().indexOf(this.predicate);
        verify(this.seq).indexOf(this.predicate);
    }

    @Test
    void shouldIndexOfWithBiPredicate() {
        assertThat(data().indexOf((k, v) -> k == 2)).hasValue(1L);
    }

    @Test
    void shouldInnerJoinStream() {
        final Stream<Integer> other = Stream.of(1);
        this.pairSeq().innerJoin(other, this.otherPredicate);
        verify(this.seq).innerJoin(other, this.otherPredicate);
    }

    @Test
    void shouldInnerJoinIterable() {
        final List<Integer> other = List.of(1);
        this.pairSeq().innerJoin(other, this.otherPredicate);
        verify(this.seq).innerJoin(other, this.otherPredicate);
    }

    @Test
    void shouldInnerJoinSeq() {
        final Seq<Integer> other = Seq.of(1);
        this.pairSeq().innerJoin(other, this.otherPredicate);
        verify(this.seq).innerJoin(other, this.otherPredicate);
    }

    @Test
    void shouldInnerJoinSeq2() {
        final Seq<Integer> other = Seq.of(1);
        this.pairSeq().innerJoin(Seq2.seq(other), this.otherPredicate);
        verify(this.seq).innerJoin(other, this.otherPredicate);
    }

    @Test
    void shouldInnerJoinPairSeq() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().innerJoin(PairSeq.seq(other), this.joinPredicate);
        verify(this.seq).innerJoin(other, this.joinPredicate);
    }

    @Test
    void shouldInnerSelfJoin() {
        this.pairSeq().innerSelfJoin(this.joinPredicate);
        verify(this.seq).innerSelfJoin(this.joinPredicate);
    }

    @Test
    void shouldIntersperseTuple() {
        this.pairSeq().intersperse(A);
        verify(this.seq).intersperse(A);
    }

    @Test
    void shouldIntersperseKeyAndValue() {
        this.pairSeq().intersperse(1, "a");
        verify(this.seq).intersperse(A);
    }

    @Test
    void shouldIsParallel() {
        this.pairSeq().isParallel();
        verify(this.seq).isParallel();
    }

    @Test
    void shouldIterator() {
        this.pairSeq().iterator();
        verify(this.seq).iterator();
    }

    @Test
    void shouldLeftOuterJoinStream() {
        final Stream<Integer> other = Stream.of(1);
        this.pairSeq().leftOuterJoin(other, this.otherPredicate);
        verify(this.seq).leftOuterJoin(other, this.otherPredicate);
    }

    @Test
    void shouldLeftOuterJoinIterable() {
        final List<Integer> other = List.of(1);
        this.pairSeq().leftOuterJoin(other, this.otherPredicate);
        verify(this.seq).leftOuterJoin(other, this.otherPredicate);
    }

    @Test
    void shouldLeftOuterJoinSeq() {
        final Seq<Integer> other = Seq.of(1);
        this.pairSeq().leftOuterJoin(other, this.otherPredicate);
        verify(this.seq).leftOuterJoin(other, this.otherPredicate);
    }

    @Test
    void shouldLeftOuterJoinSeq2() {
        final Seq<Integer> other = Seq.of(1);
        this.pairSeq().leftOuterJoin(Seq2.seq(other), this.otherPredicate);
        verify(this.seq).leftOuterJoin(other, this.otherPredicate);
    }

    @Test
    void shouldLeftOuterJoinPairSeq() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().leftOuterJoin(PairSeq.seq(other), this.joinPredicate);
        verify(this.seq).leftOuterJoin(other, this.joinPredicate);
    }

    @Test
    void shouldLeftOuterSelfJoin() {
        this.pairSeq().leftOuterSelfJoin(this.joinPredicate);
        verify(this.seq).leftOuterSelfJoin(this.joinPredicate);
    }

    @Test
    void shouldLimit() {
        this.pairSeq().limit(2);
        verify(this.seq).limit(2);
    }

    @Test
    void shouldLimitUntilWithPredicate() {
        this.pairSeq().limitUntil(this.predicate);
        verify(this.seq).limitUntil(this.predicate);
    }

    @Test
    void shouldLimitUntilWithBiPredicate() {
        assertThat(data().limitUntil((k, v) -> k == 2).toList()).containsExactly(A);
    }

    @Test
    void shouldLimitUntilClosedWithPredicate() {
        this.pairSeq().limitUntilClosed(this.predicate);
        verify(this.seq).limitUntilClosed(this.predicate);
    }

    @Test
    void shouldLimitUntilClosedWithBiPredicate() {
        assertThat(data().limitUntilClosed((k, v) -> k == 2).toList()).containsExactly(A, B);
    }

    @Test
    void shouldLimitWhileWithPredicate() {
        this.pairSeq().limitWhile(this.predicate);
        verify(this.seq).limitWhile(this.predicate);
    }

    @Test
    void shouldLimitWhileWithBiPredicate() {
        assertThat(data().limitWhile((k, v) -> k < 2).toList()).containsExactly(A);
    }

    @Test
    void shouldLimitWhileClosedWithPredicate() {
        this.pairSeq().limitWhileClosed(this.predicate);
        verify(this.seq).limitWhileClosed(this.predicate);
    }

    @Test
    void shouldLimitWhileClosedWithBiPredicate() {
        assertThat(data().limitWhileClosed((k, v) -> k < 2).toList()).containsExactly(A, B);
    }

    @Test
    void shouldMapWithFunction() {
        this.pairSeq().map(this.function);
        verify(this.seq).map(this.function);
    }

    @Test
    void shouldMapWithKeyAndValueMapper() {
        assertThat(data().map(k -> k + 1, v -> v + v).toList())
                .containsExactly(new Tuple2<>(2, "aa"), new Tuple2<>(3, "bb"), new Tuple2<>(4, "cc"));
    }

    @Test
    void shouldMapToDoubleWithFunction() {
        final ToDoubleFunction<Tuple2<Integer, String>> mapper = Tuple2::v1;
        this.pairSeq().mapToDouble(mapper);
        verify(this.seq).mapToDouble(mapper);
    }

    @Test
    void shouldMapToIntWithFunction() {
        final ToIntFunction<Tuple2<Integer, String>> mapper = Tuple2::v1;
        this.pairSeq().mapToInt(mapper);
        verify(this.seq).mapToInt(mapper);
    }

    @Test
    void shouldMapToLongWithFunction() {
        final ToLongFunction<Tuple2<Integer, String>> mapper = Tuple2::v1;
        this.pairSeq().mapToLong(mapper);
        verify(this.seq).mapToLong(mapper);
    }

    @Test
    void shouldMapToPairWithKeyAndValueMapper() {
        assertThat(data().mapToPair((k, v) -> v, (k, v) -> k).toList())
                .containsExactly(new Tuple2<>("a", 1), new Tuple2<>("b", 2), new Tuple2<>("c", 3));
    }

    @Test
    void shouldMapToPairWithBiFunction() {
        assertThat(data().mapToPair((k, v) -> new Tuple2<>(v, k + 1)).toList())
                .containsExactly(new Tuple2<>("a", 2), new Tuple2<>("b", 3), new Tuple2<>("c", 4));
    }

    @Test
    void shouldMapToPairWithFunction() {
        final Function<Tuple2<Integer, String>, Tuple2<String, Integer>> f = Tuple2::swap;
        this.pairSeq().mapToPair(f);
        verify(this.seq).map(f);
    }

    @Test
    void shouldMaxWithComparator() {
        this.pairSeq().max(this.comparator);
        verify(this.seq).max(this.comparator);
    }

    @Test
    void shouldMaxWithFunction() {
        this.pairSeq().max(this.function);
        verify(this.seq).max(this.function);
    }

    @Test
    void shouldMaxWithBiFunction() {
        assertThat(data().max(this.keyFunction)).hasValue(3);
    }

    @Test
    void shouldMaxWithFunctionAndComparator() {
        this.pairSeq().max(this.function, this.reverse);
        verify(this.seq).max(this.function, this.reverse);
    }

    @Test
    void shouldMaxWithBiFunctionAndComparator() {
        assertThat(data().max(this.keyFunction, this.reverse)).hasValue(1);
    }

    @Test
    void shouldMaxAll() {
        this.pairSeq().maxAll();
        verify(this.seq).maxAll();
    }

    @Test
    void shouldMaxAllWithComparator() {
        this.pairSeq().maxAll(this.comparator);
        verify(this.seq).maxAll(this.comparator);
    }

    @Test
    void shouldMaxAllWithFunction() {
        this.pairSeq().maxAll(this.function);
        verify(this.seq).maxAll(this.function);
    }

    @Test
    void shouldMaxAllWithBiFunction() {
        assertThat(PairSeq.seq(List.of(A, B, new Tuple2<>(2, "c"))).maxAll(this.keyFunction).toList())
                .containsExactly(2, 2);
    }

    @Test
    void shouldMaxAllWithFunctionAndComparator() {
        this.pairSeq().maxAll(this.function, this.reverse);
        verify(this.seq).maxAll(this.function, this.reverse);
    }

    @Test
    void shouldMaxAllWithBiFunctionAndComparator() {
        assertThat(PairSeq.seq(List.of(A, B, new Tuple2<>(2, "c"))).maxAll(this.keyFunction, this.reverse).toList())
                .containsExactly(1);
    }

    @Test
    void shouldMaxAllByWithFunction() {
        this.pairSeq().maxAllBy(this.function);
        verify(this.seq).maxAllBy(this.function);
    }

    @Test
    void shouldMaxAllByWithBiFunction() {
        assertThat(PairSeq.seq(List.of(A, B, new Tuple2<>(2, "c"))).maxAllBy(this.keyFunction).toList())
                .containsExactly(B, new Tuple2<>(2, "c"));
    }

    @Test
    void shouldMaxAllByWithFunctionAndComparator() {
        this.pairSeq().maxAllBy(this.function, this.reverse);
        verify(this.seq).maxAllBy(this.function, this.reverse);
    }

    @Test
    void shouldMaxAllByWithBiFunctionAndComparator() {
        assertThat(PairSeq.seq(List.of(A, B, new Tuple2<>(2, "c"))).maxAllBy(this.keyFunction, this.reverse).toList())
                .containsExactly(A);
    }

    @Test
    void shouldMaxByWithFunction() {
        this.pairSeq().maxBy(this.function);
        verify(this.seq).maxBy(this.function);
    }

    @Test
    void shouldMaxByWithBiFunction() {
        assertThat(data().maxBy(this.keyFunction)).hasValue(C);
    }

    @Test
    void shouldMaxByWithFunctionAndComparator() {
        this.pairSeq().maxBy(this.function, this.reverse);
        verify(this.seq).maxBy(this.function, this.reverse);
    }

    @Test
    void shouldMaxByWithBiFunctionAndComparator() {
        assertThat(data().maxBy(this.keyFunction, this.reverse)).hasValue(A);
    }

    @Test
    void shouldMedianByWithFunction() {
        this.pairSeq().medianBy(this.function);
        verify(this.seq).medianBy(this.function);
    }

    @Test
    void shouldMedianByWithBiFunction() {
        assertThat(PairSeq.seq(List.of(C, A, B)).medianBy(this.keyFunction)).hasValue(B);
    }

    @Test
    void shouldMedianByWithFunctionAndComparator() {
        this.pairSeq().medianBy(this.function, this.reverse);
        verify(this.seq).medianBy(this.function, this.reverse);
    }

    @Test
    void shouldMedianByWithBiFunctionAndComparator() {
        assertThat(PairSeq.seq(List.of(C, A, B)).medianBy(this.keyFunction, this.reverse)).hasValue(B);
    }

    @Test
    void shouldMinWithComparator() {
        this.pairSeq().min(this.comparator);
        verify(this.seq).min(this.comparator);
    }

    @Test
    void shouldMinWithFunction() {
        this.pairSeq().min(this.function);
        verify(this.seq).min(this.function);
    }

    @Test
    void shouldMinWithBiFunction() {
        assertThat(data().min(this.keyFunction)).hasValue(1);
    }

    @Test
    void shouldMinWithFunctionAndComparator() {
        this.pairSeq().min(this.function, this.reverse);
        verify(this.seq).min(this.function, this.reverse);
    }

    @Test
    void shouldMinWithBiFunctionAndComparator() {
        assertThat(data().min(this.keyFunction, this.reverse)).hasValue(3);
    }

    @Test
    void shouldMinAll() {
        this.pairSeq().minAll();
        verify(this.seq).minAll();
    }

    @Test
    void shouldMinAllWithComparator() {
        this.pairSeq().minAll(this.comparator);
        verify(this.seq).minAll(this.comparator);
    }

    @Test
    void shouldMinAllWithFunction() {
        this.pairSeq().minAll(this.function);
        verify(this.seq).minAll(this.function);
    }

    @Test
    void shouldMinAllWithBiFunction() {
        assertThat(PairSeq.seq(List.of(A, new Tuple2<>(1, "b"), C)).minAll(this.keyFunction).toList())
                .containsExactly(1, 1);
    }

    @Test
    void shouldMinAllWithFunctionAndComparator() {
        this.pairSeq().minAll(this.function, this.reverse);
        verify(this.seq).minAll(this.function, this.reverse);
    }

    @Test
    void shouldMinAllWithBiFunctionAndComparator() {
        assertThat(PairSeq.seq(List.of(A, new Tuple2<>(1, "b"), C)).minAll(this.keyFunction, this.reverse).toList())
                .containsExactly(3);
    }

    @Test
    void shouldMinAllByWithFunction() {
        this.pairSeq().minAllBy(this.function);
        verify(this.seq).minAllBy(this.function);
    }

    @Test
    void shouldMinAllByWithBiFunction() {
        assertThat(PairSeq.seq(List.of(A, new Tuple2<>(1, "b"), C)).minAllBy(this.keyFunction).toList())
                .containsExactly(A, new Tuple2<>(1, "b"));
    }

    @Test
    void shouldMinAllByWithFunctionAndComparator() {
        this.pairSeq().minAllBy(this.function, this.reverse);
        verify(this.seq).minAllBy(this.function, this.reverse);
    }

    @Test
    void shouldMinAllByWithBiFunctionAndComparator() {
        assertThat(PairSeq.seq(List.of(A, new Tuple2<>(1, "b"), C)).minAllBy(this.keyFunction, this.reverse).toList())
                .containsExactly(C);
    }

    @Test
    void shouldMinByWithFunction() {
        this.pairSeq().minBy(this.function);
        verify(this.seq).minBy(this.function);
    }

    @Test
    void shouldMinByWithBiFunction() {
        assertThat(data().minBy(this.keyFunction)).hasValue(A);
    }

    @Test
    void shouldMinByWithFunctionAndComparator() {
        this.pairSeq().minBy(this.function, this.reverse);
        verify(this.seq).minBy(this.function, this.reverse);
    }

    @Test
    void shouldMinByWithBiFunctionAndComparator() {
        assertThat(data().minBy(this.keyFunction, this.reverse)).hasValue(C);
    }

    @Test
    void shouldModeAll() {
        this.pairSeq().modeAll();
        verify(this.seq).modeAll();
    }

    @Test
    void shouldModeAllByWithFunction() {
        this.pairSeq().modeAllBy(this.function);
        verify(this.seq).modeAllBy(this.function);
    }

    @Test
    void shouldModeAllByWithBiFunction() {
        assertThat(PairSeq.seq(List.of(A, new Tuple2<>(2, "a"), C)).modeAllBy(this.valueFunction).toList())
                .containsExactly(A, new Tuple2<>(2, "a"));
    }

    @Test
    void shouldModeByWithFunction() {
        this.pairSeq().modeBy(this.function);
        verify(this.seq).modeBy(this.function);
    }

    @Test
    void shouldModeByWithBiFunction() {
        assertThat(PairSeq.seq(List.of(C, A, new Tuple2<>(2, "a"))).modeBy(this.valueFunction))
                .hasValueSatisfying(t -> assertThat(t.v2()).isEqualTo("a"));
    }

    @Test
    void shouldNoneMatchWithPredicate() {
        this.pairSeq().noneMatch(this.predicate);
        verify(this.seq).noneMatch(this.predicate);
    }

    @Test
    void shouldNoneMatchWithBiPredicate() {
        assertThat(data().noneMatch((k, v) -> k > 3)).isTrue();
        assertThat(data().noneMatch((k, v) -> k > 2)).isFalse();
    }

    @Test
    void shouldOfType() {
        final Class<Integer> type = Integer.class;
        this.pairSeq().ofType(type);
        verify(this.seq).ofType(type);
    }

    @Test
    void shouldOnClose() {
        final Runnable closeHandler = () -> {};
        this.pairSeq().onClose(closeHandler);
        verify(this.seq).onClose(closeHandler);
    }

    @Test
    void shouldOnEmptyTuple() {
        this.pairSeq().onEmpty(A);
        verify(this.seq).onEmpty(A);
    }

    @Test
    void shouldOnEmptyKeyAndValue() {
        this.pairSeq().onEmpty(1, "a");
        verify(this.seq).onEmpty(A);
    }

    @Test
    void shouldOnEmptyGet() {
        final Supplier<Tuple2<Integer, String>> supplier = () -> A;
        this.pairSeq().onEmptyGet(supplier);
        verify(this.seq).onEmptyGet(supplier);
    }

    @Test
    void shouldOnEmptyThrow() {
        final Supplier<IllegalStateException> supplier = IllegalStateException::new;
        this.pairSeq().onEmptyThrow(supplier);
        verify(this.seq).onEmptyThrow(supplier);
    }

    @Test
    void shouldOuterApplyWithFunction() {
        final Function<Tuple2<Integer, String>, Iterable<Integer>> f = t -> List.of(1);
        this.pairSeq().outerApply(f);
        verify(this.seq).outerApply(f);
    }

    @Test
    void shouldOuterApplyWithBiFunction() {
        final BiFunction<Integer, String, Iterable<Integer>> f = (k, v) -> k == 1 ? List.of() : List.of(k);
        assertThat(PairSeq.<Integer, String>of(A, B).outerApply(f).toList())
                .containsExactly(new Tuple2<>(A, null), new Tuple2<>(B, 2));
    }

    @Test
    void shouldParallel() {
        this.pairSeq().parallel();
        verify(this.seq).parallel();
    }

    @Test
    void shouldPartitionWithPredicate() {
        when(this.seq.partition(this.predicate)).thenReturn(new Tuple2<>(Seq.empty(), Seq.empty()));
        final Tuple2<PairSeq<Integer, String>, PairSeq<Integer, String>> result =
                this.pairSeq().partition(this.predicate);
        assertThat(result.v1().toList()).isEmpty();
        assertThat(result.v2().toList()).isEmpty();
    }

    @Test
    void shouldPartitionWithBiPredicate() {
        final Tuple2<PairSeq<Integer, String>, PairSeq<Integer, String>> result =
                data().partition((k, v) -> k > 1);
        assertThat(result.v1().toList()).containsExactly(B, C);
        assertThat(result.v2().toList()).containsExactly(A);
    }

    @Test
    void shouldPeekWithConsumer() {
        final Consumer<Tuple2<Integer, String>> action = t -> {};
        this.pairSeq().peek(action);
        verify(this.seq).peek(action);
    }

    @Test
    void shouldPeekWithBiConsumer() {
        final List<String> result = new ArrayList<>();
        assertThat(data().peek((k, v) -> result.add(k + v)).toList()).containsExactly(A, B, C);
        assertThat(result).containsExactly("1a", "2b", "3c");
    }

    @Test
    void shouldPeekKey() {
        final List<Integer> result = new ArrayList<>();
        assertThat(data().peekKey(result::add).toList()).containsExactly(A, B, C);
        assertThat(result).containsExactly(1, 2, 3);
    }

    @Test
    void shouldPeekValue() {
        final List<String> result = new ArrayList<>();
        assertThat(data().peekValue(result::add).toList()).containsExactly(A, B, C);
        assertThat(result).containsExactly("a", "b", "c");
    }

    @Test
    void shouldPercentileByWithFunction() {
        this.pairSeq().percentileBy(0.5, this.function);
        verify(this.seq).percentileBy(0.5, this.function);
    }

    @Test
    void shouldPercentileByWithBiFunction() {
        assertThat(PairSeq.seq(List.of(B, C, A)).percentileBy(0.0, this.keyFunction)).hasValue(A);
    }

    @Test
    void shouldPercentileByWithFunctionAndComparator() {
        this.pairSeq().percentileBy(0.5, this.function, this.reverse);
        verify(this.seq).percentileBy(0.5, this.function, this.reverse);
    }

    @Test
    void shouldPercentileByWithBiFunctionAndComparator() {
        assertThat(PairSeq.seq(List.of(B, C, A)).percentileBy(0.0, this.keyFunction, this.reverse)).hasValue(C);
    }

    @Test
    void shouldPrependKeyAndValue() {
        this.pairSeq().prepend(1, "a");
        verify(this.seq).prepend(A);
    }

    @Test
    void shouldPrependTuple() {
        this.pairSeq().prepend(A);
        verify(this.seq).prepend(A);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldPrependTuples() {
        this.pairSeq().prepend(A, B);
        verify(this.seq).prepend(A, B);
    }

    @Test
    void shouldPrependIterable() {
        final List<Tuple2<Integer, String>> other = List.of(A);
        this.pairSeq().prepend(other);
        verify(this.seq).prepend(other);
    }

    @Test
    void shouldPrependStream() {
        final Stream<Tuple2<Integer, String>> other = Stream.of(A);
        this.pairSeq().prepend(other);
        verify(this.seq).prepend(other);
    }

    @Test
    void shouldPrependSeq() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().prepend(other);
        verify(this.seq).prepend(other);
    }

    @Test
    void shouldPrependSeq2() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().prepend(Seq2.seq(other));
        verify(this.seq).prepend(other);
    }

    @Test
    void shouldPrependPairSeq() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().prepend(PairSeq.seq(other));
        verify(this.seq).prepend(other);
    }

    @Test
    void shouldPrependOptional() {
        final Optional<Tuple2<Integer, String>> other = Optional.of(A);
        this.pairSeq().prepend(other);
        verify(this.seq).prepend(other);
    }

    @Test
    void shouldReduceWithIdentity() {
        final BinaryOperator<Tuple2<Integer, String>> accumulator = (t1, t2) -> t1;
        this.pairSeq().reduce(A, accumulator);
        verify(this.seq).reduce(A, accumulator);
    }

    @Test
    void shouldReduce() {
        final BinaryOperator<Tuple2<Integer, String>> accumulator = (t1, t2) -> t1;
        this.pairSeq().reduce(accumulator);
        verify(this.seq).reduce(accumulator);
    }

    @Test
    void shouldReduceWithCombiner() {
        final BiFunction<Integer, Tuple2<Integer, String>, Integer> accumulator = (i, t) -> i + t.v1();
        final BinaryOperator<Integer> combiner = Integer::sum;
        this.pairSeq().reduce(0, accumulator, combiner);
        verify(this.seq).reduce(0, accumulator, combiner);
    }

    @Test
    void shouldReduceByKeyWithIdentity() {
        assertThat(PairSeq.seq(List.of(A, new Tuple2<>(1, "b"), C)).reduceByKey("", String::concat).toMap())
                .containsOnly(entry(1, "ab"), entry(3, "c"));
    }

    @Test
    void shouldReduceByKey() {
        assertThat(PairSeq.seq(List.of(A, new Tuple2<>(1, "b"), C)).reduceByKey(String::concat).toMap())
                .containsOnly(entry(1, "ab"), entry(3, "c"));
    }

    @Test
    void shouldReduceByKeyWithCombiner() {
        final Map<Integer, Integer> actual = PairSeq.seq(List.of(A, new Tuple2<>(1, "bb"), C))
                .reduceByKey(0, (sum, v) -> sum + v.length(), Integer::sum)
                .toMap();
        assertThat(actual).containsOnly(entry(1, 3), entry(3, 1));
    }

    @Test
    void shouldRemoveTuple() {
        this.pairSeq().remove(A);
        verify(this.seq).remove(A);
    }

    @Test
    void shouldRemoveKeyAndValue() {
        this.pairSeq().remove(1, "a");
        verify(this.seq).remove(A);
    }

    @Test
    void shouldRemoveAllPairSeq() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().removeAll(PairSeq.seq(other));
        verify(this.seq).removeAll(other);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldRemoveAllTuples() {
        this.pairSeq().removeAll(A, B);
        verify(this.seq).removeAll(A, B);
    }

    @Test
    void shouldRemoveAllStream() {
        final Stream<Tuple2<Integer, String>> other = Stream.of(A);
        this.pairSeq().removeAll(other);
        verify(this.seq).removeAll(other);
    }

    @Test
    void shouldRemoveAllIterable() {
        final List<Tuple2<Integer, String>> other = List.of(A);
        this.pairSeq().removeAll(other);
        verify(this.seq).removeAll(other);
    }

    @Test
    void shouldRemoveAllSeq() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().removeAll(other);
        verify(this.seq).removeAll(other);
    }

    @Test
    void shouldRemoveAllSeq2() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().removeAll(Seq2.seq(other));
        verify(this.seq).removeAll(other);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldRetainAllTuples() {
        this.pairSeq().retainAll(A, B);
        verify(this.seq).retainAll(A, B);
    }

    @Test
    void shouldRetainAllStream() {
        final Stream<Tuple2<Integer, String>> other = Stream.of(A);
        this.pairSeq().retainAll(other);
        verify(this.seq).retainAll(other);
    }

    @Test
    void shouldRetainAllIterable() {
        final List<Tuple2<Integer, String>> other = List.of(A);
        this.pairSeq().retainAll(other);
        verify(this.seq).retainAll(other);
    }

    @Test
    void shouldRetainAllSeq() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().retainAll(other);
        verify(this.seq).retainAll(other);
    }

    @Test
    void shouldRetainAllSeq2() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().retainAll(Seq2.seq(other));
        verify(this.seq).retainAll(other);
    }

    @Test
    void shouldRetainAllPairSeq() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().retainAll(PairSeq.seq(other));
        verify(this.seq).retainAll(other);
    }

    @Test
    void shouldReverse() {
        this.pairSeq().reverse();
        verify(this.seq).reverse();
    }

    @Test
    void shouldRightOuterJoinStream() {
        final Stream<Integer> other = Stream.of(1);
        this.pairSeq().rightOuterJoin(other, this.otherPredicate);
        verify(this.seq).rightOuterJoin(other, this.otherPredicate);
    }

    @Test
    void shouldRightOuterJoinIterable() {
        final List<Integer> other = List.of(1);
        this.pairSeq().rightOuterJoin(other, this.otherPredicate);
        verify(this.seq).rightOuterJoin(other, this.otherPredicate);
    }

    @Test
    void shouldRightOuterJoinSeq() {
        final Seq<Integer> other = Seq.of(1);
        this.pairSeq().rightOuterJoin(other, this.otherPredicate);
        verify(this.seq).rightOuterJoin(other, this.otherPredicate);
    }

    @Test
    void shouldRightOuterJoinSeq2() {
        final Seq<Integer> other = Seq.of(1);
        this.pairSeq().rightOuterJoin(Seq2.seq(other), this.otherPredicate);
        verify(this.seq).rightOuterJoin(other, this.otherPredicate);
    }

    @Test
    void shouldRightOuterJoinPairSeq() {
        final Seq<Tuple2<Integer, String>> other = Seq.of(A);
        this.pairSeq().rightOuterJoin(PairSeq.seq(other), this.joinPredicate);
        verify(this.seq).rightOuterJoin(other, this.joinPredicate);
    }

    @Test
    void shouldRightOuterSelfJoin() {
        this.pairSeq().rightOuterSelfJoin(this.joinPredicate);
        verify(this.seq).rightOuterSelfJoin(this.joinPredicate);
    }

    @Test
    void shouldScanLeft() {
        final BiFunction<Integer, Tuple2<Integer, String>, Integer> f = (i, t) -> i + t.v1();
        this.pairSeq().scanLeft(0, f);
        verify(this.seq).scanLeft(0, f);
    }

    @Test
    void shouldScanRight() {
        final BiFunction<Tuple2<Integer, String>, Integer, Integer> f = (t, i) -> i + t.v1();
        this.pairSeq().scanRight(0, f);
        verify(this.seq).scanRight(0, f);
    }

    @Test
    void shouldSequential() {
        this.pairSeq().sequential();
        verify(this.seq).sequential();
    }

    @Test
    void shouldShuffle() {
        this.pairSeq().shuffle();
        verify(this.seq).shuffle();
    }

    @Test
    void shouldShuffleWithRandom() {
        final Random random = new Random(1);
        this.pairSeq().shuffle(random);
        verify(this.seq).shuffle(random);
    }

    @Test
    void shouldSkip() {
        this.pairSeq().skip(1);
        verify(this.seq).skip(1);
    }

    @Test
    void shouldSkipUntilWithPredicate() {
        this.pairSeq().skipUntil(this.predicate);
        verify(this.seq).skipUntil(this.predicate);
    }

    @Test
    void shouldSkipUntilWithBiPredicate() {
        assertThat(data().skipUntil((k, v) -> k == 2).toList()).containsExactly(B, C);
    }

    @Test
    void shouldSkipUntilClosedWithPredicate() {
        this.pairSeq().skipUntilClosed(this.predicate);
        verify(this.seq).skipUntilClosed(this.predicate);
    }

    @Test
    void shouldSkipUntilClosedWithBiPredicate() {
        assertThat(data().skipUntilClosed((k, v) -> k == 2).toList()).containsExactly(C);
    }

    @Test
    void shouldSkipWhileWithPredicate() {
        this.pairSeq().skipWhile(this.predicate);
        verify(this.seq).skipWhile(this.predicate);
    }

    @Test
    void shouldSkipWhileWithBiPredicate() {
        assertThat(data().skipWhile((k, v) -> k < 2).toList()).containsExactly(B, C);
    }

    @Test
    void shouldSkipWhileClosedWithPredicate() {
        this.pairSeq().skipWhileClosed(this.predicate);
        verify(this.seq).skipWhileClosed(this.predicate);
    }

    @Test
    void shouldSkipWhileClosedWithBiPredicate() {
        assertThat(data().skipWhileClosed((k, v) -> k < 2).toList()).containsExactly(C);
    }

    @Test
    void shouldSlice() {
        this.pairSeq().slice(1, 2);
        verify(this.seq).slice(1, 2);
    }

    @Test
    void shouldSliding() {
        when(this.seq.sliding(2)).thenReturn(Seq.of(Seq.of(A, B)));
        final List<PairSeq<Integer, String>> result = this.pairSeq().sliding(2).toList();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).toList()).containsExactly(A, B);
    }

    @Test
    void shouldSorted() {
        this.pairSeq().sorted();
        verify(this.seq).sorted();
    }

    @Test
    void shouldSortedWithComparator() {
        this.pairSeq().sorted(this.comparator);
        verify(this.seq).sorted(this.comparator);
    }

    @Test
    void shouldSortedWithFunction() {
        this.pairSeq().sorted(this.function);
        verify(this.seq).sorted(this.function);
    }

    @Test
    void shouldSortedWithBiFunction() {
        assertThat(PairSeq.seq(List.of(B, C, A)).sorted(this.valueFunction).toList()).containsExactly(A, B, C);
    }

    @Test
    void shouldSortedWithFunctionAndComparator() {
        this.pairSeq().sorted(this.function, this.reverse);
        verify(this.seq).sorted(this.function, this.reverse);
    }

    @Test
    void shouldSortedWithBiFunctionAndComparator() {
        assertThat(PairSeq.seq(List.of(B, C, A)).sorted(this.keyFunction, this.reverse).toList())
                .containsExactly(C, B, A);
    }

    @Test
    void shouldSplitAt() {
        when(this.seq.splitAt(1)).thenReturn(new Tuple2<>(Seq.empty(), Seq.empty()));
        final Tuple2<PairSeq<Integer, String>, PairSeq<Integer, String>> result = this.pairSeq().splitAt(1);
        assertThat(result.v1().toList()).isEmpty();
        assertThat(result.v2().toList()).isEmpty();
    }

    @Test
    void shouldSplitAtHead() {
        when(this.seq.splitAtHead()).thenReturn(new Tuple2<>(Optional.empty(), Seq.empty()));
        final Tuple2<Optional<Tuple2<Integer, String>>, PairSeq<Integer, String>> result =
                this.pairSeq().splitAtHead();
        assertThat(result.v1()).isEmpty();
        assertThat(result.v2().toList()).isEmpty();
    }

    @Test
    void shouldSumWithFunction() {
        this.pairSeq().sum(this.function);
        verify(this.seq).sum(this.function);
    }

    @Test
    void shouldSumWithBiFunction() {
        assertThat(data().sum(this.keyFunction)).hasValue(6);
    }

    @Test
    void shouldSumDoubleWithFunction() {
        final ToDoubleFunction<Tuple2<Integer, String>> f = Tuple2::v1;
        this.pairSeq().sumDouble(f);
        verify(this.seq).sumDouble(f);
    }

    @Test
    void shouldSumDoubleWithBiFunction() {
        assertThat(data().sumDouble((k, v) -> k)).isEqualTo(6.0);
    }

    @Test
    void shouldSumIntWithFunction() {
        final ToIntFunction<Tuple2<Integer, String>> f = Tuple2::v1;
        this.pairSeq().sumInt(f);
        verify(this.seq).sumInt(f);
    }

    @Test
    void shouldSumIntWithBiFunction() {
        assertThat(data().sumInt((k, v) -> k)).isEqualTo(6);
    }

    @Test
    void shouldSumLongWithFunction() {
        final ToLongFunction<Tuple2<Integer, String>> f = Tuple2::v1;
        this.pairSeq().sumLong(f);
        verify(this.seq).sumLong(f);
    }

    @Test
    void shouldSumLongWithBiFunction() {
        assertThat(data().sumLong((k, v) -> k)).isEqualTo(6L);
    }

    @Test
    void shouldSwap() {
        assertThat(data().swapped().toList())
                .containsExactly(new Tuple2<>("a", 1), new Tuple2<>("b", 2), new Tuple2<>("c", 3));
    }

    @Test
    void shouldTake() {
        this.pairSeq().take(2);
        verify(this.seq).take(2);
    }

    @Test
    void shouldTakeWhileWithPredicate() {
        this.pairSeq().takeWhile(this.predicate);
        verify(this.seq).takeWhile(this.predicate);
    }

    @Test
    void shouldTakeWhileWithBiPredicate() {
        assertThat(data().takeWhile((k, v) -> k < 3)).containsExactly(A, B);
    }

    @Test
    void shouldToArray() {
        this.pairSeq().toArray();
        verify(this.seq).toArray();
    }

    @Test
    void shouldToArrayWithGenerator() {
        final IntFunction<Object[]> generator = Object[]::new;
        this.pairSeq().toArray(generator);
        verify(this.seq).toArray(generator);
    }

    @Test
    void shouldCollectToMapWithFunctions() {
        final Function<Tuple2<Integer, String>, String> valueMapper = Tuple2::v2;
        this.pairSeq().toMap(this.function, valueMapper);
        verify(this.seq).toMap(this.function, valueMapper);
    }

    @Test
    void shouldCollectToMapWithBiFunctions() {
        assertThat(data().toMap((k, v) -> v, (k, v) -> k))
                .containsOnly(entry("a", 1), entry("b", 2), entry("c", 3));
    }

    @Test
    void shouldCollectToMapWithFunction() {
        this.pairSeq().toMap(this.function);
        verify(this.seq).toMap(this.function);
    }

    @Test
    void shouldCollectToMapWithBiFunction() {
        assertThat(data().toMap((k, v) -> v))
                .containsOnly(entry("a", A), entry("b", B), entry("c", C));
    }

    @Test
    void shouldReturnSeq2() {
        final Seq<Tuple2<Integer, String>> actual = this.pairSeq().toSeq2().toSeq();
        assertThat((Object) actual).isSameAs(this.seq);
    }

    @Test
    void shouldTransform() {
        final Function<PairSeq<Integer, String>, List<String>> transformer = s -> s.map((k, v) -> k + v).toList();
        assertThat(data().transform(transformer)).containsExactly("1a", "2b", "3c");
    }

    @Test
    void shouldUnordered() {
        this.pairSeq().unordered();
        verify(this.seq).unordered();
    }

    @Test
    void shouldWindow() {
        this.pairSeq().window();
        verify(this.seq).window();
    }

    @Test
    void shouldWindowWithBounds() {
        this.pairSeq().window(-1, 1);
        verify(this.seq).window(-1, 1);
    }

    @Test
    void shouldWindowWithOrder() {
        this.pairSeq().window(this.comparator);
        verify(this.seq).window(this.comparator);
    }

    @Test
    void shouldWindowWithOrderAndBounds() {
        this.pairSeq().window(this.comparator, -1, 1);
        verify(this.seq).window(this.comparator, -1, 1);
    }

    @Test
    void shouldWindowWithPartitionFunction() {
        this.pairSeq().window(this.function);
        verify(this.seq).window(this.function);
    }

    @Test
    void shouldWindowWithPartitionBiFunction() {
        assertThat(data().window(this.parityFunction).map(Window::count).toList()).containsExactly(2L, 1L, 2L);
    }

    @Test
    void shouldWindowWithPartitionFunctionAndBounds() {
        this.pairSeq().window(this.function, -1, 1);
        verify(this.seq).window(this.function, -1, 1);
    }

    @Test
    void shouldWindowWithPartitionBiFunctionAndBounds() {
        assertThat(data().window(this.parityFunction, -1, 0).map(Window::count).toList())
                .containsExactly(1L, 1L, 2L);
    }

    @Test
    void shouldWindowWithPartitionFunctionAndOrder() {
        this.pairSeq().window(this.function, this.comparator);
        verify(this.seq).window(this.function, this.comparator);
    }

    @Test
    void shouldWindowWithPartitionBiFunctionAndOrder() {
        assertThat(data().window(this.parityFunction, this.comparator).map(Window::rowNumber).toList())
                .containsExactly(0L, 0L, 1L);
    }

    @Test
    void shouldWindowWithPartitionFunctionAndOrderAndBounds() {
        this.pairSeq().window(this.function, this.comparator, -1, 1);
        verify(this.seq).window(this.function, this.comparator, -1, 1);
    }

    @Test
    void shouldWindowWithPartitionBiFunctionAndOrderAndBounds() {
        assertThat(data().window(this.parityFunction, this.comparator, -1, 0).map(Window::count).toList())
                .containsExactly(1L, 1L, 2L);
    }

}
