## Tests for the accommodations supported by Iris.
Once the Iris application is running navigate to $IRISBASEURL/IrisPages/sample.xhtml where $IRISBASEURL is the URL where you are running the Iris application.
Load the item using the JSON below the checkboxes.

#### Zoom and color contrast.
- [ ] The font size starts at the maximum size.
- [ ] Clicking the Zoom Out button decreases the zoom.
- [ ] Clicking the Zoom In button increases the zoom.
- [ ] The text is yellow.
- [ ] The background is blue.


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


#### Word List Glossary and Illustration Glossary.
- [ ] Clicking the word spinner opens up the glossary panel.
- [ ] There is a picture of the spinner in the glossary panel.
- [ ] There is a tab with the Korean definition in the glossary panel.

```JSON
{
	"items": [{
		"response": "",
		"id": "I-187-1881"
	}],
	"accommodations": [{
		"type": "Word List",
		"codes": ["TDS_WL_KoreanGloss", "TDS_WL_Illustration"]
	},{
		"type": "Illustration Glossary",
		"codes": ["TDS_ILG1"]
	}]
}
```

#### Spanish translation for items and the tools menu.
- [ ] There is a Spanish translation of the item.
- [ ] The tools menu accessed by right clicking or with the button is in Spanish.

```JSON
{
	"items": [{
		"response": "",
		"id": "I-187-1844"
	}],
	"accommodations": [
{
			"type": "Item Tools Menu",
			"codes": ["TDS_ITM1"]
		},{
			"type": "Highlight",
			"codes": ["TDS_Highlight1"]
		},{
			"type": "Strikethrough",
			"codes": ["TDS_ST1"]
		},{
			"type": "Language",
			"codes": ["ESN"]
		}

	]
}
```

#### Streamlined mode.
- [ ] The item layout uses the streamlined mode which puts everything in one column.
```JSON
{
	"items": [{
		"response": "",
		"id": "I-187-2576"
	}],
	"accommodations": [{
			"type": "Streamlined Mode",
			"codes": ["TDS_SLM1"]
		}

	]
}
```

#### Item tools menu, highlighting, strikethrough, and masking.
- [ ] Right clicking brings up the item tools menu.
- [ ] Clicking the tools menu button brings up the tools menu.
- [ ] Highlighting text then right clicking or clicking the tools menu button brings up the option to highlight the selection.
- [ ] Right clicking again or clicking the tools menu button brings up the option to reset highlighting.
- [ ] Right clicking or clicking the tools menu button provides the option for strikethrough.
- [ ] Clicking on one of the answers with strikethrough mode on puts a line across the answer.
- [ ] Clicking on the tools menu or in the text area turns strikethrough mode off.
- [ ] While strikethrough mode is enabled, clicking on an answer that has the strikethrough line on it removes the line.
- [ ] Clicking the Masking button turns on the masking tool.
- [ ] While the masking tool is on clicking and dragging creates a mask.
- [ ] Clicking the Masking button again turns off the masking tool.
- [ ] Clicking the (x) on a mask closes the mask.
- [ ] Clicking the American Sign Language option in the tools menu opens the American Sign Language video.

```JSON
{
	"items": [{
		"response": "",
		"id": "I-187-1844"
	}],
	"accommodations": [
{
			"type": "Item Tools Menu",
			"codes": ["TDS_ITM1"]
		},{
			"type": "Highlight",
			"codes": ["TDS_Highlight1"]
		},{
			"type": "Strikethrough",
			"codes": ["TDS_ST1"]
		},{
			"type": "Masking",
			"codes": ["TDS_Masking1"]
		},{
			"type": "American Sign Language",
			"codes": ["TDS_ASL1"]
		}
	]
}
```

#### Expandable Passages
- [ ] Clicking the expand button in the top right corner of the passage expands it.
- [ ] Clicking the expand button when the passage is expanded shrinks it back to normal size.

```JSON
{
	"items": [{
		"response": "",
		"id": "I-187-1437"
	}],
	"accommodations": [{
			"type": "Expandable Passages",
			"codes": ["TDS_ExpandablePassages1"]
		}

	]
}
```

#### Calculator, Notes, Dictionary, and Font Type
- [ ] The font used is Verdana. This is a sans serif font.
- [ ] Clicking the calculator button opens the calculator.
- [ ] The calculator can be used to make calculations.
- [ ] Clicking the (x) in the top right corner of the calculator pane closes it.
- [ ] Clicking the notes button opens the notes panel.
- [ ] Typing in the notes panel creates text.
- [ ] Clicking Save and Close saves the text and closes the pane.
- [ ] Clicking cancel closes the panel without saving any text that was entered or deleted.
- [ ] Clicking the dictionary button opens the dictionary pane.
- [ ] If the dictionary is set up then an interactive dictionary is displayed.
- [ ] If the dictionary is not set up a 404 page is displayed.
- [ ] Clicking the (x) in the top right corner of the dictionary pane closes it.

```JSON
{
	"items": [{
		"response": "",
		"id": "I-187-2576"
	}],
	"accommodations": [{
			"type": "Calculator",
			"codes": ["TDS_CalcSciInv"]
		},{
			"type": "Notes",
			"codes": ["TDS_GN1"]
		},{
			"type": "Dictionary",
			"codes": ["TDS_Dict_SD3"]
		},{
			"type": "Font Type",
			"codes": ["TDS_FT_Verdana"]
		}
	]
}
```
