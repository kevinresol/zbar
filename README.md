# ZBar

This native extension for OpenFL features a non-fullscreen barcode scanner, using 
[ZBar](http://sourceforge.net/projects/zbar/) as the provider for barcode scanning.

For android, it uses the [barcodescanner](https://github.com/dm77/barcodescanner) library by [dm77](https://github.com/dm77)
For ios, it uses ZBar's iPhone SDK

The scanner viewfinder is presented as a separate native view over the view which draws the OpenFL stage. So there is a limitation that you cannot put any OpenFL display objects above the viewfinder.

## Usage

Command line:
```
haxelib git zbar https://github.com/kevinresol/zbar.git
lime rebuild zbar android
lime rebuild zbar ios
```

Haxe:
```haxe
var zbar = ZBar.getInstance();
zbar.addEventListener(ScanEvent.SUCCESS, function(e) 
{
	trace (e.contents, e.formatName);
	zbar.removeScanner();
});

zbar.addScanner(0, 50, 400, 500); //x, y, width, height of the scanner view
// the scanner automatically starts scanning, so no need to call startScanning here.

// call zbar.stopScanning() if you want to stop the scanning
// the viewfinder will freeze, call removeScanner to remove the viewfinder

// call zbar.startScanning after stopScanning or after zbar has dispatched an event

```
