package eu.pb4.biomeapi.mixin;

import eu.pb4.biomeapi.api.MaterialRulesInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(VanillaSurfaceRules.class)
public abstract class VanillaSurfaceRulesMixin {

    @Inject(method = "createDefaultRule", at = @At("RETURN"), cancellable = true)
    private static void biomeApi_injectOverworldRules(boolean surface, boolean bedrockRoof, boolean bedrockFloor, CallbackInfoReturnable<MaterialRules.MaterialRule> cir) {
        var list = new ArrayList<MaterialRules.MaterialRule>();

        for (var ruleInit : FabricLoader.getInstance().getEntrypointContainers("pb_biome_api:material_rules", MaterialRulesInitializer.class)) {
            ruleInit.getEntrypoint().addOverworldRules(surface, bedrockRoof, bedrockFloor, list::add);
        }

        list.add(cir.getReturnValue());

        cir.setReturnValue(MaterialRules.sequence(list.toArray(new MaterialRules.MaterialRule[0])));
    }

    @Inject(method = "createNetherSurfaceRule", at = @At("RETURN"), cancellable = true)
    private static void biomeApi_injectNetherRules(CallbackInfoReturnable<MaterialRules.MaterialRule> cir) {
        var list = new ArrayList<MaterialRules.MaterialRule>();

        for (var ruleInit : FabricLoader.getInstance().getEntrypointContainers("pb_biome_api:material_rules", MaterialRulesInitializer.class)) {
            ruleInit.getEntrypoint().addNetherRules(list::add);
        }

        list.add(cir.getReturnValue());

        cir.setReturnValue(MaterialRules.sequence(list.toArray(new MaterialRules.MaterialRule[0])));
    }

    @Inject(method = "getEndStoneRule", at = @At("RETURN"), cancellable = true)
    private static void biomeApi_injectEndRules(CallbackInfoReturnable<MaterialRules.MaterialRule> cir) {
        var list = new ArrayList<MaterialRules.MaterialRule>();

        for (var ruleInit : FabricLoader.getInstance().getEntrypointContainers("pb_biome_api:material_rules", MaterialRulesInitializer.class)) {
            ruleInit.getEntrypoint().addEndRules(list::add);
        }

        list.add(cir.getReturnValue());

        cir.setReturnValue(MaterialRules.sequence(list.toArray(new MaterialRules.MaterialRule[0])));
    }
}
