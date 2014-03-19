package com.westeroscraft.westerosblocks.asm;

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
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

public class ClassTransformer implements IClassTransformer, Opcodes {
    @Override
    public byte[] transform(String name, String tname, byte[] bytes)
    {
        if (name.equals("acg")) { // Obfuscated name for net.minecraft.world.WorldType
            bytes = transformWorldType(name, bytes, true);
        }
        else if (name.equals("net.minecraft.world.WorldType")) {    // Clear name
            bytes = transformWorldType(name, bytes, false);
        }
        else if (name.equals("arn")) {   // Obfuscated BlockWall
            bytes = transformBlockWall(name, bytes, true);
        }
        else if (name.equals("net.minecraft.block.BlockWall")) {    // Clean name for BlockWall
            bytes = transformBlockWall(name, bytes, false);
        }
        else if (name.equals("aoh")) {   // Obfuscated BlockFence
            bytes = transformBlockFence(name, bytes, true);
        }
        else if (name.equals("net.minecraft.block.BlockFence")) {    // Clean name for BlockFence
            bytes = transformBlockFence(name, bytes, false);
        }
        else if (name.equals("aqy")) {   // Obfuscated BlockPane
            bytes = transformBlockPane(name, bytes, true);
        }
        else if (name.equals("net.minecraft.block.BlockPane")) {    // Clean name for BlockPane
            bytes = transformBlockPane(name, bytes, false);
        }
        return bytes;
    }
    
