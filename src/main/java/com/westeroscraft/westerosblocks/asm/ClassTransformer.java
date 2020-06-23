package com.westeroscraft.westerosblocks.asm;

import java.util.HashMap;
import java.util.Iterator;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.westeroscraft.westerosblocks.WesterosBlocks;

import org.objectweb.asm.Opcodes;

import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer, Opcodes {
    public enum TransformState {
        NONE,
        NOTMATCHED,
        CLIENTONLY,
        DONE
    }
    public static HashMap<String, TransformState> patchState = new HashMap<String, TransformState>();
    
    @Override
    public byte[] transform(String name, String tname, byte[] bytes)
    {
        boolean match = true;
        
        if (name.equals("ayk")) { // Obfuscated name for net.minecraft.world.WorldProvider
            bytes = transformWorldProvider(name, bytes, true);
        }
        else if (name.equals("net.minecraft.world.WorldProvider")) {    // Clear name
            bytes = transformWorldProvider(name, bytes, false);
        }
        else 
        if (name.equals("aqy")) { // Obfuscated name for net.minecraft.block.BlockGlass
            bytes = transformBlockGlass(name, bytes, true);
        }
        else if (name.equals("net.minecraft.block.BlockGlass")) {    // Clear name
            bytes = transformBlockGlass(name, bytes, false);
        }
        else 
        if (name.equals("auo")) { // Obfuscated name for net.minecraft.block.BlockPane
            bytes = transformBlockGlassPane(name, bytes, true);
        }
        else if (name.equals("net.minecraft.block.BlockPane")) {    // Clear name
            bytes = transformBlockGlassPane(name, bytes, false);
        }
        else 
        if (name.equals("fd")) { // Obfuscated name for ObjectIntIdentityMap
            bytes = transformObjectIntIdentityMap(name, bytes, true);
        }
        else if (name.equals("net.minecraft.util.ObjectIntIdentityMap")) {    // Clear name
            bytes = transformObjectIntIdentityMap(name, bytes, false);
        }
        else
        if (name.equals("ko")) {
        	bytes = transformSPacketTimeUpdate(name, bytes, true);
        }
        else if (name.equals("net.minecraft.network.play.server.SPacketTimeUpdate")) {
        	bytes = transformSPacketTimeUpdate(name, bytes, false);
        }
        else
        if (name.equals("jc")) {
        	bytes = transformSPacketChangeGameState(name, bytes, true);
        }
        else if (name.equals("net.minecraft.network.play.server.SPacketChangeGameState")) {
        	bytes = transformSPacketChangeGameState(name, bytes, false);
        }
        else
        if (name.equals("aqf")) {
            bytes = transformEnchantmentTable(name, bytes, true);
        }
        else if (name.equals("net.minecraft.block.BlockEnchantmentTable")) {
            bytes = transformEnchantmentTable(name, bytes, false);
        }
        else {
            match = false;
        }
        if (match) {
            TransformState ts = patchState.get(name);
            if (ts == null) {
                patchState.put(name, TransformState.NOTMATCHED);
            }
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
//    private int findOpSequence(MethodNode meth, int[] opcodes) {
//        Iterator<AbstractInsnNode> iter = meth.instructions.iterator();
//        int index = -1;
//        int seqindex = 0;
//        int seqstartindex = -1;
//
//        while (iter.hasNext()) {
//            index++;
//            AbstractInsnNode node = iter.next();
//            if (node.getOpcode() == opcodes[seqindex]) {
//                if (seqindex == 0) seqstartindex = index;   // Remember start
//                seqindex++;
//                if (seqindex == opcodes.length) {   // Done?
//                    return seqstartindex;
//                }
//            }
//            else {  // Mismatch
//                seqindex = 0;
//                seqstartindex = -1;
//            }
//        }
//        return -1;
//    }
    
    @SuppressWarnings("unused")
    private void printMethod(MethodNode meth) {
        Iterator<AbstractInsnNode> iter = meth.instructions.iterator();
        System.out.println("printMethod(" + meth.name + ")");
        while (iter.hasNext()) {
            AbstractInsnNode n = iter.next();
            System.out.println("  " + n.getOpcode() + " (" + n.getType() + " - " + n.getClass().getName() + ")");
        }
    }
        
    
    private byte[] transformWorldProvider(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";
        String targetMethodSig = "";

        //WesterosBlocks.log.debug("Checking class " + name);
        
        if (obfus)
            targetMethodName ="f";
        else
            targetMethodName ="getCloudHeight";
        targetMethodSig = "()F";
        
        //set up ASM class manipulation stuff. Consult the ASM docs for details
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(b);
        classReader.accept(classNode, 0);

        // Now find the method
        MethodNode m = findMethod(classNode, targetMethodName, targetMethodSig);
        if (m == null) {
            //WesterosBlocks.log.warn("Cannot find "  + targetMethodName + "() in " + name + " for patching");
            patchState.put(name, TransformState.CLIENTONLY);
            return b;
        }
        // Replace method implementation
        MethodVisitor mv = m;
        m.instructions.clear();
        m.localVariables.clear();

        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(188, l0);
        mv.visitFieldInsn(GETSTATIC, "com/westeroscraft/westerosblocks/asm/ClassTransformer", "cloudHeight", "F");
        mv.visitInsn(FRETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLocalVariable("this", "Lcom/westeroscraft/westerosblocks/asm/ClassTransformer;", null, l0, l1, 0);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
        
        //ASM specific for cleaning up and returning the final bytes for JVM processing.
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        b = writer.toByteArray();
        
        //WesterosBlocks.log.debug("Method " + targetMethodName + "() of " + name + " patched!");
        patchState.put(name, TransformState.DONE);

        return b;
    }
    
    public static float cloudHeight = 256.0f;
    
    // Template for method patched in, above
    //public float getCloudHeight() {
    //    return cloudHeight;
    //}
    
    // Patch BlockGlass.getBlockLayer to be TRANSLUCENT vs CUTOUT - support alpha in textures
    private byte[] transformBlockGlass(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";
        String targetMethodSig = "";

        //System.out.println("Checking class " + name + ": transformBlockGlass");

        if (obfus) {
            targetMethodName ="f";
            targetMethodSig = "()Lamm;";
        }
        else {
            targetMethodName ="getBlockLayer";
            targetMethodSig = "()Lnet/minecraft/util/BlockRenderLayer;";
        }
        //set up ASM class manipulation stuff. Consult the ASM docs for details
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(b);
        classReader.accept(classNode, 0);

        // Now find the method
        MethodNode m = findMethod(classNode, targetMethodName, targetMethodSig);
        if (m == null) {
        	//System.out.println("Cannot find "  + targetMethodName + targetMethodSig + " in " + name + " for patching");
            patchState.put(name, TransformState.CLIENTONLY);
            return b;
        }
        // Replace method implementation
        MethodVisitor mv = m;
        m.instructions.clear();
        m.localVariables.clear();
        
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(27, l0);
        if (obfus) {
            mv.visitFieldInsn(GETSTATIC, "amm", "d", "Lamm;");
        }
        else {
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/BlockRenderLayer", "TRANSLUCENT", "Lnet/minecraft/util/BlockRenderLayer;");
        }
        mv.visitInsn(ARETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        if (obfus) {
            mv.visitLocalVariable("this", "Laqw;", null, l0, l1, 0);
        }
        else {
            mv.visitLocalVariable("this", "Lnet/minecraft/block/BlockGlass;", null, l0, l1, 0);
        }
        mv.visitMaxs(1, 1);
        mv.visitEnd();
        
        //ASM specific for cleaning up and returning the final bytes for JVM processing.
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        b = writer.toByteArray();
        
        //System.out.println("Method " + targetMethodName + targetMethodSig + " of " + name + " patched!");
        patchState.put(name, TransformState.DONE);

        return b;
    }
    
    //Template for method patched in above
    //@SideOnly(Side.CLIENT)
    //public BlockRenderLayer getBlockLayer()
    //{
    //    return BlockRenderLayer.TRANSLUCENT;
    //}

    // Patch BlockPane.getBlockLayer() to return TRANSLUCENT (support alpha on textures)
    private byte[] transformBlockGlassPane(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";
        String targetMethodSig = "";

        //System.out.println("Checking class " + name + ":transformBlockGlassPane");
        
        if (obfus) {
            targetMethodName ="f";
            targetMethodSig = "()Lamm;";
        }
        else {
            targetMethodName ="getBlockLayer";
            targetMethodSig = "()Lnet/minecraft/util/BlockRenderLayer;";
        }
        //set up ASM class manipulation stuff. Consult the ASM docs for details
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(b);
        classReader.accept(classNode, 0);

        // Now find the method
        MethodNode m = findMethod(classNode, targetMethodName, targetMethodSig);
        if (m == null) {
        	//System.out.println("Cannot find "  + targetMethodName + "() in " + name + " for patching");
            patchState.put(name, TransformState.CLIENTONLY);
            return b;
        }
        // Replace method implementation
        MethodVisitor mv = m;
        m.instructions.clear();
        m.localVariables.clear();
        
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(27, l0);
        if (obfus) {
            mv.visitFieldInsn(GETSTATIC, "amm", "d", "Lamm;");
        }
        else {
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/BlockRenderLayer", "TRANSLUCENT", "Lnet/minecraft/util/BlockRenderLayer;");
        }
        mv.visitInsn(ARETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        if (obfus) {
            mv.visitLocalVariable("this", "Laum;", null, l0, l1, 0);
        }
        else {
            mv.visitLocalVariable("this", "Lnet/minecraft/block/BlockPane;", null, l0, l1, 0);
        }
        mv.visitMaxs(1, 1);
        mv.visitEnd();
        
        //ASM specific for cleaning up and returning the final bytes for JVM processing.
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        b = writer.toByteArray();

        patchState.put(name, TransformState.DONE);

        //System.out.println("Method " + targetMethodName + "() of " + name + " patched!");

        return b;
    }

    //Template for method patched in above
    //@SideOnly(Side.CLIENT)
    //public BlockRenderLayer getBlockLayer()
    //{
    //    return BlockRenderLayer.TRANSLUCENT;
    //}

    // Patch sizing logic for ObjectIntIdentityMap: incorrectly assumpes packed indexes
    private byte[] transformObjectIntIdentityMap(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";
        String targetMethodSig = "";
        String targetClassSig  = "";
        String targetFieldID = "";

        System.out.println("Checking class " + name);

        if (obfus) {
            targetMethodName ="a";
            targetClassSig  = "fd";
            targetFieldID = "b";
        }
        else {
            targetMethodName ="size";
            targetClassSig  = "net/minecraft/util/ObjectIntIdentityMap";
            targetFieldID = "objectList";
        }
        targetMethodSig = "()I";
        //set up ASM class manipulation stuff. Consult the ASM docs for details
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(b);
        classReader.accept(classNode, 0);

        // Now find the method
        MethodNode m = findMethod(classNode, targetMethodName, targetMethodSig);
        if (m == null) {
        	System.out.println("Cannot find "  + targetMethodName + "() in " + name + " for patching");
            return b;
        }
        // Replace method implementation
        MethodVisitor mv = m;
        m.instructions.clear();
        m.localVariables.clear();

        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(397, l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, targetClassSig, targetFieldID, "Ljava/util/List;");
        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "size", "()I", true);
        mv.visitInsn(IRETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLocalVariable("this", "L" + targetClassSig + ";", "L" + targetClassSig + "<TT;>;", l0, l1, 0);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
        
        //ASM specific for cleaning up and returning the final bytes for JVM processing.
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        b = writer.toByteArray();
        
        //System.out.println("Method " + targetMethodName + "() of " + name + " patched!");
        patchState.put(name, TransformState.DONE);

        return b;
    }
    
    // Template for method replaced above (length of list shows ID range, not map size)
//    private List b;
//    public int size()
//    {
//        return this.b.size();
//    }
    
    // Provide hook in SPacketTimeUpdate.readPacketData() to fudge client time-of-day (client side hook)
    private byte[] transformSPacketTimeUpdate(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";
        String targetMethodSig = "";
        String targetClassSig  = "";
        String targetFieldID = "";
        String targetFieldID2 = "";
        String targetClassPacketBuffer = "";

        //System.out.println("Checking class " + name);
        
        if (obfus) {
            targetClassSig  = "ko";
            targetMethodName ="a";
            targetMethodSig = "(Lgy;)V";
            targetFieldID = "b";
            targetFieldID2 = "a";
            targetClassPacketBuffer = "gy";
        }
        else {
            targetClassSig  = "net/minecraft/network/play/server/SPacketTimeUpdate";
            targetMethodName ="readPacketData";
            targetMethodSig = "(Lnet/minecraft/network/PacketBuffer;)V";
            targetFieldID = "worldTime";
            targetFieldID2 = "totalWorldTime";
            targetClassPacketBuffer = "net/minecraft/network/PacketBuffer";
        }
        
        //set up ASM class manipulation stuff. Consult the ASM docs for details
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(b);
        classReader.accept(classNode, 0);

        // Now find the method
        MethodNode m = findMethod(classNode, targetMethodName, targetMethodSig);
        if (m == null) {
        	//System.out.println("Cannot find "  + targetMethodName + "() in " + name + " for patching");
            patchState.put(name, TransformState.CLIENTONLY);
            return b;
        }
        MethodVisitor mv = m;
        m.instructions.clear();
        m.localVariables.clear();
        
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(493, l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, targetClassPacketBuffer, "readLong", "()J", false);
        mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/commands/PTimeCommand", "processTotalWorldTime", "(J)J", false);
        mv.visitFieldInsn(PUTFIELD, targetClassSig, targetFieldID2, "J");
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLineNumber(494, l1);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, targetClassPacketBuffer, "readLong", "()J", false);
        mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/commands/PTimeCommand", "processWorldTime", "(J)J", false);
        mv.visitFieldInsn(PUTFIELD, targetClassSig, targetFieldID, "J");
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLineNumber(495, l2);
        mv.visitInsn(RETURN);
        Label l3 = new Label();
        mv.visitLabel(l3);
        mv.visitLocalVariable("this", "L" + targetClassSig + ";", null, l0, l3, 0);
        mv.visitLocalVariable("buf", "L" + targetClassPacketBuffer + ";", null, l0, l3, 1);
        mv.visitMaxs(3, 2);
        mv.visitEnd();
        
        //System.out.println("Method " + targetMethodName + "() of " + name + " patched!");
        
        //ASM specific for cleaning up and returning the final bytes for JVM processing.
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        b = writer.toByteArray();
        patchState.put(name, TransformState.DONE);

        return b;
    }    
    
    // Template for replacement method for above
    //private long worldTime;
    //private long totalWorldTime;
    //public void readPacketData(PacketBuffer buf) throws IOException
    //{
    //    // Transform time 
    //    this.totalWorldTime = PTimeCommand.processTotalWorldTime(buf.readLong());
    //    this.worldTime = PTimeCommand.processWorldTime(buf.readLong());
    //}

    // Client side hook in SPacketChangeGameState.readPacketData() to fudge weather data
    private byte[] transformSPacketChangeGameState(String name, byte[] b, boolean obfus) {
        String targetMethodName = "";
        String targetMethodSig = "";
        String targetClassSig  = "";
        String targetParmClassSig = "";
        String targetFieldID = "";
        String targetFieldID2 = "";

        //System.out.println("Checking class " + name);
        
        if (obfus) {
            targetClassSig  = "jc";
            targetMethodName ="a";
            targetMethodSig = "(Lgy;)V";
            targetFieldID = "b";
            targetFieldID2 = "c";
            targetParmClassSig = "gy";
        }
        else {
            targetClassSig  = "net/minecraft/network/play/server/SPacketChangeGameState";
            targetMethodName ="readPacketData";
            targetMethodSig = "(Lnet/minecraft/network/PacketBuffer;)V";
            targetFieldID = "state";
            targetFieldID2 = "value";
            targetParmClassSig = "net/minecraft/network/PacketBuffer";
        }
        
        //set up ASM class manipulation stuff. Consult the ASM docs for details
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(b);
        classReader.accept(classNode, 0);

        // Now find the method
        MethodNode m = findMethod(classNode, targetMethodName, targetMethodSig);
        if (m == null) {
        	//System.out.println("Cannot find "  + targetMethodName + "() in " + name + " for patching");
            patchState.put(name, TransformState.CLIENTONLY);
            return b;
        }
        MethodVisitor mv = m;
        m.instructions.clear();
        m.localVariables.clear();
        
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(592, l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, targetParmClassSig, "readUnsignedByte", "()S", false);
        mv.visitFieldInsn(PUTFIELD, targetClassSig, targetFieldID, "I");
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLineNumber(593, l1);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, targetParmClassSig, "readFloat", "()F", false);
        mv.visitFieldInsn(PUTFIELD, targetClassSig, targetFieldID2, "F");
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLineNumber(594, l2);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESTATIC, "com/westeroscraft/westerosblocks/commands/PWeatherCommand", "processChangeGameState", "(L" + targetClassSig + ";)V", false);
        Label l3 = new Label();
        mv.visitLabel(l3);
        mv.visitLineNumber(595, l3);
        mv.visitInsn(RETURN);
        Label l4 = new Label();
        mv.visitLabel(l4);
        mv.visitLocalVariable("this", "L" + targetClassSig + ";", null, l0, l4, 0);
        mv.visitLocalVariable("buf", "L" + targetParmClassSig + ";", null, l0, l4, 1);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
        
        //System.out.println("Method " + targetMethodName + "() of " + name + " patched!");
        
        //ASM specific for cleaning up and returning the final bytes for JVM processing.
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        b = writer.toByteArray();
        patchState.put(name, TransformState.DONE);

        return b;
    }    

    // Template for replacement method, above
    //public int state;
    //public float value;
    //public SPacketChangeGameState pkt;
    //public void readPacketData(PacketBuffer buf) throws IOException
    //{
    //    this.state = buf.readUnsignedByte();
    //    this.value = buf.readFloat();
    //    PWeatherCommand.processChangeGameState(pkt);
    //}
    
    private class AddEnchantMethodVisitor extends ClassVisitor {
        String targetMethodName = "";
        String targetMethodSig = "";
        String targetClassSig  = "";
        String targetRetClass = "";
        String targetRetEnum = "";

        public AddEnchantMethodVisitor(ClassVisitor cv, boolean obfus) {
            super(Opcodes.ASM5, cv);
            if (obfus) {
                targetClassSig  = "aqd";
                targetMethodName ="f";
                targetMethodSig = "()Lamm;";
                targetRetClass = "amm";
                targetRetEnum = "c";
            }
            else {
                targetClassSig  = "net/minecraft/block/BlockEnchantmentTable";
                targetMethodName ="getBlockLayer";
                targetMethodSig = "()Lnet/minecraft/util/BlockRenderLayer;";
                targetRetClass = "net/minecraft/util/BlockRenderLayer";
                targetRetEnum = "CUTOUT";
            }
        }
        @Override
        public void visitEnd() {
            MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, targetMethodName, targetMethodSig, null, null);
            AnnotationVisitor av0 = mv.visitAnnotation("Lnet/minecraftforge/fml/relauncher/SideOnly;", true);
            av0.visitEnum("value", "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
            av0.visitEnd();
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitFieldInsn(GETSTATIC, targetRetClass, targetRetEnum, "L" + targetRetClass + ";");
            mv.visitInsn(ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "L" + targetClassSig + ";", null, l0, l1, 0);
            mv.visitMaxs(1, 1);
            mv.visitEnd();

            super.visitEnd();
        }
    }
    
    // Inject extra method - client side method getBlockLayer() to return CUTOUT layer
    private byte[] transformEnchantmentTable(String name, byte[] b, boolean obfus) {
        //set up ASM class manipulation stuff. Consult the ASM docs for details
        ClassReader classReader = new ClassReader(b);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS + ClassWriter.COMPUTE_FRAMES);
        ClassVisitor visitor = new AddEnchantMethodVisitor(writer, obfus);
        classReader.accept(visitor, 0);
        b = writer.toByteArray();

        //System.out.println("Adding method to " + name + "(enchantmenttable)");
        
        patchState.put(name, TransformState.DONE);

        return b;
    }    
    
    //Template for method added in above
    //@SideOnly(Side.CLIENT)
    //public BlockRenderLayer getBlockLayer()
    //{
    //    return BlockRenderLayer.CUTOUT;
    //}

}

