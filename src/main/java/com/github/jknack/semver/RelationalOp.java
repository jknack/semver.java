/**
 * Copyright (c) 2013 Edgar Espina
 *
 * This file is part of amd4j (https://github.com/jknack/amd4j)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * This copy of Woodstox XML processor is licensed under the
 * Apache (Software) License, version 2.0 ("the License").
 * See the License for details about distribution rights, and the
 * specific rights regarding derivate works.
 *
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/
 *
 * A copy is also included in the downloadable source code package
 * containing Woodstox, in file "ASL2.0", under the same directory
 * as this file.
 */
package com.github.jknack.semver;

/**
 * Relational operators.
 *
 * @author edgar.espina
 * @since 0.0.1
 */
abstract class RelationalOp extends BaseExpression implements
    PrefixOperator {

  /**
   * Equals.
   *
   * @author edgar.espina
   */
  static class EqualsTo extends RelationalOp {
    @Override
    public boolean matches(final Semver expr) {
      return compareTo(expr) == 0;
    }

    @Override
    public String text() {
      return "=" + expression;
    }

    @Override
    public Type type() {
      return Type.EQ;
    }
  }

  /**
   * Less Than.
   *
   * @author edgar.espina
   */
  static class LessThan extends RelationalOp {
    @Override
    public boolean matches(final Semver expr) {
      return compareTo(expr) < 0;
    }

    @Override
    public String text() {
      return "<" + expression;
    }

    @Override
    public Type type() {
      return Type.LT;
    }
  }

  /**
   * Less than or equals to.
   *
   * @author edgar.espina
   */
  static class LessThanEqualsTo extends RelationalOp {
    @Override
    public boolean matches(final Semver expr) {
      return compareTo(expr) <= 0;
    }

    @Override
    public String text() {
      return "<=" + expression;
    }

    @Override
    public Type type() {
      return Type.LT_EQ;
    }
  }

  /**
   * Greater than.
   *
   * @author edgar.espina
   */
  static class GreaterThan extends RelationalOp {
    @Override
    public boolean matches(final Semver expr) {
      return compareTo(expr) > 0;
    }

    @Override
    public String text() {
      return ">" + expression;
    }

    @Override
    public Type type() {
      return Type.GT;
    }
  }

  /**
   * Greater than or equals to.
   *
   * @author edgar.espina
   */
  static class GreatherThanEqualsTo extends RelationalOp {
    @Override
    public boolean matches(final Semver expr) {
      return compareTo(expr) >= 0;
    }

    @Override
    public String text() {
      return ">=" + expression;
    }

    @Override
    public Type type() {
      return Type.GT_EQ;
    }
  }

  /**
   * The expression.
   */
  protected Semver expression;

  @Override
  public Semver setExpression(final Semver expr) {
    expression = expr;
    return this;
  }

  @Override
  public int compareTo(final Semver expr) {
    return expr.compareTo(expression);
  }

  /**
   * Creates a new less than operator.
   *
   * @return A new less than operator.
   */
  public static RelationalOp lt() {
    return new LessThan();
  }

  /**
   * Creates a new less than or equals to operator.
   *
   * @return A new less than or equals to operator.
   */
  public static RelationalOp ltEq() {
    return new LessThanEqualsTo();
  }

  /**
   * Creates a new greater than operator.
   *
   * @return A new greater than operator.
   */
  public static RelationalOp gt() {
    return new GreaterThan();
  }

  /**
   * Creates a new greater than or equals to operator.
   *
   * @return A new greater than or equals to operator.
   */
  public static RelationalOp gtEq() {
    return new GreatherThanEqualsTo();
  }

  /**
   * Creates a new equals to operator.
   *
   * @return A new equals to operator.
   */
  public static RelationalOp eq() {
    return new EqualsTo();
  }
}
