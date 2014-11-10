# Zbar

Haxe binding for the ZBar library

For android, it uses the [barcodescanner](https://github.com/dm77/barcodescanner) library by [dm77](https://github.com/dm77)

For ios, it uses [ZBar](http://sourceforge.net/projects/zbar/)'s iPhone SDK

## Usage

```haxe
var zbar = Zbar.getInstance();
zbar.addEventListener(ScanEvent.SUCCESS, function(e) 
{	
	trace (e.contents, e.formatName);
	zbar.stopScanning();
	zbar.removeScanner();
});

zbar.addScanner(0, 50, 400,500);
zbar.startScanning();

```
