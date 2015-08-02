#include "SuperpoweredPlayer.h"
#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include <android/log.h>

#define SAMPLE_RATE_INDEX 2
#define BUFFER_SIZE_INDEX 3

static void playerEventCallback(void *clientData, SuperpoweredAdvancedAudioPlayerEvent event, void *value) {
    if (event == SuperpoweredAdvancedAudioPlayerEvent_LoadSuccess) {
        SuperpoweredAdvancedAudioPlayer *player = *((SuperpoweredAdvancedAudioPlayer **)clientData);
    }
}

static void openSLESCallback(SLAndroidSimpleBufferQueueItf caller, void *pContext) {
	((SuperpoweredPlayer *)pContext)->process(caller);
}

void SuperpoweredPlayer::process(SLAndroidSimpleBufferQueueItf caller) {
    float *stereoBuffer = outputBuffer;

    bool silence = !player->process(stereoBuffer, false, bufferSize, vol, 0.0f, -1.0f);
    if (silence) {
        memset(stereoBuffer, 0, bufferSize * 4);
    } else {
        SuperpoweredStereoMixer::floatToShortInt(stereoBuffer, (short int *)stereoBuffer, bufferSize);
    }
    (*caller)->Enqueue(caller, stereoBuffer, bufferSize * 4);
}

static const SLboolean requireds[2] = { SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE };

SuperpoweredPlayer::SuperpoweredPlayer(const char *path, int *params) : bufferSize(params[BUFFER_SIZE_INDEX]), vol(1.0f * headroom) {
    pthread_mutex_init(&mutex, NULL); // This will keep our player volumes and playback states in sync.

    outputBuffer = (float *)memalign(16, (bufferSize + 16) * sizeof(float) * 2);

    unsigned int sampleRate = params[SAMPLE_RATE_INDEX];

    player = new SuperpoweredAdvancedAudioPlayer(&player, playerEventCallback, sampleRate, 0);
    player->open(path, params[0], params[1]);

    // sync mode?
    player->syncMode = SuperpoweredAdvancedAudioPlayerSyncMode_Tempo;

    // Create the OpenSL ES engine.
    slCreateEngine(&openSLEngine, 0, NULL, 0, NULL, NULL);
    (*openSLEngine)->Realize(openSLEngine, SL_BOOLEAN_FALSE);
    SLEngineItf openSLEngineInterface = NULL;
    (*openSLEngine)->GetInterface(openSLEngine, SL_IID_ENGINE, &openSLEngineInterface);

    // Create the output mix.
    (*openSLEngineInterface)->CreateOutputMix(openSLEngineInterface, &outputMix, 0, NULL, NULL);
    (*outputMix)->Realize(outputMix, SL_BOOLEAN_FALSE);
    SLDataLocator_OutputMix outputMixLocator = { SL_DATALOCATOR_OUTPUTMIX, outputMix };

    // Create the buffer queue player.
    SLDataLocator_AndroidSimpleBufferQueue bufferPlayerLocator = { SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, 1 };
    SLDataFormat_PCM bufferPlayerFormat = {
        SL_DATAFORMAT_PCM,
        2,
        sampleRate * 1000,
        SL_PCMSAMPLEFORMAT_FIXED_16, SL_PCMSAMPLEFORMAT_FIXED_16, SL_SPEAKER_FRONT_LEFT | SL_SPEAKER_FRONT_RIGHT, SL_BYTEORDER_LITTLEENDIAN
    };
    SLDataSource bufferPlayerSource = { &bufferPlayerLocator, &bufferPlayerFormat };
    const SLInterfaceID bufferPlayerInterfaces[1] = { SL_IID_BUFFERQUEUE };
    SLDataSink bufferPlayerOutput = { &outputMixLocator, NULL };
    (*openSLEngineInterface)->CreateAudioPlayer(openSLEngineInterface, &bufferPlayer, &bufferPlayerSource, &bufferPlayerOutput, 1, bufferPlayerInterfaces, requireds);
    (*bufferPlayer)->Realize(bufferPlayer, SL_BOOLEAN_FALSE);

    // Initialize and start the buffer queue.
    (*bufferPlayer)->GetInterface(bufferPlayer, SL_IID_BUFFERQUEUE, &bufferQueue);
    (*bufferQueue)->RegisterCallback(bufferQueue, openSLESCallback, this);
    memset(outputBuffer, 0, bufferSize * 4);
    (*bufferQueue)->Enqueue(bufferQueue, outputBuffer, bufferSize * 4);
    SLPlayItf bufferPlayerPlayInterface;
    (*bufferPlayer)->GetInterface(bufferPlayer, SL_IID_PLAY, &bufferPlayerPlayInterface);
    (*bufferPlayerPlayInterface)->SetPlayState(bufferPlayerPlayInterface, SL_PLAYSTATE_PLAYING);
}

SuperpoweredPlayer::~SuperpoweredPlayer() {
    free(outputBuffer);
    delete player;
    pthread_mutex_destroy(&mutex);
}

void SuperpoweredPlayer::onPlayPause(bool play) {
//    __android_log_print(ANDROID_LOG_INFO, "SuperpoweredPlayer", "Accessing method");
    pthread_mutex_lock(&mutex);
    if (!play) {
        player->pause();
    } else {
        player->play(true);
    }
    pthread_mutex_unlock(&mutex);
}

extern "C" {
	JNIEXPORT void Java_net_tensory_djscratch_MainActivity_SuperpoweredPlayer(JNIEnv *javaEnvironment, jobject self, jstring apkPath, jlongArray offsetAndLength);
	JNIEXPORT void Java_net_tensory_djscratch_MainActivity_onPlayPause(JNIEnv *javaEnvironment, jobject self, jboolean play);
}

static SuperpoweredPlayer *example = NULL;

// Android is not passing more than 2 custom parameters, so, per SuperpoweredExample, pack config
// for file offsets and lengths into an array.
JNIEXPORT void Java_net_tensory_djscratch_MainActivity_SuperpoweredPlayer(JNIEnv *javaEnvironment, jobject self, jstring apkPath, jlongArray params) {
	// Convert the input jlong array to a regular int array.
    jlong *longParams = javaEnvironment->GetLongArrayElements(params, JNI_FALSE);
    int paramsSize = 4;
    int arr[paramsSize];
    for (int n = 0; n < paramsSize; n++) arr[n] = longParams[n];
    javaEnvironment->ReleaseLongArrayElements(params, longParams, JNI_ABORT);

    const char *path = javaEnvironment->GetStringUTFChars(apkPath, JNI_FALSE);
    example = new SuperpoweredPlayer(path, arr);
    javaEnvironment->ReleaseStringUTFChars(apkPath, path);

}

JNIEXPORT void Java_net_tensory_djscratch_MainActivity_onPlayPause(JNIEnv *javaEnvironment, jobject self, jboolean play) {
	example->onPlayPause(play);
}