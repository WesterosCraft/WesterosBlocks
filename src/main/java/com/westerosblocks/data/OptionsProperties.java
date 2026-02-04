package com.westerosblocks.data;

import com.google.gson.annotations.SerializedName;

/**
 * Represents options-specific properties for blocks.
 * Uses a structured object format that supports typed properties.
 *
 * Example JSON:
 * <pre>
 * {"options": {"unconnect": false, "noUvlock": true}}
 * {"options": {"barsModel": true, "legacyModel": true}}
 * {"options": {"plantId": "blue_bells"}}
 * </pre>
 */
public class OptionsProperties {

    // Stair/Wall/Fence/Pane connection properties
    @SerializedName("unconnect")
    private Boolean unconnect;

    @SerializedName("connectstate")
    private Boolean connectstate;

    @SerializedName("noUvlock")
    private Boolean noUvlock;

    // Pane model properties
    @SerializedName("barsModel")
    private Boolean barsModel;

    @SerializedName("legacyModel")
    private Boolean legacyModel;

    // Leaves properties
    @SerializedName("noDecay")
    private Boolean noDecay;

    @SerializedName("betterFoliage")
    private Boolean betterFoliage;

    @SerializedName("overlay")
    private Boolean overlay;

    // Torch/Ladder properties
    @SerializedName("allowUnsupported")
    private Boolean allowUnsupported;

    @SerializedName("noParticle")
    private Boolean noParticle;

    // Door/Gate properties
    @SerializedName("locked")
    private Boolean locked;

    // Furnace properties
    @SerializedName("alwaysOn")
    private Boolean alwaysOn;

    // Flowerpot properties
    @SerializedName("plantId")
    private String plantId;

    // Web properties
    @SerializedName("noInWeb")
    private Boolean noInWeb;

    // Vines properties
    @SerializedName("noClimb")
    private Boolean noClimb;

    // Common properties
    @SerializedName("toggleOnUse")
    private Boolean toggleOnUse;

    @SerializedName("layerSensitive")
    private Boolean layerSensitive;

    @SerializedName("symmetrical")
    private Boolean symmetrical;

    // Stack block properties (cuboid-nsew-stack)
    @SerializedName("noBreakUnder")
    private Boolean noBreakUnder;

    @SerializedName("allowHalfBreak")
    private Boolean allowHalfBreak;

    // Vines properties (additional)
    @SerializedName("hasClimb")
    private Boolean hasClimb;

    @SerializedName("hasDown")
    private Boolean hasDown;

    // Wall properties
    @SerializedName("wallSize")
    private String wallSize;

    // Bed properties
    @SerializedName("bedType")
    private String bedType;

    // Render properties
    @SerializedName("rotateRandom")
    private Boolean rotateRandom;

    // Getters
    public Boolean getUnconnect() {
        return unconnect;
    }

    public Boolean getConnectstate() {
        return connectstate;
    }

    public Boolean getNoUvlock() {
        return noUvlock;
    }

    public Boolean getBarsModel() {
        return barsModel;
    }

    public Boolean getLegacyModel() {
        return legacyModel;
    }

    public Boolean getNoDecay() {
        return noDecay;
    }

    public Boolean getBetterFoliage() {
        return betterFoliage;
    }

    public Boolean getOverlay() {
        return overlay;
    }

    public Boolean getAllowUnsupported() {
        return allowUnsupported;
    }

    public Boolean getNoParticle() {
        return noParticle;
    }

    public Boolean getLocked() {
        return locked;
    }

    public Boolean getAlwaysOn() {
        return alwaysOn;
    }

    public String getPlantId() {
        return plantId;
    }

    public Boolean getNoInWeb() {
        return noInWeb;
    }

    public Boolean getNoClimb() {
        return noClimb;
    }

    public Boolean getToggleOnUse() {
        return toggleOnUse;
    }

    public Boolean getLayerSensitive() {
        return layerSensitive;
    }

    public Boolean getSymmetrical() {
        return symmetrical;
    }

    public Boolean getNoBreakUnder() {
        return noBreakUnder;
    }

    public Boolean getAllowHalfBreak() {
        return allowHalfBreak;
    }

    public Boolean getHasClimb() {
        return hasClimb;
    }

    public Boolean getHasDown() {
        return hasDown;
    }

    public String getWallSize() {
        return wallSize;
    }

    public String getBedType() {
        return bedType;
    }

    public Boolean getRotateRandom() {
        return rotateRandom;
    }

    // Setters (needed for deserializer and BlockSetExpander)
    public void setUnconnect(Boolean unconnect) {
        this.unconnect = unconnect;
    }

    public void setConnectstate(Boolean connectstate) {
        this.connectstate = connectstate;
    }

    public void setNoUvlock(Boolean noUvlock) {
        this.noUvlock = noUvlock;
    }

    public void setBarsModel(Boolean barsModel) {
        this.barsModel = barsModel;
    }

    public void setLegacyModel(Boolean legacyModel) {
        this.legacyModel = legacyModel;
    }

    public void setNoDecay(Boolean noDecay) {
        this.noDecay = noDecay;
    }

    public void setBetterFoliage(Boolean betterFoliage) {
        this.betterFoliage = betterFoliage;
    }

    public void setOverlay(Boolean overlay) {
        this.overlay = overlay;
    }

    public void setAllowUnsupported(Boolean allowUnsupported) {
        this.allowUnsupported = allowUnsupported;
    }

    public void setNoParticle(Boolean noParticle) {
        this.noParticle = noParticle;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public void setAlwaysOn(Boolean alwaysOn) {
        this.alwaysOn = alwaysOn;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public void setNoInWeb(Boolean noInWeb) {
        this.noInWeb = noInWeb;
    }

    public void setNoClimb(Boolean noClimb) {
        this.noClimb = noClimb;
    }

    public void setToggleOnUse(Boolean toggleOnUse) {
        this.toggleOnUse = toggleOnUse;
    }

    public void setLayerSensitive(Boolean layerSensitive) {
        this.layerSensitive = layerSensitive;
    }

    public void setSymmetrical(Boolean symmetrical) {
        this.symmetrical = symmetrical;
    }

    public void setNoBreakUnder(Boolean noBreakUnder) {
        this.noBreakUnder = noBreakUnder;
    }

    public void setAllowHalfBreak(Boolean allowHalfBreak) {
        this.allowHalfBreak = allowHalfBreak;
    }

    public void setHasClimb(Boolean hasClimb) {
        this.hasClimb = hasClimb;
    }

    public void setHasDown(Boolean hasDown) {
        this.hasDown = hasDown;
    }

    public void setWallSize(String wallSize) {
        this.wallSize = wallSize;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public void setRotateRandom(Boolean rotateRandom) {
        this.rotateRandom = rotateRandom;
    }
}
