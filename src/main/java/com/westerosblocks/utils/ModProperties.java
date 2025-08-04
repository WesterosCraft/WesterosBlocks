package com.westerosblocks.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;

import java.util.*;

public class ModProperties {
    public static final IntProperty CONNECTSTATE = IntProperty.of("connectstate", 0, 3);
    public static final BooleanProperty SYMMETRICAL = BooleanProperty.of("symmetrical");

    public static final class StateProperty extends Property<String> {
        public final ImmutableList<String> values;
        public final ImmutableMap<String, String> valMap;
        public final String defValue;

        public StateProperty(List<String> stateIDs) {
            super("state", String.class);
            Map<String, String> map = Maps.newHashMap();
            List<String> vals = new ArrayList<>();
            for (String s : stateIDs) {
                map.put(s, s);
                vals.add(s);
            }
            this.values = ImmutableList.copyOf(vals);
            this.valMap = ImmutableMap.copyOf(map);
            this.defValue = stateIDs.getFirst();
        }

        @Override
        public Collection<String> getValues() {
            return this.values;
        }

        @Override
        public String name(String value) {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj instanceof StateProperty stateproperty && super.equals(obj)) {
                return this.values.equals(stateproperty.values);
            } else {
                return false;
            }
        }

        @Override
        public Optional<String> parse(String value) {
            return Optional.ofNullable(this.valMap.get(value));
        }

        public int getIndex(String val) {
            int v = this.values.indexOf(val);
            return Math.max(v, 0);
        }
    }

    public static final StateProperty STATE = new StateProperty(Arrays.asList("default", "on", "off"));

}
