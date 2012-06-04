//
//  FSController.m
//  FullScreenApp
//
//  Created by Jonathan Terleski on 6/5/06.
//  Copyright 2006 __MyCompanyName__. All rights reserved.
//

#import "FSController.h"
#import "NetSocket.h"
#import "NetPacket.h"

#define HEIGHT_TO_WIDTH 0.5625

@implementation FSController

- (id) init {
	self = [super init];
	if (self != nil) {
	
		NSString *settingsPath = [[NSBundle mainBundle] bundlePath];
		settingsPath = [settingsPath stringByDeletingLastPathComponent];
		settingsPath = [settingsPath stringByAppendingString:@"/"];
		settingsPath = [settingsPath stringByAppendingString:@"settings.cfg"];
		
		NSError *error;
		NSString *settings = [[NSString alloc] initWithContentsOfFile:settingsPath
												encoding:NSUTF8StringEncoding
												error:&error];
		
		rootpath = [[NSBundle mainBundle] bundlePath];
		rootpath = [rootpath stringByDeletingLastPathComponent];
		rootpath = [rootpath stringByAppendingString:@"/MediaLibrary/"];

		if(settings != nil) {
		
			NSRange libSetting = [settings rangeOfString:@"library_folder = "];
						
			if(libSetting.location != NSNotFound) {
				NSString *libOnward = [settings substringFromIndex:libSetting.location+libSetting.length];
				NSRange lib = [libOnward rangeOfString:@"\n"];
				NSString *libPath = [[[libOnward componentsSeparatedByString:@" = "] objectAtIndex:0] substringToIndex:lib.location-1];
				
				libPath = [libPath stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
				
				if([libPath length]) {
				
					if([libPath characterAtIndex:[libPath length]-1] != '/') {
						libPath = [libPath stringByAppendingString:@"/"];
					}
			
					if([libPath characterAtIndex:0] == '/') {
						rootpath = libPath;
					}
					else {
						if([libPath characterAtIndex:0] == '~') {
							rootpath = [libPath stringByExpandingTildeInPath];
							rootpath = [rootpath stringByAppendingString:@"/"];
						}
						else {
							rootpath = [[NSBundle mainBundle] bundlePath];
							rootpath = [rootpath stringByDeletingLastPathComponent];
							rootpath = [rootpath stringByAppendingString:@"/"];
							rootpath = [rootpath stringByAppendingString:libPath];
						}
					}
				}
			}
		}
		
		[rootpath retain];
		NSLog(@"Final path: %@",rootpath);
		
		serverSocket = [NetSocket netsocketListeningOnPort:8080];
		[serverSocket retain];
		[serverSocket setDelegate:self];
		NSLog(@"Socket!");
		[serverSocket scheduleOnCurrentRunLoop];
		
		if([serverSocket isListening]) { NSLog(@"On the Air"); }
		NSLog([serverSocket description]);
	}
	return self;
}




- (void)applicationDidFinishLaunching:(NSNotification *)notification
{
    int windowLevel;
	
	// Capture the main display
    if (CGDisplayCapture( kCGDirectMainDisplay ) != kCGErrorSuccess) {
        NSLog( @"Couldn't capture the main display!" );
        // Note: you'll probably want to display a proper error dialog here
    }

	// Get the shielding window level
    windowLevel = CGShieldingWindowLevel();
	
	// Get the screen rect of our main display
    screenRect = [[NSScreen mainScreen] frame];
	
	// Put up a new window
    mainWindow = [[NSWindow alloc] initWithContentRect:screenRect
                                styleMask:NSBorderlessWindowMask
                                backing:NSBackingStoreBuffered
                                defer:NO screen:[NSScreen mainScreen]];

	 [mainWindow setLevel:windowLevel];
	 
	 [mainWindow setBackgroundColor:[NSColor blackColor]];
     [mainWindow makeKeyAndOrderFront:nil];
	 
	 
	 
	 // Load our content view
     [slideShowPanel setFrame:screenRect display:YES];
     [mainWindow setContentView:[slideShowPanel contentView]];
	 
	 NSImage *image = [[NSImage alloc] init];
	 
	 NSSize theSize = [image size];
	 NSLog(@"x:%f y:%f",theSize.width,theSize.height);
	 
	 if([serverSocket isListening]) { NSLog(@"On the Air"); }
	 if([serverSocket delegate] == self) { NSLog(@"self is the delegate"); }
}

 - (void)applicationWillTerminate:(NSNotification *)notification
    {
        [mainWindow orderOut:self];
        
        // Release the display(s)
        if (CGDisplayRelease( kCGDirectMainDisplay ) != kCGErrorSuccess) {
        	NSLog( @"Couldn't release the display(s)!" );
        	// Note: if you display an error dialog here, make sure you set
        	// its window level to the same one as the shield window level,
        	// or the user won't see anything.
        }
    }
	
//Called when a listening socket has accepted a new connection. A newly created NetSocket is created based on this new connecting client.
//• inSocket is the notifying socket (The listening socket).
//• inNewSocket is the newly connected socket, abuse it.
- (void)netsocket:(NetSocket*)inNetSocket connectionAccepted:(NetSocket*)inNewNetSocket {
	NSLog(@"New connection detected!");
	[inNewNetSocket retain];
	[inNewNetSocket setDelegate:self];
	[inNewNetSocket scheduleOnCurrentRunLoop];
}

//Called when the socket has more data available of the specified amount.
//• inSocket is the notifying socket.
//• inAmount is the amount of new data available on the socket.
- (void)netsocket:(NetSocket*)inNetSocket dataAvailable:(unsigned)inAmount
{
	NSLog(@"Data available: %d",inAmount);	
	
	
	NSString *readData = [inNetSocket readString:NSASCIIStringEncoding amount:inAmount];
	readData = [readData stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
	
	NSLog(@"Data: %@",readData);
	
	NSImage *newImage;
	
	//Lock focus for drawing
	[[mainWindow contentView] lockFocus];
		
	[[NSColor blackColor] set]; 
	NSRectFill([[mainWindow contentView] frame]);
	
	NSLog(@"Before compare %@",rootpath);
	
	//If an image needs displayed
	if(!([readData compare:@"null"] == NSOrderedSame)) {
		
		
		
		NSString *filepath = [rootpath stringByAppendingString:readData];
		NSLog(@"%@",filepath);
		
		//Create new image with path
		newImage = [[NSImage alloc] initWithContentsOfFile:filepath];
		
		//Create rect that fits image in contentView
		NSRect newRect = fitRectInRect(NSMakeRect(0,0,[newImage size].width, [newImage size].height ),[[mainWindow contentView] frame],NO);
		
		//Actually draw the image here
		[newImage drawInRect:newRect fromRect:NSMakeRect(0,0,[newImage size].width,[newImage size].height)
														  operation:NSCompositeCopy fraction:1.0];
	}
	
	//Unlock and let window know it needs to redisplay
	[[mainWindow contentView] unlockFocus];
	[mainWindow setViewsNeedDisplay:YES];
	
}


- (void)netsocketDataSent:(NetSocket*)inNetSocket {
	NSLog(@"All data sent");
	return;
}
//Called when all of the data has been sent onto the socket.
//• inSocket is the notifying socket.

- (void)netsocketConnected:(NetSocket*)inNetSocket {
	NSLog(@"Socket connected");
}
//Called when the specified socket has established a connection with the host.
//• inSocket is the notifying socket.

- (void)netsocketDisconnected:(NetSocket*)inNetSocket {
	NSLog(@"Socket disconnected");
}
//Called when the specified socket has lost its connection with the host.
//• inSocket is the notifying socket.

- (void)netsocket:(NetSocket*)inNetSocket connectionTimedOut:(NSTimeInterval)inTimeout {
	NSLog(@"Connection timed-out");
}
//Called when the socket could not connect during the specified time interval. The socket is closed automatically, you will need to open it again to use it.
//• inSocket is the notifying socket.
//• inTimeout is the alloted timeout (in seconds).

NSRect sizeRectInRect(NSRect innerRect,NSRect outerRect,bool expand){
    float proportion=NSWidth(innerRect)/NSHeight(innerRect);
    NSRect xRect=NSMakeRect(0,0,outerRect.size.width,outerRect.size.width/proportion);
    NSRect yRect=NSMakeRect(0,0,outerRect.size.height*proportion,outerRect.size.height);
    NSRect newRect;
    if (expand) newRect = NSUnionRect(xRect,yRect);
    else newRect = NSIntersectionRect(xRect,yRect);
    return newRect;
}

NSRect centerRectInRect(NSRect rect, NSRect mainRect){
    return NSOffsetRect(rect,NSMidX(mainRect)-NSMidX(rect),NSMidY(mainRect)-NSMidY(rect));
}

NSRect fitRectInRect(NSRect innerRect,NSRect outerRect,bool expand){
    return centerRectInRect(sizeRectInRect(innerRect,outerRect,expand),outerRect);
}


@end
