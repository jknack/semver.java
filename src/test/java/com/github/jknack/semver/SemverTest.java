package com.github.jknack.semver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SemverTest {

  @Test
  public void simple() {
    assertEquals("0.1.2", Semver.create("0.1.2").toString());
    assertEquals("0.1.2-7", Semver.create("0.1.2-7").toString());
    assertEquals("0.1.2-beta", Semver.create("0.1.2-beta").toString());
    assertEquals("0.1.2-7-beta", Semver.create("0.1.2-7-beta")
        .toString());

    assertTrue(Semver.create("0.1.2").matches("0.1.2"));
  }

  @Test
  public void eq() {
    assertEquals("=0.1.2", Semver.create("=0.1.2").toString());
    assertEquals("=0.1.2-7", Semver.create("=0.1.2-7").toString());
    assertEquals("=0.1.2-beta", Semver.create("=0.1.2-beta")
        .toString());
    assertEquals("=0.1.2-7-beta", Semver.create("=0.1.2-7-beta")
        .toString());

    assertTrue(Semver.create("=0.1.2").matches("0.1.2"));
    assertFalse(Semver.create("=0.1.2").matches("0.1.3"));
  }

  @Test
  public void gt() {
    assertTrue(Semver.create(">0.1.2").matches("0.1.3"));

    assertFalse(Semver.create(">0.1.2").matches("0.1.2"));

    assertTrue(Semver.create(">0.1.2-beta").matches("0.1.2"));

    assertTrue(Semver.create(">0.1.2-7").matches("0.1.2-7-beta"));

    assertTrue(Semver.create(">0.1.2-6").matches("0.1.2-7-beta"));

    assertTrue(Semver.create(">0.1.2-6").matches("0.1.2beta"));
  }

  @Test
  public void gtEq() {
    assertTrue(Semver.create(">=0.1.1").matches("0.1.2"));

    assertTrue(Semver.create(">=0.1.2").matches("0.1.2"));

    assertTrue(Semver.create(">=0.1.2-beta").matches("0.1.2"));
  }

  @Test
  public void lt() {
    assertTrue(Semver.create("<1").matches("0.1.2"));

    assertFalse(Semver.create("<0.1.2").matches("0.1.2"));

    assertTrue(Semver.create("<0.1.3").matches("0.1.2"));

    assertTrue(Semver.create("<0.2").matches("0.1.2"));
  }

  @Test
  public void ltEq() {
    assertTrue(Semver.create("<=1").matches("0.1.2"));

    assertTrue(Semver.create("<=0.1.2").matches("0.1.2"));

    assertTrue(Semver.create("<=0.1.3").matches("0.1.3"));

    assertTrue(Semver.create("<=0.2").matches("0.1.1"));
  }

  @Test
  public void tilde() {
    assertTrue(Semver.create("~1.2.3").matches("1.2.3"));

    assertFalse(Semver.create("~1.2.3").matches("1.2.2"));

    assertTrue(Semver.create("~1.2.3").matches("1.2.9"));

    assertFalse(Semver.create("~1.2.3").matches("1.3"));

    assertTrue(Semver.create("~1.2").matches("1.2.0"));

    assertTrue(Semver.create("~1.2").matches("1.2.3"));

    assertTrue(Semver.create("~1.2").matches("1.9"));

    assertFalse(Semver.create("~1.2").matches("2.0"));
  }

  @Test
  public void x() {
    assertTrue(Semver.create("1.2.x").matches("1.2.3"));

    assertFalse(Semver.create("1.2.x").matches("1.3"));

    assertTrue(Semver.create("1.x.x").matches("1"));

    assertTrue(Semver.create("1.x.x").matches("1.0"));

    assertTrue(Semver.create("1.x.x").matches("1.0.0"));
    assertTrue(Semver.create("1.x.x").matches("1.1.0"));

    assertTrue(Semver.create("1.x.x").matches("1.2.4"));

    assertFalse(Semver.create("1.x.x").matches("2.0"));
  }

  @Test
  public void uri() {
    assertEquals("http://asdf.com/asdf.tar.gz",
        Semver.create("http://asdf.com/asdf.tar.gz").toString());

    assertEquals("git://github.com/user/project.git#commit-ish",
        Semver.create("git://github.com/user/project.git#commit-ish")
            .toString());
  }

  @Test
  public void any() {
    assertEquals("*", Semver.create("*").toString());

    assertEquals("*", Semver.create("").toString());

    assertTrue(Semver.create("*").matches("1.2.3"));
    assertTrue(Semver.create("").matches("1.2.3"));
  }

  @Test
  public void range() {
    assertEquals("1.0.0 - 2.9999.9999",
        Semver.create("1.0.0 - 2.9999.9999").toString());

    assertTrue(Semver.create("1.0.0- 2.9999.9999").matches("1.0.0"));
    assertTrue(Semver.create("1.0.0 - 2.9999.9999").matches("1.5"));
    assertTrue(Semver.create("1.0.0 - 2.9999.9999").matches(
        "2.9999.9999"));

    assertFalse(Semver.create("1.0.0 - 2.9999.9999").matches("3"));

    assertEquals(">=1.0.2 <2.1.2",
        Semver.create(">=1.0.2 <2.1.2").toString());

    assertTrue(Semver.create(">=1.0.2 <2.1.2").matches("1.0.2"));
    assertTrue(Semver.create(">=1.0.2 <=2.1.2").matches("2.1.2"));
    assertFalse(Semver.create(">=1.0.2 <2.1.2").matches("2.1.2"));

    assertTrue(Semver.create(">=1.0.2 <2.1.2").matches("1.0.3"));
    assertTrue(Semver.create(">=1.0.2 <2.1.2").matches("1.1"));
    assertTrue(Semver.create(">=1.0.2 <2.1.2").matches("2.1.1"));

    assertTrue(Semver.create(">=1.0.2 <2.1.2").matches("2.1.1"));

    assertFalse(Semver.create(">1.0.2 <=2.3.4").matches("1.0.2"));
    assertTrue(Semver.create(">=1.0.2 <=2.3.4").matches("1.0.2"));
    assertTrue(Semver.create(">1.0.2 <=2.3.4").matches("1.0.3"));
    assertTrue(Semver.create(">1.0.2 <=2.3.4").matches("2.3.4"));
    assertTrue(Semver.create(">1.0.2 <=2.3.4").matches("1.5"));
    assertTrue(Semver.create(">1.0.2 <=2.3.4").matches("2.3.3"));
    assertFalse(Semver.create(">1.0.2 <=2.3.4").matches("2.3.5"));
  }

  @Test
  public void or() {
    assertEquals("1.3.4 || 1.3.5",
        Semver.create("1.3.4 || 1.3.5").toString());

    assertTrue(Semver.create("1.3.4 || 1.3.5").matches("1.3.4"));
    assertTrue(Semver.create("1.3.4 || 1.3.5").matches("1.3.5"));

    assertFalse(Semver.create("1.3.4 || 1.3.5").matches("1.3.6"));
    assertFalse(Semver.create("1.3.4 || 1.3.5").matches("1.3"));
  }

  @Test
  public void complex() {
    assertEquals("<1.0.0 || >=2.3.1 <2.4.5 || >=2.5.2 <3.0.0",
        Semver.create("<1.0.0 || >=2.3.1 <2.4.5 || >=2.5.2 <3.0.0")
            .toString());

    assertTrue(Semver.create(
        "<1.0.0 || >=2.3.1 <2.4.5 || >=2.5.2 <3.0.0").matches("0.5"));

    assertFalse(Semver.create(
        "<1.0.0 || >=2.3.1 <2.4.5 || >=2.5.2 <3.0.0").matches("1.0.5"));

    assertTrue(Semver.create(
        "<1.0.0 || >=2.3.1 <2.4.5 || >=2.5.2 <3.0.0").matches("2.3.1"));

    assertTrue(Semver.create(
        "<1.0.0 || >=2.3.1 <2.4.5 || >=2.5.2 <3.0.0").matches("2.4.4"));

    assertFalse(Semver.create(
        "<1.0.0 || >=2.3.1 <2.4.5 || >=2.5.2 <3.0.0").matches("2.3.0"));
    assertFalse(Semver.create(
        "<1.0.0 || >=2.3.1 <2.4.5 || >=2.5.2 <3.0.0").matches("2.4.6"));
    assertFalse(Semver.create(
        "<1.0.0 || >=2.3.1 <2.4.5 || >=2.5.2 <3.0.0").matches("2.5.0"));

    assertTrue(Semver.create(
        "<1.0.0 || >=2.3.1 <2.4.5 || >=2.5.2 <3.0.0").matches("2.5.2"));
    assertTrue(Semver.create(
        "<1.0.0 || >=2.3.1 <2.4.5 || >=2.5.2 <3.0.0").matches("2.5.5"));
    assertTrue(Semver.create(
        "<1.0.0 || >=2.3.1 <2.4.5 || >=2.5.2 <3.0.0").matches("2.9"));
    assertFalse(Semver.create(
        "<1.0.0 || >=2.3.1 <2.4.5 || >=2.5.2 <3.0.0").matches("3"));
  }

  @Test
  public void semver() {
    assertTrue(Semver.create("<1.0.0-alpha").matches("1.0.0-alpha.1"));
    assertTrue(Semver.create("<1.0.0-beta.2").matches("1.0.0-alpha"));
    assertTrue(Semver.create("<1.0.0-beta.2").matches("1.0.0-alpha.1"));
    assertTrue(Semver.create("<1.0.0-beta.11").matches("1.0.0-beta.2"));
    assertTrue(Semver.create("<1.0.0-rc.1").matches("1.0.0-beta.11"));
    assertTrue(Semver.create("<1.0.0-rc.1+build.1").matches("1.0.0-rc.1"));
    assertTrue(Semver.create("<1.0.0").matches("1.0.0-rc.1+build.1"));
    assertTrue(Semver.create("<1.0.0+0.3.7").matches("1.0.0"));
    assertTrue(Semver.create("<1.3.7+build").matches("1.0.0+0.3.7"));
    assertTrue(Semver.create("<1.3.7+build.2.b8f12d7").matches("1.3.7+build"));
    assertTrue(Semver.create("<1.3.7+build.11.e0f985a").matches("1.3.7+build"));
    assertTrue(Semver.create("<1.3.7+build.11.e0f985a").matches("1.3.7+build.2.b8f12d7"));
  }
}
