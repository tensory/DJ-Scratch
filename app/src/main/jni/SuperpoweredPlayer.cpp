#include "SuperpoweredPlayer.h"
#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include <android/log.h>

#define SAMPLE_RATE_INDEX = 0
#define BUFFER_SIZE_INDEX = 1


SuperpoweredPlayer::SuperpoweredPlayer(const char *path, int *params) : bufferSize(params[1]) {
    outputBuffer = (float *)memalign(16, (bufferSize + 16) * sizeof(float) * 2);
}

SuperpoweredPlayer::~SuperpoweredPlayer() {
    free(outputBuffer);
    delete playerA;
}

void SuperpoweredPlayer::onPlayPause(bool play) {
//    __android_log_print(ANDROID_LOG_VERBOSE, "SuperpoweredPlayer", "Works");
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
    int paramsSize = 2;
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