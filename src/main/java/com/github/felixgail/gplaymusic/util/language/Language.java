package com.github.felixgail.gplaymusic.util.language;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;

public class Language {

  private final static Locale defaultLocale = Locale.US;
  private final static Properties defaultLang;

  private static Locale currentLocale;
  private static Properties currentLang;

  static {
    currentLocale = defaultLocale;
    try {
      Properties lang = getNewLanguage(defaultLocale);
      currentLang = lang;
    } catch (IOException | NullPointerException e) {
      Logger.getGlobal().warning(String.format("Default language file (%s) not found.",
          defaultLocale.toString()));
      currentLang = new Properties();
    }
    defaultLang = currentLang;
  }

  private static Properties getNewLanguage(Locale locale) throws IOException {
    Properties lang = new Properties();
    InputStream in = Language.class
        .getClassLoader()
        .getResourceAsStream("lang/" + locale.toString());
    InputStreamReader reader = new InputStreamReader(in, Charset.forName("UTF-8"));
    lang.load(reader);
    in.close();
    return lang;
  }

  public static void setLocale(Locale locale) {
    if (!locale.equals(currentLocale)) {
      if (locale.equals(defaultLocale)) {
        currentLocale = defaultLocale;
        currentLang = defaultLang;
      } else {
        try {
          currentLang = getNewLanguage(locale);
          currentLocale = locale;
        } catch (IOException | NullPointerException e) {
          Logger.getGlobal().warning(
              String.format(Language.get("language.FileNotFound"),
                  locale.toString()));
        }
      }
    }
  }

  public static void setDefault() {
    currentLocale = defaultLocale;
    currentLang = defaultLang;
  }

  public static String get(String key) {
    if (currentLang.containsKey(key)) {
      return currentLang.getProperty(key);
    }
    Logger.getGlobal().warning(
        String.format(Language.get("language.KeyNotFoundCurrent"),
            currentLocale.toString(), key));
    if (defaultLang.containsKey(key)) {
      return defaultLang.getProperty(key);
    }
    Logger.getGlobal().warning(
        String.format(Language.get("language.KeyNotFoundDefault"),
            defaultLocale.toString(), key));
    return String.format("{%s}", key);
  }
}
