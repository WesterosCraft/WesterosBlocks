package com.westeroscraft.westerosblocks.asm;

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
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.Opcodes;

import com.westeroscraft.westerosblocks.WesterosBlocks;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

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
        /*NOTYET
        if (name.equals("acg")) { // Obfuscated name for net.minecraft.world.WorldType
            bytes = transformWorldType(name, bytes, true);
        }
        else if (name.equals("net.minecraft.world.WorldType")) {    // Clear name
            bytes = transformWorldType(name, bytes, false);
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
        */
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
        WesterosBlocks.log.fine("Method " + targetMethodName + "() of " + name + " patched!");
        
        return b;
    }

    public static float getWorldHeight(int wtIndex) {
        return 256.0F;
    }
    
    /*NOTYET
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
    

    public static boolean canPaneConnectTo(BlockPane blk, IBlockAccess access, int x, int y, int z, ForgeDirection dir)
    {
        int id = access.getBlockId(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
        if (blk.canThisPaneConnectToThisBlockID(id) || access.isBlockSolidOnSide(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ, dir.getOpposite(), false))
            return true;
        if ((Block.blocksList[id] instanceof BlockPane) && (blk.blockMaterial == Block.blocksList[id].blockMaterial))
            return true;
        return false;
    }
    */

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
    /*NOTYET
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
    */
}

