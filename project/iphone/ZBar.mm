#include "include/ZBarSDK.h"
#include "ZBar.h"
#include "ZBarInterface.h"
#include <objc/runtime.h>
#include <UIKit/UIKit.h>


// Function that dynamically implements the NMEAppDelegate.supportedInterfaceOrientationsForWindow
// callback to allow portrait orientation even if app only supports landscape.
// This is required as barcode scanning camera simulator needs portrait mode.
//
static NSUInteger ApplicationSupportedInterfaceOrientationsForWindow(id self, SEL _cmd, UIApplication* application, UIWindow* window)
{
    return UIInterfaceOrientationMaskAll;
}


//
// Delegate for notifications from the ZBar library.
//
@interface ZBarReaderDelegate : NSObject <ZBarReaderDelegate>
{
}
- (void) dismissReader:(UIImagePickerController*)reader;
@end

@implementation ZBarReaderDelegate
- (void) imagePickerController:(UIImagePickerController*)reader didFinishPickingMediaWithInfo:(NSDictionary*)info
{
    // get the decode results
    id<NSFastEnumeration> results = [info objectForKey: ZBarReaderControllerResults];
    ZBarSymbol* symbol = nil;
    for (symbol in results)
        // just grab the first barcode
        break;

    // do something useful with the barcode data
    printf("Scanned barcode: type=%s, value=%s\n", [symbol.typeName UTF8String], [symbol.data UTF8String]);

    // raise an OpenFL event BarcodeScannedEvent.BARCODE_SCANNED
    dispatchEvent("success", [symbol.data UTF8String], [symbol.typeName UTF8String]);

    // dismiss with a slight delay to avoid conflicting with the reader view still updating
    [self performSelector:@selector(dismissReader:) withObject:reader afterDelay:1.0f];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)reader
{
    printf("Barcode scanning cancelled.\n");

    // raise an OpenFL event BarcodeScannedEvent.BARCODE_SCANNED
    dispatchEvent("cancelled", "", "");

    [self dismissReader:reader];
}

- (void) dismissReader:(UIImagePickerController*)reader
{
    // dismiss the controller (NB dismiss from the *reader*!)
    ((ZBarReaderViewController*) reader).readerDelegate = nil;
    //[reader dismissViewControllerAnimated:YES completion:nil];
    [reader willMoveToParentViewController:nil];
	[reader.view removeFromSuperview];
	[reader removeFromParentViewController];
    [self release];
}
@end


namespace zbar 
{

	namespace iphone 
	{
		ZBarReaderViewController * reader;

		void initialize()
        {
            // Ensure we support portrait orientation else UIImagePickerController crashes
            class_addMethod(NSClassFromString(@"NMEAppDelegate"),
                            @selector(application:supportedInterfaceOrientationsForWindow:),
                            (IMP) ApplicationSupportedInterfaceOrientationsForWindow,
                            "I@:@@");
        }

		void startScanning (int x, int y, int width, int height)
		{
			NSLog(@"startScanning");
		    // Get our topmost view controller
		    UIViewController* topViewController = [[UIApplication sharedApplication] keyWindow].rootViewController;

		    // Present a barcode reader that scans from the camera feed
		    reader = [ZBarReaderViewController new];
		    reader.readerDelegate = [[ZBarReaderDelegate alloc] init];
		    reader.supportedOrientationsMask = ZBarOrientationMaskAll;
		    reader.showsZBarControls = NO;
			reader.readerView.frame = CGRectMake(x, y, width, height);
			reader.view.backgroundColor = [UIColor clearColor];

		    ZBarImageScanner* scanner = reader.scanner;
		    // TODO: (optional) additional reader configuration here
		    // EXAMPLE: disable rarely used I2/5 to improve performance
		    [scanner setSymbology: ZBAR_I25
		             config: ZBAR_CFG_ENABLE
		             to: 0];

		    // present and release the controller
		    [topViewController addChildViewController:reader];
		    [topViewController.view addSubview:reader.view];
		    [reader didMoveToParentViewController:topViewController];
		    //[topViewController presentViewController:reader animated:YES completion:nil];
		    [reader release];


		    //return true;
		}

		void stopScanning()
		{
			if(reader)
			{
				reader.readerDelegate = nil;
				[reader willMoveToParentViewController:nil];
				[reader.view removeFromSuperview];
				[reader removeFromParentViewController];
			}
		}
	}
}