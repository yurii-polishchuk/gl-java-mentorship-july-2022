package com.example.statussvc;

import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings("RedundantModifiersUtilityClassLombok")
public class Constants {

    // SERVICE
    public static final String SERVICE_PACKAGE = "com.example";
    public static final String SERVICE_NAME = "srtatussvc";
    public static final String ROOT_PACKAGE = SERVICE_PACKAGE + "." + SERVICE_NAME;

    // REST
    public static final String URL_SEPARATOR = "/";
    public static final String API_V1 = "/api/v1";

}
