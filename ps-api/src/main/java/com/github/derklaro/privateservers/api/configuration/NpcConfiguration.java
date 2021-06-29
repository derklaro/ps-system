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

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class NpcConfiguration {

  private int actionDistance;
  private int spawnDistance;
  private int tablistRemovalTicks;

  private boolean imitatePlayer;
  private boolean lookAtPlayer;

  private LabyModConfiguration labyModConfiguration;
  private KnockbackConfiguration knockbackConfiguration;

  public NpcConfiguration() {
    this.actionDistance = 20;
    this.spawnDistance = 50;
    this.tablistRemovalTicks = 30;

    this.imitatePlayer = true;
    this.lookAtPlayer = true;

    this.labyModConfiguration = new LabyModConfiguration();
    this.knockbackConfiguration = new KnockbackConfiguration();
  }

  public int getActionDistance() {
    return this.actionDistance;
  }

  public void setActionDistance(int actionDistance) {
    this.actionDistance = actionDistance;
  }

  public int getSpawnDistance() {
    return this.spawnDistance;
  }

  public void setSpawnDistance(int spawnDistance) {
    this.spawnDistance = spawnDistance;
  }

  public int getTablistRemovalTicks() {
    return this.tablistRemovalTicks;
  }

  public void setTablistRemovalTicks(int tablistRemovalTicks) {
    this.tablistRemovalTicks = tablistRemovalTicks;
  }

  public boolean isImitatePlayer() {
    return this.imitatePlayer;
  }

  public void setImitatePlayer(boolean imitatePlayer) {
    this.imitatePlayer = imitatePlayer;
  }

  public boolean isLookAtPlayer() {
    return this.lookAtPlayer;
  }

  public void setLookAtPlayer(boolean lookAtPlayer) {
    this.lookAtPlayer = lookAtPlayer;
  }

  public LabyModConfiguration getLabyModConfiguration() {
    return this.labyModConfiguration;
  }

  public void setLabyModConfiguration(LabyModConfiguration labyModConfiguration) {
    this.labyModConfiguration = labyModConfiguration;
  }

  public KnockbackConfiguration getKnockbackConfiguration() {
    return this.knockbackConfiguration;
  }

  public void setKnockbackConfiguration(KnockbackConfiguration knockbackConfiguration) {
    this.knockbackConfiguration = knockbackConfiguration;
  }

  @ToString
  @EqualsAndHashCode
  public static class LabyModConfiguration {

    private int[] emoteIds;
    private int emoteDelayTicks;

    public LabyModConfiguration() {
      this.emoteIds = new int[]{2, 49};
      this.emoteDelayTicks = 20 * 20;
    }

    public int[] getEmoteIds() {
      return this.emoteIds;
    }

    public void setEmoteIds(int[] emoteIds) {
      this.emoteIds = emoteIds;
    }

    public int getEmoteDelayTicks() {
      return this.emoteDelayTicks;
    }

    public void setEmoteDelayTicks(int emoteDelayTicks) {
      this.emoteDelayTicks = emoteDelayTicks;
    }
  }

  @ToString
  @EqualsAndHashCode
  public static class KnockbackConfiguration {

    private boolean enabled;
    private String bypassPermission;
    private double knockbackDistance;
    private double knockbackStrength;

    public KnockbackConfiguration() {
      this.enabled = true;
      this.bypassPermission = "ps.npc.knockback.bypass";
      this.knockbackDistance = 1.0;
      this.knockbackStrength = 0.8;
    }

    public boolean isEnabled() {
      return this.enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public String getBypassPermission() {
      return this.bypassPermission;
    }

    public void setBypassPermission(String bypassPermission) {
      this.bypassPermission = bypassPermission;
    }

    public double getKnockbackDistance() {
      return this.knockbackDistance;
    }

    public void setKnockbackDistance(double knockbackDistance) {
      this.knockbackDistance = knockbackDistance;
    }

    public double getKnockbackStrength() {
      return this.knockbackStrength;
    }

    public void setKnockbackStrength(double knockbackStrength) {
      this.knockbackStrength = knockbackStrength;
    }
  }
}
