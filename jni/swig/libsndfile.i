%module libsndfile

/* simplifies how arrays are handled - see 21.8.3 
 * 'Wrapping C arrays with Java arrays' in the SWIG docs
 */

%include "carrays.i"
%array_class(float, CArrayFloat);
%array_class(double, CArrayDouble);
%array_class(int, CarrayInt);
%array_class(short, CArrayShort);

%{
#include "../libsndfile/src/sndfile.h"
%}

%include "enums.swg"
%javaconst(1);
//%javaconst(0) SF_COUNT_MAX;

#define SEEK_SET 0  /* Seek from beginning of file.  */
#define SEEK_CUR 1  /* Seek from current position.  */
#define SEEK_END 2  /* Seek from end of file.  */

typedef long long int64_t;
typedef long int32_t ;
typedef unsigned long uint32_t;

%include "../libsndfile/src/sndfile.h"
