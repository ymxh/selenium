package selenium.page;

import org.openqa.selenium.By;

import selenium.method.WebDriverMethod;


public class HomePage extends BasePage {

	
	/**
	 * 主站首页，也就是唯一的测试入口，所以使用了static final修饰，后面代码不可修改
	 */
	public static final String startUrl = "http://www.8868.cn/";
	
	
	/**
	 * 测试地址入口，首页地址;打开地址后使用this返回本对象，继续操作
	 * @return
	 */
	public HomePage openPageRequestUrl(){
		WebDriverMethod.ThreadDriver.get().get(startUrl);
		return this;
	}
	
	
	/**
	 * 未登录，链接为登录入口,返回LoginPage对象
	 */
	public LoginPage loginLink(){
		FindElementClick(By.xpath("//div[@class='hd-barLogin']/a[2]"));
		return new LoginPage();
	}
	
	
	/**
	 * 未登陆，按钮为注册入口；登录成功显示用户名
	 */
	public void registerLink(){
		FindElementClick(By.xpath("//div[@class='hd-barLogin']/a[1]"));
	}
	
	
	/**
	 * 登录成功，个人中心入口模块，返回"8868手游交易平台个人中心"
	 * @return
	 */
	public String userCenter(){
		return FindElementGetText(By.xpath("//div[@class='user']/div/div[1]"));
	}
}
