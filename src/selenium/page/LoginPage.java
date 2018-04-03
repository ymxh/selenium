package selenium.page;


import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import selenium.method.WebDriverMethod;

public class LoginPage extends BasePage{
	
	private static Logger logger = Logger.getLogger(LoginPage.class);
	
	/**
	 * 用户名输入框
	 * @param username
	 */
	public void usernameTextBox(String username){
		FindElementSendKeys(By.id("username"),username);
		logger.info("登录用户名输入："+username);
	}
	
	/**
	 * 密码输入框
	 * @param pw
	 */
	public void passwordTextBox(String pw){
		FindElementSendKeys(By.id("userpwd"),pw);
		logger.info("登录密码输入："+pw);
	}
	
	/**
	 * 登录按钮
	 */
	public void login_Button(){
		FindElementClick(By.name("button"));
		logger.info("点击登录按钮");
	}
	
	
	/**
	 * 登录失败时的提示语
	 * @return
	 */
	public String error_Warn(){
		String errorWarn = FindElementGetText(By.cssSelector(".content"));
		logger.info("登录失败提示语:"+errorWarn);
		return errorWarn;
	}
	
	
	/**
	 * 登录操作
	 * @param list 入参参数
	 * @return  返回验证的结果
	 */
	public String login(List<String> list){
		String act = null;
		String startTitle = WebDriverMethod.ThreadDriver.get().getTitle();
		usernameTextBox(list.get(0));
		passwordTextBox(list.get(1));
		login_Button();
		String stopTitle = WebDriverMethod.ThreadDriver.get().getTitle();
		if(startTitle.equals(stopTitle)){
			act = error_Warn();
			logger.info("登录失败,页面停留在本页，错误内容："+act);
		}else{
			act = "登录成功";
		}
		return act;
	}
}
