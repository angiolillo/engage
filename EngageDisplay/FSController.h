//
//  FSController.h
//  FullScreenApp
//
//  Created by Jonathan Terleski on 6/5/06.
//  Copyright 2006 __MyCompanyName__. All rights reserved.
//

#import <Cocoa/Cocoa.h>
#import "NetSocket.h"

NSRect sizeRectInRect(NSRect innerRect,NSRect outerRect,bool expand);
NSRect fitRectInRect(NSRect innerRect,NSRect outerRect,bool expand);
NSRect centerRectInRect(NSRect rect, NSRect mainRect);

@interface FSController : NSObject {

	NetSocket *serverSocket;
	NSString *rootpath;

	NSWindow *mainWindow;
	NSRect screenRect;
	IBOutlet id slideShowPanel;
	
}

@end
