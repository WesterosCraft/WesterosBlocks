package com.westeroscraft.westerosblocks.asm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.Opcodes;

import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class ClassTransformer implements IClassTransformer, Opcodes {
    @Override
    public byte[] transform(String name, String tname, byte[] bytes)
    {
        if (name.equals("aoi")) {   // Obfuscated BlockWall
            bytes = transformBlockWall(name, bytes, true);
        }
        else if (name.equals("net.minecraft.block.BlockWall")) {    // Clean name for BlockWall
            bytes = transformBlockWall(name, bytes, false);
        }
        else if (name.equals("akz")) {   // Obfuscated BlockFence
            bytes = transformBlockFence(name, bytes, true);
        }
        else if (name.equals("net.minecraft.block.BlockFence")) {    // Clean name for BlockFence
            bytes = transformBlockFence(name, bytes, false);
        }
        else if (name.equals("aoa")) {   // Obfuscated BlockPane
            bytes = transformBlockPane(name, bytes, true);
        }
        else if (name.equals("net.minecraft.block.BlockPane")) {    // Clean name for BlockPane
            bytes = transformBlockPane(name, bytes, false);
        }
        else if (name.equals("ahm")) { // Obfuscated name for net.minecraft.world.WorldType
            bytes = transformWorldType(name, bytes, true);
        }
        else if (name.equals("net.minecraft.world.WorldType")) {    // Clear name
            bytes = transformWorldType(name, bytes, false);
        }
        return bytes;
    }
    
    private MethodNode findMethod(ClassNode cls, String methname, String methsig) {
        Iterator<MethodNode> methods = cls.methods.iterator();
        while(methods.hasNext()) {
            MethodNode m = methods.next();
            // Check if method we want
            if (m.name.equals(methname) && (m.desc.equals(methsig))) {
                return m;
            }
        }
        return null;
    }
    private int findOpSequence(MethodNode meth, int[] opcodes) {
        Iterator<AbstractInsnNode> iter = meth.instructions.iterator();
        int index = -1;
        int seqindex = 0;
        int seqstartindex = -1;

        while (iter.hasNext()) {
            index++;
            AbstractInsnNode node = iter.next();
            if (node.getOpcode() == opcodes[seqindex]) {
                if (seqindex == 0) seqstartindex = index;   // Remember start
                seqindex++;
                if (seqindex == opcodes.length) {   // Done?
                    return seqstartindex;
                }
            }
            else {  // Mismatch
                seqindex = 0;
                seqstartindex = -1;
            }
        }
        return -1;
    }
    
    @SuppressWarnings("unused")
    private void printMethod(MethodNode meth) {
        Iterator<AbstractInsnNode> iter = meth.instructions.iterator();
        System.out.println("printMethod(" + meth.name + ")");
        while (iter.hasNext()) {
            AbstractInsnNode n = iter.next();
            System.out.println("  " + n.getOpcode() + " (" + n.getType() + " - " + n.getClass().getName() + ")");
        }
    }
        
    
    private byte[] transformWorldType(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";
        String targetMethodSig = "";

        WesterosBlocks.log.fine("Checking class " + name);
        
        targetMethodName ="getCloudHeight";
        targetMethodSig = "()F";
        
        //set up ASM class manipulation stuff. Consult the ASM docs for details
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(b);
        classReader.accept(classNode, 0);

        // Now find the method
        MethodNode m = findMethod(classNode, targetMethodName, targetMethodSig);
        if (m == null) {
            WesterosBlocks.log.warning("Cannot find "  + targetMethodName + "() in " + name + " for patching");
            return b;
        }
        // Find our target op sequence
        int ldx_index = findOpSequence(m, new int[] { LDC } );
        if (ldx_index < 0) {
            WesterosBlocks.log.warning("Cannot patch "  + targetMethodName + "() in " + name);
            return b;
        }
        // Replace method implementation
        MethodVisitor mv = m;
        m.instructions.clear();
        m.localVariables.clear();

        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(11, l0);
        mv.visitVarInsn(ALOAD, 0);
        if (obfus) {
            mv.visitFieldInsn(GETFIELD, "ahm", "g", "I");
        }
        else {
            mv.visitFieldInsn(GETFIELD, "net/minecraft/world/WorldType", "worldTypeId", "I");
        }
        mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "getWorldHeight", "(I)F");
        mv.visitVarInsn(FSTORE, 1);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLineNumber(12, l1);
        mv.visitVarInsn(FLOAD, 1);
        mv.visitInsn(FCONST_0);
        mv.visitInsn(FCMPL);
        Label l2 = new Label();
        mv.visitJumpInsn(IFLE, l2);
        Label l3 = new Label();
        mv.visitLabel(l3);
        mv.visitLineNumber(13, l3);
        mv.visitVarInsn(FLOAD, 1);
        mv.visitInsn(FRETURN);
        mv.visitLabel(l2);
        mv.visitLineNumber(15, l2);
        mv.visitLdcInsn(new Float("128.0"));
        mv.visitInsn(FRETURN);
        Label l4 = new Label();
        mv.visitLabel(l4);
        if (obfus) {
            mv.visitLocalVariable("this", "Lahm;", null, l0, l4, 0);
        }
        else {
            mv.visitLocalVariable("this", "Lnet/minecraft/world/WorldType;", null, l0, l4, 0);
        }
        mv.visitLocalVariable("rslt", "F", null, l1, l4, 1);
        mv.visitMaxs(2, 2);
        mv.visitEnd();

        //ASM specific for cleaning up and returning the final bytes for JVM processing.
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        b = writer.toByteArray();
        
        WesterosBlocks.log.fine("Method " + targetMethodName + "() of " + name + " patched!");
        
        return b;
    }

    private byte[] transformBlockWall(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";
        String targetMethodDesc = "";

        WesterosBlocks.log.fine("Checking class " + name);

        if(obfus == true) {
            targetMethodName = "e"; // canConnectWallTo()
            targetMethodDesc = "(Lahl;III)Z";
        }
        else {
            targetMethodName ="canConnectWallTo";
            targetMethodDesc = "(Lnet/minecraft/world/IBlockAccess;III)Z";
        }

        //set up ASM class manipulation stuff. Consult the ASM docs for details
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(b);
        classReader.accept(classNode, 0);

        // Now find the method
        MethodNode m = findMethod(classNode, targetMethodName, targetMethodDesc);
        if (m == null) {
            WesterosBlocks.log.warning("Cannot find "  + targetMethodName + "() in " + name + " for patching");
            return b;
        }
        // Find our target op sequence
        int ldx_index = findOpSequence(m, new int[] { ALOAD, ILOAD, ILOAD, ILOAD, INVOKEINTERFACE } );
        if (ldx_index < 0) {
            WesterosBlocks.log.warning("Cannot patch "  + targetMethodName + "() in " + name);
            return b;
        }
        // Replace method
        MethodVisitor mv = m;
        m.instructions.clear();
        m.localVariables.clear();
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ILOAD, 2);
        mv.visitVarInsn(ILOAD, 3);
        mv.visitVarInsn(ILOAD, 4);
        if (obfus) {
            mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "canConnectWallTo", "(Laji;Lahl;III)Z", false);
        }
        else {
            mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "canConnectWallTo", "(Lnet/minecraft/block/Block;Lnet/minecraft/world/IBlockAccess;III)Z", false);
        }
        mv.visitInsn(IRETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        if (obfus) {
            mv.visitLocalVariable("this", "Laji;", null, l0, l1, 0);
            mv.visitLocalVariable("world", "Lahl;", null, l0, l1, 1);
        }
        else {
            mv.visitLocalVariable("this", "Lnet/minecraft/block/Block;", null, l0, l1, 0);
            mv.visitLocalVariable("world", "Lnet/minecraft/world/IBlockAccess;", null, l0, l1, 1);
        }
        mv.visitLocalVariable("x", "I", null, l0, l1, 2);
        mv.visitLocalVariable("y", "I", null, l0, l1, 3);
        mv.visitLocalVariable("z", "I", null, l0, l1, 4);
        mv.visitMaxs(5, 5);
        mv.visitEnd();
        
        //ASM specific for cleaning up and returning the final bytes for JVM processing.
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        b = writer.toByteArray();
        
        WesterosBlocks.log.fine("Method " + targetMethodName + "() of " + name + " patched!");
        
        return b;
    }

    private byte[] transformBlockFence(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";
        String targetMethodDesc = "";
        
        WesterosBlocks.log.fine("Checking class " + name);

        if(obfus == true) {
            targetMethodName = "e"; // canConnectFenceTo()
            targetMethodDesc = "(Lahl;III)Z";
        }
        else {
            targetMethodName ="canConnectFenceTo";
            targetMethodDesc = "(Lnet/minecraft/world/IBlockAccess;III)Z";
        }

        //set up ASM class manipulation stuff. Consult the ASM docs for details
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(b);
        classReader.accept(classNode, 0);

        // Now find the method
        MethodNode m = findMethod(classNode, targetMethodName, targetMethodDesc);
        if (m == null) {
            WesterosBlocks.log.warning("Cannot find "  + targetMethodName + "() in " + name + " for patching");
            return b;
        }
        // Find our target op sequence
        int ldx_index = findOpSequence(m, new int[] { ALOAD, ILOAD, ILOAD, ILOAD, INVOKEINTERFACE } );
        if (ldx_index < 0) {
            WesterosBlocks.log.warning("Cannot patch "  + targetMethodName + "() in " + name);
            return b;
        }
        // Replace method
        MethodVisitor mv = m;
        m.instructions.clear();
        m.localVariables.clear();
        mv.visitCode();
        
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(162, l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ILOAD, 2);
        mv.visitVarInsn(ILOAD, 3);
        mv.visitVarInsn(ILOAD, 4);
        if (obfus) {
            mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "canConnectFenceTo", "(Laji;Lahl;III)Z", false);
        }
        else {
            mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "canConnectFenceTo", "(Lnet/minecraft/block/Block;Lnet/minecraft/world/IBlockAccess;III)Z", false);
        }
        mv.visitInsn(IRETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        if (obfus) {
            mv.visitLocalVariable("this", "Laji;", null, l0, l1, 0);
            mv.visitLocalVariable("world", "Lahl;", null, l0, l1, 1);
        }
        else {
            mv.visitLocalVariable("this", "Lnet/minecraft/block/Block;", null, l0, l1, 0);
            mv.visitLocalVariable("world", "Lnet/minecraft/world/IBlockAccess;", null, l0, l1, 1);
        }
        mv.visitLocalVariable("x", "I", null, l0, l1, 2);
        mv.visitLocalVariable("y", "I", null, l0, l1, 3);
        mv.visitLocalVariable("z", "I", null, l0, l1, 4);
        mv.visitMaxs(5, 5);
        mv.visitEnd();
        
        WesterosBlocks.log.fine("Method " + targetMethodName + "() of " + name + " patched!");

        // Second method func_149825_a(Block)
        if(obfus == true) {
            targetMethodName = "a"; // func_149825_a()
            targetMethodDesc = "(Laji;)Z";
        }
        else {
            targetMethodName ="func_149825_a";
            targetMethodDesc = "(Lnet/minecraft/block/Block;)Z";
        }
        // Now find the method
        m = findMethod(classNode, targetMethodName, targetMethodDesc);
        if (m == null) {
            WesterosBlocks.log.warning("Cannot find "  + targetMethodName + "() in " + name + " for patching");
            return b;
        }
        // Find our target op sequence
        ldx_index = findOpSequence(m, new int[] { ALOAD, GETSTATIC } );
        if (ldx_index < 0) {
            WesterosBlocks.log.warning("Cannot patch "  + targetMethodName + "() in " + name);
            return b;
        }
        // Replace method
        mv = m;
        m.instructions.clear();
        m.localVariables.clear();
        mv.visitCode();

        l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(158, l0);
        mv.visitVarInsn(ALOAD, 0);
        if (obfus) {
            mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "isFenceBlock", "(Laji;)Z", false);
        }
        else {
            mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "isFenceBlock", "(Lnet/minecraft/block/Block;)Z", false);
        }
        mv.visitInsn(IRETURN);
        l1 = new Label();
        mv.visitLabel(l1);
        if (obfus) {
            mv.visitLocalVariable("b", "Laji;", null, l0, l1, 0);
        }
        else {
            mv.visitLocalVariable("b", "Lnet/minecraft/block/Block;", null, l0, l1, 0);
        }
        mv.visitMaxs(1, 1);
        mv.visitEnd();


        //ASM specific for cleaning up and returning the final bytes for JVM processing.
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        b = writer.toByteArray();
        
        WesterosBlocks.log.fine("Method " + targetMethodName + "() of " + name + " patched!");
        
        return b;
    }

    private byte[] transformBlockPane(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";
        String targetMethodDesc = "";

        WesterosBlocks.log.fine("Checking class " + name);

        targetMethodName ="canPaneConnectTo";
        if(obfus == true) {
            targetMethodDesc = "(Lahl;IIILnet/minecraftforge/common/util/ForgeDirection;)Z";
        }
        else {
            targetMethodDesc = "(Lnet/minecraft/world/IBlockAccess;IIILnet/minecraftforge/common/util/ForgeDirection;)Z";
        }

        //set up ASM class manipulation stuff. Consult the ASM docs for details
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(b);
        classReader.accept(classNode, 0);

        // Now find the method
        MethodNode m = findMethod(classNode, targetMethodName, targetMethodDesc);
        if (m == null) {
            WesterosBlocks.log.warning("Cannot find "  + targetMethodName + "() in " + name + " for patching");
            return b;
        }
        // Find our target op sequence
        int ldx_index = findOpSequence(m, new int[] { ALOAD, ALOAD, ILOAD, ILOAD, ILOAD, INVOKEINTERFACE } );
        if (ldx_index < 0) {
            WesterosBlocks.log.warning("Cannot patch "  + targetMethodName + "() in " + name);
            return b;
        }
        // Replace method
        MethodVisitor mv = m;
        m.instructions.clear();
        m.localVariables.clear();
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(162, l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ILOAD, 2);
        mv.visitVarInsn(ILOAD, 3);
        mv.visitVarInsn(ILOAD, 4);
        mv.visitVarInsn(ALOAD, 5);
        if (obfus) {
            mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "canPaneConnectTo", "(Laoa;Lahl;IIILnet/minecraftforge/common/util/ForgeDirection;)Z", false);
        }
        else {
            mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "canPaneConnectTo", "(Lnet/minecraft/block/BlockPane;Lnet/minecraft/world/IBlockAccess;IIILnet/minecraftforge/common/util/ForgeDirection;)Z", false);
        }
        mv.visitInsn(IRETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        if (obfus) {
            mv.visitLocalVariable("this", "Laoa;", null, l0, l1, 0);
            mv.visitLocalVariable("world", "Lahl;", null, l0, l1, 1);
        }
        else {
            mv.visitLocalVariable("this", "Lnet/minecraft/block/BlockPane;", null, l0, l1, 0);
            mv.visitLocalVariable("world", "Lnet/minecraft/world/IBlockAccess;", null, l0, l1, 1);
        }
        mv.visitLocalVariable("x", "I", null, l0, l1, 2);
        mv.visitLocalVariable("y", "I", null, l0, l1, 3);
        mv.visitLocalVariable("z", "I", null, l0, l1, 4);
        mv.visitLocalVariable("dir", "Lnet/minecraftforge/common/util/ForgeDirection;", null, l0, l1, 5);
        mv.visitMaxs(6, 6);
        mv.visitEnd();

        //ASM specific for cleaning up and returning the final bytes for JVM processing.
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        b = writer.toByteArray();
        
        WesterosBlocks.log.fine("Method " + targetMethodName + "() of " + name + " patched!");
        
        return b;
    }
  
    public static float getWorldHeight(int wtIndex) {
        return 256.0F;
    }
    
    public static boolean canConnectFenceTo(Block blk, IBlockAccess world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);

        if ((blk != block) && (block != Blocks.fence_gate) && (!(block instanceof BlockFence))) {
            Material m = block.getMaterial();
            return block != null && m.isOpaque() && block.renderAsNormalBlock() ? m != Material.gourd : false;
        }
        else {
            return true;
        }
    }
    
    public static boolean isFenceBlock(Block blk) {
        return blk instanceof BlockFence;
    }
    
    public static boolean canPaneConnectTo(BlockPane block, IBlockAccess world, int x, int y, int z, ForgeDirection dir)
    {
        Block blk = world.getBlock(x, y, z);
        if (block.canPaneConnectToBlock(blk) || world.isSideSolid(x, y, z, dir.getOpposite(), false)) {
            return true;
        }
        if ((blk instanceof BlockPane) && (block.getMaterial() == blk.getMaterial())) {
            return true;
        }
        return false;
    }

    public static boolean canConnectWallTo(Block blk, IBlockAccess world, int x, int y, int z)
    {
        Block block = world.getBlock(x, y, z);


        if (blk != block && block != Blocks.fence_gate && (!(block instanceof BlockWall))) {
            Material m = block.getMaterial();
            return block != blk && block != Blocks.fence_gate ? (m.isOpaque() && block.renderAsNormalBlock() ? m != Material.gourd : false) : true;
        }
        else {
            return true;
        }
    }

    public static Method ctmMethod = null;
    
    public static boolean checkForCTMSupport() {
        Class<?> cls;
        try {
            cls = Class.forName("com.prupe.mcpatcher.ctm.CTMUtils");
            // public static Icon getTile(RenderBlocks renderBlocks, Block block, int i, int j, int k, int face, Icon icon, Tessellator tessellator) {
            ctmMethod = cls.getDeclaredMethod("getTile", new Class[] { RenderBlocks.class, Block.class, int.class, int.class, int.class, int.class, IIcon.class, Tessellator.class });
            WesterosBlocks.log.fine("CTM support found for water fix");
        } catch (ClassNotFoundException e) {
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
        }
        return (ctmMethod != null);
    }
    
    private static Field canBlockGrass = null;
    
    public static boolean getCanBlockGrass(Block blk) {
        if (canBlockGrass == null) {
            for (Field f : Block.class.getDeclaredFields()) {
                String cname = f.getName();
                if ((f.getType() == boolean.class) && (cname.equals("canBlockGrass") || cname.equals("s") || cname.equals("field_149785_s"))) {
                    canBlockGrass = f;
                    f.setAccessible(true);
                    break;
                }
            }
            if (canBlockGrass == null) {
                WesterosBlocks.crash("Cannot find Block.canBlockGrass!!!");
            }
        }
        try {
            return canBlockGrass.getBoolean(blk);
        } catch (IllegalArgumentException e) {
            return false;
        } catch (IllegalAccessException e) {
            return false;
        }
    }
    private static Field useNeighborBrightness = null;
    
    public static void setUseNeighborBrightness(Block blk, boolean val) {
        if (useNeighborBrightness == null) {
            for (Field f : Block.class.getDeclaredFields()) {
                String cname = f.getName();
                if ((f.getType() == boolean.class) && (cname.equals("useNeighborBrightness") || cname.equals("u") || cname.equals("field_149783_u"))) {
                    useNeighborBrightness = f;
                    f.setAccessible(true);
                    break;
                }
            }
            if (useNeighborBrightness == null) {
                WesterosBlocks.crash("Cannot find Block.useNeighborBrightness!!!");
            }
        }
        try {
            useNeighborBrightness.setBoolean(blk, val);
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }
    
    private static Method canSmelt;
    
    public static boolean canSmelt(TileEntityFurnace te) {
        if (canSmelt == null) {
            for (Method m : TileEntityFurnace.class.getDeclaredMethods()) {
                String cname = m.getName();
                if ((m.getReturnType() == boolean.class) && (cname.equals("canSmelt") || cname.equals("func_145948_k"))) {
                    canSmelt = m;
                    m.setAccessible(true);
                    break;
                }
            }
            if (canSmelt == null) {
                WesterosBlocks.crash("Cannot find TileEntityFurnace.canSmelt()!!!");
            }
        }
        try {
            return (Boolean) canSmelt.invoke(te);
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e) {
        } catch (InvocationTargetException e) {
        }
        return false;
    }
}

