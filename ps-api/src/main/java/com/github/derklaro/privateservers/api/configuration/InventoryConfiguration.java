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

package com.github.derklaro.privateservers.api.configuration;

import com.github.derklaro.privateservers.api.cloud.service.template.CloudServiceTemplate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;
import java.util.List;

/**
 * Represents the main configuration file for all inventories used in the system.
 */
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class InventoryConfiguration {

  private int inventorySize;
  private String inventoryTitle;

  @Getter
  @Setter
  @ToString
  @EqualsAndHashCode(callSuper = true)
  public static class MainMenuConfiguration extends InventoryConfiguration {

    private ItemLayout startServerLayout;
    private ItemLayout stopServerLayout;
    private ItemLayout joinServerLayout;
    private ItemLayout publicServerListLayout;

    public MainMenuConfiguration(int inventorySize, String inventoryTitle, ItemLayout startServerLayout,
                                 ItemLayout stopServerLayout, ItemLayout joinServerLayout, ItemLayout publicServerListLayout) {
      super(inventorySize, inventoryTitle);
      this.startServerLayout = startServerLayout;
      this.stopServerLayout = stopServerLayout;
      this.joinServerLayout = joinServerLayout;
      this.publicServerListLayout = publicServerListLayout;
    }
  }

  @Getter
  @Setter
  @ToString
  @EqualsAndHashCode(callSuper = true)
  public static class PublicServerListConfiguration extends InventoryConfiguration {

    private ItemLayout openServerLayout;
    private ItemLayout serverWithWhitelistLayout;

    public PublicServerListConfiguration(int inventorySize, String inventoryTitle, ItemLayout openServerLayout,
                                         ItemLayout serverWithWhitelistLayout) {
      super(inventorySize, inventoryTitle);
      this.openServerLayout = openServerLayout;
      this.serverWithWhitelistLayout = serverWithWhitelistLayout;
    }
  }

  @Getter
  @Setter
  @ToString
  @EqualsAndHashCode(callSuper = true)
  public static class ServiceTypeStartInventory extends InventoryConfiguration {

    private Collection<ServiceItemMapping> serviceItems;

    public ServiceTypeStartInventory(int inventorySize, String inventoryTitle, Collection<ServiceItemMapping> serviceItems) {
      super(inventorySize, inventoryTitle);
      this.serviceItems = serviceItems;
    }
  }

  @Data
  @ToString
  @EqualsAndHashCode
  @AllArgsConstructor
  public static class ServiceItemMapping {

    private String groupName;
    private String templateName;
    private String templateBackend;
    private boolean copyAfterStop;
    private int maxIdleSeconds;
    private boolean deleteOnOwnerLeave;
    private ItemLayout itemLayout;

    public CloudServiceTemplate toTemplate() {
      return CloudServiceTemplate.builder()
        .templateName(this.templateName)
        .templateBackend(this.templateBackend)
        .build();
    }
  }

  @Data
  @ToString
  @EqualsAndHashCode
  @AllArgsConstructor
  public static class ItemLayout {

    private int slot;
    private String usePermission;

    private String material;
    private int subId;

    private String displayName;
    private List<String> lore;
  }
}
