# nativeframework
nativeframework plugin for devkit

# Known Issue
If you use this plugin with other modules, such as facebook, you may need to remove this line:
    "libs/bolts-tasks-1.3.0.jar"
from config.json as it may conflict with the one from facebook.

## Demo
Check out [the demo application](https://bitbucket.org/incredibl3/braintuner.git)

## Installation
Install the module using using the standard devkit install process:

~~~
devkit install https://bitbucket.org/incredibl3/nativeframework#v0.0.1
~~~

## Usage
The NativeFramework module provides an API to:
	- Show Admob banners and interstitial
	- Show dialog in all native platforms
	- Work with Parse platform: http://parse.com/

### Dialog module
At the top of your js:
~~~
import nativeframework.nativedialog;
~~~

Now you can call showDialog to show a native dialog
~~~
nativedialog.showDialog({
	title: "title", 
	messages: ["line1", "line2", "line3"], 
	buttons: ["Positive Text", "Negative Text - Optional", "Neutral Text - Optinal"]
});
~~~

To use callback, register "nativedialog" event just before you show the dialog:
~~~
NATIVE.events.registerHandler('nativedialog', bind(this, function(e) {
	logger.log("{nativedialog} Got response:", e.buttonPressed);
	this.quesViewPool.releaseAllViews();

	if (e.buttonPressed == 0) { 			// Positive Button
		// Your code
	} else if (e.buttonPressed == 1) {		// Negative Button
		// Your code
	} else { 								// Neutral Button
		// Your code
	}
}));
~~~

## Platform-specific notes

### iOS status
	- Admob: Partly supportted
	- Dialog: Not yet supportted
	- Parse: Not yet supportted

### Android
	- Admob: Fully supportted
	- Dialog: Supportted
		Android dialog is created by using Alert Dialog
		As devkit support android-19 for now, DialogFragment implementation isn't in used yet
	- Parse: Supportted
		To-Do: Create general request, allow adding key as parameter