/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ratpack.func;

import ratpack.util.Exceptions;

/**
 * A function that returns {@code true} or {@code false} for a value.
 * <p>
 * This type serves the same purpose as the JDK's {@link java.util.function.Predicate}, but allows throwing checked exceptions.
 * It contains methods for bridging to and from the JDK type.
 *
 * @param <T> the type of object “tested” by the predicate
 */
@FunctionalInterface
public interface Predicate<T> {

  /**
   * Tests the given value.
   *
   * @param t the value to “test”
   * @return {@code true} if the predicate applied, otherwise {@code false}
   * @throws Exception any
   */
  boolean apply(T t) throws Exception;

  /**
   * Creates a JDK {@link java.util.function.Predicate} from this predicate.
   * <p>
   * Any exceptions thrown by {@code this} action will be unchecked via {@link ratpack.util.Exceptions#uncheck(Throwable)} and rethrown.
   *
   * @return this function as a JDK style predicate.
   */
  default java.util.function.Predicate<T> toPredicate() {
    return t -> {
      try {
        return apply(t);
      } catch (Exception e) {
        throw Exceptions.uncheck(e);
      }
    };
  }

  /**
   * Creates a Guava {@link com.google.common.base.Predicate} from this predicate.
   * <p>
   * Any exceptions thrown by {@code this} action will be unchecked via {@link ratpack.util.Exceptions#uncheck(Throwable)} and rethrown.
   *
   * @return this function as a Guava style predicate.
   */
  default com.google.common.base.Predicate<T> toGuavaPredicate() {
    return t -> {
      try {
        return apply(t);
      } catch (Exception e) {
        throw Exceptions.uncheck(e);
      }
    };
  }

  /**
   * Creates a predicate from a JDK predicate.
   *
   * @param predicate the JDK predicate
   * @param <T> the type of object this predicate tests
   * @return the given JDK predicate as a predicate
   */
  static <T> Predicate<T> from(java.util.function.Predicate<T> predicate) {
    return predicate::test;
  }

  /**
   * Creates a predicate from a Guava predicate.
   *
   * @param predicate the Guava predicate
   * @param <T> the type of object this predicate tests
   * @return the given Guava predicate as a predicate
   */
  static <T> Predicate<T> fromGuava(com.google.common.base.Predicate<T> predicate) {
    return predicate::apply;
  }

}
