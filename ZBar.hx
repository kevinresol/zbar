package;

#if ios
import cpp.Lib;
#end

#if android
import openfl.utils.JNI;
#end


import openfl.events.EventDispatcher;


class ZBar extends EventDispatcher
{
	private static var instance:ZBar;

	#if ios
	private static inline var CPP_NAMESPACE:String = CPP_NAMESPACE;
	#end

	#if android
	private static inline var JAVA_CLASS_NAME:String = "org.haxe.extension.ZBar";
	#end
	
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

	public static function getInstance():ZBar
	{
		if(instance == null)
			instance = new ZBar();

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
		zbar_start_scanning();
	}

	public function stopScanning():Void
	{
		zbar_stop_scanning();
	}

	private function dispatch(type:String, contents:String, formatName:String)
	{
		dispatchEvent(new ScanEvent(type, contents, formatName));
	}
	
	
	#if ios
	private static var zbar_init = Lib.load (CPP_NAMESPACE, "zbar_init", 1);
	private static var zbar_add_scanner = Lib.load (CPP_NAMESPACE, "zbar_add_scanner", 4);
	private static var zbar_remove_scanner = Lib.load (CPP_NAMESPACE, "zbar_remove_scanner", 0);
	private static var zbar_start_scanning = Lib.load (CPP_NAMESPACE, "zbar_start_scanning", 0);
	private static var zbar_stop_scanning = Lib.load (CPP_NAMESPACE, "zbar_stop_scanning", 0);
	#end

	#if android
	private static var zbar_init = JNI.createStaticMethod (JAVA_CLASS_NAME, "init", "(Lorg/haxe/lime/HaxeObject;)V");
	private static var zbar_add_scanner = JNI.createStaticMethod (JAVA_CLASS_NAME, "addScanner", "(IIII)V");
	private static var zbar_remove_scanner = JNI.createStaticMethod (JAVA_CLASS_NAME, "removeScanner", "()V");
	private static var zbar_start_scanning = JNI.createStaticMethod (JAVA_CLASS_NAME, "startScanning", "()V");
	private static var zbar_stop_scanning = JNI.createStaticMethod (JAVA_CLASS_NAME, "stopScanning", "()V");
	#end
	
	
}


