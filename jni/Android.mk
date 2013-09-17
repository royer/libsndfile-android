LOCAL_PATH := $(call my-dir)
MY_LOCAL_PATH := $(LOCAL_PATH)

include $(CLEAR_VARS)

include $(LOCAL_PATH)/ogg/Android.mk
include $(MY_LOCAL_PATH)/vorbis/Android.mk
include $(MY_LOCAL_PATH)/flac/Android.mk
include $(MY_LOCAL_PATH)/libsndfile/Android.mk
include $(MY_LOCAL_PATH)/swig/Android.mk
#include $(addprefix $(LOCAL_PATH)/, $(addsuffix /Android.mk, \
	ogg \
	vorbis \
	flac \
	libsndfile \
	))
