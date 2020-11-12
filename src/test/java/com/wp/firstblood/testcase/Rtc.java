package com.wp.firstblood.testcase;

import com.google.gson.Gson;
import com.wp.firstblood.model.Bag;
import com.wp.firstblood.screen.HomePage;
import com.wp.firstblood.screen.LivePage;
import com.wp.firstblood.screen.ResolutionPage;
import com.wp.firstblood.screen.RoomSetting;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import okhttp3.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Rtc {
    private volatile static Bag bag;
    private static List<DesiredCapabilities> configs = new ArrayList<>();
    private static List<AppiumDriver> drivers = new ArrayList<>();
    private static List<String> servers = new ArrayList<>();
    private ExecutorService executor = Executors.newCachedThreadPool();

    @BeforeClass
    public static void initCloud() {
        String URL_LOCAL = "http://10.61.160.153:8000/cloud/init";
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder().build();
//                .add("key", "temp")
//                .add()
//                .build();
        Request request = new Request.Builder().url(URL_LOCAL).post(formBody).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            Gson gson = new Gson();
            String bodyJson = response.body().string();
            System.out.println(bodyJson);
            bag = gson.fromJson(bodyJson, Bag.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @BeforeClass
    public static void setupClass() {
        try {
            for (int i = 0; i < servers.size(); i++) {
                AppiumDriver driver = new AndroidDriver(new URL(servers.get(i)), configs.get(i));
                driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                drivers.add(driver);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void teardownClass() {
        for (AppiumDriver driver : drivers) {
            driver.quit();
        }
    }

    static {
        DesiredCapabilities desiredCapabilities = new SDesiredCapabilities.Builder()
                .setPlatformName("Android")
                .setAppiumVersion("1.18.3")
                .setPlatformVersion("10")
                .setDeviceName("P7C0218116009553")
                .setAppPackageName("com.linkv.live")
                .setAppActivity(".activity.MainActivity")
                .build();

        DesiredCapabilities desiredCapabilities2 = new SDesiredCapabilities.Builder()
                .setPlatformName("Android")
                .setAppiumVersion("1.18.3")
                .setPlatformVersion("9")
                .setDeviceName("R28M31K5FPJ")
                .setAppPackageName("com.linkv.live")
                .setAppActivity(".activity.MainActivity")
                .build();


        DesiredCapabilities iosDes = new DesiredCapabilities();
        iosDes.setCapability(CapabilityType.PLATFORM_NAME, "iOS");
        iosDes.setCapability("platformVersion", "13.4.1");
        iosDes.setCapability("deviceName", "iPhone 8 Plus");
        iosDes.setCapability("automationName", "XCUITest");
        iosDes.setCapability("app", "xxx");
        iosDes.setCapability("onReset", true);
        configs.add(desiredCapabilities);
        configs.add(desiredCapabilities2);
        servers.add("http://127.0.0.1:4723/wd/hub");
        servers.add("http://127.0.0.1:4725/wd/hub");
    }

    private static class SDesiredCapabilities extends DesiredCapabilities {
        public SDesiredCapabilities(Builder builder) {
            setCapability(CapabilityType.PLATFORM_NAME, builder.platformName);
            setCapability("appium-version", builder.appiumVersion);
            setCapability("platformVersion", builder.platformVersion);
            setCapability("deviceName", builder.deviceName);
            setCapability("appPackage", builder.appPackageName);
            setCapability("appActivity", builder.appActivity);
            setCapability("newCommandTimeout", 180);
            setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
        }

        private static class Builder {
            private String platformName;
            private String appiumVersion;
            private String platformVersion;
            private String deviceName;
            private String appPackageName;
            private String appActivity;

            SDesiredCapabilities build() {
                return new SDesiredCapabilities(this);
            }

            Builder setPlatformName(String platformName) {
                this.platformName = platformName;
                return this;
            }

            Builder setAppiumVersion(String appiumVersion) {
                this.appiumVersion = appiumVersion;
                return this;
            }

            Builder setPlatformVersion(String platformVersion) {
                this.platformVersion = platformVersion;
                return this;
            }

            Builder setDeviceName(String deviceName) {
                this.deviceName = deviceName;
                return this;
            }

            Builder setAppPackageName(String appPackageName) {
                this.appPackageName = appPackageName;
                return this;
            }

            Builder setAppActivity(String appActivity) {
                this.appActivity = appActivity;
                return this;
            }
        }

    }


    @Before
    public void setup() {
        for (AppiumDriver driver : drivers) {
            if (driver instanceof AndroidDriver) {
                AndroidDriver androidDriver = (AndroidDriver) driver;
                androidDriver.unlockDevice();
            }
            if (driver instanceof IOSDriver) {

            }
        }

    }

    @Test
    public void myFirstBlood() {
        int i = 0;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
        for (AppiumDriver driver : drivers) {
            if (i == 0) {
                executor.execute(() -> {
                    HomePage homePage = new HomePage(driver);
                    LivePage livePage = homePage.startLive().enterRoom();
                    RoomSetting roomSetting = livePage.openSettingPane().openSei();
                    ResolutionPage resolutionPage = roomSetting.showResolutionPane().adjustParam(ResolutionPage.Resolution.P_270);
                    resolutionPage.ensure();
                    livePage.startLive();
                    livePage.keepScreenLive(10);
                    String room = livePage.getRoom();
                    for (int temp = 0; temp < drivers.size() - 1; temp++) {
                        queue.offer(room);
                        System.out.println(room);
                    }
                    countDownLatch.countDown();
                    livePage.keepScreenLive(Integer.parseInt(bag.getData().getCaseConfig().getKeepLiveWithRoom()));
                });
            } else {
                executor.execute(() -> {
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    HomePage homePage = new HomePage(driver);
                    HomePage.WatchListPage watchListPage = homePage.startWatch();
                    LivePage page = null;
                    try {
                        page = watchListPage.letWatch(queue.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ResolutionPage rePage = page.openSettingPane().showResolutionPane().adjustParam(ResolutionPage.Resolution.P_270);
                    rePage.ensure();
                    watchListPage.keepScreenLive(Integer.parseInt(bag.getData().getCaseConfig().getKeepLiveWithRoom()));
                });
            }
            i++;
        }
        try {
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


