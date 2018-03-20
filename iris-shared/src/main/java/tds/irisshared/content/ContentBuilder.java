/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.irisshared.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import AIR.Common.Configuration.AppSettingsHelper;
import tds.blackbox.ContentRequestException;
import tds.irisshared.content.ContentException;
import tds.irisshared.repository.IContentBuilder;
import tds.itemrenderer.data.*;

@Component
@Scope("singleton")
public class ContentBuilder implements IContentBuilder {
    private static final Logger _logger = LoggerFactory.getLogger(ContentBuilder.class);
    private ConfigBuilder _directoryScanner = null;

    public synchronized void init(String location) throws ContentException {
        try {
            _directoryScanner = new ConfigBuilder(location);
            //_directoryScanner.create();
        } catch (Exception exp) {
            _logger.error("Error loading IRiS content.", exp);
            throw new ContentException(exp);
        }
    }

    public synchronized void init() throws ContentException{
        // scan the local folder.
        init(AppSettingsHelper.get("iris.ContentPath"));
    }

    //add a new file to the directory
    public synchronized void loadFile(String fileName) {
        try {
            _directoryScanner.addFile(fileName);
        } catch (Exception e) {
            _logger.error("Error loading IRiS content.", e);
            throw new ContentException(e);
        }
    }

    //remove a file from the directory
    public synchronized void removeFile(String fileName) {
        try {
            _directoryScanner.removeFile(fileName);
        } catch (Exception e) {
            _logger.error("Error loading IRiS content.", e);
            throw new ContentException(e);
        }
    }

    @Override
    public IITSDocument getITSDocument(String id) throws ContentRequestException {
        return _directoryScanner.getRenderableDocument(id);
    }

    @Override
    public IITSDocument getITSDocumentAcc(String id, AccLookup accLookup) {
        return _directoryScanner.getRenderableDocument(id, accLookup);

    }
}
