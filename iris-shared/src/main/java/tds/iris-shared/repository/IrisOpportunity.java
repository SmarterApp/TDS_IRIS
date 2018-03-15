/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *       
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.iris.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import TDS.Shared.Data.ReturnStatus;
import TDS.Shared.Exceptions.ReturnStatusException;
import tds.student.sql.abstractions.*;
import tds.student.sql.data.BrowserCapabilities;
import tds.student.sql.data.ClientLatency;
import tds.student.sql.data.OpportunityAccommodation;
import tds.student.sql.data.OpportunityInfo;
import tds.student.sql.data.OpportunityInstance;
import tds.student.sql.data.OpportunitySegment.OpportunitySegments;
import tds.student.sql.data.OpportunityStatus;
import tds.student.sql.data.ServerLatency;
import tds.student.sql.data.TestConfig;
import tds.student.sql.data.TestSelection;
import tds.student.sql.data.ToolUsed;

@Component
@Scope ("prototype")
public class IrisOpportunity implements IOpportunityRepository
{

  @Override
  public List<TestSelection> getEligibleTests (long testeeKey, UUID sessionKey, String grade) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ReturnStatus approveAccommodations (OpportunityInstance oppInstance, int segment, String segmentAccoms) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public OpportunityInfo openTestOpportunity (long testeeKey, String testKey, UUID sessionKey, UUID browserKey) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<OpportunityAccommodation> getOpportunityAccommodations (OpportunityInstance oppInstance, String testKey) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public TestConfig startTestOpportunity (OpportunityInstance oppInstance, String testKey, String formKeyList) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public OpportunitySegments getOpportunitySegments (OpportunityInstance oppInstance) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public OpportunitySegments getOpportunitySegments (UUID oppKey) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public OpportunityStatus validateAccess (OpportunityInstance oppInstance) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ReturnStatus setStatus (UUID oppKey, String status, String comment) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ReturnStatus setStatusWithValidation (OpportunityInstance oppInstance, String status, String comment) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void logOpportunityClient (OpportunityInstance oppInstance, int restart, BrowserCapabilities browserCapabilities) throws ReturnStatusException {
    // TODO Auto-generated method stub

  }

  @Override
  public int recordServerLatency (OpportunityInstance oppInstance, ServerLatency serverLatency) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int recordClientLatency (OpportunityInstance oppInstance, ClientLatency clientLatency) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int recordClientLatencies (OpportunityInstance oppInstance, List<ClientLatency> clientLatencies) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int recordToolsUsed (UUID oppKey, List<ToolUsed> toolsUsed) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int submitRequest (OpportunityInstance oppInstance, int itemPage, int itemPosition, String requestType, String requestDescription, String requestValue, String requestParameters)
      throws ReturnStatusException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public ReturnStatus waitForSegment (OpportunityInstance oppInstance, int segment, boolean entry, boolean exit) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ReturnStatus exitSegment (OpportunityInstance oppInstance, int segment) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void recordComment (UUID sessionKey, long testeeKey, UUID oppKey, String comment) throws ReturnStatusException {
    // TODO Auto-generated method stub

  }

  @Override
  public String getComment (UUID oppKey) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getOpportunityNumber (UUID oppKey) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return 0;
  }

}
