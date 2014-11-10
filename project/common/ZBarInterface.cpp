#include "ZBarInterface.h"
#include "../iphone/ZBar.h"
#include <hx/CFFI.h>


namespace zbar {
	
	AutoGCRoot * mEventDispatcher;
	
	void initialize(AutoGCRoot * eventDispatcher) {
		
		mEventDispatcher = eventDispatcher;
		
		#ifdef IPHONE
        iphone::initialize();
		#endif
	}


	void addScanner(int x, int y, int width, int height) {
		
		#ifdef IPHONE
        iphone::addScanner(x, y, width , height);
        #endif
	}

	void removeScanner() {
		#ifdef IPHONE
        iphone::removeScanner();
        #endif
	}

	void startScanning() {
		#ifdef IPHONE
        iphone::startScanning();
        #endif
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