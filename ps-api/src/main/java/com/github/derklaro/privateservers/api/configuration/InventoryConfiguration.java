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
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;
import java.util.List;

@ToString
@EqualsAndHashCode
public class InventoryConfiguration {

  private int inventorySize;
  private String inventoryTitle;

  public InventoryConfiguration(int inventorySize, String inventoryTitle) {
    this.inventorySize = inventorySize;
    this.inventoryTitle = inventoryTitle;
  }

  public int getInventorySize() {
    return this.inventorySize;
  }

  public void setInventorySize(int inventorySize) {
    this.inventorySize = inventorySize;
  }

  public String getInventoryTitle() {
    return this.inventoryTitle;
  }

  public void setInventoryTitle(String inventoryTitle) {
    this.inventoryTitle = inventoryTitle;
  }

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

  @ToString
  @EqualsAndHashCode(callSuper = true)
  public static class ServiceTypeStartInventory extends InventoryConfiguration {

    private Collection<ServiceItemMapping> serviceItems;

    public ServiceTypeStartInventory(int inventorySize, String inventoryTitle, Collection<ServiceItemMapping> serviceItems) {
      super(inventorySize, inventoryTitle);
      this.serviceItems = serviceItems;
    }

    public Collection<ServiceItemMapping> getServiceItems() {
      return this.serviceItems;
    }

    public void setServiceItems(Collection<ServiceItemMapping> serviceItems) {
      this.serviceItems = serviceItems;
    }
  }

  @ToString
  @EqualsAndHashCode
  public static class ServiceItemMapping {

    private String groupName;
    private String templateName;
    private String templateBackend;
    private boolean copyAfterStop;
    private boolean deleteOnOwnerLeave;
    private int maxIdleSeconds;
    private ItemLayout itemLayout;
    private transient CloudServiceTemplate template;

    public ServiceItemMapping(String groupName, String templateName, String templateBackend,
                              boolean copyAfterStop, int maxIdleSeconds, boolean deleteOnOwnerLeave, ItemLayout itemLayout) {
      this.groupName = groupName;
      this.templateName = templateName;
      this.templateBackend = templateBackend;
      this.copyAfterStop = copyAfterStop;
      this.maxIdleSeconds = maxIdleSeconds;
      this.deleteOnOwnerLeave = deleteOnOwnerLeave;
      this.itemLayout = itemLayout;
    }

    public String getGroupName() {
      return this.groupName;
    }

    public void setGroupName(String groupName) {
      this.groupName = groupName;
    }

    public String getTemplateName() {
      return this.templateName;
    }

    public void setTemplateName(String templateName) {
      this.templateName = templateName;
    }

    public String getTemplateBackend() {
      return this.templateBackend;
    }

    public void setTemplateBackend(String templateBackend) {
      this.templateBackend = templateBackend;
    }

    public boolean isCopyAfterStop() {
      return this.copyAfterStop;
    }

    public void setCopyAfterStop(boolean copyAfterStop) {
      this.copyAfterStop = copyAfterStop;
    }

    public boolean isDeleteOnOwnerLeave() {
      return this.deleteOnOwnerLeave;
    }

    public void setDeleteOnOwnerLeave(boolean deleteOnOwnerLeave) {
      this.deleteOnOwnerLeave = deleteOnOwnerLeave;
    }

    public int getMaxIdleSeconds() {
      return this.maxIdleSeconds;
    }

    public void setMaxIdleSeconds(int maxIdleSeconds) {
      this.maxIdleSeconds = maxIdleSeconds;
    }

    public ItemLayout getItemLayout() {
      return this.itemLayout;
    }

    public void setItemLayout(ItemLayout itemLayout) {
      this.itemLayout = itemLayout;
    }

    public CloudServiceTemplate toTemplate() {
      if (this.template == null) {
        this.template = CloudServiceTemplate.builder()
          .templateName(this.templateName)
          .templateBackend(this.templateBackend)
          .build();
      }
      return this.template;
    }
  }

  @ToString
  @EqualsAndHashCode
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
