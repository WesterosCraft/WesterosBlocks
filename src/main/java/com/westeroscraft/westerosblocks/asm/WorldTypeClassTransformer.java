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

import net.minecraft.launchwrapper.IClassTransformer;

public class WorldTypeClassTransformer implements IClassTransformer, Opcodes {
    @Override
    public byte[] transform(String name, String tname, byte[] bytes)
    {
        if (name.equals("acg")) { // Obfuscated name for net.minecraft.world.WorldType
            bytes = transformWorldType(name, bytes, true);
        }
        else if (name.equals("net.minecraft.world.WorldType")) {    // Clear name
            bytes = transformWorldType(name, bytes, false);
        }
        return bytes;
    }
    
    private byte[] transformWorldType(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";

        //if(obfus == true)
        //    targetMethodName = "f";
        //else
            targetMethodName ="getCloudHeight";

        //set up ASM class manipulation stuff. Consult the ASM docs for details
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(b);
        classReader.accept(classNode, 0);

        // Now find the method
        @SuppressWarnings("unchecked")
        Iterator<MethodNode> methods = classNode.methods.iterator();
        int ldc_index = -1;
        boolean matched = false;
        while(methods.hasNext()) {
            MethodNode m = methods.next();
            // Check if method we want
            if (m.name.equals(targetMethodName) && (m.desc.equals("()F"))) {
                AbstractInsnNode currentNode = null;
                AbstractInsnNode targetNode = null;
                @SuppressWarnings("unchecked")
                Iterator<AbstractInsnNode> iter = m.instructions.iterator();

                int index = -1;
                //Loop over the instruction set and find the instruction LDC
                while (iter.hasNext()) {
                    index++;
                    currentNode = iter.next();
                    if (currentNode.getOpcode() == LDC) {
                        targetNode = currentNode;
                        ldc_index = index;
                    }
                }
                if ((targetNode == null) || (ldc_index == -1)) { 
                    return b;
                }
                
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
                mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/asm/WorldTypeClassTransformer", "getWorldHeight", "(I)F");
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
                matched = true;
                break;  // Done with patch
            }
        }
        if (!matched) {
            return b;
        }
        //ASM specific for cleaning up and returning the final bytes for JVM processing.
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS /*| ClassWriter.COMPUTE_FRAMES*/);
        classNode.accept(writer);
        b = writer.toByteArray();
        return b;
    }
    
    public static float getWorldHeight(int wtIndex) {
        return 256.0F;
    }
}

