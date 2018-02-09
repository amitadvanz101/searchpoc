/**
 *
 */
package com.advanz101.error.handler;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.advanz101.error.domain.ValidationError;
import com.advanz101.error.domain.impl.CustomRestErrorDomainImpl;
import com.advanz101.error.domain.impl.RestErrorDomainImpl;
import com.advanz101.error.exception.ApplicationException;
import com.advanz101.error.exception.BadRequestException;
import com.advanz101.error.exception.BusinessException;
import com.advanz101.error.exception.CustomExceptionWarning;
import com.advanz101.error.exception.InternalServerException;
import com.advanz101.error.exception.ResourceNotFoundException;
import com.advanz101.error.exception.RestErrorsEnum;
import com.advanz101.error.exception.TooManyRequestException;
import com.advanz101.error.exception.UnauthenticatedException;
import com.advanz101.error.exception.UnauthorizedException;
import com.advanz101.error.service.RestErrorResolver;
import com.advanz101.error.service.impl.RestErrorConverterImpl;
import com.advanz101.error.service.impl.RestErrorResolverImpl;
import com.advanz101.response.Metadata;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author 
 *
 */

@ControllerAdvice
// Name as MainErrorHandler 
public class RestErrorHandler extends ResponseEntityExceptionHandler {

//	private static final Logger LOGGER = LoggerFactory.getLogger(RestErrorHandler.class);

    @Autowired
    private MessageSource messageSource;

    private RestErrorResolver restErrorResolver;


	public RestErrorHandler() {
        this.restErrorResolver = new RestErrorResolverImpl();
    }

	/**
	 *
	 * @param badRequestException
	 * @param request
	 * @return
	 */
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Object> badRequestHandler(BadRequestException badRequestException, WebRequest request) {
		//LOGGER.error(badRequestException.getApplicationCode() + "-" + badRequestException.getMessage() , badRequestException);
		RestErrorDomainImpl restErrorDomainImpl = restErrorResolver.resolveBadRequestError(badRequestException);
        return handleExceptionInternal(badRequestException, RestErrorConverterImpl.restErrorConvertor(restErrorDomainImpl),
        		getHeaders(), HttpStatus.OK, request);
	}

	/**
	 *
	 * @param unauthenticatedException
	 * @param request
	 * @return
	 */
	@ExceptionHandler(UnauthenticatedException.class)
	public ResponseEntity<Object> unauthenticatedRequestHandler(UnauthenticatedException unauthenticatedException, WebRequest request) {
		//LOGGER.error(unauthenticatedException.getMessage(), unauthenticatedException);
		RestErrorDomainImpl restErrorDomainImpl = restErrorResolver.resolveAuthenticatedRequestError(unauthenticatedException);
        return handleExceptionInternal(unauthenticatedException, RestErrorConverterImpl.restErrorConvertor(restErrorDomainImpl),
        		getHeaders(), HttpStatus.UNAUTHORIZED, request);
	}

	/**
	 *
	 * @param unauthorizedException
	 * @param request
	 * @return
	 */
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<Object> unauthorizedRequestHandler(UnauthorizedException unauthorizedException, WebRequest request) {
		//LOGGER.error(unauthorizedException.getMessage(), unauthorizedException);
		RestErrorDomainImpl restErrorDomainImpl = restErrorResolver.resolveUnauthorizedRequestError(unauthorizedException);
		return handleExceptionInternal(unauthorizedException, RestErrorConverterImpl.restErrorConvertor(restErrorDomainImpl),
	        		getHeaders(), HttpStatus.UNAUTHORIZED, request);
	}

	/**
	 *
	 * @param resourceNotFoundException
	 * @param request
	 * @return
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> resourceNotFoundExceptionRequestHandler(ResourceNotFoundException resourceNotFoundException, WebRequest request) {
		//LOGGER.error(resourceNotFoundException.getMessage());
		RestErrorDomainImpl restErrorDomainImpl = restErrorResolver.resolveResourceNotFoundRequestError(resourceNotFoundException);
		return handleExceptionInternal(resourceNotFoundException, RestErrorConverterImpl.restErrorConvertor(restErrorDomainImpl),
				getHeaders(), HttpStatus.OK, request);
	}

	/**
	 *
	 * @param tooManyRequestException
	 * @param request
	 * @return
	 */
	@ExceptionHandler(TooManyRequestException.class)
	public ResponseEntity<Object> tooManyRequestExceptionRequestHandler(TooManyRequestException tooManyRequestException, WebRequest request) {
		//LOGGER.error(tooManyRequestException.getMessage(), tooManyRequestException);
		RestErrorDomainImpl restErrorDomainImpl = restErrorResolver.resolveTooManyRequestRequestError(tooManyRequestException);
		return handleExceptionInternal(tooManyRequestException, RestErrorConverterImpl.restErrorConvertor(restErrorDomainImpl),
        		getHeaders(), HttpStatus.OK, request);
	}

	/**
	 *
	 * @param internalServerException
	 * @param request
	 * @return
	 */
	@ExceptionHandler(InternalServerException.class)
	public ResponseEntity<Object> internalServerExceptionRequestHandler(InternalServerException internalServerException, WebRequest request) {
		//LOGGER.error(internalServerException.getMessage(), internalServerException);
		
		RestErrorDomainImpl restErrorDomainImpl = restErrorResolver.resolveInternalServerExceptionRequest(internalServerException);
		return handleExceptionInternal(internalServerException, RestErrorConverterImpl.restErrorConvertor(restErrorDomainImpl),
        		getHeaders(), HttpStatus.OK, request);
	}

