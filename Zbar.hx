package;

#if cpp
import cpp.Lib;
#elseif neko
import neko.Lib;
#end

#if (android && openfl)
import openfl.utils.JNI;
#end


class Zbar {
	
	
	public static function sampleMethod (inputValue:Int):Int {
		
		#if (android && openfl)
		
		var resultJNI = zbar_sample_method_jni(inputValue);
		var resultNative = zbar_sample_method(inputValue);
		
		if (resultJNI != resultNative) {
			
			throw "Fuzzy math!";
			
		}
		
		return resultNative;
		
		#else
		
		return zbar_sample_method(inputValue);
		
		#end
		
	}
	
	
	private static var zbar_sample_method = Lib.load ("zbar", "zbar_sample_method", 1);
	
	#if (android && openfl)
	private static var zbar_sample_method_jni = JNI.createStaticMethod ("org.haxe.extension.Zbar", "sampleMethod", "(I)I");
	#end
	
	
}