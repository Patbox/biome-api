package eu.pb4.biomeapi.impl.sampler;

public interface ModdedNoiseSampler {
    default double sample(double x, double y, double z) {
        return sampleUnfiltered(x, y, z);
    }

    double sampleUnfiltered(double x, double y, double z);
}
