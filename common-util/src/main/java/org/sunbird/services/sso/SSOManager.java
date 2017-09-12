/**
 * 
 */
package org.sunbird.services.sso;

import java.util.Map;

/**
 * @author Manzarul
 * This interface will handle all call related to 
 * single sign out.
 */
public interface SSOManager {
	
	/**
	 * This method will verify user access token and provide
	 * userId if token is valid. in case of invalid access token it
	 * will throw ProjectCommon exception with 401.
	 * @param token String JWT access token
	 * @return String
	 */
	public String verifyToken(String token );
	
	/**
	 * This method will do user creation inside Single sign on server.
	 * @param request Map<String,Object>
	 * @return String
	 */
	public String createUser(Map<String,Object> request);
	
	/**
	 * Method to update user account in keycloak on basis of userId.
	 * @param request
	 * @return
	 */
	public String updateUser (Map<String,Object> request);

	/**
	 *Method to remove user from keycloak account on basis of userId .
	 * @param request
	 * @return
	 */
	public String removeUser (Map<String,Object> request);
	
	/**
	 * This method will check email is verified by user or not.
	 * @param userId String
	 * @return boolean
	 */
	public boolean isEmailVerified(String userId);

	/**
	 * Method to deactivate user from keycloak , it is like soft delete .
	 * @param request
	 * @return
	 */
	public String deactivateUser(Map<String, Object> request);

	/**
	 * Method to activate user from keycloak , it is like soft delete .
	 * @param request
	 * @return
	 */
	public String activateUser(Map<String, Object> request);
}
