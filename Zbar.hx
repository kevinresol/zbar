package;

#if cpp
import cpp.Lib;
#elseif neko
import neko.Lib;
#end

#if (android && openfl)
import openfl.events.EventDispatcher;
import openfl.utils.JNI;
#end



class Zbar extends EventDispatcher
{
	private static var instance:Zbar;
	private static inline var JAVA_CLASS_NAME:String = "org.haxe.extension.Zbar";

	public var scanning(default, null):Bool = false;
	
	private function new()
	{
		super();
		zbar_init(this);
	}

	public static function getInstance():Zbar
	{
		if(instance == null)
			instance = new Zbar();

		return instance;
	}

	public function startScanning(x:Int = 0, y:Int = 0, width:Int = 0, height:Int = 0):Void
	{
		if(!scanning)
		{
			scanning = true;
			zbar_start_scanning(x, y, width, height);
		}
	}

	public function stopScanning():Void
	{
		if(scanning)
		{
			scanning = false;
			zbar_stop_scanning();
		}
	}

	private function dispatch(type:String, contents:String, formatName:String)
	{
		dispatchEvent(new ScanEvent(type, contents, formatName));
	}
	
	
	//private static var zbar_sample_method = Lib.load ("zbar", "zbar_sample_method", 1);
	
	#if (android && openfl)
	private static var zbar_init = JNI.createStaticMethod (JAVA_CLASS_NAME, "init", "(Lorg/haxe/lime/HaxeObject;)V");
	private static var zbar_start_scanning = JNI.createStaticMethod (JAVA_CLASS_NAME, "startScanning", "(IIII)V");
	private static var zbar_stop_scanning = JNI.createStaticMethod (JAVA_CLASS_NAME, "stopScanning", "()V");
	#end
	
	
}


