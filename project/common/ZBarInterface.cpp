#include "ZBarInterface.h"
#include "../iphone/ZBar.h"
#include <hx/CFFI.h>


namespace zbar {
	
	AutoGCRoot * mEventDispatcher;
	
	void initialize(AutoGCRoot * eventDispatcher) {
		
		mEventDispatcher = eventDispatcher;
        iphone::initialize();
		
	}

	void startScanning(int x, int y, int width, int height) {
        iphone::startScanning(x, y, width , height);
        
	}

	void stopScanning() {
		#ifdef IPHONE
        iphone::stopScanning();
        #endif
	}

	void dispatchEvent(const char * type, const char * contents, const char * formatName) {
    	val_call3(mEventDispatcher->get(), alloc_string(type), alloc_string(contents), alloc_string(formatName)); 
    }
	
	
}