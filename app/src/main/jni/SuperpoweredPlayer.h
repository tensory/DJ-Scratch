#ifndef Header_SuperpoweredPlayer
#define Header_SuperpoweredPlayer

#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>

#include "SuperpoweredPlayer.h"
#include "SuperpoweredAdvancedAudioPlayer.h"

class SuperpoweredPlayer {
public:
    SuperpoweredPlayer(const char *path, int *params);
    ~SuperpoweredPlayer();

    void process(SLAndroidSimpleBufferQueueItf caller);
    void onPlayPause(bool play);

private:
    SLObjectItf openSLEngine, outputMix, bufferPlayer;
    SLAndroidSimpleBufferQueueItf bufferQueue;

    SuperpoweredAdvancedAudioPlayer *playerA;

    float *outputBuffer;
    int bufferSize;
};

#endif