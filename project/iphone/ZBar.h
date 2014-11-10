#ifndef ZBARIPHONE_H
#define ZBARIPHONE_H


namespace zbar
{
    namespace iphone
    {
    	void initialize();
		void addScanner(int x, int y, int width, int height);
		void removeScanner();
		void startScanning();
		void stopScanning();
    }
}


#endif