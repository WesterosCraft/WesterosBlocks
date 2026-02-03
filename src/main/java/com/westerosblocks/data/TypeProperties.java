package com.westerosblocks.data;

import com.google.gson.annotations.SerializedName;

/**
 * Represents type-specific properties for blocks.
 * This class replaces the legacy comma-separated string format for the "type" field
 * with a structured object format that supports typed properties.
 *
 * Example JSON (new format):
 * <pre>
 * {"type": {"unconnect": false, "noUvlock": true}}
 * {"type": {"barsModel": true, "legacyModel": true}}
 * {"type": {"plantId": "blue_bells"}}
 * </pre>
 *
 * Legacy format still supported via deserializer:
 * <pre>
 * {"type": "unconnect:false,no-uvlock"}
 * {"type": "bars-model,legacy-model"}
 * {"type": "plant-id:blue_bells"}
 * </pre>
 */
public class TypeProperties {

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
}
