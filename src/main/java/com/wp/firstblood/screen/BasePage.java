package com.wp.firstblood.screen;
import io.appium.java_client.AppiumDriver;

public abstract class BasePage {
    AppiumDriver appiumDriver;


    BasePage(AppiumDriver appiumDriver) {
        this.appiumDriver = appiumDriver;
    }

    void clickView(String id){
        appiumDriver.findElementById(id).click();
    }

    public void keepScreenLive(int time){
        try {
            Thread.sleep(time * 1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}