    private MethodNode findMethod(ClassNode cls, String methname, String methsig) {
        @SuppressWarnings("unchecked")
        Iterator<MethodNode> methods = cls.methods.iterator();
        int ldc_index = -1;
        boolean matched = false;
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
        @SuppressWarnings("unchecked")
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
        
    
    private byte[] transformWorldType(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";
        String targetMethodSig = "";

        targetMethodName ="getCloudHeight";
        targetMethodSig = "()F";
        
        //set up ASM class manipulation stuff. Consult the ASM docs for details
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(b);
        classReader.accept(classNode, 0);

        // Now find the method
        MethodNode m = findMethod(classNode, targetMethodName, targetMethodSig);
        if (m == null) {
            WesterosBlocks.log.info("Cannot find "  + targetMethodName + "() in " + name + " for patching");
            return b;
        }
        // Find our target op sequence
        int ldx_index = findOpSequence(m, new int[] { LDC } );
        if (ldx_index < 0) {
            WesterosBlocks.log.info("Cannot patch "  + targetMethodName + "() in " + name);
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
            mv.visitFieldInsn(GETFIELD, "acg", "f", "I");
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
            mv.visitLocalVariable("this", "Lacg;", null, l0, l4, 0);
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
        
        System.out.println("Method " + targetMethodName + "() of " + name + " patched!");
        
        return b;
    }

    private byte[] transformBlockWall(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";
        String targetMethodDesc = "";
        if(obfus == true) {
            targetMethodName = "d"; // canConnectWallTo()
            targetMethodDesc = "(Lacf;III)Z";
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
            WesterosBlocks.log.info("Cannot find "  + targetMethodName + "() in " + name + " for patching");
            return b;
        }
        // Find our target op sequence
        int ldx_index = findOpSequence(m, new int[] { ALOAD, ILOAD, ILOAD, ILOAD, INVOKEINTERFACE } );
        if (ldx_index < 0) {
            WesterosBlocks.log.info("Cannot patch "  + targetMethodName + "() in " + name);
            return b;
        }
        // Replace method
        MethodVisitor mv = m;
        m.instructions.clear();
        m.localVariables.clear();
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(21, l0);
        mv.visitVarInsn(ALOAD, 0);
        if (obfus) {
            mv.visitFieldInsn(GETFIELD, "arn", "cF", "I");
        }
        else {
            mv.visitFieldInsn(GETFIELD, "net/minecraft/block/BlockWall", "blockID", "I");
        }
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ILOAD, 2);
        mv.visitVarInsn(ILOAD, 3);
        mv.visitVarInsn(ILOAD, 4);
        if (obfus) {
            mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "canConnectWallTo", "(ILacf;III)Z");
        }
        else {
            mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "canConnectWallTo", "(ILnet/minecraft/world/IBlockAccess;III)Z");
        }
        mv.visitInsn(IRETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        if (obfus) {
            mv.visitLocalVariable("this", "Larn;", null, l0, l1, 0);
            mv.visitLocalVariable("world", "Lacf;", null, l0, l1, 1);
        }
        else {
            mv.visitLocalVariable("this", "Lnet/minecraft/block/BlockWall;", null, l0, l1, 0);
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
        
        System.out.println("Method " + targetMethodName + "() of " + name + " patched!");
        
        return b;
    }

    private byte[] transformBlockFence(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";
        String targetMethodDesc = "";
        if(obfus == true) {
            targetMethodName = "d"; // canConnectFenceTo()
            targetMethodDesc = "(Lacf;III)Z";
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
            WesterosBlocks.log.info("Cannot find "  + targetMethodName + "() in " + name + " for patching");
            return b;
        }
        // Find our target op sequence
        int ldx_index = findOpSequence(m, new int[] { ALOAD, ILOAD, ILOAD, ILOAD, INVOKEINTERFACE } );
        if (ldx_index < 0) {
            WesterosBlocks.log.info("Cannot patch "  + targetMethodName + "() in " + name);
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
        if (obfus) {
            mv.visitFieldInsn(GETFIELD, "aoh", "cF", "I");
        }
        else {
            mv.visitFieldInsn(GETFIELD, "net/minecraft/block/BlockFence", "blockID", "I");
        }
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ILOAD, 2);
        mv.visitVarInsn(ILOAD, 3);
        mv.visitVarInsn(ILOAD, 4);
        if (obfus) {
            mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "canConnectFenceTo", "(ILacf;III)Z");
        }
        else {
            mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "canConnectFenceTo", "(ILnet/minecraft/world/IBlockAccess;III)Z");
        }
        mv.visitInsn(IRETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        if (obfus) {
            mv.visitLocalVariable("this", "Laoh;", null, l0, l1, 0);
            mv.visitLocalVariable("world", "Lacf;", null, l0, l1, 1);
        }
        else {
            mv.visitLocalVariable("this", "Lnet/minecraft/block/BlockFence;", null, l0, l1, 0);
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
        
        System.out.println("Method " + targetMethodName + "() of " + name + " patched!");
        
        return b;
    }

    private byte[] transformBlockPane(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";
        String targetMethodDesc = "";

        targetMethodName ="canPaneConnectTo";
        if(obfus == true) {
            targetMethodDesc = "(Lacf;IIILnet/minecraftforge/common/ForgeDirection;)Z";
        }
        else {
            targetMethodDesc = "(Lnet/minecraft/world/IBlockAccess;IIILnet/minecraftforge/common/ForgeDirection;)Z";
        }

        //set up ASM class manipulation stuff. Consult the ASM docs for details
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(b);
        classReader.accept(classNode, 0);

        // Now find the method
        MethodNode m = findMethod(classNode, targetMethodName, targetMethodDesc);
        if (m == null) {
            WesterosBlocks.log.info("Cannot find "  + targetMethodName + "() in " + name + " for patching");
            return b;
        }
        // Find our target op sequence
        int ldx_index = findOpSequence(m, new int[] { ALOAD, ALOAD, ILOAD, ALOAD, GETFIELD } );
        if (ldx_index < 0) {
            WesterosBlocks.log.info("Cannot patch "  + targetMethodName + "() in " + name);
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
        mv.visitVarInsn(ALOAD, 5);
        if (obfus) {
            mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "canPaneConnectTo", "(Laqy;Lacf;IIILnet/minecraftforge/common/ForgeDirection;)Z");
        }
        else {
            mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "canPaneConnectTo", "(Lnet/minecraft/block/BlockPane;Lnet/minecraft/world/IBlockAccess;IIILnet/minecraftforge/common/ForgeDirection;)Z");
        }
        mv.visitInsn(IRETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        if (obfus) {
            mv.visitLocalVariable("this", "Laqy;", null, l0, l1, 0);
            mv.visitLocalVariable("access", "Lacf;", null, l0, l1, 1);
        }
        else {
            mv.visitLocalVariable("this", "Lcom/westeroscraft/westerosblocks/FixedPane;", null, l0, l1, 0);
            mv.visitLocalVariable("access", "Lnet/minecraft/world/IBlockAccess;", null, l0, l1, 1);
        }
        mv.visitLocalVariable("x", "I", null, l0, l1, 2);
        mv.visitLocalVariable("y", "I", null, l0, l1, 3);
        mv.visitLocalVariable("z", "I", null, l0, l1, 4);
        mv.visitLocalVariable("dir", "Lnet/minecraftforge/common/ForgeDirection;", null, l0, l1, 5);
        mv.visitMaxs(6, 6);
        mv.visitEnd();

        //ASM specific for cleaning up and returning the final bytes for JVM processing.
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        b = writer.toByteArray();
        
        System.out.println("Method " + targetMethodName + "() of " + name + " patched!");
        
        return b;
    }

    public static float getWorldHeight(int wtIndex) {
        return 256.0F;
    }
    
    public static boolean canConnectFenceTo(int blkid, IBlockAccess world, int x, int y, int z) {
        int id = world.getBlockId(x, y, z);
        Block block = Block.blocksList[id];

        if ((id != blkid) && (id != Block.fenceGate.blockID) && (!(block instanceof BlockFence))) {
            return block != null && block.blockMaterial.isOpaque() && block.renderAsNormalBlock() ? block.blockMaterial != Material.pumpkin : false;
        }
        else {
            return true;
        }
    }
    
    public static boolean canConnectWallTo(int blkid, IBlockAccess world, int x, int y, int z)
    {
        int id = world.getBlockId(x, y, z);
        Block block = Block.blocksList[id];

        if (id != blkid && id != Block.fenceGate.blockID && (!(block instanceof BlockWall))) {
            return block != null && block.blockMaterial.isOpaque() && block.renderAsNormalBlock() ? block.blockMaterial != Material.pumpkin : false;
        }
        else {
            return true;
        }
    }

    public static boolean canPaneConnectTo(BlockPane blk, IBlockAccess access, int x, int y, int z, ForgeDirection dir)
    {
        int id = access.getBlockId(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
        if (blk.canThisPaneConnectToThisBlockID(id) || access.isBlockSolidOnSide(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ, dir.getOpposite(), false))
            return true;
        if ((Block.blocksList[id] instanceof BlockPane) && (blk.blockMaterial == Block.blocksList[id].blockMaterial))
            return true;
        return false;
    }

}

