package net.atired.silkysong.init;

import net.atired.silkysong.SilkySong;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SKsoundEventInit {
    public static final RegistryEntry.Reference<SoundEvent> MAN = registerSound("music_disc.man");
    private static  RegistryEntry.Reference<SoundEvent> registerSound(String id) {
        Identifier identifier = SilkySong.id(id);
        return Registry.registerReference(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    public static void init() {
    }
}
