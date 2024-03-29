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

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.lambda.Seq;

@RequiredArgsConstructor
class Seq2Impl<T> implements Seq2<T> {
    private final Seq<T> wrapped;

    @Override
    public List<T> toList() {
        return this.toSeq().toList();
    }

    @Override
    public Seq<T> toSeq() {
        return this.wrapped;
    }
}
