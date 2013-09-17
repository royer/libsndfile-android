LOCAL_PATH	:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_C_INCLUDES	:= $(LOCAL_PATH)/../../include

LOCAL_MODULE		:= sndfile
LOCAL_CFLAGS		+= -g -std=gnu99 -Wall -Wextra -Wdeclaration-after-statement -Wpointer-arith -funsigned-char -D_FORTIFY_SOURCE=2 -Wstrict-prototypes -Wmissing-prototypes -Waggregate-return -Wcast-align -Wcast-qual -Wnested-externs -Wshadow -Wbad-function-cast -Wwrite-strings -Wundef -Wmissing-declarations -Wuninitialized -Winit-self -pipe 
LOCAL_SHARED_LIBRARIES:= ogg FLAC vorbis
LOCAL_SRC_FILES		:= \
	aiff.c alac.c alaw.c au.c audio_detect.c avr.c broadcast.c \
	caf.c cart.c chanmap.c chunk.c command.c common.c dither.c \
	double64.c dwd.c dwvw.c file_io.c flac.c float32.c g72x.c \
	gsm610.c htk.c id3.c ima_adpcm.c ima_oki_adpcm.c interleave.c \
	ircam.c macbinary3.c macos.c mat4.c mat5.c mpc2k.c ms_adpcm.c \
	new.c nist.c ogg.c ogg_opus.c ogg_pcm.c ogg_speex.c ogg_vorbis.c \
	paf.c pcm.c pvf.c raw.c rf64.c rx2.c sd2.c sds.c sndfile.c \
	strings.c svx.c txw.c ulaw.c voc.c vox_adpcm.c w64.c wav.c \
	wav_w64.c windows.c wve.c xi.c \
	GSM610/add.c GSM610/code.c GSM610/decode.c GSM610/gsm_create.c \
	GSM610/gsm_decode.c GSM610/gsm_destroy.c GSM610/gsm_encode.c \
	GSM610/gsm_option.c GSM610/long_term.c GSM610/lpc.c GSM610/preprocess.c \
	GSM610/rpe.c GSM610/short_term.c GSM610/table.c \
	G72x/g721.c G72x/g723_16.c G72x/g723_24.c G72x/g723_40.c \
	G72x/g72x.c G72x/g72x_test.c \
	ALAC/ag_dec.c \
	ALAC/ag_enc.c \
	ALAC/ALACBitUtilities.c \
	ALAC/alac_decoder.c \
	ALAC/alac_encoder.c \
	ALAC/dp_dec.c \
	ALAC/dp_enc.c \
	ALAC/matrix_dec.c \
	ALAC/matrix_enc.c

include $(BUILD_SHARED_LIBRARY)
