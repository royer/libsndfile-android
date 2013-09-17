/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.8
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.meganerd.sndfile;

public class SF_CART_TIMER {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected SF_CART_TIMER(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(SF_CART_TIMER obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libsndfileJNI.delete_SF_CART_TIMER(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setUsage(String value) {
    libsndfileJNI.SF_CART_TIMER_usage_set(swigCPtr, this, value);
  }

  public String getUsage() {
    return libsndfileJNI.SF_CART_TIMER_usage_get(swigCPtr, this);
  }

  public void setValue(int value) {
    libsndfileJNI.SF_CART_TIMER_value_set(swigCPtr, this, value);
  }

  public int getValue() {
    return libsndfileJNI.SF_CART_TIMER_value_get(swigCPtr, this);
  }

  public SF_CART_TIMER() {
    this(libsndfileJNI.new_SF_CART_TIMER(), true);
  }

}
