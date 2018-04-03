package selenium.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
public class ListUtil {
	private static Logger logger = Logger.getLogger(ListUtil.class);
	/**
	 * 对比两个List的值，适合用于table、下拉框值的比较
	 * @param list_a
	 * @param list_b
	 * @return
	 */
	public static boolean listCompare(List<String> list_a,List<String> list_b){
		if(list_a.size()!=list_b.size()){
			logger.error("两个list集合长度不一致，无法对比");
			return false;
		}
		//对List里面的内容排序
		Collections.sort(list_a);  
        Collections.sort(list_b);  
        for(int i=0;i<list_a.size();i++){  
            if(!list_a.get(i).equals(list_b.get(i))){
            	logger.error("两个list集合内容不相等");
                return false;  
            }
        }
        logger.info("list集合内容一致");
        return true;
	}
	
	/**
	 * 传入逗号字符串，分割成List集合返回
	 * @param text
	 * @return
	 */
	public static List<String> returnList(String text){
		List<String> list = new ArrayList<String>();
		try {
			String t[] = text.split(",");
			for(int i=0;i<t.length;i++){
				if(t[i].equals("空")){
					t[i] = "";
				}
				list.add(t[i]);
			}
			logger.info("param参数内容："+list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("list集合读取异常："+e.getMessage());
		}
		return list;
	}
}
