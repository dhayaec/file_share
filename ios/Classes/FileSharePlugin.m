#import "FileSharePlugin.h"
#import <file_share/file_share-Swift.h>

@implementation FileSharePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFileSharePlugin registerWithRegistrar:registrar];
}
@end
