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
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import TDS.Shared.Browser.BrowserRule;
import TDS.Shared.Exceptions.ReturnStatusException;
import tds.student.sql.abstractions.IConfigRepository;
import tds.student.sql.data.AccList;
import tds.student.sql.data.AppExterns;
import tds.student.sql.data.ForbiddenApps;
import tds.student.sql.data.ItemScoringConfig;
import tds.student.sql.data.NetworkDiagnostic;
import tds.student.sql.data.PTSetup;
import tds.student.sql.data.TTSVoicePack;
import tds.student.sql.data.TesteeAttributeMetadata;

@Component
@Scope ("prototype")
public class ConfigRepository implements IConfigRepository, ApplicationContextAware
{
  private static final Logger _logger = LoggerFactory.getLogger (ConfigRepository.class);

  @Override
  public void setApplicationContext (ApplicationContext arg0) throws BeansException {
    // TODO Auto-generated method stub

  }

  @Override
  public List<String> getClients () throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public AppExterns getExterns () throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PTSetup getPTSetup () throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Iterable<TesteeAttributeMetadata> getLoginRequirements () throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<String, TesteeAttributeMetadata> getTesteeAttributeMetadata (String[] ids, String[] types, String[] atLogins) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ForbiddenApps getForbiddenApps () throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Iterable<TTSVoicePack> getVoicePacks () throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public AccList getGlobalAccommodations () throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Object getAppSetting (String name) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Iterable<BrowserRule> getBrowserRules () throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Iterable<BrowserRule> getBrowserTestRules (String testKey) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Iterable<NetworkDiagnostic> getNetworkDiagnostics () throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Iterable<String> getComments () throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Iterable<ItemScoringConfig> getItemScoringConfigs () throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

@Override
public Map<String, Object> getClientAppSettings() throws ReturnStatusException {
	// TODO Auto-generated method stub
	return null;
}

}
