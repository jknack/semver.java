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

import java.util.Comparator;

/**
 * A semantic version.
 * <ul>
 * <li><code>version</code> Must match version exactly</li>
 * <li><code>=version</code> Same as just version</li>
 * <li><code>>version</code> Must be greater than version</li>
 * <li><code>>=version</code> etc</li>
 * <li><code>&lt;version</code></li>
 * <li><code><=version</code></li>
 * <li><code>~version</code> See 'Tilde Version Ranges' below</li>
 * <li><code>1.2.x</code> See 'X Version Ranges' below</li>
 * <li><code>http://...</code> See 'URLs as Dependencies' below</li>
 * <li><code>*</code>Matches any version</li>
 * <li><code>""</code> (just an empty string) Same as *</li>
 * <li><code>version1 - version2</code> Same as >=version1 <=version2.</li>
 * <li><code>range1 || range2</code> Passes if either range1 or range2 are satisfied.</li>
 * <li><code>git...</code>See 'Git URLs as Dependencies' below</li>
 * </ul>
 *
 * @author edgar.espina
 * @since 0.0.1
 */
public abstract class Semver implements Comparable<Semver> {

  public static enum Type {
    ANY, AND, OR, RANGE, TILDE, X_RANGE, EQ, GT, GT_EQ, LT, LT_EQ, URL, STATIC, LATEST;
  }

  /**
   * Identify the latest version of a dependency.
   */
  public static final Semver LATEST = new BaseExpression() {
    @Override
    public boolean matches(final Semver expr) {
      return text().equals(expr.text());
    }

    @Override
    public int compareTo(final Semver expr) {
      return matches(expr) ? 0 : 1;
    }

    @Override
    public String text() {
      return "latest";
    }

    @Override
    public Type type() {
      return Type.LATEST;
    }

    @Override
    public boolean isStatic() {
      return false;
    }
  };

  public static final Comparator<String> DESC = new Comparator<String>() {
    @Override
    public int compare(final String o1, final String o2) {
      Semver v1 = Semver.create(o1);
      Semver v2 = Semver.create(o2);
      if (v1 == LATEST) {
        return v2 == LATEST ? 0 : 1;
      }
      if (v2 == LATEST) {
        return -1;
      }
      return -v1.compareTo(v2);
    }
  };

  /**
   * Match any version.
   */
  public static final Semver ANY = new BaseExpression() {
    @Override
    public boolean matches(final Semver expr) {
      return true;
    }

    @Override
    public int compareTo(final Semver expr) {
      return 0;
    }

    @Override
    public String text() {
      return "*";
    }

    @Override
    public Type type() {
      return Type.ANY;
    }

    @Override
    public boolean isStatic() {
      return true;
    }
  };

  /**
   * True if the given expression matches.
   *
   * @param expr The candidate expression.
   * @return True if the given expression matches.
   */
  public abstract boolean matches(Semver expr);

  /**
   * True if the given expression matches.
   *
   * @param expr The candidate expression.
   * @return True if the given expression matches.
   */
  public abstract boolean matches(String expr);

  @Override
  public abstract int compareTo(Semver expr);

  public abstract boolean isStatic();

  public abstract String text();

  public abstract Type type();

  @Override
  public String toString() {
    return text();
  }

  /**
   * Parse a version expression.
   *
   * @param version A version expression.
   * @return An expression.
   */
  public static Semver create(final String version) {
    return new SemverParser(version).parse();
  }
}
