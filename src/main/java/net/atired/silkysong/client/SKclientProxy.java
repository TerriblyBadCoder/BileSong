package net.atired.silkysong.client;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.atired.silkysong.client.test.DynamicTESTUniforms;
import org.joml.Vector3f;

public class SKclientProxy {
    public SKclientProxy(){

    }
    public GpuBuffer buffer;
    public DynamicTESTUniforms sillyUniforms;
    public Vector3f colours =new Vector3f(0.0f,0.0f,0.0f);
    public float vingette =0.0f;
    public float silky =0.0f;
    public float slopped =0.0f;
}
