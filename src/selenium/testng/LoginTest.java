package selenium.testng;

import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import selenium.page.HomePage;
import selenium.util.ListUtil;
import selenium.util.XmlReader;

import org.testng.annotations.DataProvider;
import org.testng.Assert;


public class LoginTest extends TestCaseBeforeAfter{
	
	private Logger logger = Logger.getLogger(LoginTest.class);
	
	@DataProvider(name="testProvider",parallel=true)
	public Object[][] testProvider(){
		logger.info("DataProvider数据源");
		return XmlReader.readerCase("testcase.xml","Login");
	}
	
	
	@Test(dataProvider="testProvider")
	public void login(String param,String exp){
		logger.info("使用数据源数据:参数param值="+param+" 预期结果exp值="+exp);
		Assert.assertEquals(new HomePage().openPageRequestUrl().loginLink().login(ListUtil.returnList(param)), exp);	
	}
}
