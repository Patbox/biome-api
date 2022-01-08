package eu.pb4.biomeapi.mixin;

import eu.pb4.biomeapi.impl.WorldInfoImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(targets = {"net/minecraft/world/biome/source/util/MultiNoiseUtil$SearchTree"})
public class SearchTreeMixin {
    @ModifyConstant(method = {"create", "getEnclosingParameters"}, constant = @Constant(intValue = 7))
    private static int biomeApi_replaceConstant(int constant) {
        return WorldInfoImpl.NOISE_COUNT;
    }
}
