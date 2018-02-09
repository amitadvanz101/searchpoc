/**
 *
 */
package com.advanz101.error.domain.impl;

import java.util.List;

import com.advanz101.error.domain.BaseRestErrorDomain;
import com.advanz101.error.domain.FieldMessage;
import com.advanz101.response.Metadata;

/**
 * @author 
 *
 */
public class RestErrorDomainImpl extends BaseRestErrorDomain {

	public RestErrorDomainImpl(String httpStatusCode, String applicationCode, String userMessage,List<FieldMessage> fieldMessages, String systemMessage,
			Metadata metadata) {
		super(httpStatusCode, applicationCode, userMessage,fieldMessages, systemMessage, metadata);
	}
}
