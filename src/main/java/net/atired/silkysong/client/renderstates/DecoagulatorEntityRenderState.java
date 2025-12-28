package net.atired.silkysong.client.renderstates;

import net.atired.silkysong.entities.DecoagulatorEntity;
import net.minecraft.client.render.item.ItemRenderState;

public class DecoagulatorEntityRenderState extends IcosphereEntityRenderState {
    public final ItemRenderState itemRenderState = new ItemRenderState();
    public DecoagulatorEntity entity = null;
}
