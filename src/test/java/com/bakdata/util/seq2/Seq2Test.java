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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class Seq2Test {

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

}