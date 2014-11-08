
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
	var EAN8 = "EAN8";
	var UPCE = "UPCE";
	var ISBN10 = "ISBN10";
	var UPCA = "UPCA";
	var EAN13 = "EAN13";
	var ISBN13 = "ISBN13";
	var I25 = "I25";
	var DATABAR = "DATABAR";
	var DATABAR_EXP = "DATABAR_EXP";
	var CODABAR = "CODABAR";
	var CODE39 = "CODE39";
	var PDF417 = "PDF417";
	var QRCODE = "QRCODE";
	var CODE93 = "CODE93";
	var CODE128 = "CODE128";
	var NONE = "NONE";
}