# Iris Overview

## Setup
The Iris is packaged as a WAR application. If you are building from source it can be built using Maven with the command `mvn clean install`.
It can be deployed to Tomcat using the standard WAR deployment steps.
The following parameter must be added to the context.xml file of the Tomcat where Iris is deployed.
```xml
<Parameter name="tds.iris.EncryptionKey" override="false" value="24 characters alphanumeric Encryption key" />
```
Replace the value with the 24 character alphanumeric encryption key you want to use.

Content packages should be unzipped into the directory specified in the settings-mysql.xml file under the `iris.ContentPath` entry. If this directory does not exist the application will not start.
If the content package is changed while the Iris application is running the only reliable way to make sure the system picks up all newly deployed content is to hit the API endpoint: `/Pages/API/content/reload`.

The Iris application presents a page at `/IrisPages/sample.xhtml` where users can enter a JSON token to load an item and accommodations. Example JSON tokens are provided in the Example Requests document.

## How it works
Iris serves content items from a package stored on the local file system.
It uses the Student application as a dependency to include the JavaScript and styling from the Student application, and the Item Renderer blackbox.
The blackbox is run at the Iris application root, and the Iris renders it in an iFrame.
When a JSON token is loaded, the token is posted from the JavaScript frontend to the Iris Web Handler controller mapped to `/Pages/API/content/load` with a jQuery Ajax post request.
The controller adds any accommodations specified in the JSON token, and produces an XML document that it returns to the JavaScript frontend.
The JavaScript frontend uses the scripts loaded with the blackbox iFrame to render and display the xml document and accommodations in the blackbox iFrame.
The word list glossary uses a controller mapped to `Pages/API/WordList.axd/resolve`.
If any word list accommodations are specified when the XML document is rendered in JavaScript, the word definitions are fetched by a jQuery Ajax post to the word list glossary controller.

## Using Iris with other applications
### As an iFrame
The Iris sample page can be loaded in other applications as an iFrame without any changes.


### As a dependency
If you want to load and display content items using a different page than the Iris sample page you will need to extend the Iris application.
One way to do this is by using the Iris WAR as a [Maven WAR overlay](http://maven.apache.org/plugins/maven-war-plugin/overlays.html).
You should add anything from the Iris WAR that you do not want included in your project to the excludes in the Maven WAR overlay plugin.
For example, Iris uses logback classic as the logging framework SLF4J binds to.
If you want to use a different logging framework you need to exclude the logback classic jar.
If you add your own controllers and mappings you will need to use your own web.xml file to map them. Exclude the web.xml from the Iris WAR overlay. Copy the mappings from the Iris web.xml file, and then add your own mappings to the web.xml for your project.


To load items without using the Iris sample page add your own controllers, page templates, and JavaScript for the page you want to use.
You must add JavaScript that posts a JSON token containing the item and accommodations to the Iris Web Handler controller, then handles the response with the XML document and uses the blackbox JavaScript to render the XML document.
The JSON token needs to be in the following format for the Iris controller to accept it.
```JSON
{
	"items": [{
		"response": "",
		"id": "I-187-856"
	}],
	"accommodations": [{
			"type": "Print Size",
			"codes": ["TDS_PS_L4"]
		}]
}
```
To use the blackbox JavaScript load the blackbox using an iFrame the same way Iris does.
`Blackbox.setAccommodations` is used to set accommodations in the blackbox JavaScript. `Blackbox.changeAccommodations` is used to change the accommodations that are currently loaded in the blackbox JavaScript.
The `ContentManager` JavaScript that is loaded with the blackbox is used to render and display the XML document.
The `loadToken` and `loadContent` functions in the iris.js file are examples of how to call the blackbox and ContentManager JavaScript to render items.
Items can not be rendered until the blackbox iFrame has loaded. The `Blackbox.events.on('ready' function() {})` function can be used to run scripts after the blackbox iFrame has loaded. Replace `function() {}` with a function that does what you want once the blackbox has loaded such as calling the functions to render and display content.
