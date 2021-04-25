/*
 * This file is part of ps-system, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 - 2021 Pasqual Koschmieder and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.derklaro.privateservers.api.translation;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.translation.Translator;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TranslationManager {

  protected static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
  private static final int PROPERTIES_EXTENSION_LENGTH = ".properties".length();
  private static final Path TRANSLATION_PATH = Paths.get("plugins/ps/translations");

  private final TranslationRegistry translationRegistry;
  private final Set<Locale> loadedTranslations = ConcurrentHashMap.newKeySet();

  public TranslationManager() {
    this.translationRegistry = TranslationRegistry.create(Key.key("ps", "main"));
    this.translationRegistry.defaultLocale(DEFAULT_LOCALE);

    this.loadDefault();
    this.loadCustom();

    GlobalTranslator.get().addSource(this.translationRegistry);
  }

  protected void loadDefault() {
    ResourceBundle bundle = ResourceBundle.getBundle("ps", DEFAULT_LOCALE, UTF8ResourceBundleControl.get());
    this.translationRegistry.registerAll(DEFAULT_LOCALE, bundle, false);
  }

  protected void loadCustom() {
    if (Files.notExists(TRANSLATION_PATH)) {
      try {
        Files.createDirectories(TRANSLATION_PATH);
      } catch (IOException exception) {
        exception.printStackTrace();
      }
      return;
    }

    Map<Locale, ResourceBundle> loaded = new HashMap<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(TRANSLATION_PATH, "*.properties")) {
      for (Path path : stream) {
        Map.Entry<Locale, ResourceBundle> result = this.loadTranslationFile(path);
        if (result != null) {
          loaded.put(result.getKey(), result.getValue());
        }
      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }

    loaded.forEach((locale, bundle) -> {
      Locale localeCountryLess = new Locale(locale.getLanguage());
      if (!locale.equals(localeCountryLess) && !localeCountryLess.equals(DEFAULT_LOCALE) && this.loadedTranslations.add(localeCountryLess)) {
        this.translationRegistry.registerAll(localeCountryLess, bundle, false);
      }
    });
  }

  protected @Nullable Map.Entry<Locale, ResourceBundle> loadTranslationFile(@NotNull Path translationFile) {
    String fileName = translationFile.getFileName().toString();
    String localeString = fileName.substring(0, fileName.length() - PROPERTIES_EXTENSION_LENGTH);

    Locale locale = Translator.parseLocale(localeString);
    if (locale == null) {
      throw new RuntimeException("Unknown locale " + localeString + " from file " + translationFile);
    }

    PropertyResourceBundle bundle;
    try (Reader reader = Files.newBufferedReader(translationFile, StandardCharsets.UTF_8)) {
      bundle = new PropertyResourceBundle(reader);
    } catch (IOException exception) {
      throw new RuntimeException("Exception while reading language file " + translationFile, exception);
    }

    this.translationRegistry.registerAll(locale, bundle, false);
    this.loadedTranslations.add(locale);

    return new AbstractMap.SimpleEntry<>(locale, bundle);
  }
}
