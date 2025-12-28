package net.atired.silkysong.client.test;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.DynamicUniformStorage;
import org.joml.*;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

public class DynamicTESTUniforms {

    public static final int SIZE = new Std140SizeCalculator().putFloat().putFloat().putVec3().get();
    private static final int DEFAULT_CAPACITY = 2;

    public static final GpuBuffer BUFFER = RenderSystem.getDevice().createBuffer(() -> "Dynamic TEST Transforms UBO", 136, SIZE);
    public static void set(Vector3fc cameraPos, float powuh){
        try(MemoryStack stack = MemoryStack.stackPush()){
            ByteBuffer byteBuffer = Std140Builder.onStack(stack,SIZE).putFloat(powuh).putVec3(cameraPos).get();
            RenderSystem.getDevice().createCommandEncoder().writeToBuffer(BUFFER.slice(),byteBuffer);
        }
    }
}
