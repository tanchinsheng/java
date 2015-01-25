package it.txt.tpms.lineset.accessibility;

import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;


/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 4-ott-2006
 * Time: 12.35.37
 * Ths class represents the accessibility information for a specific lineset
 */
public class OneLinesetAccessData {
    /*
	<LS>
		<LS_NAME>sample lineset</LS_NAME>
		<LS_VOB_NAME>Dvl vob</LS_VOB_NAME>
		<LS_CREATOR>centem</LS_CREATOR>
		<ALLOWED_USERS_LOGIN>
			<USER_LOGIN>dettore</USER_LOGIN>
			<USER_LOGIN>mosca</USER_LOGIN>
			<USER_LOGIN>landi</USER_LOGIN>
		</ALLOWED_USERS_LOGIN>
	</LS>
*/
    private String linesetName = null;
    private String vobName = null;
    private String linesetCreatorLogin = null;
    private ArrayList allowedUsersLogin = new ArrayList();


    public OneLinesetAccessData() {

    }

    public OneLinesetAccessData(String linesetName, String vobName, String linesetCreatorLogin, ArrayList allowedUsersLogin) {
        this.linesetName = linesetName;
        this.vobName = vobName;
        this.linesetCreatorLogin = linesetCreatorLogin;
        this.allowedUsersLogin = allowedUsersLogin;
    }


    public OneLinesetAccessData(Element oneLinesetAccessibilityData) {
        this.linesetName = XmlUtils.getTextValue(oneLinesetAccessibilityData, LinesetAccessibilityConstants.LS_NAME_TAG);
        this.vobName = XmlUtils.getTextValue(oneLinesetAccessibilityData, LinesetAccessibilityConstants.LS_VOB_NAME_TAG);
        this.linesetCreatorLogin = XmlUtils.getTextValue(oneLinesetAccessibilityData, LinesetAccessibilityConstants.LS_CREATOR_TAG);
        NodeList nlAllowedUsersLogin = oneLinesetAccessibilityData.getElementsByTagName(LinesetAccessibilityConstants.USER_LOGIN_TAG);
        if (nlAllowedUsersLogin != null) {
            int usersLoginCount = nlAllowedUsersLogin.getLength();
            String tmpUserLogin;
            Element elOneUserLogin;
            for (int i = 0; i < usersLoginCount; i++) {
                elOneUserLogin = (Element) nlAllowedUsersLogin.item(i);
                if (elOneUserLogin != null) {
                    tmpUserLogin = elOneUserLogin.getFirstChild().getNodeValue();//XmlUtils.getTextValue(elOneUserLogin, LinesetAccessibilityConstants.USER_LOGIN_TAG);
                    this.addAllowedUser(tmpUserLogin);
                }
            }
        }
    }


    public String getAccessibilityId() {
        return linesetName + "_" + vobName;
    }

    public String getLinesetName() {
        return linesetName;
    }

    public void setLinesetName(String linesetName) {
        this.linesetName = linesetName;
    }

    public String getVobName() {
        return vobName;
    }

    public void setVobName(String vobName) {
        this.vobName = vobName;
    }

    public String getLinesetCreatorLogin() {
        return linesetCreatorLogin;
    }

    public void setLinesetCreatorLogin(String linesetCreatorLogin) {
        this.linesetCreatorLogin = linesetCreatorLogin;
    }

    public ArrayList getAllowedUsersLogin() {
        return allowedUsersLogin;
    }

    public void setAllowedUsersLogin(ArrayList allowedUsersLogin) {
        this.allowedUsersLogin = allowedUsersLogin;
    }

    public boolean addAllowedUser(String userLogin) {
        if (!GeneralStringUtils.isEmptyString(userLogin)) {
            if (allowedUsersLogin == null) {
                allowedUsersLogin = new ArrayList();
            }
            return allowedUsersLogin.add(userLogin);
        }
        return false;
    }

    public boolean removeAllowedUser(String userLogin) {
        if (allowedUsersLogin != null && !allowedUsersLogin.isEmpty()) {
            return allowedUsersLogin.remove(userLogin);
        } else {
            allowedUsersLogin = new ArrayList();
            return false;
        }
    }

    /**
     * this method returns true if and only if the user identified by the given login can access the lineset in order to get it:
     * <B>NOTE:</B> the user identified by creatorLogin may ALWAYS get a lineset
     * @param login
     * @return if the given login is present in allowedUsersLogin or if equal to the linesetCreatorLogin, false otherwise
     */
    public boolean isUserAccessAllowed(String login) {
        if (GeneralStringUtils.isEmptyString(login)) {
            //in this case a null or empty login is given... block the access.
            return false;
        } else if (allowedUsersLogin == null || allowedUsersLogin.isEmpty()) {
            //if the allowedUsersLogin is empty at least verify if the current uer is the creator: if yes return true.
            return linesetCreatorLogin.equals(login);
        } else if (linesetCreatorLogin.equals(login)) {
            //if the current user is the creator of the lineset return true
            return true;
        } else {
            //look for the given login inside of the list of allowed users: if found return true; false otherwise
            return allowedUsersLogin.contains(login);
        }
    }
}
