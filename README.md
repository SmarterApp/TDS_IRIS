# Welcome to the IRIS Application
The IRIS takes a folder of Smarter Balanced assessment items as input, and loads them in an iframe.
A compiled WAR file is hosted in the [Smarter Balanced artifactory](https://airdev.artifactoryonline.com/airdev).
IRiS versions can be downloaded [here](https://airdev.artifactoryonline.com/airdev/libs-releases-local/org/opentestsystem/delivery/iris/) or checkout the [releases](https://github.com/SmarterApp/TDS_IRIS/releases).

For docker versions, please see [dockerhub](https://hub.docker.com/r/osucass/tds_iris/tags/)

## License
This project is licensed under the [Mozilla Public License Version 2.0](https://www.mozilla.org/en-US/MPL/2.0/).

## Getting Involved
We would be happy to receive feedback on its capabilities, problems, or future enhancements:
* For general questions or discussions, please use the [Forum](http://forum.opentestsystem.org/viewforum.php?f=9)
* Use the Issues link to file bugs or enhancement requests.
* Feel free to Fork this project and develop your changes!

## Configuration
### IRIS Configuration Files
IRIS uses the `iris/src/main/resources/settings-mysql.xml` for application configuration.
The `iris.ContentPath` variable in `iris/src/main/resources/settings-mysql.xml` specifies the directory containing the Smarter Balanced assessment items.
This must be set to a valid directory or the application will not run.
The content path in the precompiled IRIS WAR file is `/home/tomcat7/content`.
This must be changed if you wish to host the content in a different directory.

### Apache Tomcat Configuration Files
IRIS requires a 25 character alphanumeric encryption key set in Apache Tomcat's `conf/context.xml`.
```xml
<Parameter name="tds.iris.EncryptionKey" override="false" value="24 characters alphanumeric Encryption key" />
```

## Docker
content tagged images have content 
Use non-content tagged to use different content

### Change memory 
JAVA_OPTS='-Xmx1g' 

### Content Volume
-v /your/content/:/home/tomcat7/content

## Running IRiS
### Runtime Dependencies
* Java 7
* Apache Tomcat 7 or newer

Deploy IRIS to Tomcat by placing the WAR file in the Tomcat webapps directory, then restarting Tomcat.

### Displaying items
Navigate to `{irisRootURL}/IrisPages/sample.xhtml`.

#### Required Request

To specify which item and accessibility options to load you must give the IRIS a JSON token with the following format.
```JSON
{
    "items": [{
        "response": "",
        "id": "I-ItemBank-ItemKey"
    }],
    "accommodations": [{
            "type": "AccessibilityFamily",
            "codes": ["AccessibilityCode1", "AccessibilityCode2"]
        }
    ]
}
```


#### Example Request

For example, to load an item with bank 187 and key 856, with color contrast and print size accessibility options.
```JSON
{
    "items": [{
        "response": "",
        "id": "I-187-856"
    }],
    "accommodations": [{
            "type": "ColorContrast",
            "codes": ["TDS_CCYellowB"]
        },{
            "type": "Print Size",
            "codes": ["TDS_PS_L4"]
        }

    ]
}
```

#### Optional, LoadFrom 

Can be specified to load and or reload from a specific directory using an absolute path to the content. Example to reload items in /home/tomcat7/temp1 use `loadFrom` request. This will overwrite any existing keys with the specified content in the singleton.
```JSON
{
    "items": [{
        "response": "",
        "id": "I-ItemBank-ItemKey"
    }],
    "loadFrom": "absolute/path/url",
    "accommodations": [{
            "type": "AccessibilityFamily",
            "codes": ["AccessibilityCode1", "AccessibilityCode2"]
        }
    ]
}
```

#### Optional, Load multiple and/or accessibility items

To load multiple items, if items are related by passage
```JSON
{
    "items": [{
        "response": "",
        "id": "I-ItemBank-ItemKey"
    },
    {
        "response": "",
        "id": "I-ItemBank-ItemKey"
    }],
    "loadFrom": "absolute/path/url",
    "accommodations": [{
            "type": "AccessibilityFamily",
            "codes": ["AccessibilityCode1", "AccessibilityCode2"]
        },
        {
            "type": "AccessibilityFamily",
            "codes": ["AccessibilityCode1", "AccessibilityCode2"]
        }
    ]
}
```

### Reloading items

To reload all items in IRiS, call `Pages/API/content/reload`.

## Building From Source
### Compile Time Dependencies
* Java 7
* Apache Maven


IRIS has a compile time dependency on several SmarterApp projects.
You will need to configure Maven to fetch these from an artifactory, or build and install them locally using Maven.
The SmarterApp artifactory is located at https://airdev.artifactoryonline.com/airdev/.
If you want to build and install them locally they can be downloaded from [SmarterApp's GitHub](https://github.com/SmarterApp).

The SmarterApp managed compile time dependencies are:
* student
* item-renderer
* shared-tr-api
* tds-itemselection-aironline
* shared-web
* shared-logging
* shared-threading
* shared-test
* tds-dll-api
* tds-dll-mssql
* tds-dll-mysql
* ResourceBundler
* tds-itemselection-common
* item-scoring-api
* item-scoring-engine
* testscoring
* spellcheck