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

import static org.apache.commons.lang3.Validate.notNull;

/**
 * And operator over two expression.
 *
 * @author edgar.espina
 * @since 0.0.1
 */
class AndExpression extends BaseExpression {

  /**
   * The left side expression.
   */
  private Semver left;

  /**
   * The right side expression.
   */
  private Semver right;

  /**
   * Creates a new {@link AndExpression}.
   *
   * @param left The left side expression. Required.
   * @param right The right side expression. Required.
   */
  public AndExpression(final Semver left, final Semver right) {
    this.left = notNull(left, "The left side expression is required.");
    this.right = notNull(right, "The right side expression is required.");
  }

  @Override
  public boolean matches(final Semver expr) {
    boolean left = this.left.matches(expr);
    boolean right = this.right.matches(expr);
    return left && right;
  }

  @Override
  public int compareTo(final Semver expr) {
    boolean left = this.left.compareTo(expr) >= 0;
    boolean right = expr.compareTo(this.right) <= 0;
    return left && right ? 0 : left ? -1 : 1;
  }

  @Override
  public String text() {
    return left + " " + right;
  }

  @Override
  public Type type() {
    return Type.AND;
  }
}
