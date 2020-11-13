package com.wp.firstblood.screen;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.WebElement;

public class ResolutionPage extends BasePage {
    private String resolutionItem = "sk_video_resolution";
    private String BitRateItem = "sk_video_bitrate";
    private String fpsItem = "sk_video_fps";
    private String sure = "btn_config_finish";

    public ResolutionPage(AppiumDriver appiumDriver) {
        super(appiumDriver);
    }

    public ResolutionPage adjustParam(Resolution resolution) {
        if (resolution == null) {
            throw new IllegalArgumentException();
        }
        WebElement webElement = appiumDriver.findElementById(resolutionItem);
        int x = webElement.getLocation().getX();
        int y = webElement.getLocation().getY();
        int w = webElement.getSize().getWidth();
        int moveToXDirectionAt = (int) (w * resolution.getIndex());
        TouchAction act = new TouchAction(appiumDriver);
        act.press(PointOption.point(x, y)).moveTo(PointOption.point(moveToXDirectionAt, y)).release().perform();
        return this;
    }

    public enum Resolution {
        P_180(0f), P_270(0.2f), P_360(0.4f), P_480(0.6f), P_540(0.8f), P_720(1f);

        private float index;

        Resolution(float index) {
            this.index = index;
        }

        public float getIndex() {
            return index;
        }
    }

    public LivePage ensure() {
        clickView(sure);
        return new LivePage(appiumDriver);
    }

}
