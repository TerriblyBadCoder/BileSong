package net.atired.silkysong.client.renderstates;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.util.math.Vec3d;

public class MissileToadEntityRenderState extends LivingEntityRenderState {
    public final ItemRenderState itemRenderState = new ItemRenderState();
    public float bloat = 0.0f;
    public float health = 100.0f;
    public float fallticks = 0.0f;
    public Vec3d tonguePos = Vec3d.ZERO;
}
