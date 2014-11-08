#ifndef ZBAR_H
#define ZBAR_H

#include <hx/CFFI.h>


namespace zbar {
	
	void initialize(AutoGCRoot * eventDispatcher);

	void startScanning(int x, int y, int width, int height);
	void stopScanning();
	void dispatchEvent(const char * type, const char * contents, const char * formatName);
}


#endif