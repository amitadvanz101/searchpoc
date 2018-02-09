/**
 *
 */
package com.advanz101.error.exception;

import java.util.List;

import com.advanz101.error.domain.FieldMessage;
import com.advanz101.response.Metadata;

/**
 * @author 
 *
 */
public class BadRequestException extends ApplicationException {

    private static final long serialVersionUID = -9215466226316062049L;

    /**
     *
     * @param applicationCode
     * @param metadata
     */
    public BadRequestException(String applicationCode, Metadata metadata) {
		super(applicationCode,metadata);
	}

	public BadRequestException(String applicationCode, Metadata metadata, String userMessages, List<FieldMessage> fieldMessages) {
		super(applicationCode,metadata,userMessages,fieldMessages);
	}

	public BadRequestException(String applicationCode, Metadata metadata, String userMessages) {
		super(applicationCode,metadata,userMessages,null);
	}
}
