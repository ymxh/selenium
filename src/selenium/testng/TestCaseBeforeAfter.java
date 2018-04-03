package selenium.testng;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.log4testng.Logger;

import selenium.method.WebDriverMethod;

/**
 * 所有testng测试类继承
 * @author Administrator
 *
 */
public class TestCaseBeforeAfter{
	
	private Logger logger = Logger.getLogger(TestCaseBeforeAfter.class);
	
	/**
	 * 所有测试测试方法执行前执行，alwaysRun属性表示方法必须执行
	 * @param browserName
	 * @param browserVersion
	 */
	@BeforeMethod(alwaysRun=true)
	@Parameters({"browserName","browserVersion"})
	public void TestBeforeMethod(String browserName,String browserVersion){
		System.out.println("*********测试用例开始*********");
		logger.info("*********测试用例开始*********");
		WebDriverMethod.driverStart(browserName,browserVersion);
		logger.info("设置浏览器名："+browserName+"浏览器版本号："+browserVersion);
	}
	
	
	/**
	 * 所有测试方法执行完后执行,下面所有层级都关闭一次
	 */
	@AfterMethod(alwaysRun=true)
	public void TestAfterMethod(){
		WebDriverMethod.driverQuit();
		logger.info("*********测试用例结束*********");
	}
}
