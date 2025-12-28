package net.atired.silkysong.client.renderstates;

import net.atired.silkysong.entities.BileMosqoEntity;
import net.atired.silkysong.entities.ManEntity;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;

public class BileMosqoEntityRenderState extends LivingEntityRenderState {
    public final ItemRenderState itemRenderState = new ItemRenderState();
    public float velocity = 0.0f;
    public float health = 100.0f;
    public float fallticks = 0.0f;
}
