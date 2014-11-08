#ifndef STATIC_LINK
#define IMPLEMENT_API
#endif

#if defined(HX_WINDOWS) || defined(HX_MACOS) || defined(HX_LINUX)
#define NEKO_COMPATIBLE
#endif


#include <hx/CFFI.h>
#include "ZBarInterface.h"


using namespace zbar;


static value zbar_init(value eventDispatcher) {
	val_check_function(eventDispatcher, 3);
	initialize(new AutoGCRoot(eventDispatcher));
	return alloc_null();
}
DEFINE_PRIM (zbar_init, 1);


static value zbar_start_scanning (value x, value y, value width, value height) {
	
	startScanning(val_int(x), val_int(y), val_int(width), val_int(height));
	return alloc_null();
	
}
DEFINE_PRIM (zbar_start_scanning, 4);

static value zbar_stop_scanning () {

	stopScanning();
	return alloc_null();
}
DEFINE_PRIM (zbar_stop_scanning, 0);


extern "C" void zbar_main () {
	
	val_int(0); // Fix Neko init
	
}
DEFINE_ENTRY_POINT (zbar_main);



extern "C" int zbar_haxe_register_prims () { 
	
	return 0; 
}