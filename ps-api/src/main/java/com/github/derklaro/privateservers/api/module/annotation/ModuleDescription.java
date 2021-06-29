/*
 * This file is part of ps-system, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020-2021 Pasqual K. and contributors
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

package com.github.derklaro.privateservers.api.module.annotation;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ModuleDescription {

  private final String id;
  private final String displayName;
  private final String version;
  private final String website;
  private final String description;
  private final String mainClass;
  private final String[] authors;

  public ModuleDescription(String id, String displayName, String version, String website, String description, String mainClass, String[] authors) {
    this.id = id;
    this.displayName = displayName;
    this.version = version;
    this.website = website;
    this.description = description;
    this.mainClass = mainClass;
    this.authors = authors;
  }

  public static ModuleDescription from(Module annotation, String mainClass) {
    return new ModuleDescription(annotation.id(), annotation.displayName(), annotation.version(),
      annotation.website(), annotation.description(), mainClass, annotation.authors());
  }

  public String getId() {
    return this.id;
  }

  public String getDisplayName() {
    return this.displayName;
  }

  public String getVersion() {
    return this.version;
  }

  public String getWebsite() {
    return this.website;
  }

  public String getDescription() {
    return this.description;
  }

  public String getMainClass() {
    return this.mainClass;
  }

  public String[] getAuthors() {
    return this.authors;
  }
}
