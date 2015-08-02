#ifndef Header_SuperpoweredPlayer
#define Header_SuperpoweredPlayer

#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <math.h>
#include <pthread.h>

#include "SuperpoweredPlayer.h"
#include "SuperpoweredAdvancedAudioPlayer.h"
#include "SuperpoweredMixer.h"

#define HEADROOM_DECIBEL 3.0f
static const float headroom = powf(10.0f, -HEADROOM_DECIBEL * 0.025);

class SuperpoweredPlayer {
public:
    SuperpoweredPlayer(const char *path, int *params);
    ~SuperpoweredPlayer();

    void process(SLAndroidSimpleBufferQueueItf caller);
    void onPlayPause(bool play);

private:
    SLObjectItf openSLEngine, outputMix, bufferPlayer;
    SLAndroidSimpleBufferQueueItf bufferQueue;

    SuperpoweredAdvancedAudioPlayer *player;
    float vol;
    pthread_mutex_t mutex;

    float *outputBuffer;
    int bufferSize;
};

#endif