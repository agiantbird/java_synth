package com.agiantbird.synth;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;


public class AudioThread extends Thread {

    static final int BUFFER_SIZE = 512;
    static final int BUFFER_COUNT = 8;

    private final int[] buffers = new int[BUFFER_COUNT];
    private final long device = alcOpenDevice(alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER));
    private final long context = alcCreateContext(device, new int[1]);
    private final int source;

    private int bufferIndex;

    AudioThread() {
        alcMakeContextCurrent(context);
        AL.createCapabilities(ALC.createCapabilities(device));
        source = alGenSources();
        for (int i = 0; i < BUFFER_COUNT; i++) {
            bufferSamples(new short[0]);
        }
        alSourcePlay(source);
        // catch internal exceptions (alc can't catch java exceptions)
        start();
    }

    private void bufferSamples(short[] samples) {
        int buf = buffers[bufferIndex++];
        alBufferData(buf, AL_FORMAT_MONO16, samples, Synthesizer.AudioInfo.SAMPLE_RATE);
        alSourceQueueBuffers(source, buf);
        // get back to start of queue once bufferIndex == BUFFER_COUNT
        bufferIndex %= BUFFER_COUNT;
    }

    private void catchInternalExceptions() {
        int err = alcGetError(device);
        if (err != ALC_NO_ERROR) {

        }
    }

}
