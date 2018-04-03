package selenium.page;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import selenium.method.WebDriverMethod;

public class BasePage {
	
	private static Logger logger = Logger.getLogger(BasePage.class);
	
	public WebDriver driver;
	
	public BasePage(){
		this.driver = WebDriverMethod.ThreadDriver.get();
		logger.info("使用BasePage类初始化driver对象");
	}
	
	/**
	 * 智能等待，元素不可见时，最多等待10S，10S内找到元素立即返回
	 * @param locator
	 * @return
	 */
	public boolean AutoWait(By locator){
		boolean waitResult = false;
		try {
			WebDriverWait wait = new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			logger.info("元素位置"+locator+"可见");
			waitResult =  true;
		} catch (Exception e) {
			// TODO: handle exception
			waitResult =  false;
			e.printStackTrace();
			logger.error("元素位置"+locator+"不可见"+e.getMessage());
			throw new RuntimeException(e.getMessage()); 	
		}
		return waitResult;
	}
	
	/**
	 * 找到元素并点击操作
	 * @param elment 传参为By元素locator
	 */
	public void FindElementClick(By locator){
		try {
			if(AutoWait(locator)){
				WebElement element = driver.findElement(locator);
				if(element.isEnabled()){
					element.click();
					logger.info("元素点击操作，位置"+locator);
				}	
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("元素点击操作异常"+e.getMessage());
		}
	}
	
	/**
	 * 找到元素并输入内容
	 * @param elment 传参为By元素locator
	 * @param text  传参输入的内容
	 */
	public void FindElementSendKeys(By locator,String text){
		try {
			if(AutoWait(locator)){
				WebElement element = driver.findElement(locator);
				if(element.isEnabled()){
					element.clear();
					element.sendKeys(text);
					logger.info("元素输入内容"+text);
				}	
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("元素输入操作异常"+e.getMessage());
		}
	}
	
	

	/**
	 *找到元素并取出元素文本值返回
	 * @param elment  传参为By元素locator
	 * @return  返回文本值，没有文本值返回null
	 */
	public String FindElementGetText(By locator){
		String result = null;
		try {
			if(AutoWait(locator)){
				WebElement element = driver.findElement(locator);
				if(element.isEnabled()){
					result = element.getText();
					logger.info("获取元素文本值:"+result);		
				}	
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("获取元素文本值操作异常"+e.getMessage());
		}
		return result;
	}
	
	
	
	/**
	 *找到元素并根据param传入的属性名取出属性值
	 * @param elment  传参为By元素locator
	 * @param attributeName  传参为属性名
	 * @return  返回属性值值，没有值返回null
	 */
	public String FindElementGetAttribute(By locator,String attributeName){
		String result = null;
		try {
			if(AutoWait( locator)){
				WebElement element = driver.findElement(locator);
				if(element.isEnabled()){
					result = element.getAttribute(attributeName);
				}	
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 下拉框根据文本值选择
	 * @param locator
	 * @param text
	 */
	public void selectByVisibleText(By locator,String text){
		Select select = new Select(driver.findElement(locator));
		select.selectByVisibleText(text);
	}
	
	/**
	 * 下拉框根据index位置选择
	 * @param locator
	 * @param index
	 */
	public void selectByIndex(By locator,int index){
		Select select = new Select(driver.findElement(locator));
		select.selectByIndex(index);
	}
	
	/**
	 * 下拉框根据value值选择
	 * @param locator
	 * @param text
	 */
	public void selectByValue(By locator,String text){
		Select select = new Select(driver.findElement(locator));
		select.selectByValue(text);
	}
	
	/**
	 * 取出所有下拉框的对象，然后循环对象取出text文本值放入到List返回
	 * @param locator
	 * @param text
	 * @return
	 */
	public List<String> selectGetOptions(By locator){
		Select select = new Select(driver.findElement(locator));
		List<String> selectValue = new ArrayList<>();
		for(int i=0;i<select.getOptions().size();i++){
			selectValue.add(select.getOptions().get(i).getText());
		}
        return selectValue;
	}
	
	/**
	 * 取table的行列，取行定位到tr,取列定位到td
	 * @param locator
	 * @return
	 */
	public int tableRowsORCols(By locator){
		return driver.findElements(locator).size();
	}
	
	/**
	 * 切换到iframe框架
	 * @param locator
	 */
	public void selectiframe(By locator){
		driver.switchTo().frame(driver.findElement(locator));
	}
	
	/**
	 * 离开iframe框架
	 */
	public void quitiframe(){
		driver.switchTo().defaultContent();
	}
	
	/**
	 * 输入存放路径，截图名字，固定以png格式图片
	 * @param path
	 * @param pictureName
	 */
	public void printScreen(String path,String pictureName){
		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(srcFile, new File(path+pictureName+".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 鼠标操作之一，滚动到元素位置
	 * @param locator
	 */
	public void moveMenu(By locator){
		//滚动到素材管理菜单
		Actions actions = new Actions(driver);
		if(AutoWait(locator)){
			actions.moveToElement(driver.findElement(locator)).perform();
			logger.info("滚动到元素位置");
		}
		
	}
	
	
	/**
	 * 使用js操作滚动条，滚动位置根据hight传值来定
	 * @param hight
	 */
	public void Scroll(String hight){
		//滚动条滚动
		JavascriptExecutor JS=(JavascriptExecutor) driver;
		String high="scroll(0,"+hight+");";//滚动到Y值10000像素的位置，一般10000就到页面的底部了，可以根据自己需要调试
		JS.executeScript(high);
	}
}
