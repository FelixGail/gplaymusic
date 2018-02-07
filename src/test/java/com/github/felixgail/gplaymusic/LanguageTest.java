package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.util.language.Language;
import java.util.Locale;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LanguageTest {

  @Before
  public void resetLanguage() {
    Language.setDefault();
  }

  @Test
  public void testExistingKey() {
    Assert.assertEquals("Test string.", Language.get("test.TestString"));
  }

  @Test
  public void testNotExistingKey() {
    //Change if you should ever add this key.
    String key = "qeasldkfjoicjasdf.awek";
    Assert.assertEquals(String.format("{%s}", key), Language.get(key));
  }

  @Test
  public void testNotExistingLocale() {
    //Change if this locale is added.
    Locale locale = Locale.KOREAN;
    Assert.assertEquals("Test string.", Language.get("test.TestString"));
  }

  @Test
  public void testExistingLocale() {
    Language.setLocale(Locale.GERMANY);
    Assert.assertEquals("Test Zeichenkette.", Language.get("test.TestString"));
  }

  @Test
  public void testExistingInDefault() {
    Language.setLocale(Locale.GERMANY);
    Assert.assertEquals("default string.", Language.get("test.OnlyInUS"));
  }
}
