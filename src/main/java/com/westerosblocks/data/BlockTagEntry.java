package com.westerosblocks.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BlockTagEntry {
    @SerializedName("customTag")
    private String customTag;

    @SerializedName("blockNames")
    private List<String> blockNames;

    public String getCustomTag() {
        return customTag;
    }

    public List<String> getBlockNames() {
        return blockNames != null ? blockNames : new ArrayList<>();
    }
}
