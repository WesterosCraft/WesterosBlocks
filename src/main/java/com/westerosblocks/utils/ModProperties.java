package com.westerosblocks.utils;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;

public class ModProperties {
    public static final IntProperty CONNECTSTATE = IntProperty.of("connectstate", 0, 3);
    public static BooleanProperty SYMMETRICAL = BooleanProperty.of("symmetrical");

}
