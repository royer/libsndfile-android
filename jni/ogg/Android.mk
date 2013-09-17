LOCAL_PATH:=$(call my-dir)

include $(CLEAR_VARS)

LOCAL_C_INCLUDES	:= $(LOCAL_PATH)/../include
LOCAL_MODULE		:= ogg
LOCAL_SRC_FILES		:= src/bitwise.c src/framing.c

include $(BUILD_SHARED_LIBRARY)
