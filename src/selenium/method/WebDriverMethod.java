package selenium.method;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;


public class WebDriverMethod {
	
	private static Logger logger =  Logger.getLogger(WebDriverMethod.class);
	
	public static ThreadLocal<WebDriver> ThreadDriver=new ThreadLocal<WebDriver>() ;

	/**
	 * 创建driver,加入到ThreadLocal中
	 * @return
	 */
	public static WebDriver driverStart(String browserName,String browserVersion){
		WebDriver driver = null;
		try {
			DesiredCapabilities capability = new DesiredCapabilities();
			//解决浏览器出现"正在受到测试流程控制"
			ChromeOptions option = new ChromeOptions();
			option.addArguments("disable-infobars");		
			capability.setCapability(ChromeOptions.CAPABILITY, option);
			capability.setBrowserName(browserName);
			capability.setVersion(browserVersion);
			driver = ThreadDriver.get();
			if (driver==null){
					driver = new RemoteWebDriver(new URL("http://192.168.0.183:4444/wd/hub"), capability);
					logger.info("创建driver");
					ThreadDriver.set(driver);
					logger.info("新建driver加入ThreadDriver");
			}
			driver.manage().window().maximize();
			logger.info("浏览器窗口放大");
		} catch (MalformedURLException e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("url报错："+e.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("创建driver报错："+e.getMessage());
		}
		return driver;
	}
	
	/**
	 * 关闭driver驱动启动的socket服务，关闭浏览器并把ThreadDriver里的对象引用移除
	 */
	public static void driverQuit(){
		ThreadDriver.get().quit();
		ThreadDriver.remove();
		logger.info("关闭driver服务与浏览器，把ThreadDriver里的对象引用移除");
	}
}
