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
package com.github.derklaro.privateservers.api.configuration;

import java.util.List;

public class InventoryConfiguration {

  private int inventorySize;

  public InventoryConfiguration(int inventorySize) {
    this.inventorySize = inventorySize;
  }

  public int getInventorySize() {
    return this.inventorySize;
  }

  public void setInventorySize(int inventorySize) {
    this.inventorySize = inventorySize;
  }

  public static class MainMenuConfiguration extends InventoryConfiguration {

    private ItemLayout startServerLayout;
    private ItemLayout stopServerLayout;
    private ItemLayout joinServerLayout;
    private ItemLayout publicServerListLayout;

    public MainMenuConfiguration(int inventorySize, ItemLayout startServerLayout, ItemLayout stopServerLayout,
                                 ItemLayout joinServerLayout, ItemLayout publicServerListLayout) {
      super(inventorySize);
      this.startServerLayout = startServerLayout;
      this.stopServerLayout = stopServerLayout;
      this.joinServerLayout = joinServerLayout;
      this.publicServerListLayout = publicServerListLayout;
    }

    public ItemLayout getStartServerLayout() {
      return this.startServerLayout;
    }

    public void setStartServerLayout(ItemLayout startServerLayout) {
      this.startServerLayout = startServerLayout;
    }

    public ItemLayout getStopServerLayout() {
      return this.stopServerLayout;
    }

    public void setStopServerLayout(ItemLayout stopServerLayout) {
      this.stopServerLayout = stopServerLayout;
    }

    public ItemLayout getJoinServerLayout() {
      return this.joinServerLayout;
    }

    public void setJoinServerLayout(ItemLayout joinServerLayout) {
      this.joinServerLayout = joinServerLayout;
    }

    public ItemLayout getPublicServerListLayout() {
      return this.publicServerListLayout;
    }

    public void setPublicServerListLayout(ItemLayout publicServerListLayout) {
      this.publicServerListLayout = publicServerListLayout;
    }
  }

  public static class PublicServerListConfiguration extends InventoryConfiguration {

    private ItemLayout openServerLayout;
    private ItemLayout serverWithWhitelistLayout;

    public PublicServerListConfiguration(int inventorySize, ItemLayout openServerLayout, ItemLayout serverWithWhitelistLayout) {
      super(inventorySize);
      this.openServerLayout = openServerLayout;
      this.serverWithWhitelistLayout = serverWithWhitelistLayout;
    }

    public ItemLayout getOpenServerLayout() {
      return this.openServerLayout;
    }

    public void setOpenServerLayout(ItemLayout openServerLayout) {
      this.openServerLayout = openServerLayout;
    }

    public ItemLayout getServerWithWhitelistLayout() {
      return this.serverWithWhitelistLayout;
    }

    public void setServerWithWhitelistLayout(ItemLayout serverWithWhitelistLayout) {
      this.serverWithWhitelistLayout = serverWithWhitelistLayout;
    }
  }

  public static class ItemLayout {

    private int slot;
    private String usePermission;

    private String material;
    private int subId;

    private String displayName;
    private List<String> lore;

    public ItemLayout(int slot, String usePermission, String material, int subId, String displayName, List<String> lore) {
      this.slot = slot;
      this.usePermission = usePermission;
      this.material = material;
      this.subId = subId;
      this.displayName = displayName;
      this.lore = lore;
    }

    public int getSlot() {
      return this.slot;
    }

    public void setSlot(int slot) {
      this.slot = slot;
    }

    public String getUsePermission() {
      return this.usePermission;
    }

    public void setUsePermission(String usePermission) {
      this.usePermission = usePermission;
    }

    public String getMaterial() {
      return this.material;
    }

    public void setMaterial(String material) {
      this.material = material;
    }

    public int getSubId() {
      return this.subId;
    }

    public void setSubId(int subId) {
      this.subId = subId;
    }

    public String getDisplayName() {
      return this.displayName;
    }

    public void setDisplayName(String displayName) {
      this.displayName = displayName;
    }

    public List<String> getLore() {
      return this.lore;
    }

    public void setLore(List<String> lore) {
      this.lore = lore;
    }
  }
}
