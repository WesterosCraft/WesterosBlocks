package com.westerosblocks.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BlockTagDefinition {
    @SerializedName("blockTags")
    private List<BlockTagEntry> blockTags;

    public List<BlockTagEntry> getBlockTags() {
        return blockTags != null ? blockTags : new ArrayList<>();
    }

    public boolean hasBlockTags() {
        return blockTags != null && !blockTags.isEmpty();
    }
}
