package eu.pb4.biomeapi.impl.duck;

import eu.pb4.biomeapi.impl.sampler.ModdedNoiseSampler;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface ExtendedColumnSampler {
    double[] biomeApi_sampleModNoise(double x, double y, double z);

    ModdedNoiseSampler biomeApi_getSampler();
    void biomeApi_setSampler(ModdedNoiseSampler sampler);
}
