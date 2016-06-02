#include"com_bigzhao_cbtcrack_Hack.h"
#include "dependency.h"
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
/*
template<class T,class... ARGS>
T callStatic(const char*sym,ARGS...args){
	typedef T(*F)(ARGS...);
	F f=(F)dlsym(RTLD_DEFAULT,sym);
	LOGE("callStatic:%x",(int)f);
	return f(args...);
}

template<class T>
T getElement(const char*sym){
	T t=(T)dlsym(RTLD_DEFAULT,sym);
	return t;
}

template<int bufsize=256>
void printbuf(const char*buf,int len) {
	char dest[bufsize];
	const char*p = buf;
	int l = 0;
	for (int i = 0; i < len; ++i) {
		l += sprintf(dest + l, "%x,", buf[i]);
	}
	LOGE("buf:[%s]", dest);
}

template<class T,class THIS,class... ARGS>
T callMember(const char*sym,THIS*_this,ARGS...args){
    typedef T (THIS::*F)(ARGS...);
	LOGE("callMember:SIZE:%d", sizeof(F));
	F f;
	char buf[sizeof(F)];
	void *x = dlsym(RTLD_DEFAULT, sym);
	LOGE("callMember:%x", (int)x);
	memset(buf, 0, 8);
	memcpy(buf, &x, 4);
	memcpy(&f, buf, 8);
	printbuf((char*)&f, 8);
	return (_this->*f)(args...);
}
*/

JNIEXPORT jbyteArray JNICALL Java_com_bigzhao_cbtcrack_Hack_readFile(JNIEnv *env, jclass cls, jstring str){
	
	return nullptr;
}

const char*err;
typedef const char* (*fn_s)();
JNIEXPORT jstring JNICALL Java_com_bigzhao_cbtcrack_Hack_getVersion(JNIEnv *env, jclass) {
	LOGE("start");
	
}
