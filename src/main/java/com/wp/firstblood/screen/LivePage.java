package com.wp.firstblood.screen;

import io.appium.java_client.AppiumDriver;

public class LivePage extends BasePage {
    private String exitBtn = "iv_back";
    private String roomSetting = "iv_setting";
    private String startBtn = "tv_start_live";
    private String sid = "tv_room_id";

    LivePage(AppiumDriver appiumDriver) {
        super(appiumDriver);
    }

    public RoomSetting openSettingPane(){
        clickView(roomSetting);
        return new RoomSetting(appiumDriver);
    }

    public LivePage startLive(){
        clickView(startBtn);
        return this;
    }

    public String getRoom(){
        return appiumDriver.findElementById(sid).getText();
    }

}
