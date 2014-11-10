package;

#if cpp
import cpp.Lib;
#elseif neko
import neko.Lib;
#end

#if (android && openfl)
import openfl.utils.JNI;
#end


import openfl.events.EventDispatcher;


class Zbar extends EventDispatcher
{
	private static var instance:Zbar;
	private static inline var JAVA_CLASS_NAME:String = "org.haxe.extension.Zbar";

	public var scanning(default, null):Bool = false;
	
	private function new()
	{
		super();
		#if ios
		zbar_init(dispatch);
		#end

		#if android
		zbar_init(this);
		#end
	}

	public static function getInstance():Zbar
	{
		if(instance == null)
			instance = new Zbar();

		return instance;
	}

	public function addScanner(x:Int = 0, y:Int = 0, width:Int = 0, height:Int = 0):Void
	{
		zbar_add_scanner(x, y, width, height);
	}

	public function removeScanner():Void
	{
		zbar_remove_scanner();
	}

	public function startScanning():Void
	{
		if(!scanning)
		{
			scanning = true;
			zbar_start_scanning();
		}
	}

	public function stopScanning():Void
	{
		if(scanning)
		{
			scanning = false;
			trace("stopscanning");
			zbar_stop_scanning();
		}
	}

	private function dispatch(type:String, contents:String, formatName:String)
	{
		dispatchEvent(new ScanEvent(type, contents, formatName));
	}
	
	
	#if ios

	private static var zbar_init = Lib.load ("zbar", "zbar_init", 1);
	private static var zbar_add_scanner = Lib.load ("zbar", "zbar_add_scanner", 4);
	private static var zbar_remove_scanner = Lib.load ("zbar", "zbar_remove_scanner", 0);
	private static var zbar_start_scanning = Lib.load ("zbar", "zbar_start_scanning", 0);
	private static var zbar_stop_scanning = Lib.load ("zbar", "zbar_stop_scanning", 0);
	#end

	#if (android && openfl)
	private static var zbar_init = JNI.createStaticMethod (JAVA_CLASS_NAME, "init", "(Lorg/haxe/lime/HaxeObject;)V");
	private static var zbar_start_scanning = JNI.createStaticMethod (JAVA_CLASS_NAME, "startScanning", "(IIII)V");
	private static var zbar_stop_scanning = JNI.createStaticMethod (JAVA_CLASS_NAME, "stopScanning", "()V");
	#end
	
	
}


