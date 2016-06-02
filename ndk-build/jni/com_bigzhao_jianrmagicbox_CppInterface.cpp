#include"com_bigzhao_jianrmagicbox_CppInterface.h"
#include "inject.h"
#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <functional>
#include <dlfcn.h>
#include <android/log.h>
#include <vector>
#include <algorithm>

#ifndef LOG_TAG
#define LOG_TAG "ANDROID_LAB"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#endif

JNIEXPORT void JNICALL Java_com_bigzhao_jianrmagicbox_CppInterface_init(JNIEnv *, jobject){
  LOGE("init");
}
