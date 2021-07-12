package com.westeroscraft.westerosblocks.properties;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import net.minecraft.block.properties.PropertyHelper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class PropertyMeta extends PropertyHelper<Integer>
{
    private final ImmutableSet<Integer> allowedValues;
    private static final Integer[] VALUES = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };

    protected PropertyMeta(String name, List<Integer> valid_meta)
    {
        super(name, Integer.class);

        Set<Integer> set = Sets.<Integer>newHashSet();
        for (Integer v : valid_meta) {
            if ((v < 0) || (v > 15)) {
                throw new IllegalArgumentException("Invalid meta value");
            }
            set.add(VALUES[v]);
        }
        this.allowedValues = ImmutableSet.copyOf(set);
    }

    public Collection<Integer> getAllowedValues()
    {
        return this.allowedValues;
    }

    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        else if (obj instanceof PropertyMeta && super.equals(obj))
        {
            PropertyMeta propertymeta = (PropertyMeta)obj;
            return this.allowedValues.equals(propertymeta.allowedValues);
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return 31 * super.hashCode() + this.allowedValues.hashCode();
    }

    public static PropertyMeta create(String name, List<Integer> valid_vals)
    {
        return new PropertyMeta(name, valid_vals);
    }

    public Optional<Integer> parseValue(String value)
    {
        try
        {
            Integer integer = Integer.valueOf(value);
            return this.allowedValues.contains(integer) ? Optional.of(VALUES[integer]) : Optional.<Integer>absent();
        }
        catch (NumberFormatException var3)
        {
            return Optional.<Integer>absent();
        }
    }

    public String getName(Integer value)
    {
        return value.toString();
    }
    
    public Integer fromMeta(int m) {
    	return VALUES[m];
    }
}