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
package com.github.jknack.semver;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.Validate.notNull;

import org.apache.commons.lang3.StringUtils;

class SemverParser {

  private static abstract class Matcher {
    public abstract boolean match(char ch);
  }

  private static final Matcher PRE_RELEASE_OR_BUILD_NUMBER = new Matcher() {
    @Override
    public boolean match(final char ch) {
      return firstOf(eq('-'), eq('.'), range('0', '9'), range('a', 'z'), range('A', 'Z')).match(ch);
    }
  };

  /**
   * The End-of-Input non-character.
   */
  public static final char EOF = '\uFFFF';

  private String input;

  private int position;

  public SemverParser(final String input) {
    this.input = notNull(input, "The input is required.").trim();
  }

  public Semver parse() {
    if (input.length() == 0) {
      return Semver.ANY;
    }
    return expression();
  }

  private PrefixOperator operator() {
    if (match('=')) {
      return RelationalOp.eq();
    } else if (match("<=")) {
      return RelationalOp.ltEq();
    } else if (match(">=")) {
      return RelationalOp.gtEq();
    } else if (match("<")) {
      return RelationalOp.lt();
    } else if (match(">")) {
      return RelationalOp.gt();
    } else if (match('~')) {
      return Range.tilde();
    }
    return null;
  }

  private Semver expression() {
    // any version
    if (match('*')) {
      return Semver.ANY;
    }
    // uri version
    String[] protocols = {"https", "http", "git+shh", "git+https", "git+http", "git" };
    for (String protocol : protocols) {
      String prefix = protocol + "://";
      if (match(prefix)) {
        return new UrlExpression(prefix + input.substring(position));
      }
    }
    Semver result = rangeOrVersion();
    // check for '||' expression
    while (!eof()) {
      // drop whites
      ws();
      if (match("||")) {
        Semver right = rangeOrVersion();
        result = new OrExpression(result, right);
      }
    }
    // normal version
    return result;
  }

  private Semver rangeOrVersion() {
    // drop whites
    ws();
    Semver result = version();
    String ws = ws();
    if (!extract(2).equals("||") && (match('-') || ws.length() > 0)) {
      ws();
      Semver right = version();
      if (result instanceof Version && right instanceof Version) {
        result = Range.range((Version) result, (Version) right);
      } else {
        result = new AndExpression(result, right);
      }
    }
    // drop whites
    ws();
    return result;
  }

  private boolean eof() {
    return ch() == EOF;
  }

  private Semver version() {
    // operator matching
    final PrefixOperator operator = operator();

    // drop whites
    ws();

    int start = position;
    // drop 'v'
    match('v');

    // major
    int major = versionNumber("0-9+, =, <, <=, >, >= or ~", false);

    // minor
    int minor = 0;
    if (match('.')) {
      minor = versionNumber("0-9+ or x", true);
    }

    // patch
    int patch = 0;
    String[] prerelease = {};
    String[] buildNumber = {};
    if (match('.')) {
      patch = versionNumber("0-9+ or x", true);
      // pre-release number
      prerelease = preReleaseOrBuildNumber('-');
      // build number
      buildNumber = preReleaseOrBuildNumber('+');
    }
    int end = position;
    String text = input.substring(start, end).trim();
    Version version = new Version(text, major, minor, patch);
    version.setPreRelease(prerelease);
    version.setBuildNumber(buildNumber);
    Semver result = text.contains("x") ? Range.x(version) : version;
    return operator == null ? result : operator.setExpression(result);
  }

  private String[] preReleaseOrBuildNumber(final char separator) {
    match(separator);
    String preRelease = match(PRE_RELEASE_OR_BUILD_NUMBER);
    return isEmpty(preRelease) ? new String[0] : StringUtils.split(preRelease, ".");
  }

  private int versionNumber(final String label, final boolean allowX) {
    String digits = digits(label);
    if (!allowX && digits.equalsIgnoreCase("x")) {
      throw error(label, digits.length());
    }
    return toNumber(digits);
  }

  private int toNumber(final String digits) {
    return digits.equalsIgnoreCase("x") ? 0 : Integer.parseInt(digits);
  }

  private RuntimeException error(final String expected) {
    return error(expected, 0);
  }

  private RuntimeException error(final String expected, final int offset) {
    char ch = ch(position - offset);
    return new IllegalArgumentException("found: '" + (ch == EOF ? "eof" : ch) + "', expected: '"
        + expected + "'");
  }

  private String digits(final String expected) {
    String match = match(new Matcher() {
      @Override
      public boolean match(final char ch) {
        return Character.isDigit(ch) || ch == 'x' || ch == 'X';
      }
    });

    if (isEmpty(match)) {
      throw error(expected);
    }
    return match;
  }

  private String match(final Matcher matcher) {
    char ch = ch();
    int start = position;
    while (ch != EOF && matcher.match(ch)) {
      consume();
      ch = ch();
    }
    int end = position;
    if (start < end) {
      return input.substring(start, end);
    }
    return "";
  }

  private boolean match(final char ch) {
    char la = ch();
    if (la == ch) {
      consume();
      return true;
    }
    return false;
  }

  private String extract(final int len) {
    return input.substring(position, Math.min(len + position, input.length()));
  }

  private boolean match(final String token) {
    String chunk = extract(token.length());
    if (token.equals(chunk)) {
      consume(token.length());
      return true;
    }
    return false;
  }

  private String ws() {
    return match(new Matcher() {
      @Override
      public boolean match(final char ch) {
        return Character.isWhitespace(ch);
      }
    });
  }

  private char ch() {
    return ch(position);
  }

  private char ch(final int position) {
    if (position < 0 || position >= input.length()) {
      return EOF;
    }
    return input.charAt(position);
  }

  private void consume() {
    consume(1);
  }

  private void consume(final int count) {
    for (int i = 0; i < count; i++) {
      position++;
    }
  }

  private static Matcher range(final char lower, final char upper) {
    return new Matcher() {
      @Override
      public boolean match(final char ch) {
        return ch >= lower && ch <= upper;
      }
    };
  }

  private static Matcher eq(final char ch) {
    return new Matcher() {
      @Override
      public boolean match(final char candidate) {
        return ch == candidate;
      }
    };
  }

  private static Matcher firstOf(final Matcher... matchers) {
    return new Matcher() {
      @Override
      public boolean match(final char ch) {
        for (Matcher matcher : matchers) {
          if (matcher.match(ch)) {
            return true;
          }
        }
        return false;
      }
    };
  }
}
