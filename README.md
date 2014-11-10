# Zbar

Haxe binding for the [ZBar](http://sourceforge.net/projects/zbar/) library

For android, it uses the [barcodescanner](https://github.com/dm77/barcodescanner) library by [dm77](https://github.com/dm77)

For ios, it uses ZBar's iPhone SDK

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
	zbar.stopScanning();
	zbar.removeScanner();
});

zbar.addScanner(0, 50, 400,500);
zbar.startScanning();

```
