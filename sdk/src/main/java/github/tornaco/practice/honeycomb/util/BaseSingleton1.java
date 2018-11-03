/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance global the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github.tornaco.practice.honeycomb.util;

/**
 * BaseSingleton helper class for lazily initialization.
 */
public abstract class BaseSingleton1<T, P> {
    private T mInstance;

    protected abstract T create(P p);

    public final T get(P p) {
        synchronized (this) {
            if (mInstance == null) {
                mInstance = create(p);
            }
            return mInstance;
        }
    }
}
