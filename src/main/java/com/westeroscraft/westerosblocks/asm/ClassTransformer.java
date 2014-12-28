package com.westeroscraft.westerosblocks.asm;

import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.WEST;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.Opcodes;

import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.blocks.WCFluidCTMRenderer;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockStationary;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.Icon;
import net.minecraft.world.ChunkCache;
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
        else if (name.equals("amx")) {   // Obfuscated BlockBasePressurePlate
            bytes = transformBlockBasePressurePlate(name, bytes, true);
        }
        else if (name.equals("net.minecraft.block.BlockBasePressurePlate")) {    // Clean name for BlockBasePressurePlate
            bytes = transformBlockBasePressurePlate(name, bytes, false);
        }
        else if (name.equals("ape")) {   // Obfuscated BlockStationary
            bytes = transformBlockStationary(name, bytes, true);
        }
        else if (name.equals("net.minecraft.block.BlockStationary")) {    // Clean name for BlockStationary
            bytes = transformBlockStationary(name, bytes, false);
        }
        else if (name.equals("acl")) {   // Obfuscated ChunkCache
            bytes = transformChunkCache(name, bytes, true);
        }
        else if (name.equals("net.minecraft.world.ChunkCache")) {    // Clean name for ChunkCache
            bytes = transformChunkCache(name, bytes, false);
        }
        else if (name.equals("bfr")) {   // Obfuscated RenderBlocks
            bytes = transformRenderBlocks(name, bytes, true);
        }
        else if (name.equals("net.minecraft.client.renderer.RenderBlocks")) {    // Clean name for RenderBlocks
            bytes = transformRenderBlocks(name, bytes, false);
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
        
        WesterosBlocks.log.fine("Method " + targetMethodName + "() of " + name + " patched!");
        
        return b;
    }

    private byte[] transformBlockWall(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";
        String targetMethodDesc = "";

        WesterosBlocks.log.fine("Checking class " + name);

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
        
        WesterosBlocks.log.fine("Method " + targetMethodName + "() of " + name + " patched!");
        
        return b;
    }

    private byte[] transformBlockFence(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";
        String targetMethodDesc = "";
        
        WesterosBlocks.log.fine("Checking class " + name);

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
        
        WesterosBlocks.log.fine("Method " + targetMethodName + "() of " + name + " patched!");
        
        return b;
    }

    private byte[] transformBlockPane(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";
        String targetMethodDesc = "";

        WesterosBlocks.log.fine("Checking class " + name);

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
            WesterosBlocks.log.warning("Cannot find "  + targetMethodName + "() in " + name + " for patching");
            return b;
        }
        // Find our target op sequence
        int ldx_index = findOpSequence(m, new int[] { ALOAD, ALOAD, ILOAD, ALOAD, GETFIELD } );
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
        
        WesterosBlocks.log.fine("Method " + targetMethodName + "() of " + name + " patched!");
        
        return b;
    }

    
    private byte[] transformBlockBasePressurePlate(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";   //canPlaceBlockAt(World par1World, int par2, int par3, int par4)
        String targetMethodSig = "";
        String targetMethodName2 = "";  // onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
        String targetMethodSig2 = "";

        WesterosBlocks.log.fine("Checking class " + name);

        if (obfus) {
            targetMethodName = "c"; //canPlaceBlockAt
            targetMethodSig = "(Labw;III)Z";
            targetMethodName2 ="a"; //onNeighborBlockChange
            targetMethodSig2 = "(Labw;IIII)V";
        }
        else {
            targetMethodName ="canPlaceBlockAt";
            targetMethodSig = "(Lnet/minecraft/world/World;III)Z";
            targetMethodName2 ="onNeighborBlockChange";
            targetMethodSig2 = "(Lnet/minecraft/world/World;IIII)V";
        }
        
        //set up ASM class manipulation stuff. Consult the ASM docs for details
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(b);
        classReader.accept(classNode, 0);

        // Now find the first method
        MethodNode m = findMethod(classNode, targetMethodName, targetMethodSig);
        if (m == null) {
            WesterosBlocks.log.warning("Cannot find "  + targetMethodName + "() in " + name + " for patching");
            return b;
        }

        // Find target op seqence before patch in canPlaceBlockAt method (insert after last instruction in sequence)
        // mv.visitInsn(ICONST_1);
        // mv.visitInsn(ISUB);
        // mv.visitVarInsn(ILOAD, 4);
        // mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "doesBlockHaveSolidTopSurface", "(III)Z", false);
        // mv.visitJumpInsn(IFNE, l1);        
        int index = findOpSequence(m, new int[] { ICONST_1, ISUB, ILOAD, INVOKEVIRTUAL, IFNE } );
        if (index < 0) {
            WesterosBlocks.log.warning("Cannot patch "  + targetMethodName + "() in " + name);
            return b;
        }
        if (obfus) {
            m.instructions.insert(m.instructions.get(index+4), new FieldInsnNode(GETSTATIC, "aqz", "s", "[Laqz;"));
        }
        else {
            m.instructions.insert(m.instructions.get(index+4), new FieldInsnNode(GETSTATIC, "net/minecraft/block/Block", "blocksList", "[Lnet/minecraft/block/Block;"));
        }

        // Find second patch in canPlaceBlockAt methdod (replace last instruction in sequence)
        // mv.visitInsn(ICONST_1);
        // mv.visitInsn(ISUB);
        // mv.visitVarInsn(ILOAD, 4);
        // mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "getBlockId", "(III)I", false);
        // mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/block/BlockFence", "isIdAFence", "(I)Z", false);
        index = findOpSequence(m, new int[] { ICONST_1, ISUB, ILOAD, INVOKEVIRTUAL, INVOKESTATIC } );
        if (index < 0) {
            WesterosBlocks.log.warning("Cannot patch#2 "  + targetMethodName + "() in " + name);
            return b;
        }
        m.instructions.remove(m.instructions.get(index+4));   // Remove old INVOKESTATIC
        AbstractInsnNode n = m.instructions.get(index+4);   // Get instruction after
        m.instructions.insertBefore(n, new InsnNode(AALOAD));
        if (obfus) {
            m.instructions.insertBefore(n, new TypeInsnNode(INSTANCEOF, "aoh"));
        }
        else {
            m.instructions.insertBefore(n, new TypeInsnNode(INSTANCEOF, "net/minecraft/block/BlockFence"));
        }
        
        // Now find the second method
        m = findMethod(classNode, targetMethodName2, targetMethodSig2);
        if (m == null) {
            WesterosBlocks.log.warning("Cannot find "  + targetMethodName2 + "() in " + name + " for patching");
            return b;
        }

        // Find first patch location in onNeighborBlockChange method
        // mv.visitInsn(ICONST_1);
        // mv.visitInsn(ISUB);
        // mv.visitVarInsn(ILOAD, 4);
        // mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "doesBlockHaveSolidTopSurface", "(III)Z", false);
        // mv.visitJumpInsn(IFNE, l2);
        index = findOpSequence(m, new int[] { ICONST_1, ISUB, ILOAD, INVOKEVIRTUAL, IFNE } );
        if (index < 0) {
            WesterosBlocks.log.warning("Cannot patch "  + targetMethodName2 + "() in " + name);
            return b;
        }
        if (obfus) {
            m.instructions.insert(m.instructions.get(index+4), new FieldInsnNode(GETSTATIC, "aqz", "s", "[Laqz;"));
        }
        else {
            m.instructions.insert(m.instructions.get(index+4), 
                    new FieldInsnNode(GETSTATIC, "net/minecraft/block/Block", "blocksList", "[Lnet/minecraft/block/Block;"));
        }
        // Find second patch in onNeighborBlockChange methdod (replace first instruction in sequence)
        // mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/block/BlockFence", "isIdAFence", "(I)Z", false);
        // mv.visitJumpInsn(IFNE, l2);
        // mv.visitInsn(ICONST_1);
        // mv.visitVarInsn(ISTORE, 6)
        index = findOpSequence(m, new int[] { INVOKESTATIC, IFNE, ICONST_1, ISTORE } );
        if (index < 0) {
            WesterosBlocks.log.warning("Cannot patch#2 "  + targetMethodName2 + "() in " + name);
            return b;
        }
        m.instructions.remove(m.instructions.get(index));   // Remove old INVOKESTATIC
        n = m.instructions.get(index);   // Get instruction after
        m.instructions.insertBefore(n, new InsnNode(AALOAD));
        if (obfus) {
            m.instructions.insertBefore(n, new TypeInsnNode(INSTANCEOF, "aoh"));
        }
        else {
            m.instructions.insertBefore(n, new TypeInsnNode(INSTANCEOF, "net/minecraft/block/BlockFence"));
        }
        //ASM specific for cleaning up and returning the final bytes for JVM processing.
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        b = writer.toByteArray();

        WesterosBlocks.log.fine("Method " + targetMethodName + "() of " + name + " patched!");
        
        return b;
    }

    private byte[] transformBlockStationary(String name, byte[] b, boolean obfus) {

        WesterosBlocks.log.fine("Checking class " + name);
                
        //set up ASM class manipulation stuff. Consult the ASM docs for details
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(b);
        classReader.accept(classNode, 0);

        // Add net method for getIcon()
        MethodVisitor mv;
        AnnotationVisitor av0;
        if (obfus) {
            mv = classNode.visitMethod(ACC_PUBLIC, "a", "(II)Lms;", null, null);
        }
        else {
            mv = classNode.visitMethod(ACC_PUBLIC, "getIcon", "(II)Lnet/minecraft/util/Icon;", null, null);
        }
        av0 = mv.visitAnnotation("Lcpw/mods/fml/relauncher/SideOnly;", true);
        av0.visitEnum("value", "Lcpw/mods/fml/relauncher/Side;", "CLIENT");
        av0.visitEnd();
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitVarInsn(ILOAD, 1);
        mv.visitVarInsn(ILOAD, 2);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ILOAD, 1);
        mv.visitVarInsn(ILOAD, 2);
        if (obfus) {
            mv.visitMethodInsn(INVOKESPECIAL, "apc", "a", "(II)Lms;");
        }
        else {
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/block/BlockFluid", "getIcon", "(II)Lnet/minecraft/util/Icon;");
        }
        mv.visitVarInsn(ALOAD, 0);
        if (obfus) {
            mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "handleFluidGetIcon", "(IILms;Lape;)Lms;");
        }
        else {
            mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "handleFluidGetIcon", "(IILnet/minecraft/util/Icon;Lnet/minecraft/block/BlockStationary;)Lnet/minecraft/util/Icon;");
        }
        mv.visitInsn(ARETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        if (obfus) {
            mv.visitLocalVariable("this", "Lape;", null, l0, l1, 0);
        }
        else {
            mv.visitLocalVariable("this", "Lnet/minecraft/block/BlockStationary;", null, l0, l1, 0);
        }
        mv.visitLocalVariable("side", "I", null, l0, l1, 1);
        mv.visitLocalVariable("meta", "I", null, l0, l1, 2);
        mv.visitMaxs(5, 3);
        mv.visitEnd();

        // Add getRenderType method
        if (obfus) {
            mv = classNode.visitMethod(ACC_PUBLIC, "d", "()I", null, null);
        }
        else {
            mv = classNode.visitMethod(ACC_PUBLIC, "getRenderType", "()I", null, null);
        }
        mv.visitCode();
        l0 = new Label();
        mv.visitLabel(l0);
        mv.visitFieldInsn(GETSTATIC, "com/westeroscraft/westerosblocks/WesterosBlocks", "fluidCTMRenderID", "I");
        mv.visitInsn(IRETURN);
        l1 = new Label();
        mv.visitLabel(l1);
        if (obfus) {
            mv.visitLocalVariable("this", "Lape;", null, l0, l1, 0);
        }
        else {
            mv.visitLocalVariable("this", "Lnet/minecraft/block/BlockStationary;", null, l0, l1, 0);
        }
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        //ASM specific for cleaning up and returning the final bytes for JVM processing.
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        b = writer.toByteArray();

        WesterosBlocks.log.fine("Methdos for  " + name + " patched!");
        
        
        return b;
    }    
    
    private byte[] transformChunkCache(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";   //    public int getLightValueExt(int par1, int par2, int par3, boolean par4)
        String targetMethodSig = "";

        WesterosBlocks.log.fine("Checking class " + name);

        if (FMLCommonHandler.instance().getSide() == Side.SERVER) {
            WesterosBlocks.log.fine("Nothing to patch on server side for " + name);
            return b;
        }
        if (obfus) {
            targetMethodName = "a"; //getLightValueExt
            targetMethodSig = "(IIIZ)I";
        }
        else {
            targetMethodName ="getLightValueExt";
            targetMethodSig = "(IIIZ)I";
        }
        
        //set up ASM class manipulation stuff. Consult the ASM docs for details
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(b);
        classReader.accept(classNode, 0);

        // Now find the first method
        MethodNode m = findMethod(classNode, targetMethodName, targetMethodSig);
        if (m == null) {
            WesterosBlocks.log.warning("Cannot find "  + targetMethodName + "() in " + name + " for patching");
            return b;
        }

        // Find target op seqence before patch in getLightValueExt method (insert after last instruction in sequence)
        // mv.visitVarInsn(ALOAD, 0);
        // mv.visitVarInsn(ILOAD, 1);
        // mv.visitVarInsn(ILOAD, 2);
        // mv.visitVarInsn(ILOAD, 3);
        // mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/ChunkCache", "getBlockId", "(III)I", false);
        int index = findOpSequence(m, new int[] { ALOAD, ILOAD, ILOAD, ILOAD, INVOKEVIRTUAL } );
        if (index < 0) {
            WesterosBlocks.log.warning("Cannot patch "  + targetMethodName + "() in " + name);
            return b;
        }
        m.instructions.remove(m.instructions.get(index+4));   // Remove old INVOKEVIRTUAL
        AbstractInsnNode n = m.instructions.get(index+4);   // Get instruction after
        if (obfus) {
            m.instructions.insertBefore(n, new MethodInsnNode(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "getBlockIDForLightValue", "(Lacl;III)I"));
        }
        else {
            m.instructions.insertBefore(n, new MethodInsnNode(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "getBlockIDForLightValue", "(Lcom/westeroscraft/westerosblocks/asm/ChunkCache;III)I"));
        }
        //ASM specific for cleaning up and returning the final bytes for JVM processing.
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        b = writer.toByteArray();

        WesterosBlocks.log.fine("Method " + targetMethodName + "() of " + name + " patched!");
        
        return b;
    }

    private byte[] transformRenderBlocks(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";   //    drawCrossedSquares()
        String targetMethodSig = "";
        String targetMethodName2 = "";   //    renderBlockPane()
        String targetMethodSig2 = "";

        System.out.println("Checking class " + name);

        if (obfus) {
            targetMethodName = "a"; //drawCrossedSquares
            targetMethodSig = "(Laqz;IDDDF)V";
            targetMethodName2 = "a"; //renderBlockPane
            targetMethodSig2 = "(Laqy;III)Z";
        }
        else {
            targetMethodName ="drawCrossedSquares";
            targetMethodSig = "(Lnet/minecraft/block/Block;IDDDF)V";
            targetMethodName2 ="renderBlockPane";
            targetMethodSig2 = "(Lnet/minecraft/block/BlockPane;III)Z";
        }
        
        //set up ASM class manipulation stuff. Consult the ASM docs for details
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(b);
        classReader.accept(classNode, 0);

        // Now find the first method
        MethodNode m = findMethod(classNode, targetMethodName, targetMethodSig);
        if (m == null) {
            System.out.println("Cannot find "  + targetMethodName + "() in " + name + " for patching");
            return b;
        }        
        // Find sequence 
        //  mv.visitVarInsn(ALOAD, 10);
        //  mv.visitVarInsn(DLOAD, 22);
        //  mv.visitVarInsn(DLOAD, 5);
        // mv.visitVarInsn(FLOAD, 9);
        // mv.visitInsn(F2D);
        // mv.visitInsn(DADD);
        // mv.visitVarInsn(DLOAD, 26);
        // mv.visitVarInsn(DLOAD, 12);
        // mv.visitVarInsn(DLOAD, 14);
        // mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/Tessellator", "addVertexWithUV", "(DDDDD)V", false);
        int index = findOpSequence(m, new int[] { ALOAD, DLOAD, DLOAD, FLOAD, F2D, DADD, DLOAD, DLOAD, DLOAD, INVOKEVIRTUAL } );
        if (index < 0) {
            System.out.println("Cannot patch "  + targetMethodName + "() in " + name);
            return b;
        }
        AbstractInsnNode n = m.instructions.get(index);   // Get first instruction
        m.instructions.insertBefore(n, new VarInsnNode(ALOAD, 1));
        if (obfus) {
            m.instructions.insertBefore(n, new MethodInsnNode(INVOKEVIRTUAL, "aqz", "n", "()I"));
        }
        else {
            m.instructions.insertBefore(n, new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/block/Block", "getRenderBlockPass", "()I"));
        }
        m.instructions.insertBefore(n, new InsnNode(ICONST_1));
        LabelNode l1 = new LabelNode();
        m.instructions.insertBefore(n, new JumpInsnNode(IF_ICMPNE, l1));
        m.instructions.insertBefore(n, new VarInsnNode(DLOAD, 12));
        m.instructions.insertBefore(n, new VarInsnNode(DLOAD, 14));
        m.instructions.insertBefore(n, new VarInsnNode(DLOAD, 16));
        m.instructions.insertBefore(n, new VarInsnNode(DLOAD, 18));
        m.instructions.insertBefore(n, new VarInsnNode(DLOAD, 22));
        m.instructions.insertBefore(n, new VarInsnNode(DLOAD, 24));
        m.instructions.insertBefore(n, new VarInsnNode(DLOAD, 26));
        m.instructions.insertBefore(n, new VarInsnNode(DLOAD, 28));
        m.instructions.insertBefore(n, new VarInsnNode(DLOAD, 5));
        m.instructions.insertBefore(n, new VarInsnNode(FLOAD, 9));
        m.instructions.insertBefore(n, new InsnNode(F2D));
        m.instructions.insertBefore(n, new MethodInsnNode(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "drawCrossedSquares", "(DDDDDDDDDD)V"));
        m.instructions.insertBefore(n, new InsnNode(RETURN));
        m.instructions.insertBefore(n, l1);
        
        System.out.println("Method " + targetMethodName + "() of " + name + " patched!");

        // Now find the second method
        m = findMethod(classNode, targetMethodName2, targetMethodSig2);
        if (m == null) {
            System.out.println("Cannot find "  + targetMethodName2 + "() in " + name + " for patching");
            return b;
        }        
        // Find sequence 
        // mv.visitVarInsn(ALOAD, 12);
        // mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/util/Icon", "getMinU", "()F", true);
        // mv.visitInsn(F2D);
        // mv.visitVarInsn(DSTORE, 14);
        index = findOpSequence(m, new int[] { ALOAD, INVOKEINTERFACE, F2D, DSTORE } );
        if (index < 0) {
            System.out.println("Cannot patch "  + targetMethodName2 + "() in " + name);
            return b;
        }
        n = m.instructions.get(index);   // Get first instruction
        m.instructions.insertBefore(n, new VarInsnNode(ALOAD, 1));
        if (obfus) {
            m.instructions.insertBefore(n, new MethodInsnNode(INVOKEVIRTUAL, "aqy", "n", "()I"));
        }
        else {
            m.instructions.insertBefore(n, new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/block/BlockPane", "getRenderBlockPass", "()I"));
        }
        m.instructions.insertBefore(n, new InsnNode(ICONST_1));
        LabelNode l24 = new LabelNode();
        m.instructions.insertBefore(n, new JumpInsnNode(IF_ICMPNE, l24));
        m.instructions.insertBefore(n, new VarInsnNode(ALOAD, 1));
        m.instructions.insertBefore(n, new VarInsnNode(ILOAD, 2));
        m.instructions.insertBefore(n, new VarInsnNode(ILOAD, 3));
        m.instructions.insertBefore(n, new VarInsnNode(ILOAD, 4));
        m.instructions.insertBefore(n, new VarInsnNode(ILOAD, 5));
        m.instructions.insertBefore(n, new VarInsnNode(ALOAD, 12));
        m.instructions.insertBefore(n, new VarInsnNode(ALOAD, 13));
        m.instructions.insertBefore(n, new VarInsnNode(ALOAD, 0));
        if (obfus) {
            m.instructions.insertBefore(n, new FieldInsnNode(GETFIELD, "bfr", "a", "Lacf;"));
            m.instructions.insertBefore(n, new MethodInsnNode(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "renderBlockPane", "(Laqy;IIIILms;Lms;Lacf;)Z"));
        }
        else {
            m.instructions.insertBefore(n, new FieldInsnNode(GETFIELD, "com/westeroscraft/westerosblocks/asm/RenderBlocks", "blockAccess", "Lnet/minecraft/world/IBlockAccess;"));
            m.instructions.insertBefore(n, new MethodInsnNode(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "renderBlockPane", "(Lnet/minecraft/block/BlockPane;IIIILnet/minecraft/util/Icon;Lnet/minecraft/util/Icon;Lnet/minecraft/world/IBlockAccess;)Z"));
        }
        m.instructions.insertBefore(n, new InsnNode(IRETURN));
        m.instructions.insertBefore(n, l24);
        
        //ASM specific for cleaning up and returning the final bytes for JVM processing.
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        b = writer.toByteArray();

        System.out.println("Method " + targetMethodName2 + "() of " + name + " patched!");
        
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

    public static Method ctmMethod = null;
    
    public static boolean checkForCTMSupport() {
        Class<?> cls;
        try {
            cls = Class.forName("com.prupe.mcpatcher.ctm.CTMUtils");
            // public static Icon getTile(RenderBlocks renderBlocks, Block block, int i, int j, int k, int face, Icon icon, Tessellator tessellator) {
            ctmMethod = cls.getDeclaredMethod("getTile", new Class[] { RenderBlocks.class, Block.class, int.class, int.class, int.class, int.class, Icon.class, Tessellator.class });
            WesterosBlocks.log.fine("CTM support found for water fix");
        } catch (ClassNotFoundException e) {
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
        }
        return (ctmMethod != null);
    }
    
    public static Icon handleFluidGetIcon(int side, int meta, Icon ico, BlockStationary blk) {
        if ((side == 1) && (ctmMethod != null) && (WCFluidCTMRenderer.World != null)) {
            try {
                ico = (Icon) ctmMethod.invoke(null, WCFluidCTMRenderer.Renderer, blk, WCFluidCTMRenderer.X,
                        WCFluidCTMRenderer.Y, WCFluidCTMRenderer.Z, side, ico, WCFluidCTMRenderer.Tessell);
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return ico;
    }

    public static int getBlockIDForLightValue(ChunkCache chunkCache, int x, int y, int z) {
        int id = chunkCache.getBlockId(x, y, z);
        if (WesterosBlocks.slabStyleLightingBlocks.get(id)) {
            id = Block.stoneSingleSlab.blockID;
        }
        return id;
    }
    
    
    public static void drawCrossedSquares(double d3, double d4, double d5, double d6, double d8, double d9, double d10, double d11, double par5, double par9)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.addVertexWithUV(d8, par5 + par9, d10, d3, d4);
        tessellator.addVertexWithUV(d8, par5 + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d9, par5 + 0.0D, d11, d5, d6);
        tessellator.addVertexWithUV(d9, par5 + par9, d11, d5, d4);
        tessellator.addVertexWithUV(d8, par5 + par9, d11, d3, d4);
        tessellator.addVertexWithUV(d8, par5 + 0.0D, d11, d3, d6);
        tessellator.addVertexWithUV(d9, par5 + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d9, par5 + par9, d10, d5, d4);
    }   

    public static boolean renderBlockPane(BlockPane par1BlockPane, int par2, int par3, int par4, int l, Icon icon, Icon icon1, IBlockAccess blockAccess) {
        Tessellator tessellator = Tessellator.instance;

        double d0 = (double)icon.getMinU();
        double d1 = (double)icon.getInterpolatedU(8.0D);
        double d2 = (double)icon.getMaxU();
        double d3 = (double)icon.getMinV();
        double d4 = (double)icon.getMaxV();
        double d5 = (double)icon1.getInterpolatedU(7.0D);
        double d6 = (double)icon1.getInterpolatedU(9.0D);
        double d7 = (double)icon1.getMinV();
        double d8 = (double)icon1.getInterpolatedV(8.0D);
        double d9 = (double)icon1.getMaxV();
        double d10 = (double)par2;
        double d11 = (double)par2 + 0.5D;
        double d12 = (double)(par2 + 1);
        double d13 = (double)par4;
        double d14 = (double)par4 + 0.5D;
        double d15 = (double)(par4 + 1);
        double d16 = (double)par2 + 0.5D - 0.0625D;
        double d17 = (double)par2 + 0.5D + 0.0625D;
        double d18 = (double)par4 + 0.5D - 0.0625D;
        double d19 = (double)par4 + 0.5D + 0.0625D;
        boolean flag = par1BlockPane.canPaneConnectTo(blockAccess,par2, par3, par4, NORTH);
        boolean flag1 = par1BlockPane.canPaneConnectTo(blockAccess,par2, par3, par4, SOUTH);
        boolean flag2 = par1BlockPane.canPaneConnectTo(blockAccess,par2, par3, par4, WEST);
        boolean flag3 = par1BlockPane.canPaneConnectTo(blockAccess,par2, par3, par4, EAST);
        boolean flag4 = par1BlockPane.shouldSideBeRendered(blockAccess, par2, par3 + 1, par4, 1);
        boolean flag5 = par1BlockPane.shouldSideBeRendered(blockAccess, par2, par3 - 1, par4, 0);
        double d20 = 0.01D;
        double d21 = 0.005D;

        if ((!flag2 || !flag3) && (flag2 || flag3 || flag || flag1))
        {
            if (flag2 && !flag3)
            {
                tessellator.addVertexWithUV(d10, (double)(par3 + 1), d14, d0, d3);
                tessellator.addVertexWithUV(d10, (double)(par3 + 0), d14, d0, d4);
                tessellator.addVertexWithUV(d11, (double)(par3 + 0), d14, d1, d4);
                tessellator.addVertexWithUV(d11, (double)(par3 + 1), d14, d1, d3);
                //tessellator.addVertexWithUV(d11, (double)(par3 + 1), d14, d0, d3);
                //tessellator.addVertexWithUV(d11, (double)(par3 + 0), d14, d0, d4);
                //tessellator.addVertexWithUV(d10, (double)(par3 + 0), d14, d1, d4);
                //tessellator.addVertexWithUV(d10, (double)(par3 + 1), d14, d1, d3);

                if (!flag1 && !flag)
                {
                    tessellator.addVertexWithUV(d11, (double)(par3 + 1), d19, d5, d7);
                    tessellator.addVertexWithUV(d11, (double)(par3 + 0), d19, d5, d9);
                    tessellator.addVertexWithUV(d11, (double)(par3 + 0), d18, d6, d9);
                    tessellator.addVertexWithUV(d11, (double)(par3 + 1), d18, d6, d7);
                    //tessellator.addVertexWithUV(d11, (double)(par3 + 1), d18, d5, d7);
                    //tessellator.addVertexWithUV(d11, (double)(par3 + 0), d18, d5, d9);
                    //tessellator.addVertexWithUV(d11, (double)(par3 + 0), d19, d6, d9);
                    //tessellator.addVertexWithUV(d11, (double)(par3 + 1), d19, d6, d7);
                }

                if (flag4 || par3 < l - 1 && blockAccess.isAirBlock(par2 - 1, par3 + 1, par4))
                {
                    tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d9);
                    tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                    //tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                    //tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d9);
                    //tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d9);
                    //tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                }

                if (flag5 || par3 > 1 && blockAccess.isAirBlock(par2 - 1, par3 - 1, par4))
                {
                    tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d9);
                    tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d8);
                    //tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d8);
                    //tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d9);
                    //tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d9);
                    //tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d8);
                }
            }
            else if (!flag2 && flag3)
            {
                tessellator.addVertexWithUV(d11, (double)(par3 + 1), d14, d1, d3);
                tessellator.addVertexWithUV(d11, (double)(par3 + 0), d14, d1, d4);
                tessellator.addVertexWithUV(d12, (double)(par3 + 0), d14, d2, d4);
                tessellator.addVertexWithUV(d12, (double)(par3 + 1), d14, d2, d3);
                //tessellator.addVertexWithUV(d12, (double)(par3 + 1), d14, d1, d3);
                //tessellator.addVertexWithUV(d12, (double)(par3 + 0), d14, d1, d4);
                //tessellator.addVertexWithUV(d11, (double)(par3 + 0), d14, d2, d4);
                //tessellator.addVertexWithUV(d11, (double)(par3 + 1), d14, d2, d3);

                if (!flag1 && !flag)
                {
                    tessellator.addVertexWithUV(d11, (double)(par3 + 1), d18, d5, d7);
                    tessellator.addVertexWithUV(d11, (double)(par3 + 0), d18, d5, d9);
                    tessellator.addVertexWithUV(d11, (double)(par3 + 0), d19, d6, d9);
                    tessellator.addVertexWithUV(d11, (double)(par3 + 1), d19, d6, d7);
                    //tessellator.addVertexWithUV(d11, (double)(par3 + 1), d19, d5, d7);
                    //tessellator.addVertexWithUV(d11, (double)(par3 + 0), d19, d5, d9);
                    //tessellator.addVertexWithUV(d11, (double)(par3 + 0), d18, d6, d9);
                    //tessellator.addVertexWithUV(d11, (double)(par3 + 1), d18, d6, d7);
                }

                if (flag4 || par3 < l - 1 && blockAccess.isAirBlock(par2 + 1, par3 + 1, par4))
                {
                    tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d7);
                    //tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d7);
                    //tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                    //tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                    //tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d7);
                }

                if (flag5 || par3 > 1 && blockAccess.isAirBlock(par2 + 1, par3 - 1, par4))
                {
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d7);
                    //tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d7);
                    //tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d8);
                    //tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d8);
                    //tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d7);
                }
            }
        }
        else
        {
            tessellator.addVertexWithUV(d10, (double)(par3 + 1), d14, d0, d3);
            tessellator.addVertexWithUV(d10, (double)(par3 + 0), d14, d0, d4);
            tessellator.addVertexWithUV(d12, (double)(par3 + 0), d14, d2, d4);
            tessellator.addVertexWithUV(d12, (double)(par3 + 1), d14, d2, d3);
            //tessellator.addVertexWithUV(d12, (double)(par3 + 1), d14, d0, d3);
            //tessellator.addVertexWithUV(d12, (double)(par3 + 0), d14, d0, d4);
            //tessellator.addVertexWithUV(d10, (double)(par3 + 0), d14, d2, d4);
            //tessellator.addVertexWithUV(d10, (double)(par3 + 1), d14, d2, d3);

            if (flag4)
            {
                tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d9);
                tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d7);
                tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d7);
                tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d9);
                //tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d9);
                //tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d7);
                //tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d7);
                //tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d9);
            }
            else
            {
                if (par3 < l - 1 && blockAccess.isAirBlock(par2 - 1, par3 + 1, par4))
                {
                    tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d9);
                    tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                    //tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                    //tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d9);
                    //tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d9);
                    //tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                }

                if (par3 < l - 1 && blockAccess.isAirBlock(par2 + 1, par3 + 1, par4))
                {
                    tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d7);
                    //tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d7);
                    //tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                    //tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                    //tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d7);
                }
            }

            if (flag5)
            {
                tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d9);
                tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d7);
                tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d7);
                tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d9);
                //tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d9);
                //tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d7);
                //tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d7);
                //tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d9);
            }
            else
            {
                if (par3 > 1 && blockAccess.isAirBlock(par2 - 1, par3 - 1, par4))
                {
                    tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d9);
                    tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d8);
                    //tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d8);
                    //tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d9);
                    //tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d9);
                    //tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d8);
                }

                if (par3 > 1 && blockAccess.isAirBlock(par2 + 1, par3 - 1, par4))
                {
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d7);
                    //tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d7);
                    //tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d8);
                    //tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d8);
                    //tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d7);
                }
            }
        }

        if ((!flag || !flag1) && (flag2 || flag3 || flag || flag1))
        {
            if (flag && !flag1)
            {
                tessellator.addVertexWithUV(d11, (double)(par3 + 1), d13, d0, d3);
                tessellator.addVertexWithUV(d11, (double)(par3 + 0), d13, d0, d4);
                tessellator.addVertexWithUV(d11, (double)(par3 + 0), d14, d1, d4);
                tessellator.addVertexWithUV(d11, (double)(par3 + 1), d14, d1, d3);
                //tessellator.addVertexWithUV(d11, (double)(par3 + 1), d14, d0, d3);
                //tessellator.addVertexWithUV(d11, (double)(par3 + 0), d14, d0, d4);
                //tessellator.addVertexWithUV(d11, (double)(par3 + 0), d13, d1, d4);
                //tessellator.addVertexWithUV(d11, (double)(par3 + 1), d13, d1, d3);

                if (!flag3 && !flag2)
                {
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1), d14, d5, d7);
                    tessellator.addVertexWithUV(d16, (double)(par3 + 0), d14, d5, d9);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 0), d14, d6, d9);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1), d14, d6, d7);
                    //tessellator.addVertexWithUV(d17, (double)(par3 + 1), d14, d5, d7);
                    //tessellator.addVertexWithUV(d17, (double)(par3 + 0), d14, d5, d9);
                    //tessellator.addVertexWithUV(d16, (double)(par3 + 0), d14, d6, d9);
                    //tessellator.addVertexWithUV(d16, (double)(par3 + 1), d14, d6, d7);
                }

                if (flag4 || par3 < l - 1 && blockAccess.isAirBlock(par2, par3 + 1, par4 - 1))
                {
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d13, d6, d7);
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d14, d6, d8);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d14, d5, d8);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d13, d5, d7);
                    //tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d14, d6, d7);
                    //tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d13, d6, d8);
                    //tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d13, d5, d8);
                    //tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d14, d5, d7);
                }

                if (flag5 || par3 > 1 && blockAccess.isAirBlock(par2, par3 - 1, par4 - 1))
                {
                    tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d13, d6, d7);
                    tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d14, d6, d8);
                    tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d14, d5, d8);
                    tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d13, d5, d7);
                    //tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d14, d6, d7);
                    //tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d13, d6, d8);
                    //tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d13, d5, d8);
                    //tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d14, d5, d7);
                }
            }
            else if (!flag && flag1)
            {
                tessellator.addVertexWithUV(d11, (double)(par3 + 1), d14, d1, d3);
                tessellator.addVertexWithUV(d11, (double)(par3 + 0), d14, d1, d4);
                tessellator.addVertexWithUV(d11, (double)(par3 + 0), d15, d2, d4);
                tessellator.addVertexWithUV(d11, (double)(par3 + 1), d15, d2, d3);
                //tessellator.addVertexWithUV(d11, (double)(par3 + 1), d15, d1, d3);
                //tessellator.addVertexWithUV(d11, (double)(par3 + 0), d15, d1, d4);
                //tessellator.addVertexWithUV(d11, (double)(par3 + 0), d14, d2, d4);
                //tessellator.addVertexWithUV(d11, (double)(par3 + 1), d14, d2, d3);

                if (!flag3 && !flag2)
                {
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1), d14, d5, d7);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 0), d14, d5, d9);
                    tessellator.addVertexWithUV(d16, (double)(par3 + 0), d14, d6, d9);
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1), d14, d6, d7);
                    //tessellator.addVertexWithUV(d16, (double)(par3 + 1), d14, d5, d7);
                    //tessellator.addVertexWithUV(d16, (double)(par3 + 0), d14, d5, d9);
                    //tessellator.addVertexWithUV(d17, (double)(par3 + 0), d14, d6, d9);
                    //tessellator.addVertexWithUV(d17, (double)(par3 + 1), d14, d6, d7);
                }

                if (flag4 || par3 < l - 1 && blockAccess.isAirBlock(par2, par3 + 1, par4 + 1))
                {
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d14, d5, d8);
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d15, d5, d9);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d15, d6, d9);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d14, d6, d8);
                    //tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d15, d5, d8);
                    //tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d14, d5, d9);
                    //tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d14, d6, d9);
                    //tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d15, d6, d8);
                }

                if (flag5 || par3 > 1 && blockAccess.isAirBlock(par2, par3 - 1, par4 + 1))
                {
                    tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d14, d5, d8);
                    tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d15, d5, d9);
                    tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d15, d6, d9);
                    tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d14, d6, d8);
                    //tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d15, d5, d8);
                    //tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d14, d5, d9);
                    //tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d14, d6, d9);
                    //tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d15, d6, d8);
                }
            }
        }
        else
        {
            tessellator.addVertexWithUV(d11, (double)(par3 + 1), d15, d0, d3);
            tessellator.addVertexWithUV(d11, (double)(par3 + 0), d15, d0, d4);
            tessellator.addVertexWithUV(d11, (double)(par3 + 0), d13, d2, d4);
            tessellator.addVertexWithUV(d11, (double)(par3 + 1), d13, d2, d3);
            //tessellator.addVertexWithUV(d11, (double)(par3 + 1), d13, d0, d3);
            //tessellator.addVertexWithUV(d11, (double)(par3 + 0), d13, d0, d4);
            //tessellator.addVertexWithUV(d11, (double)(par3 + 0), d15, d2, d4);
            //tessellator.addVertexWithUV(d11, (double)(par3 + 1), d15, d2, d3);

            if (flag4)
            {
                tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d15, d6, d9);
                tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d13, d6, d7);
                tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d13, d5, d7);
                tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d15, d5, d9);
                //tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d13, d6, d9);
                //tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d15, d6, d7);
                //tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d15, d5, d7);
                //tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d13, d5, d9);
            }
            else
            {
                if (par3 < l - 1 && blockAccess.isAirBlock(par2, par3 + 1, par4 - 1))
                {
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d13, d6, d7);
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d14, d6, d8);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d14, d5, d8);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d13, d5, d7);
                    //tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d14, d6, d7);
                    //tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d13, d6, d8);
                    //tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d13, d5, d8);
                    //tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d14, d5, d7);
                }

                if (par3 < l - 1 && blockAccess.isAirBlock(par2, par3 + 1, par4 + 1))
                {
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d14, d5, d8);
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d15, d5, d9);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d15, d6, d9);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d14, d6, d8);
                    //tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d15, d5, d8);
                    //tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d14, d5, d9);
                    //tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d14, d6, d9);
                    //tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d15, d6, d8);
                }
            }

            if (flag5)
            {
                tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d15, d6, d9);
                tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d13, d6, d7);
                tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d13, d5, d7);
                tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d15, d5, d9);
                //tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d13, d6, d9);
                //tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d15, d6, d7);
                //tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d15, d5, d7);
                //tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d13, d5, d9);
            }
            else
            {
                if (par3 > 1 && blockAccess.isAirBlock(par2, par3 - 1, par4 - 1))
                {
                    tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d13, d6, d7);
                    tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d14, d6, d8);
                    tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d14, d5, d8);
                    tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d13, d5, d7);
                    //tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d14, d6, d7);
                    //tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d13, d6, d8);
                    //tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d13, d5, d8);
                    //tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d14, d5, d7);
                }

                if (par3 > 1 && blockAccess.isAirBlock(par2, par3 - 1, par4 + 1))
                {
                    tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d14, d5, d8);
                    tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d15, d5, d9);
                    tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d15, d6, d9);
                    tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d14, d6, d8);
                    //tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d15, d5, d8);
                    //tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d14, d5, d9);
                    //tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d14, d6, d9);
                    //tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d15, d6, d8);
                }
            }
        }

        return true;
    }

}

