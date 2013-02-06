package com.github.jknack.semver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class SemverErrorTest {

  @Test
  public void invalidMajor() {
    execute(runnable("a.0.0"), "found: 'a', expected: '0-9+, =, <, <=, >, >= or ~'");
  }

  @Test
  public void xNotAlledInMajor() {
    execute(runnable("x.0.0"), "found: 'x', expected: '0-9+, =, <, <=, >, >= or ~'");
  }

  @Test
  public void invalidMinor() {
    execute(runnable("0.#.0"), "found: '#', expected: '0-9+ or x'");
  }

  @Test
  public void invalidPatch() {
    execute(runnable("0.0.^"), "found: '^', expected: '0-9+ or x'");
  }

  @Test
  public void invalidOperator() {
    execute(runnable("-0.0.1"), "found: '-', expected: '0-9+, =, <, <=, >, >= or ~'");
  }

  @Test
  public void invalidRange() {
    execute(runnable("0.0.1 -"), "found: 'eof', expected: '0-9+, =, <, <=, >, >= or ~'");
  }

  @Test
  public void invalidOR() {
    execute(runnable("0.0.1 |"), "found: '|', expected: '0-9+, =, <, <=, >, >= or ~'");
  }

  @Test
  public void invalidOROR() {
    execute(runnable("0.0.1 ||"), "found: 'eof', expected: '0-9+, =, <, <=, >, >= or ~'");
  }

  private Runnable runnable(final String expression) {
    return new Runnable() {
      @Override
      public void run() {
        Semver.create(expression);
      }

      @Override
      public String toString() {
        return expression;
      }
    };
  }

  private static void execute(final Runnable runnable, final String expected) {
    try {
      runnable.run();
      fail("expression: '" + runnable.toString() + "' MUST fail");
    } catch (IllegalArgumentException ex) {
      assertEquals(expected, ex.getMessage());
    }
  }
}
