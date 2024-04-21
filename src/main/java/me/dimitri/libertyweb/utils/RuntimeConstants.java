package me.dimitri.libertyweb.utils;

public class RuntimeConstants {

    public static String getBackendVersion() {
        return BACKEND_VERSION;
    }

    public static void setBackendVersion(String backendVersion) {
        BACKEND_VERSION = backendVersion;
    }

    public static String BACKEND_VERSION = "";

    public static String getFrontendVersion() {
        return FRONTEND_VERSION;
    }

    public static void setFrontendVersion(String frontendVersion) {
        FRONTEND_VERSION = frontendVersion;
    }

    public static String FRONTEND_VERSION = "";

}
