LOCAL_PATH	:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE	:= FLAC
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include \
					$(LOCAL_PATH)/../.. \
					$(LOCAL_PATH)/../../include \
					$(LOCAL_PATH)/../../../include

LOCAL_SRC_FILES	:= \
bitmath.c \
bitreader.c \
bitwriter.c \
cpu.c \
crc.c \
fixed.c \
float.c \
format.c \
lpc.c \
md5.c \
memory.c \
metadata_iterators.c \
metadata_object.c \
ogg_decoder_aspect.c \
ogg_encoder_aspect.c \
ogg_helper.c \
ogg_mapping.c \
stream_decoder.c \
stream_encoder.c \
stream_encoder_framing.c \
window.c


LOCAL_CFLAGS += -DHAVE_CONFIG_H


LOCAL_SHARED_LIBRARIES := ogg

include $(BUILD_SHARED_LIBRARY)
