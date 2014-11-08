
package ;

import openfl.events.Event;

class ScanEvent extends Event
{
	public static var SUCCESS:String = "success";
	public static var CANCELLED:String = "cancelled";

	public var contents:String;
	public var formatName:FormatName;

	public function new(type:String, contents:String, formatName:FormatName, bubbles:Bool = false, cancelable:Bool = false) 
	{
		super(type, bubbles, cancelable);
		this.contents = contents;
		this.formatName = formatName;	
	}
	
	
	override public function clone():ScanEvent 
	{
		var event = new ScanEvent(type, contents, formatName, bubbles, cancelable);
		//event.eventPhase = eventPhase;
		event.target = target;
		event.currentTarget = currentTarget;
		return event;
	}
}

@:enum 
abstract FormatName(String) from String to String
{
	var EAN2 = "EAN-2";
	var EAN5 = "EAN-5";
	var EAN8 = "EAN-8";
	var UPCE = "UPC-E";
	var ISBN10 = "ISBN-10";
	var UPCA = "UPC-A";
	var EAN13 = "EAN-13";
	var ISBN13 = "ISBN-13";
	var COMPOSITE = "COMPOSITE";
	var I25 = "I2/5";
	var DATABAR = "DataBar";
	var DATABAR_EXP = "DataBar-Exp";
	var CODABAR = "Codabar";
	var CODE39 = "CODE-39";
	var CODE93 = "CODE-93";
	var CODE128 = "CODE-128";
	var PDF417 = "PDF417";
	var QRCODE = "QR-Code";
	var UNKNOWN = "UNKNOWN";
}
