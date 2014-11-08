# Zbar

Haxe binding for the ZBar library

## Usage

```haxe
var zbar = Zbar.getInstance();
zbar.addEventListener(ScanEvent.SUCCESS, function(e) 
{	
	trace (e.contents, e.formatName);
	zbar.stopScanning();
});

zbar.startScanning(0, 50, 700, 0); // x, y, width, height (if both w, h are zero, use camera default preview dimension. If either one is zero, match camera default preview aspect ratio)

```