	/**
	 *
	 * @param businessException
	 * @param request
	 * @return
	 */
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Object> 
	businessExceptionRequestHandler
	(BusinessException businessException, WebRequest req
		) {
		RestErrorDomainImpl restErrorDomainImpl = restErrorResolver.resolveBusinessExceptionRequest(businessException,null);
		return handleExceptionInternal(businessException, RestErrorConverterImpl.restErrorConvertor(restErrorDomainImpl),
       		getHeaders(), HttpStatus.OK, req);
	}

	/**
	 *
	 * @param customWarningException
	 * @param request
	 * @return
	 */
	@ExceptionHandler(CustomExceptionWarning.class)
	public ResponseEntity<Object> customExceptionWarningRequestHandler(CustomExceptionWarning customWarningException , WebRequest request) {
		//LOGGER.error(customWarningException.getMessage(), customWarningException);
		CustomRestErrorDomainImpl customRestErrorDomainImpl = restErrorResolver.resolveCustomExceptionWarningRequest(customWarningException);
		return handleExceptionInternal(customWarningException, RestErrorConverterImpl.customExceptionConverter(customRestErrorDomainImpl),
				getHeaders(),HttpStatus.SEE_OTHER, request);
	}


	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Object> runTimeError(RuntimeException e, WebRequest request) {
		//LOGGER.error(e.getMessage(), e);
		InternalServerException ex = new InternalServerException(null,null,null);
		ex.setSystemErrorMessage(e.toString());			
		
		return internalServerExceptionRequestHandler(ex, request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> unknowError(Exception e, WebRequest request) {
		//LOGGER.error(e.getMessage(), e);
		InternalServerException ex = new InternalServerException(null,null,null);
		ex.setSystemErrorMessage(e.toString());		
		return internalServerExceptionRequestHandler(ex, request);
	}


	@ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handle(ConstraintViolationException ex
			) {
		Set<ConstraintViolation<?>> fieldErrors = ex.getConstraintViolations();
		return handleExceptionInternal(ex, processFieldErrors(fieldErrors), null,  HttpStatus.OK, null);
    }	

	private Object processFieldErrors(Set<ConstraintViolation<?>> fieldErrors) {
        ValidationError dto = new ValidationError();

        for (ConstraintViolation<?> fieldError: fieldErrors) {
            String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
           // logger.debug("Adding error message: "+localizedErrorMessage+" to field: "+fieldError.getMessage());
            dto.addFieldError(((PathImpl)fieldError.getPropertyPath()).getLeafNode().getName(), localizedErrorMessage);
        }

        return dto;
	}

	private String resolveLocalizedErrorMessage(ConstraintViolation<?> fieldError) {
		Locale currentLocale = LocaleContextHolder.getLocale();
		String localizedErrorMessage = fieldError.getMessage();// messageSource.getMessage(fieldError.getMessage(),fieldError.getExecutableParameters(),currentLocale);
		return localizedErrorMessage;
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		BindingResult result = ex.getBindingResult();

		Class<? extends Object> targetClass = result.getModel().get(result.getObjectName()).getClass();
		Field[] fields = targetClass.getDeclaredFields();
		List<FieldError> fieldErrors = result.getFieldErrors();
		Metadata metadata = new Metadata();
		metadata.setHttpStatus(Integer.valueOf(RestErrorsEnum.BAD_REQUEST.getHttpStatusCode()));

		ApplicationException appException = new ApplicationException("", metadata, null,processFieldErrors(fieldErrors, fields).getFieldErrors());
		RestErrorDomainImpl restErrorDomainImpl = restErrorResolver.resolveMethodArgumentNotValidExceptionRequest(appException, "");
		return handleExceptionInternal(appException, RestErrorConverterImpl.restErrorConvertor(restErrorDomainImpl),
				getHeaders(), HttpStatus.OK, request);

	}

	String getJsonFieldName(Field[] fields, String fieldName) {
		String jsonName = fieldName;
		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				JsonProperty property = field.getAnnotation(com.fasterxml.jackson.annotation.JsonProperty.class);
				if (property != null) {
					jsonName = property.value();
				}
				break;
			}
		}
		return jsonName;
	}
	
    private ValidationError processFieldErrors(List<FieldError> fieldErrors, Field[] fields) {
        ValidationError dto = new ValidationError();

        for (FieldError fieldError: fieldErrors) {
            String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
            dto.addFieldError(getJsonFieldName(fields, fieldError.getField()), localizedErrorMessage);
        }
        return dto;
    }

    private String resolveLocalizedErrorMessage(FieldError fieldError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedErrorMessage ;
        String message=localizedErrorMessage= messageSource.getMessage(fieldError, currentLocale);
        
        //If a message was not found, return the most accurate field error code instead.
        //You can remove this check if you prefer to get the default error message.
        if (message.equals(fieldError.getDefaultMessage())) {
            String[] fieldErrorCodes = fieldError.getCodes();
            localizedErrorMessage = fieldErrorCodes[0];
            if(localizedErrorMessage.equals(fieldError.getCodes()[0])) {
            	localizedErrorMessage=message;
            }
        }
        else {
        	localizedErrorMessage=message;
        }
        return localizedErrorMessage;
    }

	/**
	 *
	 * @return
	 */
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}


}
