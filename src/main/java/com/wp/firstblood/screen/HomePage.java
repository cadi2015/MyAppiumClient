package com.wp.firstblood.screen;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;


import java.util.List;

public class HomePage extends BasePage {

    private String inputRoomNumId = "et_room_id";
    private String startLiveBtnId = "btn_play_live";
    private String startWatchBtnId = "btn_watch_live";
    private String settingBtnId = "tv_setting";
    private String commonLiveItem = "tv_common_live";

    public HomePage(AppiumDriver appiumDriver) {
        super(appiumDriver);
    }

    public HomePage startLive() {
        clickView(startLiveBtnId);
        return this;
    }

    public LivePage enterRoom() {
        clickView(commonLiveItem);
        return new LivePage(appiumDriver);
    }


    public WatchListPage startWatch() {
        clickView(startWatchBtnId);
        return new WatchListPage(appiumDriver);
    }

    public static class WatchListPage extends BasePage {
        private String itemStr = "android.widget.FrameLayout";
        private String btnEnterId = "btn_enter_live";
        private String listId = "lv_room_list";
        private String roomId = "tv_room_id";


        public WatchListPage(AppiumDriver appiumDriver) {
            super(appiumDriver);
        }

        public LivePage letWatch(String str) {
            System.out.println("哥哥得到了：" + str);
            MobileElement webElement = (MobileElement) appiumDriver.findElementById(listId);
            List<MobileElement> items = webElement.findElementsByClassName(itemStr);
            for (MobileElement web : items) {
                if (web.findElementById(roomId).getText().equals(str)) {
                    MobileElement temp = web.findElementById(btnEnterId);
                    if (temp != null) {
                        temp.click();
                        break;
                    }
                }
            }
            return new LivePage(appiumDriver);
        }
    }

}
