LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := MagicBox
LOCAL_CFLAGS := -ldl -std=c++11 -D_STLP_USE_NEWALLOC
LOCAL_LDLIBS := \
	-llog \
	-lz \
	-lm \
#	-lD:\AndroidWS\cbt_crack\app\src\main\jniLibs\armeabi\libcocos2djs.so 
	
#LOCAL_STATIC_LIBRARIES += 

LOCAL_SRC_FILES := \
	com_bigzhao_cbtcrack_Hack.cpp \

#LOCAL_C_INCLUDES += D:\AndroidWS\cbt_crack\app\src\main\jni
#LOCAL_C_INCLUDES += D:\AndroidWS\cbt_crack\app\src\debug\jni

include $(BUILD_SHARED_LIBRARY)
