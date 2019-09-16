#import "BeaconSuedtirolMobileSdk.h"


@implementation BeaconSuedtirolMobileSdk

RCT_EXPORT_MODULE(NearbyBeacons)

RCT_EXPORT_METHOD(sampleMethod:(NSString *)stringArgument numberParameter:(nonnull NSNumber *)numberArgument callback:(RCTResponseSenderBlock)callback)
{
    // TODO: Implement some actually useful functionality
	callback(@[[NSString stringWithFormat: @"numberArgument: %@ stringArgument: %@", numberArgument, stringArgument]]);
}

@end
