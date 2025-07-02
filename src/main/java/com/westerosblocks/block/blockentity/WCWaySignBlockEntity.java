package com.westerosblocks.block.blockentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class WCWaySignBlockEntity extends BlockEntity {
    
    private static final String TEXT_KEY = "Text";
    private static final String FACING_KEY = "Facing";
    private static final String WOOD_TYPE_KEY = "WoodType";
    
    private List<Text> text = new ArrayList<>();
    private Direction facing = Direction.NORTH;
    private String woodType = "oak";
    
    public WCWaySignBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WAY_SIGN_BLOCK_ENTITY, pos, state);
    }
    
    public void setText(List<Text> text) {
        this.text = new ArrayList<>(text);
        this.markDirty();
        if (this.world != null) {
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), 3);
        }
    }
    
    public List<Text> getText() {
        return this.text;
    }
    
    public void setFacing(Direction facing) {
        this.facing = facing;
        this.markDirty();
        if (this.world != null) {
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), 3);
        }
    }
    
    public Direction getFacing() {
        return this.facing;
    }
    
    public void setWoodType(String woodType) {
        this.woodType = woodType;
        this.markDirty();
        if (this.world != null) {
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), 3);
        }
    }
    
    public String getWoodType() {
        return this.woodType;
    }
    
    @Override
    public void readNbt(NbtCompound nbt, net.minecraft.registry.RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        
        this.text.clear();
        if (nbt.contains(TEXT_KEY, 9)) {
            var textList = nbt.getList(TEXT_KEY, 8);
            for (int i = 0; i < textList.size(); ++i) {
                String textString = textList.getString(i);
                this.text.add(Text.literal(textString));
            }
        }
        
        if (nbt.contains(FACING_KEY, 3)) {
            this.facing = Direction.byId(nbt.getInt(FACING_KEY));
        }
        
        if (nbt.contains(WOOD_TYPE_KEY, 8)) {
            this.woodType = nbt.getString(WOOD_TYPE_KEY);
        }
    }
    
    @Override
    public void writeNbt(NbtCompound nbt, net.minecraft.registry.RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        
        var textList = new net.minecraft.nbt.NbtList();
        for (Text textLine : this.text) {
            textList.add(net.minecraft.nbt.NbtString.of(textLine.getString()));
        }
        nbt.put(TEXT_KEY, textList);
        
        nbt.putInt(FACING_KEY, this.facing.getId());
        nbt.putString(WOOD_TYPE_KEY, this.woodType);
    }
    
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
    
    @Override
    public NbtCompound toInitialChunkDataNbt(net.minecraft.registry.RegistryWrapper.WrapperLookup registryLookup) {
        return this.createNbt(registryLookup);
    }
    
    public static void tick(World world, BlockPos pos, BlockState state, WCWaySignBlockEntity blockEntity) {
        // Tick logic if needed
    }
} 