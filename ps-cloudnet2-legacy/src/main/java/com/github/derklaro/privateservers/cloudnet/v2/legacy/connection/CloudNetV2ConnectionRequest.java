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

package com.github.derklaro.privateservers.cloudnet.v2.legacy.connection;

import com.github.derklaro.privateservers.api.cloud.service.CloudService;
import com.github.derklaro.privateservers.common.cloud.util.DefaultConnectionRequest;
import com.google.common.base.Preconditions;
import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.utility.document.Document;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CloudNetV2ConnectionRequest extends DefaultConnectionRequest {

  protected CloudNetV2ConnectionRequest(CloudService targetService, UUID targetPlayer) {
    super(targetService, targetPlayer);
  }

  public static CloudNetV2ConnectionRequest of(@NotNull CloudService targetService, @NotNull UUID player) {
    Preconditions.checkNotNull(targetService, "targetService");
    Preconditions.checkNotNull(player, "player");

    return new CloudNetV2ConnectionRequest(targetService, player);
  }

  @Override
  public void fire() {
    CloudAPI.getInstance().sendCustomSubProxyMessage(
      "cloudnet_internal",
      "sendPlayer",
      new Document("uniqueId", super.getTargetPlayer()).append("server", super.getTargetService().getName())
    );
  }
}
