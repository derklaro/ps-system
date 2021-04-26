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
package com.github.derklaro.privateservers.lobby.dependencies;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ProtocolLibInstaller {

  private static final Path TARGET_PATH = Paths.get("plugins/ProtocolLib.jar");

  private static final String MAIN_DOWNLOAD_URL = "https://ci.dmulloy2.net/job/ProtocolLib/lastSuccessfulBuild/artifact/target/ProtocolLib.jar";
  private static final String FALLBACK_DOWNLOAD_URL = "https://github.com/dmulloy2/ProtocolLib/releases/latest/download/ProtocolLib.jar";

  public boolean installProtocolLib() {
    if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
      return true;
    } else {
      if (this.downloadProtocolLib()) {
        try {
          return Bukkit.getPluginManager().loadPlugin(TARGET_PATH.toFile()) != null;
        } catch (Exception ignored) {
        }
      }
      return false;
    }
  }

  protected boolean downloadProtocolLib() {
    if (this.download(MAIN_DOWNLOAD_URL, TARGET_PATH)) {
      return true;
    }
    // try to download from the fallback github release
    return this.download(FALLBACK_DOWNLOAD_URL, TARGET_PATH);
  }

  protected boolean download(@NotNull String url, @NotNull Path target) {
    try {
      HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
      connection.setRequestProperty("User-Agent",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
      connection.setUseCaches(false);
      connection.setReadTimeout(5000);
      connection.setReadTimeout(5000);
      connection.connect();

      if (connection.getResponseCode() == 200) {
        try (InputStream inputStream = connection.getInputStream()) {
          Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
          return true;
        }
      }
    } catch (Exception ignored) {
    }
    return false;
  }
}
