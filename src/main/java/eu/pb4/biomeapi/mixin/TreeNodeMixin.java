package eu.pb4.biomeapi.mixin;

import eu.pb4.biomeapi.impl.WorldInfoImpl;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(targets = "net/minecraft/world/biome/source/util/MultiNoiseUtil$SearchTree$TreeNode")
public class TreeNodeMixin {
    /*@ModifyConstant(method = {"getSquaredDistance"}, constant = @Constant(intValue = 7))
    private static int biomeApi_replaceConstant(int constant) {
        return WorldInfoImpl.NOISE_COUNT;
    }*/

    @Shadow
    @Final
    protected MultiNoiseUtil.ParameterRange[] parameters;

    /**
     * @author Patbox
     * @reason Custom logic!
     */
    @Overwrite
    public long getSquaredDistance(long[] otherParameters) {
        if (this.parameters[7].getDistance(otherParameters[7]) == 0) {
            return MathHelper.square(this.parameters[0].getDistance(otherParameters[0]))
                    + MathHelper.square(this.parameters[1].getDistance(otherParameters[1]))
                    + MathHelper.square(this.parameters[2].getDistance(otherParameters[2]))
                    + MathHelper.square(this.parameters[3].getDistance(otherParameters[3]))
                    + MathHelper.square(this.parameters[4].getDistance(otherParameters[4]))
                    + MathHelper.square(this.parameters[5].getDistance(otherParameters[5]))
                    + MathHelper.square(this.parameters[6].getDistance(otherParameters[6]))
                    + MathHelper.square(this.parameters[8].getDistance(otherParameters[8]));
        }

        return Long.MAX_VALUE - 1;
    }
}
