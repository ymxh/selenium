package selenium.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

public class CustomReport implements IReporter{
    
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        List<ITestResult> list = new ArrayList<ITestResult>();
        List<Long> passTestsList = new ArrayList<Long>();
        List<Long> failedTestsList = new ArrayList<Long>();
        List<Long> skippedTestsList = new ArrayList<Long>();
        List<Long> suiteList = new ArrayList<Long>();
        List<Long> totalData = new ArrayList<Long>();
        //循环读取suites
        for (ISuite suite : suites) {
        	//拿到suite的结果
            Map<String, ISuiteResult> suiteResults = suite.getResults();
            //循环读取结果
            for (ISuiteResult suiteResult : suiteResults.values()) {
                ITestContext testContext = suiteResult.getTestContext();
                IResultMap passedTests = testContext.getPassedTests();
                IResultMap failedTests = testContext.getFailedTests();
                IResultMap skippedTests = testContext.getSkippedTests();
               // IResultMap failedConfig = testContext.getFailedConfigurations();
                //统计总的测试用例与总测试时间
                passTestsList.add((long)passedTests.size());
                failedTestsList.add((long)failedTests.size());
                skippedTestsList.add((long)skippedTests.size());
                suiteList.add(testContext.getEndDate().getTime()-testContext.getStartDate().getTime());
                //统计通过、失败、跳过的测试用例数
                list.addAll(this.listTestResult(passedTests));
                list.addAll(this.listTestResult(failedTests));
                list.addAll(this.listTestResult(skippedTests));
             //   list.addAll(this.listTestResult(failedConfig));
            }          
        }
        totalData.add(this.forTestCaseData(passTestsList));
        totalData.add(this.forTestCaseData(failedTestsList));
        totalData.add(this.forTestCaseData(skippedTestsList));
        totalData.add(this.forTestCaseData(suiteList));
        this.sort(list);
        this.outputResult(totalData, list, outputDirectory+"/custom_report");
    }
    
    /**
     * 转化为ArrayList返回
     * @param resultMap
     * @return
     */
    private ArrayList<ITestResult> listTestResult(IResultMap resultMap){
        Set<ITestResult> results = resultMap.getAllResults();    
        return new ArrayList<ITestResult>(results);
    }
    
    /**
     * 排序
     * @param list
     */
    private void sort(List<ITestResult> list){
        Collections.sort(list, new Comparator<ITestResult>() {
            @Override
            public int compare(ITestResult r1, ITestResult r2) {
                if(r1.getStartMillis()>r2.getStartMillis()){
                    return 1;
                }else{
                    return -1;
                }              
            }
        });
    }
    
    /**
     * List的值加起来返回结果
     * @param list
     * @return
     */
    private Long forTestCaseData(List<Long> list){
    	long testData = 0;
    	for(int i=0;i<list.size();i++){
    		testData += list.get(i);
    	}
    	return testData;
    }
    
    
    /**
     * 报告输出类
     * @param dlist
     * @param list
     * @param path
     */
    private void outputResult(List<Long> dlist, List<ITestResult> list, String path){
        try {
        	//报告输出路径，不存在则创建
        	File reportDir = new File(path);    
            if (!reportDir.exists()) {    
                reportDir.mkdirs();    
            }    
            String reportName = "seleniumReport.html";    
            String reportPath = reportDir+"/"+reportName;    
            File report = new File(reportDir,reportName);    
            if(report.exists()){      
            	report.createNewFile();      
            }
            
            BufferedWriter output = new BufferedWriter(new FileWriter(new File(reportPath)));
            StringBuffer sb = new StringBuffer();
            //得到总用例数
            long totalTestCase = dlist.get(0)+dlist.get(1)+dlist.get(2);
            sb.append("<html><head><title>测试报告</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
            		+ "<style type=\"text/css\">"
            		+ ".all{margin:0 10px 0 10px;}"
            		+ ".bugCount{padding-top:10px;}"
            		+ "input{border:0;background-color:white;}"
            		+ ".failed{color:red;}.success{color:green;}"
            		+ "td{border:solid #add9c0; border-width:0px 1px 1px 0px; }"
            		+ "table{border:solid #add9c0; border-width:1px 0px 0px 1px;table-layout:fixed;word-break:break-all;width:100%;}.title{background-color:#7FFFD4;}"
            		+ "tr td{width: 30%;box-sizing: border-box;}tr td:nth-of-type(1),tr td:nth-of-type(3){width: 25%;}tr td:nth-of-type(2),tr td:nth-of-type(4){width: 10%;}</style></head>"
            		+"<body><div class=\"all\"><div><div><h2>Selenium自动化测试报告</h2></div><div><div><strong>执行结果总计</strong></div><div class=\"bugCount\">");
            sb.append("<b>总用例:</b><input value="+totalTestCase+" disabled=\"disabled\" id=\"case_count\"/>"
            		+ "<b>通过:</b><input value="+dlist.get(0)+" disabled=\"disabled\" id=\"success\" class=\"success\"/>"
            		+ "<b>失败:</b><input value="+dlist.get(1)+" disabled=\"disabled\" id=\"failed\" class=\"failed\"/>"
            		+ "<b>跳过:</b><input value="+dlist.get(2)+" disabled=\"disabled\" id=\"skided\" class=\"skided\"/>"
            		+ "<b>通过率:</b><input value='"+((dlist.get(0)/totalTestCase)*100)+"%' disabled=\"disabled\" id=\"pass_rate\" />"
            		+ "<b>总耗时:</b><input value="+dlist.get(3)+" disabled=\"disabled\" id=\"times\"/></div></div></div><hr /><div>");
            		
            sb.append("<table><tr class=\"title\"><td>测试类</td><td>测试方法</td><td>测试参数</td><td>执行时间</td><td>测试结果</td></tr>");  
            for (ITestResult result : list) {
                 sb.append("<tr><td>"+result.getTestClass().getRealClass().getName());
                 sb.append("</td><td>");
                 sb.append(result.getMethod().getMethodName());
                 sb.append("</td><td>");
                 sb.append("<div align=\"left\">测试数据:");
                 sb.append(result.getParameters()[0]);
                 sb.append("</div><div align=\"left\">预期结果:");
                 sb.append(result.getParameters()[1]);
                 sb.append("</div></td><td>");
                 sb.append(result.getEndMillis()-result.getStartMillis());
                 sb.append("</td><td>");
                 if("SUCCESS".equals(getStatus(result.getStatus()))){
                	 sb.append("<font color=\"green\">Passed</font>");
                 }else if("SKIP".equals(getStatus(result.getStatus()))){
                	 sb.append("<font color=\"yellow\">Skipped</font>");
                     sb.append("<p align=\"left\">测试用例<font color=\"red\">跳过</font>，原因：<br/>");  
                     sb.append("<br><a style=\"background-color:#CCCCCC;\">");  
                     Throwable throwable = result.getThrowable();   
                     sb.append(throwable.getMessage());   
                     sb.append("</a></p>");
                 }else{
                	 sb.append("<font color=\"yellow\">Skipped</font>");
                     sb.append("<p align=\"left\">测试用例<font color=\"red\">失败</font>，原因：<br/>");  
                     sb.append("<br><a style=\"background-color:#CCCCCC;\">");  
                     Throwable throwable = result.getThrowable();   
                     sb.append(throwable.getMessage());   
                     sb.append("</a></p>"); 
                 }
                 sb.append("</td></tr>");          
            }
            sb.append("</table></div></div></body></html>");
            output.write(sb.toString());
            output.flush();
            output.close();
        } catch (IOException e) {          
            e.printStackTrace();
        }
         
    }
    
    /**
     * TestNg状态码，1表示成功、2表示失败、3表示跳过
     * @param status
     * @return
     */
    private String getStatus(int status){
        String statusString = null;
        switch (status) {      
        case 1:
            statusString = "SUCCESS";
            break;
        case 2:
            statusString = "FAILURE";
            break;
        case 3:
            statusString = "SKIP";
            break;
        default:           
            break;
        }
        return statusString;
    }
}
