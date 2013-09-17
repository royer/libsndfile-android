/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.8
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.meganerd.sndfile;

public class SF_DITHER_INFO {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected SF_DITHER_INFO(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(SF_DITHER_INFO obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libsndfileJNI.delete_SF_DITHER_INFO(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setType(int value) {
    libsndfileJNI.SF_DITHER_INFO_type_set(swigCPtr, this, value);
  }

  public int getType() {
    return libsndfileJNI.SF_DITHER_INFO_type_get(swigCPtr, this);
  }

  public void setLevel(double value) {
    libsndfileJNI.SF_DITHER_INFO_level_set(swigCPtr, this, value);
  }

  public double getLevel() {
    return libsndfileJNI.SF_DITHER_INFO_level_get(swigCPtr, this);
  }

  public void setName(String value) {
    libsndfileJNI.SF_DITHER_INFO_name_set(swigCPtr, this, value);
  }

  public String getName() {
    return libsndfileJNI.SF_DITHER_INFO_name_get(swigCPtr, this);
  }

  public SF_DITHER_INFO() {
    this(libsndfileJNI.new_SF_DITHER_INFO(), true);
  }

}
