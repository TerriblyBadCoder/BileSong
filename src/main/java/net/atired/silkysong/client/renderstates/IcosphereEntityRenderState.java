package net.atired.silkysong.client.renderstates;

import net.atired.silkysong.entities.IcosphereEntity;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;

public class IcosphereEntityRenderState extends EntityRenderState {
    public final ItemRenderState itemRenderState = new ItemRenderState();
    public IcosphereEntity entity = null;
}
