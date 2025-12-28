package net.atired.silkysong.client.renderstates;

import net.atired.silkysong.entities.ManEntity;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;

public class ManEntityRenderState extends BipedEntityRenderState {
    public final ItemRenderState itemRenderState = new ItemRenderState();
    public ManEntity entity = null;
}
