package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


public class util {

	
	public Properties readBranchProperties() {
		
		Properties props = new Properties();
		
		try {
			//jar 위치 -> properties 경로
			String filePath = ClassLoader.getSystemClassLoader().getResource(".").getPath() + "config/branch.properties";
			System.out.println(filePath);
			//파일 생성
			File file = new File(filePath);
			
			//fileInputStream 생성
			FileInputStream fis = new FileInputStream(file);
			
			//properties 생성
			props.load(fis);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return props;	
	}
	
	public Properties readLocalProperties() {
		Properties prop = new Properties();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config/local.properties");
		
		try {
			if(inputStream != null) {
				prop.load(inputStream);
				return prop;
			} else {
				throw new Exception("Error!!");
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
