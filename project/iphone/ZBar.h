#ifndef ZBARIPHONE_H
#define ZBARIPHONE_H


namespace zbar
{
    namespace iphone
    {
    	void initialize();
        void startScanning(int x, int y, int width, int height);
        void stopScanning();
    }
}


#endif