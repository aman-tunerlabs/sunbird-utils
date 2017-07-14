package org.sunbird.common.request;

import static org.sunbird.common.models.util.ProjectUtil.isNull;

import java.util.List;
import java.util.Map;
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.models.util.ProjectUtil;
import org.sunbird.common.models.util.ProjectUtil.AddressType;
import org.sunbird.common.responsecode.ResponseCode;


/**
 * This call will do validation
 * for all incoming request data.
 * @author Manzarul
 * @author Amit Kumar
 */
public final class RequestValidator {
    
	/**
	 * This method will do course enrollment request data
	 * validation. if all mandatory data is coming then it won't do any thing
	 * if any mandatory data is missing then it will throw exception.
	 * @param courseRequestDto CourseRequestDto
	 */
	public static void validateEnrollCourse(Request courseRequestDto) {
		if (courseRequestDto.getRequest().get(JsonKey.COURSE_ID) == null) {
		  throw new ProjectCommonException(
					ResponseCode.courseIdRequiredError.getErrorCode(),
					ResponseCode.courseIdRequiredError.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
		}
	}
	
	/**
	 * This method will do content state request data
	 * validation. if all mandatory data is coming then it won't do any thing
	 * if any mandatory data is missing then it will throw exception.
	 * @param  contentRequestDto Request
	 */
	@SuppressWarnings("unchecked")
	public static void validateUpdateContent(Request contentRequestDto) {
			if (((List<Map<String,Object>>)(contentRequestDto.getRequest().get(JsonKey.CONTENTS))).size()== 0 ) {
			  throw new ProjectCommonException(ResponseCode.contentIdRequired.getErrorCode(),
					ResponseCode.contentIdRequiredError.getErrorMessage(),ResponseCode.CLIENT_ERROR.getResponseCode());
			}else{
				List<Map<String,Object>> list= (List<Map<String,Object>>)(contentRequestDto.getRequest().get(JsonKey.CONTENTS));
				for(Map<String,Object> map :list){
					if(map.containsKey(JsonKey.CONTENT_ID)){
						
						if(null == map.get(JsonKey.CONTENT_ID)){
						  throw new ProjectCommonException(ResponseCode.contentIdRequired.getErrorCode(),
									ResponseCode.contentIdRequiredError.getErrorMessage(),ResponseCode.CLIENT_ERROR.getResponseCode());
						}
						if(ProjectUtil.isNull(map.get(JsonKey.STATUS))){
						  throw new ProjectCommonException(ResponseCode.contentStatusRequired.getErrorCode(),
									ResponseCode.contentStatusRequired.getErrorMessage(),ResponseCode.CLIENT_ERROR.getResponseCode());
						}
						
					}else{
					  throw new ProjectCommonException(ResponseCode.contentIdRequired.getErrorCode(),
								ResponseCode.contentIdRequiredError.getErrorMessage(),ResponseCode.CLIENT_ERROR.getResponseCode());
					}
				}
			}
	}
	

	/**
	 * This method will validate create user data.
	 * @param userRequest Request
	 */
	@SuppressWarnings("unchecked")
  public static void validateCreateUser(Request userRequest) {
	  Map<String,Object> addrReqMap = null;
	  Map<String,Object> reqMap = null;
		if (userRequest.getRequest().get(JsonKey.USERNAME) == null) {
			throw new ProjectCommonException(ResponseCode.userNameRequired.getErrorCode(),
					ResponseCode.userNameRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
		}
		if (userRequest.getRequest().get(JsonKey.EMAIL) == null) {
			throw new ProjectCommonException(ResponseCode.emailRequired.getErrorCode(),
					ResponseCode.emailRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
		}
		if (null != userRequest.getRequest().get(JsonKey.EMAIL) && !ProjectUtil.isEmailvalid((String) userRequest.getRequest().get(JsonKey.EMAIL))) {
			throw new ProjectCommonException(ResponseCode.emailFormatError.getErrorCode(),
					ResponseCode.emailFormatError.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
		}
		if (userRequest.getRequest().get(JsonKey.FIRST_NAME) == null
				|| (ProjectUtil.isStringNullOREmpty((String) userRequest.getRequest().get(JsonKey.FIRST_NAME)))) {
			throw new ProjectCommonException(ResponseCode.firstNameRequired.getErrorCode(),
					ResponseCode.firstNameRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
		} 
		if (userRequest.getRequest().get(JsonKey.PHONE) == null
				|| (ProjectUtil.isStringNullOREmpty((String) userRequest.getRequest().get(JsonKey.PHONE)))) {
			throw new ProjectCommonException(ResponseCode.phoneNoRequired.getErrorCode(),
					ResponseCode.phoneNoRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
		} 
		if (userRequest.getRequest().containsKey(JsonKey.ROLES) && null != userRequest.getRequest().get(JsonKey.ROLES) 
		    && !(userRequest.getRequest().get(JsonKey.ROLES) instanceof List)) {
        throw new ProjectCommonException(ResponseCode.dataTypeError.getErrorCode(),
                ProjectUtil.formatMessage(ResponseCode.dataTypeError.getErrorMessage(), JsonKey.ROLES,JsonKey.LIST), ResponseCode.CLIENT_ERROR.getResponseCode());
        }
		if (userRequest.getRequest().containsKey(JsonKey.LANGUAGE) && null != userRequest.getRequest().get(JsonKey.LANGUAGE) 
            && !(userRequest.getRequest().get(JsonKey.LANGUAGE) instanceof List)) {
        throw new ProjectCommonException(ResponseCode.dataTypeError.getErrorCode(),
            ProjectUtil.formatMessage(ResponseCode.dataTypeError.getErrorMessage(), JsonKey.LANGUAGE,JsonKey.LIST), ResponseCode.CLIENT_ERROR.getResponseCode());
        }
		
		if (userRequest.getRequest().containsKey(JsonKey.ADDRESS) && null != userRequest.getRequest().get(JsonKey.ADDRESS) ) {
		  if(!(userRequest.getRequest().get(JsonKey.ADDRESS) instanceof List)){
	          throw new ProjectCommonException(ResponseCode.dataTypeError.getErrorCode(),
	            ProjectUtil.formatMessage(ResponseCode.dataTypeError.getErrorMessage(), JsonKey.ADDRESS,JsonKey.LIST), ResponseCode.CLIENT_ERROR.getResponseCode());
	          }else if(userRequest.getRequest().get(JsonKey.ADDRESS) instanceof List){
	            List<Map<String,Object>> reqList = (List<Map<String,Object>>)userRequest.get(JsonKey.ADDRESS);
	              for(int i = 0 ; i < reqList.size() ;i++ ){
	                addrReqMap = reqList.get(i);
	                  validateAddress(addrReqMap, JsonKey.ADDRESS);
	              }
	          }
		}
		
		if (userRequest.getRequest().containsKey(JsonKey.EDUCATION) && null != userRequest.getRequest().get(JsonKey.EDUCATION) ) {
          if(!(userRequest.getRequest().get(JsonKey.EDUCATION) instanceof List)){
          throw new ProjectCommonException(ResponseCode.dataTypeError.getErrorCode(),
            ProjectUtil.formatMessage(ResponseCode.dataTypeError.getErrorMessage(), JsonKey.EDUCATION,JsonKey.LIST), ResponseCode.CLIENT_ERROR.getResponseCode());
          }else if(userRequest.getRequest().get(JsonKey.EDUCATION) instanceof List){
            List<Map<String,Object>> reqList = (List<Map<String,Object>>)userRequest.get(JsonKey.EDUCATION);
              for(int i = 0 ; i < reqList.size() ;i++ ){
                reqMap = reqList.get(i);
                  if(ProjectUtil.isStringNullOREmpty((String)reqMap.get(JsonKey.NAME))){
                    throw new ProjectCommonException(ResponseCode.educationNameError.getErrorCode(),
                    ResponseCode.educationNameError.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
                  }
                  if(ProjectUtil.isStringNullOREmpty((String)reqMap.get(JsonKey.DEGREE))){
                    throw new ProjectCommonException(ResponseCode.educationDegreeError.getErrorCode(),
                    ResponseCode.educationDegreeError.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
                  }
                  if(reqMap.containsKey(JsonKey.ADDRESS) && null != reqMap.get(JsonKey.ADDRESS)){
                    addrReqMap = (Map<String, Object>) reqMap.get(JsonKey.ADDRESS);
                    validateAddress(addrReqMap, JsonKey.EDUCATION);
                  }
              }
           }
        }
		
		if (userRequest.getRequest().containsKey(JsonKey.JOB_PROFILE) && null != userRequest.getRequest().get(JsonKey.JOB_PROFILE) ) {
          if(!(userRequest.getRequest().get(JsonKey.JOB_PROFILE) instanceof List)){
          throw new ProjectCommonException(ResponseCode.dataTypeError.getErrorCode(),
            ProjectUtil.formatMessage(ResponseCode.dataTypeError.getErrorMessage(), JsonKey.JOB_PROFILE,JsonKey.LIST), ResponseCode.CLIENT_ERROR.getResponseCode());
          }else if(userRequest.getRequest().get(JsonKey.JOB_PROFILE) instanceof List){
            List<Map<String,Object>> reqList = (List<Map<String,Object>>)userRequest.get(JsonKey.JOB_PROFILE);
              for(int i = 0 ; i < reqList.size() ;i++ ){
                  reqMap = reqList.get(i);
                  if(ProjectUtil.isStringNullOREmpty((String)reqMap.get(JsonKey.JOB_NAME))){
                    throw new ProjectCommonException(ResponseCode.jobNameError.getErrorCode(),
                    ResponseCode.jobNameError.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
                  }
                  if(ProjectUtil.isStringNullOREmpty((String)reqMap.get(JsonKey.ORG_NAME))){
                    throw new ProjectCommonException(ResponseCode.organisationNameError.getErrorCode(),
                    ResponseCode.organisationNameError.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
                  }
                  if(reqMap.containsKey(JsonKey.ADDRESS) && null != reqMap.get(JsonKey.ADDRESS)){
                    addrReqMap = (Map<String, Object>) reqMap.get(JsonKey.ADDRESS);
                    validateAddress(addrReqMap, JsonKey.JOB_PROFILE);
                  }
              }
           }
        }
		
	}
	

	public static void validateCreateOrg(Request request) {
	    if (ProjectUtil
	        .isStringNullOREmpty((String) request.getRequest().get(JsonKey.ORG_NAME))) {
	      throw new ProjectCommonException(ResponseCode.organisationNameRequired.getErrorCode(),
	          ResponseCode.organisationNameRequired.getErrorMessage(),
	          ResponseCode.CLIENT_ERROR.getResponseCode());
	    }
	  }

	  public static void validateOrg(Request request) {
	    if (ProjectUtil
	        .isStringNullOREmpty((String) request.getRequest().get(JsonKey.ORGANISATION_ID))) {
	      if((ProjectUtil
	            .isStringNullOREmpty((String) request.getRequest().get(JsonKey.SOURCE)))&& (ProjectUtil
	            .isStringNullOREmpty((String) request.getRequest().get(JsonKey.EXTERNAL_ID)))) {
	        throw new ProjectCommonException(ResponseCode.sourceAndExternalIdValidationError.getErrorCode(),
	              ResponseCode.sourceAndExternalIdValidationError.getErrorMessage(),
	              ResponseCode.CLIENT_ERROR.getResponseCode());
	      }
	    }
	  }

	  public static void validateUpdateOrg(Request request) {
	   validateOrg(request);
	    if (request.getRequest().get(JsonKey.STATUS) != null || !(ProjectUtil
	        .isStringNullOREmpty((String) request.getRequest().get(JsonKey.STATUS)))) {
	      throw new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode(),
	          ResponseCode.invalidRequestData.getErrorMessage(),
	          ResponseCode.CLIENT_ERROR.getResponseCode());
	    }
	  }

	  public static void validateUpdateOrgStatus(Request request) {
	    validateOrg(request);
	    if (request.getRequest().get(JsonKey.STATUS) == null || (ProjectUtil
	        .isStringNullOREmpty((String) request.getRequest().get(JsonKey.STATUS)))) {
	      throw new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode(),
	          ResponseCode.invalidRequestData.getErrorMessage(),
	          ResponseCode.CLIENT_ERROR.getResponseCode());
	    }
	  }


  /**
   * This method will validate update user data.
	 * @param userRequest Request
	 */
	@SuppressWarnings("rawtypes")
	public static void validateUpdateUser(Request userRequest) {
		if (userRequest.getRequest().containsKey(JsonKey.FIRST_NAME) && (ProjectUtil.isStringNullOREmpty((String) userRequest.getRequest().get(JsonKey.FIRST_NAME)))) {
			throw new ProjectCommonException(ResponseCode.firstNameRequired.getErrorCode(),
					ResponseCode.firstNameRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
		} 
		
		if (userRequest.getRequest().containsKey(JsonKey.EMAIL) && userRequest.getRequest().get(JsonKey.EMAIL) != null) {
		  if(!ProjectUtil.isEmailvalid((String) userRequest.getRequest().get(JsonKey.EMAIL))) {
			throw new ProjectCommonException(ResponseCode.emailFormatError.getErrorCode(),
					ResponseCode.emailFormatError.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
		  }
		}
		
		if (userRequest.getRequest().containsKey(JsonKey.ROLES) && null != userRequest.getRequest().get(JsonKey.ROLES)) { 
            if( !(userRequest.getRequest().get(JsonKey.ROLES) instanceof List)){
              throw new ProjectCommonException(ResponseCode.dataTypeError.getErrorCode(),
                ProjectUtil.formatMessage(ResponseCode.dataTypeError.getErrorMessage(), JsonKey.ROLES,JsonKey.LIST), ResponseCode.CLIENT_ERROR.getResponseCode());
            }
            if(userRequest.getRequest().get(JsonKey.ROLES) instanceof List && ((List)userRequest.getRequest().get(JsonKey.LANGUAGE)).isEmpty()){
              throw new ProjectCommonException(ResponseCode.rolesRequired.getErrorCode(),
                  ResponseCode.rolesRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
            }
         }
        if (userRequest.getRequest().containsKey(JsonKey.LANGUAGE) && null != userRequest.getRequest().get(JsonKey.LANGUAGE) ) {
           if(!(userRequest.getRequest().get(JsonKey.LANGUAGE) instanceof List)){
             throw new ProjectCommonException(ResponseCode.dataTypeError.getErrorCode(),
                 ProjectUtil.formatMessage(ResponseCode.dataTypeError.getErrorMessage(), JsonKey.LANGUAGE,JsonKey.LIST), ResponseCode.CLIENT_ERROR.getResponseCode());
           }
           if(userRequest.getRequest().get(JsonKey.LANGUAGE) instanceof List && ((List)userRequest.getRequest().get(JsonKey.LANGUAGE)).isEmpty()){
             throw new ProjectCommonException(ResponseCode.languageRequired.getErrorCode(),
                 ResponseCode.languageRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
           }
        }
		if (userRequest.getRequest().get(JsonKey.ADDRESS) != null
            && ((List) userRequest.getRequest().get(JsonKey.ADDRESS)).isEmpty()) {
        throw new ProjectCommonException(ResponseCode.addressRequired.getErrorCode(),
                ResponseCode.addressRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
       }
		if (userRequest.getRequest().get(JsonKey.EDUCATION) != null
           && ((List) userRequest.getRequest().get(JsonKey.EDUCATION)).isEmpty()) {
       throw new ProjectCommonException(ResponseCode.educationRequired.getErrorCode(),
               ResponseCode.educationRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
        }
	   if (userRequest.getRequest().get(JsonKey.JOB_PROFILE) != null
           && ((List) userRequest.getRequest().get(JsonKey.JOB_PROFILE)).isEmpty()) {
         throw new ProjectCommonException(ResponseCode.jobDetailsRequired.getErrorCode(),
           ResponseCode.jobDetailsRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
        }
        if (userRequest.getRequest().get(JsonKey.ADDRESS) != null
            && (!((List) userRequest.getRequest().get(JsonKey.ADDRESS)).isEmpty())) {
        
          List<Map<String,Object>> reqList = (List<Map<String,Object>>)userRequest.get(JsonKey.ADDRESS);
          for(int i = 0 ; i < reqList.size() ;i++ ){
              Map<String,Object> reqMap = reqList.get(i);
              
              if(reqMap.containsKey(JsonKey.IS_DELETED) && null != reqMap.get(JsonKey.IS_DELETED) && ((boolean)reqMap.get(JsonKey.IS_DELETED)) 
                  && ProjectUtil.isStringNullOREmpty((String)reqMap.get(JsonKey.ID))){
                throw new ProjectCommonException(ResponseCode.idRequired.getErrorCode(),
                    ResponseCode.idRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());  
              }
                validateAddress(reqMap, JsonKey.ADDRESS);
          }
       } 
        
        if (userRequest.getRequest().get(JsonKey.JOB_PROFILE) != null
            && (!((List) userRequest.getRequest().get(JsonKey.JOB_PROFILE)).isEmpty())) {
        
          List<Map<String,Object>> reqList = (List<Map<String,Object>>)userRequest.get(JsonKey.JOB_PROFILE);
          for(int i = 0 ; i < reqList.size() ;i++ ){
              Map<String,Object> reqMap = reqList.get(i);
              if(reqMap.containsKey(JsonKey.IS_DELETED) && null != reqMap.get(JsonKey.IS_DELETED) && ((boolean)reqMap.get(JsonKey.IS_DELETED)) 
                  && ProjectUtil.isStringNullOREmpty((String)reqMap.get(JsonKey.ID))){
                throw new ProjectCommonException(ResponseCode.idRequired.getErrorCode(),
                    ResponseCode.idRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
              }
              
              if(ProjectUtil.isStringNullOREmpty((String)reqMap.get(JsonKey.JOB_NAME))){
                throw new ProjectCommonException(ResponseCode.jobNameError.getErrorCode(),
                ResponseCode.jobNameError.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
              }
              if(ProjectUtil.isStringNullOREmpty((String)reqMap.get(JsonKey.ORG_NAME))){
                throw new ProjectCommonException(ResponseCode.organisationNameError.getErrorCode(),
                ResponseCode.organisationNameError.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
              }
              
              if(reqMap.containsKey(JsonKey.ADDRESS) && null != reqMap.get(JsonKey.ADDRESS)){
               validateAddress((Map<String,Object>)reqMap.get(JsonKey.ADDRESS), JsonKey.JOB_PROFILE);
              }
          }
       } 
        if (userRequest.getRequest().get(JsonKey.EDUCATION) != null
            && (!((List) userRequest.getRequest().get(JsonKey.EDUCATION)).isEmpty())) {
        
          List<Map<String,Object>> reqList = (List<Map<String,Object>>)userRequest.get(JsonKey.EDUCATION);
          for(int i = 0 ; i < reqList.size() ;i++ ){
              Map<String,Object> reqMap = reqList.get(i);
              if(reqMap.containsKey(JsonKey.IS_DELETED) && null != reqMap.get(JsonKey.IS_DELETED) && ((boolean)reqMap.get(JsonKey.IS_DELETED)) 
                  && ProjectUtil.isStringNullOREmpty((String)reqMap.get(JsonKey.ID))){
                throw new ProjectCommonException(ResponseCode.idRequired.getErrorCode(),
                    ResponseCode.idRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
              }
              if(ProjectUtil.isStringNullOREmpty((String)reqMap.get(JsonKey.NAME))){
                throw new ProjectCommonException(ResponseCode.educationNameError.getErrorCode(),
                ResponseCode.educationNameError.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
              }
              if(ProjectUtil.isStringNullOREmpty((String)reqMap.get(JsonKey.DEGREE))){
                throw new ProjectCommonException(ResponseCode.educationDegreeError.getErrorCode(),
                ResponseCode.educationDegreeError.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
              }
              if(reqMap.containsKey(JsonKey.ADDRESS) && null != reqMap.get(JsonKey.ADDRESS)){
                validateAddress((Map<String,Object>)reqMap.get(JsonKey.ADDRESS), JsonKey.EDUCATION);
               }
          }
       } 
        
        
   }

	/**
	 * This method will validate user login data.
	 * @param userRequest Request
	 */
	public static void validateUserLogin(Request userRequest) {
		if (userRequest.getRequest().get(JsonKey.USERNAME) == null) {
			throw new ProjectCommonException(ResponseCode.userNameRequired.getErrorCode(),
					ResponseCode.userNameRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
		} 
		if (userRequest.getRequest().get(JsonKey.PASSWORD) == null
				|| (ProjectUtil.isStringNullOREmpty((String) userRequest.getRequest().get(JsonKey.PASSWORD)))) {
			throw new ProjectCommonException(ResponseCode.passwordRequired.getErrorCode(),
					ResponseCode.passwordRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
		}
		if (userRequest.getRequest().get(JsonKey.SOURCE) == null
				|| (ProjectUtil.isStringNullOREmpty((String) userRequest.getRequest().get(JsonKey.PASSWORD)))) {
			throw new ProjectCommonException(ResponseCode.sourceRequired.getErrorCode(),
					ResponseCode.sourceRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
		}
	}
	
	/**
	 * This method will validate change password requested data.
	 * @param userRequest Request
	 */
	public static void validateChangePassword(Request userRequest) {
	    if (userRequest.getRequest().get(JsonKey.PASSWORD) == null
				|| (ProjectUtil.isStringNullOREmpty((String) userRequest.getRequest().get(JsonKey.PASSWORD)))) {
			throw new ProjectCommonException(ResponseCode.passwordRequired.getErrorCode(),
					ResponseCode.passwordRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
		} 
	    if (userRequest.getRequest().get(JsonKey.NEW_PASSWORD) == null
				|| (ProjectUtil.isStringNullOREmpty((String) userRequest.getRequest().get(JsonKey.NEW_PASSWORD)))) {
			throw new ProjectCommonException(ResponseCode.sourceRequired.getErrorCode(),
					ResponseCode.sourceRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
		}
	}
	
	/**
	 * This method will validate get page data api.
	 * @param request Request
	 */
	public static void validateGetPageData(Request request) {
	    if (request == null
				|| (ProjectUtil.isStringNullOREmpty((String)request.get(JsonKey.SOURCE)))) {
			throw new ProjectCommonException(ResponseCode.sourceRequired.getErrorCode(),
					ResponseCode.sourceRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
		} 
	    if (request == null
            || (ProjectUtil.isStringNullOREmpty((String)request.get(JsonKey.PAGE_NAME)))) {
        throw new ProjectCommonException(ResponseCode.pageNameRequired.getErrorCode(),
                ResponseCode.pageNameRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
    }
	    
	}
	
	/**
	 * This method will validate add course request data.
	 * @param courseRequest Request
	 */
	public static void validateAddCourse(Request courseRequest) {

		 if (courseRequest.getRequest().get(JsonKey.CONTENT_ID) == null) {
		   throw new ProjectCommonException(ResponseCode.courseIdRequiredError.getErrorCode(),
					ResponseCode.courseIdRequiredError.getErrorMessage(),ResponseCode.CLIENT_ERROR.getResponseCode());
		}
		 if (courseRequest.getRequest().get(JsonKey.COURSE_NAME) == null) {
		   throw new ProjectCommonException(ResponseCode.courseNameRequired.getErrorCode(),
					ResponseCode.courseNameRequired.getErrorMessage(),ResponseCode.CLIENT_ERROR.getResponseCode());
		}
		 if (courseRequest.getRequest().get(JsonKey.ORGANISATION_ID) == null) {
		   throw new ProjectCommonException(ResponseCode.organisationIdRequiredError.getErrorCode(),
					ResponseCode.organisationIdRequiredError.getErrorMessage(),ResponseCode.CLIENT_ERROR.getResponseCode());
		}
		 if (courseRequest.getRequest().get(JsonKey.ENROLLMENT_START_DATE) == null) {
		   throw new ProjectCommonException(ResponseCode.enrollmentStartDateRequiredError.getErrorCode(),
					ResponseCode.enrollmentStartDateRequiredError.getErrorMessage(),ResponseCode.CLIENT_ERROR.getResponseCode());
		}
		 if (courseRequest.getRequest().get(JsonKey.COURSE_DURATION) == null) {
		   throw new ProjectCommonException(ResponseCode.courseDurationRequiredError.getErrorCode(),
					ResponseCode.courseDurationRequiredError.getErrorMessage(),ResponseCode.CLIENT_ERROR.getResponseCode());
		}
	
	}
	
	/**
	 * This method will validate update course request data.
	 * @param request Request
	 */
	public static void validateUpdateCourse(Request request) {

		if (request.getRequest().get(JsonKey.COURSE_ID) == null) {
		  throw new ProjectCommonException(ResponseCode.courseIdRequired.getErrorCode(),
					ResponseCode.courseIdRequired.getErrorMessage(),ResponseCode.CLIENT_ERROR.getResponseCode());
		}

	}
	
	/**
	 * This method will validate published course request data.
	 * @param request Request
	 */
	public static void validatePublishCourse(Request request) {
		 if (request.getRequest().get(JsonKey.COURSE_ID) == null) {
		   throw new ProjectCommonException(ResponseCode.courseIdRequiredError.getErrorCode(),
					ResponseCode.courseIdRequiredError.getErrorMessage(),ResponseCode.CLIENT_ERROR.getResponseCode());
		}
	}
	
	
	/**
	 * This method will validate Delete course request data.
	 * @param request Request
	 */
	public static void validateDeleteCourse(Request request) {
		 if (request.getRequest().get(JsonKey.COURSE_ID) == null) {
		   throw new ProjectCommonException(ResponseCode.courseIdRequiredError.getErrorCode(),
					 ResponseCode.courseIdRequiredError.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
		 }
	}
	
	
	/* This method will validate create section data
	 * @param userRequest Request
	 */
	public static void validateCreateSection(Request request) {
		if (ProjectUtil.isStringNullOREmpty((String) (request.getRequest().get(JsonKey.SECTION_NAME) != null
				? request.getRequest().get(JsonKey.SECTION_NAME) : ""))) {
		  throw new ProjectCommonException(
					ResponseCode.sectionNameRequired.getErrorCode(), ResponseCode.sectionNameRequired.getErrorMessage(),
					ResponseCode.CLIENT_ERROR.getResponseCode());
		}
		if (ProjectUtil.isStringNullOREmpty((String) (request.getRequest().get(JsonKey.SECTION_DATA_TYPE) != null
				? request.getRequest().get(JsonKey.SECTION_DATA_TYPE) : ""))) {
		  throw new ProjectCommonException(
					ResponseCode.sectionDataTypeRequired.getErrorCode(), ResponseCode.sectionDataTypeRequired.getErrorMessage(),
					ResponseCode.CLIENT_ERROR.getResponseCode());
		}
	}
	
	/**
	 * This method will validate update section request data
	 * @param request Request
	 */
	public static void validateUpdateSection(Request request) {
		if (ProjectUtil.isStringNullOREmpty((String) (request.getRequest().get(JsonKey.SECTION_NAME) != null
				? request.getRequest().get(JsonKey.SECTION_NAME) : ""))) {
		  throw new ProjectCommonException(
					ResponseCode.sectionNameRequired.getErrorCode(), ResponseCode.sectionNameRequired.getErrorMessage(),
					ResponseCode.CLIENT_ERROR.getResponseCode());
		}
		if (ProjectUtil.isStringNullOREmpty((String) (request.getRequest().get(JsonKey.ID) != null
				? request.getRequest().get(JsonKey.ID) : ""))) {
			 throw new ProjectCommonException(
					ResponseCode.sectionIdRequired.getErrorCode(), ResponseCode.sectionIdRequired.getErrorMessage(),
					ResponseCode.CLIENT_ERROR.getResponseCode());	
		}
		if (ProjectUtil.isStringNullOREmpty((String) (request.getRequest().get(JsonKey.SECTION_DATA_TYPE) != null
				? request.getRequest().get(JsonKey.SECTION_DATA_TYPE) : ""))) {
		  throw new ProjectCommonException(
					ResponseCode.sectionDataTypeRequired.getErrorCode(), ResponseCode.sectionDataTypeRequired.getErrorMessage(),
					ResponseCode.CLIENT_ERROR.getResponseCode());
		}
	}
	
	
	/**
	 * This method will validate create page data
	 * @param request Request
	 */
	public static void validateCreatePage(Request request) {
		if (ProjectUtil.isStringNullOREmpty((String) (request.getRequest().get(JsonKey.PAGE_NAME) != null
				? request.getRequest().get(JsonKey.PAGE_NAME) : ""))) {
		  throw new ProjectCommonException(
					ResponseCode.pageNameRequired.getErrorCode(), ResponseCode.pageNameRequired.getErrorMessage(),
					ResponseCode.CLIENT_ERROR.getResponseCode());
		}
	}
	
	/**
	 * This method will validate update page request data
	 * @param request Request
	 */
	public static void validateUpdatepage(Request request) {
		if (ProjectUtil.isStringNullOREmpty((String) (request.getRequest().get(JsonKey.PAGE_NAME) != null
				? request.getRequest().get(JsonKey.PAGE_NAME) : ""))) {
		  throw new ProjectCommonException(
					ResponseCode.pageNameRequired.getErrorCode(), ResponseCode.pageNameRequired.getErrorMessage(),
					ResponseCode.CLIENT_ERROR.getResponseCode());
		}
		if (ProjectUtil.isStringNullOREmpty((String) (request.getRequest().get(JsonKey.ID) != null
				? request.getRequest().get(JsonKey.ID) : ""))) {
			 throw new ProjectCommonException(
					ResponseCode.pageIdRequired.getErrorCode(), ResponseCode.pageIdRequired.getErrorMessage(),
					ResponseCode.CLIENT_ERROR.getResponseCode());	
		}
	}
	
	
	/**
	 * This method will validate save Assessment data.
	 * @param request Request
	 */
	public static void validateSaveAssessment(Request request) {
		if (ProjectUtil.isStringNullOREmpty((String) (request.getRequest().get(JsonKey.COURSE_ID) != null
				? request.getRequest().get(JsonKey.COURSE_ID) : ""))) {
		  throw new ProjectCommonException(
					ResponseCode.courseIdRequired.getErrorCode(), ResponseCode.courseIdRequired.getErrorMessage(),
					ResponseCode.CLIENT_ERROR.getResponseCode());
		} 
		if (ProjectUtil.isStringNullOREmpty((String) (request.getRequest().get(JsonKey.CONTENT_ID) != null
				? request.getRequest().get(JsonKey.CONTENT_ID) : ""))) {
			throw new ProjectCommonException(ResponseCode.contentIdRequired.getErrorCode(),
					ResponseCode.contentIdRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
		}
		if (ProjectUtil.isStringNullOREmpty((String) (request.getRequest().get(JsonKey.ATTEMPT_ID) != null
				? request.getRequest().get(JsonKey.ATTEMPT_ID) : ""))) {
			throw new ProjectCommonException(ResponseCode.attemptIdRequired.getErrorCode(),
					ResponseCode.attemptIdRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
		}
		if( request.getRequest().get(JsonKey.ASSESSMENT) != null ){
			@SuppressWarnings("unchecked")
			List<Map<String,Object>> list = (List<Map<String, Object>>) request.getRequest().get(JsonKey.ASSESSMENT);
			for(Map<String,Object> map :list){
				if (ProjectUtil
						.isStringNullOREmpty((String) (map.get(JsonKey.ASSESSMENT_ITEM_ID) != null
								? map.get(JsonKey.ASSESSMENT_ITEM_ID) : ""))) {
					throw new ProjectCommonException(ResponseCode.assessmentItemIdRequired.getErrorCode(),
							ResponseCode.assessmentItemIdRequired.getErrorMessage(),
							ResponseCode.CLIENT_ERROR.getResponseCode());
				}
				if (ProjectUtil.isStringNullOREmpty((String) (map.get(JsonKey.ASSESSMENT_TYPE) != null
						? map.get(JsonKey.ASSESSMENT_TYPE) : ""))) {
					throw new ProjectCommonException(ResponseCode.assessmentTypeRequired.getErrorCode(),
							ResponseCode.assessmentTypeRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
				}
				if (ProjectUtil
						.isStringNullOREmpty((String) (map.get(JsonKey.ASSESSMENT_ANSWERS) != null
								? map.get(JsonKey.ASSESSMENT_ANSWERS) : ""))) {
					throw new ProjectCommonException(ResponseCode.assessmentAnswersRequired.getErrorCode(),
							ResponseCode.assessmentAnswersRequired.getErrorMessage(), 
							ResponseCode.CLIENT_ERROR.getResponseCode());
				}
				if (ProjectUtil
						.isStringNullOREmpty((String) (map.get(JsonKey.ASSESSMENT_MAX_SCORE) != null
								? map.get(JsonKey.ASSESSMENT_ANSWERS) : ""))) {
					throw new ProjectCommonException(ResponseCode.assessmentmaxScoreRequired.getErrorCode(),
							ResponseCode.assessmentmaxScoreRequired.getErrorMessage(),
							ResponseCode.CLIENT_ERROR.getResponseCode());
				}
			}
		}
		
	}
	
	
	/**
	 * This method will validate get Assessment data.
	 * @param request Request
	 */
	public static void validateGetAssessment(Request request) {
		if (ProjectUtil.isStringNullOREmpty((String) (request.getRequest().get(JsonKey.COURSE_ID) != null
				? request.getRequest().get(JsonKey.COURSE_ID) : ""))) {
		  throw new ProjectCommonException(
					ResponseCode.courseIdRequiredError.getErrorCode(),
					ResponseCode.courseIdRequiredError.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
		}
	}

	/**
	 * This method will validate user org requested data.
	 * @param userRequest Request
	 */
	public static void validateUserOrg(Request userRequest) {
		validateOrg(userRequest);
		if (isNull(userRequest.getRequest().get(JsonKey.USER_ID))
				|| (ProjectUtil.isStringNullOREmpty((String) userRequest.getRequest().get(JsonKey.USER_ID)))) {
			throw new ProjectCommonException(ResponseCode.userIdRequired.getErrorCode(),
					ResponseCode.userIdRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
		}
	}
	/**
     * This method will validate verifyUser requested data.
     * @param userRequest Request
     */
    public static void validateVerifyUser(Request userRequest) {
        if(ProjectUtil.isStringNullOREmpty((String)userRequest.getRequest().get(JsonKey.LOGIN_ID))){
          throw new ProjectCommonException(ResponseCode.loginIdRequired.getErrorCode(),
              ResponseCode.loginIdRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
        }
    }
	
	/**
     * This method will validate composite search request data.
     * @param searchRequest Request
     */
    public static void validateCompositeSearch(Request searchRequest) {
    }
    
  /**
   * This method will validate user org requested data.
   * 
   * @param userRequest Request
   */
  @SuppressWarnings("rawtypes")
  public static void validateAddMember(Request userRequest) {
    validateUserOrg(userRequest);
    if (isNull(userRequest.getRequest().get(JsonKey.ROLES))
        || ((List) userRequest.getRequest().get(JsonKey.ROLES)).isEmpty()) {
      throw new ProjectCommonException(ResponseCode.roleRequired.getErrorCode(),
          ResponseCode.roleRequired.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
    }
  }
	
    private static void validateAddress(Map<String,Object> address,String type){
        if(ProjectUtil.isStringNullOREmpty((String)address.get(JsonKey.ADDRESS_LINE1))){
          throw new ProjectCommonException(ResponseCode.addressError.getErrorCode(),
              ProjectUtil.formatMessage(ResponseCode.addressError.getErrorMessage(),type , JsonKey.ADDRESS_LINE1), ResponseCode.CLIENT_ERROR.getResponseCode());  
        }
        if(ProjectUtil.isStringNullOREmpty((String)address.get(JsonKey.CITY))){
          throw new ProjectCommonException(ResponseCode.addressError.getErrorCode(),
              ProjectUtil.formatMessage(ResponseCode.addressError.getErrorMessage(),type , JsonKey.CITY), ResponseCode.CLIENT_ERROR.getResponseCode());  
        }
        if(address.containsKey(JsonKey.ADD_TYPE) && type.equals(JsonKey.ADDRESS)){
          
          if(ProjectUtil.isStringNullOREmpty((String)address.get(JsonKey.ADD_TYPE))){
            throw new ProjectCommonException(ResponseCode.addressError.getErrorCode(),
                ProjectUtil.formatMessage(ResponseCode.addressError.getErrorMessage(),type , JsonKey.TYPE), ResponseCode.CLIENT_ERROR.getResponseCode());  
          }
          
          if(!ProjectUtil.isStringNullOREmpty((String)address.get(JsonKey.ADD_TYPE)) && !checkAddressType((String)address.get(JsonKey.ADD_TYPE))){
            throw new ProjectCommonException(ResponseCode.addressTypeError.getErrorCode(),
                ResponseCode.addressTypeError.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());  
          }
        }
    }
	
    private static boolean checkAddressType(String addrType){
      for(AddressType type : AddressType.values()){
        if(type.getTypeName().equals(addrType)){
          return true;
        }
      }
      return false;
    }
}
