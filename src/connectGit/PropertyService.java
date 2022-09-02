package connectGit;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


public class PropertyService {

	public Properties getProperties() {
		
		Properties props = new Properties();
		
		try {
			//jar 위치 -> properties 경로
			String filePath = ClassLoader.getSystemClassLoader().getResource(".").getPath() + "local.properties";
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
}
