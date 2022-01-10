package eu.pb4.biomeapi.impl.sampler;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface ModdedNoiseSampler {
    static ModdedNoiseSampler noop() {
        return (x, y, z) -> new double[]{0, 0};
    }

    default double[] sample(double x, double y, double z) {
        return sampleUnfiltered(x, y, z);
    }

    double[] sampleUnfiltered(double x, double y, double z);
}
