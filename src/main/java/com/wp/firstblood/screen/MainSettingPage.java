package com.wp.firstblood.screen;

import io.appium.java_client.AppiumDriver;

public class MainSettingPage extends BasePage{
    private String appEnvItem = "fl_app_env";
    private String appTypeItem = "fl_app_language";
    private String envOnline = "tv_online_env";
    private String envTest = "tv_test_env";
    private String appTypeChina = "tv_chinese";
    private String appTypeEnglish = "tv_english";
    private String backBtn = "iv_back";

    public MainSettingPage(AppiumDriver appiumDriver){
        super(appiumDriver);
    }

    public MainSettingPage pressEnv(){
        clickView(appEnvItem);
        return this;
    }

    public MainSettingPage selectOnlineEnv(){
        clickView(envOnline);
        return this;
    }

    public MainSettingPage selectTestEnv(){
        clickView(envTest);
        return this;
    }

    public MainSettingPage pressAppType(){
        clickView(appTypeItem);
        return this;
    }

    public MainSettingPage selectChina(){
        clickView(appTypeChina);
        return this;
    }

    public MainSettingPage selectEnglish(){
        clickView(appTypeEnglish);
        return this;
    }

    public HomePage exit(){
        clickView(backBtn);
        return new HomePage(appiumDriver);
    }

}
