/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *       
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.iris.abstractions.repository;

import tds.blackbox.ContentRequestException;
import tds.iris.web.data.ContentRequest;
import tds.itemrenderer.data.AccLookup;
import tds.itemrenderer.data.IITSDocument;
import tds.itemrenderer.data.ItemRenderGroup;

public interface IContentHelper
{
  public ItemRenderGroup loadRenderGroup (ContentRequest contentRequest);
  public ItemRenderGroup loadRenderGroupAcc (ContentRequest contentRequest, AccLookup accLookup);
  public ItemRenderGroup loadTutorial(long bankKey, long itemKey, String language);
  public boolean reloadContent ();
  public void addFile(String fileName);
  public void removeFile(String fileName);
  public IITSDocument getITSDocument(String id) throws ContentRequestException;
  public IITSDocument getITSDocument(String id, AccLookup accLookup) throws ContentRequestException;
  public String getItemFormattedId(long bankKey, long itemKey);
  public String getStimulusFormattedId(long bankKey, long itemKey);

  }
