package com.wp.firstblood.screen;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class RoomSetting extends BasePage {
    private String listId = "lv_setting";
    private String seiId = "tool_switch";

    public RoomSetting(AppiumDriver appiumDriver) {
        super(appiumDriver);
    }

    public RoomSetting openSei() {
        List<WebElement> list = appiumDriver.findElements(By.id(listId));
        System.out.println("list:" + list.size());
        WebElement webElement = list.get(ItemIndex.FIRST.ordinal());
        webElement.findElement(By.id(seiId)).click();
        return this;
    }

    public ResolutionPage showResolutionPane(){
        WebElement webElement = findItem(ItemIndex.THIRD);
        webElement.click();
        return new ResolutionPage(appiumDriver);
    }

    private WebElement findItem(ItemIndex itemIndex){
        WebElement webElement = (WebElement) appiumDriver.findElementsById("fl_item_setting").get(itemIndex.ordinal());
        return webElement;
    }



    public LivePage closePane() {
        if (appiumDriver instanceof AndroidDriver) {
            AndroidDriver fk = (AndroidDriver) appiumDriver;
            fk.pressKey(new KeyEvent(AndroidKey.BACK));
        }
        return new LivePage(appiumDriver);
    }

    private enum ItemIndex {
        FIRST, SECOND, THIRD
    }
}
