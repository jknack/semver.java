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

import java.net.URI;

/**
 * URI expression.
 *
 * @author edgar.espina
 * @since 0.0.1
 */
class UrlExpression extends BaseExpression {

  /**
   * The URI expression.
   */
  private URI uri;

  /**
   * Creates a new {@link UrlExpression}.
   *
   * @param uri The uri expression.
   */
  public UrlExpression(final String uri) {
    this.uri = URI.create(uri);
  }

  @Override
  public String text() {
    return uri.toString();
  }

  @Override
  public boolean matches(final Semver expr) {
    return uri.toString().equals(expr.toString());
  }

  @Override
  public int compareTo(final Semver expr) {
    throw new UnsupportedOperationException("for " + this);
  }

  @Override
  public Type type() {
    return Type.URL;
  }
}
