package it.txt.tpms.users;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 10-ott-2006
 * Time: 13.23.59
 */
public class TpmsUser {


    public static final String ID = "USER_ID";
    public static final String LDAP_LOGIN = "LDAP_LOGIN";
    public static final String LOGIN = "USER_LOGIN";
    public static final String UNIX_USER = "UNIX_USER";
    public static final String UNIX_GROUP = "UNIX_GROUP";
    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String SURNAME = "SURNAME";
    public static final String EMAIL = "EMAIL";
    public static final String DIVISION = "DIVISION";
    public static final String CRIPTED_PASSWORD = "PWD";
    public static final String HOME_DIRECTORY = "HOME_DIRECTORY";
    public static final String WORK_DIRECTORY = "WORK_DIRECTORY";
    public static final String ROLE = "ROLE";
    public static final String WORK_MODE = "WORK_MODE";
    public static final String DEFAULT_DEVELOPMENT_VOB = "DEFAULT_DEVELOPMENT_VOB";
    public static final String DEFAULT_RECEIVING_VOB = "DEFAULT_RECEIVING_VOB";
    public static final String USER_PLANT_ID = "PLANT_ID";


    private String ldapLogin = null;
    private String name = null;
    private String unixUser = null;

    private String unixGroup = null;
    private String firstName = null;
    private String surname = null;
    private String email = null;
    private String division = null;
    private String criptedPassword = null;
    private String homeDirectory = null;
    private String workDirectory = null;
    private String role = null;
    private String workMode = null;
    private String defaultDevelopmentVobName = null;
    private String defaultReceivingVobName = null;

    private String installationId = null;

    private HashMap thisHashMap;


    public TpmsUser(){

    }

    /*public TpmsUser(String ldapLogin, String name, String unixUser, String unixGroup, String firstName, String surname, String email, String division, String criptedPwd, String homeDir, String workDir, String role, String workMode, String defaultDevelopmentVobName, String defaultReceivingVobName) {

        this.ldapLogin = ldapLogin;
        this.name = name;
        this.unixUser = unixUser;
        this.unixGroup = unixGroup;
        this.firstName = firstName;
        this.surname = surname;
        this.email = email;
        this.division = division;
        this.criptedPassword = criptedPwd;
        this.homeDirectory = homeDir;
        this.workDirectory = workDir;
        this.role = role;
        this.workMode = workMode;
        this.defaultDevelopmentVobName = defaultDevelopmentVobName;
        this.defaultReceivingVobName = defaultReceivingVobName;

    }*/


    private void populateThisHashMap() {
        thisHashMap = new HashMap();
        thisHashMap.put(ID, this.getId());
        thisHashMap.put(LDAP_LOGIN, this.getLdapLogin());
        thisHashMap.put(LOGIN, this.getName());
        thisHashMap.put(UNIX_USER, this.getUnixUser());
        thisHashMap.put(UNIX_GROUP, this.getUnixGroup());
        thisHashMap.put(FIRST_NAME, this.getFirstName());
        thisHashMap.put(SURNAME, this.getSurname());
        thisHashMap.put(EMAIL, this.getEmail());
        thisHashMap.put(DIVISION, this.getDivision());
        thisHashMap.put(CRIPTED_PASSWORD, this.getCriptedPassword());
        thisHashMap.put(HOME_DIRECTORY, this.getHomeDirectory());
        thisHashMap.put(WORK_DIRECTORY, this.getWorkDirectory());
        thisHashMap.put(ROLE, this.getRole());
        thisHashMap.put(WORK_MODE, this.getWorkMode());
        thisHashMap.put(DEFAULT_DEVELOPMENT_VOB, this.getDefaultDevelopmentVobName());
        thisHashMap.put(DEFAULT_RECEIVING_VOB, this.getDefaultReceivingVobName());
        thisHashMap.put(USER_PLANT_ID, this.getInstallationId());
    }

    public HashMap getUserDataHashMap() {
        if (thisHashMap == null || thisHashMap.size() == 0) {
            populateThisHashMap();
        }
        return thisHashMap;
    }



    public TpmsUser(String ldapLogin, String name, String unixUser, String unixGroup, String firstName, String surname, String email, String division, String criptedPwd, String homeDir, String workDir, String role, String workMode, String defaultDevelopmentVobName, String defaultReceivingVobName, String instalaltionId) {

            this.ldapLogin = ldapLogin;
            this.name = name;
            this.unixUser = unixUser;
            this.unixGroup = unixGroup;
            this.firstName = firstName;
            this.surname = surname;
            this.email = email;
            this.division = division;
            this.criptedPassword = criptedPwd;
            this.homeDirectory = homeDir;
            this.workDirectory = workDir;
            this.role = role;
            this.workMode = workMode;
            this.defaultDevelopmentVobName = defaultDevelopmentVobName;
            this.defaultReceivingVobName = defaultReceivingVobName;
            this.installationId = instalaltionId;
            populateThisHashMap();
        }


    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }


    public String getId(){
        return name + "_" + installationId;
    }



    public String getLdapLogin() {
        return ldapLogin;
    }

    public void setLdapLogin(String ldapLogin) {
        this.ldapLogin = ldapLogin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTpmsLogin(String tpmsLogin) {
        this.name = tpmsLogin;
    }

    public String getTpmsLogin() {
       return name;
    }


    public String getUnixUser() {
        return unixUser;
    }

    public void setUnixUser(String unixUser) {
        this.unixUser = unixUser;
    }

    public String getUnixGroup() {
        return unixGroup;
    }

    public void setUnixGroup(String unixGroup) {
        this.unixGroup = unixGroup;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getCriptedPassword() {
        return criptedPassword;
    }

    public void setCriptedPassword(String criptedPassword) {
        this.criptedPassword = criptedPassword;
    }

    public String getHomeDirectory() {
        return homeDirectory;
    }

    public void setHomeDirectory(String homeDirectory) {
        this.homeDirectory = homeDirectory;
    }

    public String getWorkDirectory() {
        return workDirectory;
    }

    public void setWorkDirectory(String workDirectory) {
        this.workDirectory = workDirectory;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getWorkMode() {
        return workMode;
    }

    public void setWorkMode(String workMode) {
        this.workMode = workMode;
    }

    public String getDefaultDevelopmentVobName() {
        return defaultDevelopmentVobName;
    }

    public void setDefaultDevelopmentVobName(String defaultDevelopmentVobName) {
        this.defaultDevelopmentVobName = defaultDevelopmentVobName;
    }

    public String getDefaultReceivingVobName() {
        return defaultReceivingVobName;
    }

    public void setDefaultReceivingVobName(String defaultReceivingVobName) {
        this.defaultReceivingVobName = defaultReceivingVobName;
    }


}
