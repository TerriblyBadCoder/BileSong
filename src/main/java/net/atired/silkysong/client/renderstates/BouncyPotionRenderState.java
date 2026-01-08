package net.atired.silkysong.client.renderstates;

import net.minecraft.client.render.entity.state.FlyingItemEntityRenderState;
import org.joml.Vector3f;

public class BouncyPotionRenderState extends FlyingItemEntityRenderState {
    public Vector3f colours;
    public float lerpThingie = 1.0f;
}
