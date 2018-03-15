/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *       
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.irisshared.data;

import org.opentestsystem.shared.trapi.ITrClient;
import org.opentestsystem.shared.trapi.TrApiContentType;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class IrisTrClient implements ITrClient
{

  @Override
  public String getPackage (String url) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getForObject (String url) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> T getForObject (String url, Class<T> responseType) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getForObject (String url, TrApiContentType contentType) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> T getForObject (String url, TrApiContentType contentType, Class<T> responseType) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> T postForObject (String url, Object request, Class<T> responseType) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void put (String url, Object request) {
    // TODO Auto-generated method stub

  }

  @Override
  public ResponseEntity<String> exchange (String url, String requestBody, TrApiContentType contentType, HttpMethod httpMethod) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> ResponseEntity<T> exchange (String url, String requestBody, TrApiContentType contentType, HttpMethod httpMethod, Class<T> responseType) {
    // TODO Auto-generated method stub
    return null;
  }

}
