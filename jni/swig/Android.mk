LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := sndfile_wrap
LOCAL_SHARED_LIBRARIES := sndfile

LOCAL_SRC_FILES := libsndfile_wrap.c

include $(BUILD_SHARED_LIBRARY)
