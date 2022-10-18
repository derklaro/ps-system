/*
 * This file is part of ps-system, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020-2022 Pasqual K. and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.derklaro.privateservers.api.translation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.Translator;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * A renderer for components. Not part of the public api, don't feel safe when using one of these methods in your plugin.
 */
@ApiStatus.Internal
public final class ComponentRenderer {

  private ComponentRenderer() {
    throw new UnsupportedOperationException();
  }

  /**
   * Renders a component to a minecraft message string.
   *
   * @param component the component to render.
   * @return the rendered component as a string.
   */
  public static @NotNull String renderToString(@NotNull Component component) {
    return LegacyComponentSerializer.legacySection().serialize(render(component));
  }

  /**
   * Renders a component to a minecraft message string.
   *
   * @param component the component to render.
   * @param locale the locale to use while rendering.
   * @return the rendered component as a string.
   */
  public static @NotNull String renderToString(@NotNull Component component, @Nullable String locale) {
    return LegacyComponentSerializer.legacySection().serialize(render(component, locale));
  }

  /**
   * Renders a component to a minecraft message string.
   *
   * @param component the component to render.
   * @param locale the locale to use while rendering.
   * @return the rendered component as a string.
   */
  public static @NotNull String renderToString(@NotNull Component component, @Nullable Locale locale) {
    return LegacyComponentSerializer.legacySection().serialize(render(component, locale));
  }

  /**
   * Renders a component to a component.
   *
   * @param component the component to render.
   * @return the rendered component.
   */
  public static @NotNull Component render(@NotNull Component component) {
    return render(component, Locale.getDefault());
  }

  /**
   * Renders a component to a component.
   *
   * @param component the component to render.
   * @param locale the locale to use while rendering.
   * @return the rendered component.
   */
  public static @NotNull Component render(@NotNull Component component, @Nullable String locale) {
    return render(component, parseLocale(locale));
  }

  /**
   * Renders a component to a component.
   *
   * @param component the component to render.
   * @param locale the locale to use while rendering.
   * @return the rendered component.
   */
  public static @NotNull Component render(@NotNull Component component, @Nullable Locale locale) {
    if (locale == null) {
      locale = Locale.getDefault();
      if (locale == null) {
        locale = TranslationManager.DEFAULT_LOCALE;
      }
    }

    return GlobalTranslator.render(component, locale);
  }

  /**
   * Parses the input to a locale.
   *
   * @param input the locale input.
   * @return the parsed locale or null.
   */
  private static @Nullable Locale parseLocale(@Nullable String input) {
    return input == null ? null : Translator.parseLocale(input);
  }
}
