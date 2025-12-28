package net.atired.silkysong.misc;
//Based on the math class from Alex's Caves
public class SKgetDatNoise {

    public static float sampleNoise2D(int x, int z, float simplexSampleRate) {
        return (float) ((SKsimplexNoise.noise((x + simplexSampleRate) / simplexSampleRate, (z + simplexSampleRate) / simplexSampleRate)));
    }

    public static float sampleNoise3D(int x, int y, int z, float simplexSampleRate) {
        return (float) ((SKsimplexNoise.noise((x + simplexSampleRate) / simplexSampleRate, (y + simplexSampleRate) / simplexSampleRate, (z + simplexSampleRate) / simplexSampleRate)));
    }

    public static float sampleNoise3D(float x, float y, float z, float simplexSampleRate) {
        return (float) ((SKsimplexNoise.noise((x + simplexSampleRate) / simplexSampleRate, (y + simplexSampleRate) / simplexSampleRate, (z + simplexSampleRate) / simplexSampleRate)));
    }


}
