package connectGit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import java.util.Properties;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import util.util;


public class GitService {

	Scanner scan = new Scanner(System.in);
	util util = new util();

	Properties localProps = util.readLocalProperties();
	Properties branchProps = util.readBranchProperties();
	
	String username = localProps.getProperty("username");
	String password = localProps.getProperty("password");
	
	String branch = branchProps.getProperty("branch");
	;
	
	
	//1. 조직 리포지토리 목록 구하기
	public JSONArray getOrgRepository() throws Exception 
	{
		String apiUrl = "https://api.github.com/orgs/bsJavaOrg/repos";
		String restType = "GET";
		
		StringBuilder buffer = callRestApi(apiUrl, restType);
		
//		System.out.println("결과 : " + buffer.toString());
		JSONArray jsonArr = new JSONArray(buffer.toString());

		return jsonArr;
	}
	
	//2. 조직 리포지토리 별 풀리퀘스트 목록 가져오기(브랜치)
	public HashMap<String, String> getPullRequestList(String repoAddr) throws Exception 
	{
		String apiUrl = "https://api.github.com/repos/" + repoAddr + "/pulls";	// 각자 상황에 맞는 IP & url 사용 	
		String restType = "GET";
		
		StringBuilder buffer = callRestApi(apiUrl, restType);

		JSONArray jsonArr = new JSONArray(buffer.toString());
		
		HashMap<String, String> map = getPullRequest(jsonArr);
		
		return map;
	}
	
	public void mergeRequest(String prUrl) throws Exception 
	{

	    String apiUrl = prUrl + "/merge";	// 각자 상황에 맞는 IP & url 사용 	
	    String restType = "PUT";
	    callRestApi(apiUrl, restType);
	    
	}
	
	private HashMap<String, String> getPullRequest(JSONArray jsonArr) {
		HashMap<String, String> map = new HashMap<String, String>();
		
		try {
			
			for(int i = 0; i < jsonArr.length(); i++) {
				JSONObject jsonObj = (JSONObject) jsonArr.get(i);
				
				String branchTitle = new JSONObject(jsonObj.optString("head")).optString("ref");
				
				if(branchTitle.equals(branch)) {
					System.out.println("");
					System.out.println("================= 풀리퀘스트 ==========================");
					System.out.println("리포지토리 : " + new JSONObject(new JSONObject(jsonObj.optString("head")).optString("repo")).optString("full_name"));
					System.out.println("브랜치 : " + new JSONObject(jsonObj.optString("head")).optString("ref"));
					System.out.println("메시지 : " + jsonObj.optString("title"));
					String timezone = new JSONObject(new JSONObject(jsonObj.optString("head")).optString("repo")).optString("pushed_at").toString();
					String convertTime = convertTime(timezone);
					System.out.println("pushed_at : " + convertTime);
					map.put("repository", new JSONObject(new JSONObject(jsonObj.optString("head")).optString("repo")).optString("full_name"));
					map.put("branch", new JSONObject(jsonObj.optString("head")).optString("ref"));
					map.put("title", jsonObj.optString("title"));
					map.put("url", jsonObj.optString("url"));
					
				} else {
					continue;
				}
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	
	private StringBuilder callRestApi(String apiUrl, String restType) {

		StringBuilder buffer = null;
		try {
			String readLine = null;
			BufferedReader bufferedReader = null;
			BufferedWriter bufferedWriter = null;
			HttpURLConnection urlConnection = null;
				
			try 
			{
				urlConnection = connectUrlConnection(new URL(apiUrl), restType, password);
				
				buffer = new StringBuilder();
				if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) 
				{
					bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
					while((readLine = bufferedReader.readLine()) != null) 
					{
						buffer.append(readLine).append("\n");
					}
				}
				else 
				{
					buffer.append("code : ");
					buffer.append(urlConnection.getResponseCode()).append("\n");
					buffer.append("message : ");
					buffer.append(urlConnection.getResponseMessage()).append("\n");
				}
			}
			catch(Exception ex) 
			{
				ex.printStackTrace();
			}
			finally 
			{
				try 
				{
					if (bufferedWriter != null) { bufferedWriter.close(); }
					if (bufferedReader != null) { bufferedReader.close(); }
				}
				catch(Exception ex) 
				{ 
					ex.printStackTrace();
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return buffer;
	}
	
	private HttpURLConnection connectUrlConnection(URL url, String type, String password) throws IOException {
		
		int connTimeout = 5000;
	    int readTimeout = 3000;
		
		HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection(); 
		
		urlConnection.setRequestMethod(type);
        urlConnection.setConnectTimeout(connTimeout);
        urlConnection.setReadTimeout(readTimeout);
        urlConnection.setRequestProperty("Accept", "application/json;");
		urlConnection.setRequestProperty("Authorization", "token " + password);
		
		return urlConnection;
	}
	
	private String convertTime(String timezone) throws ParseException {
	
		String orgTime = timezone;
		System.out.println(timezone);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		
		Date date = formatter.parse(orgTime);
		
		System.out.println(date);
		
		
		return orgTime;
	}

	
 
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Deprecated
	public void apiTestPost() throws Exception 
	{
		
		System.out.println(password);
		URL url = null;
		String readLine = null;
		StringBuilder buffer = null;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		HttpURLConnection urlConnection = null;
		
		int connTimeout = 5000;
		int readTimeout = 3000;
		
		String apiUrl = "https://api.github.com/repos/soobin10236/test_repo/pulls";	// 각자 상황에 맞는 IP & url 사용 		
		
		try 
		{
			url = new URL(apiUrl);
			urlConnection = (HttpURLConnection)url.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setConnectTimeout(connTimeout);
			urlConnection.setReadTimeout(readTimeout);
			urlConnection.setRequestProperty("Accept", "application/json;");
			urlConnection.setRequestProperty("Authorization", "token " + password);
			
			urlConnection.setDoOutput(true); //OutputStream을 사용해서 post body 데이터 전송
			
			String ParamData = "";
			ParamData =  "{ \"title\" : \"commit java!!!\", \"head\" : \"dev-tnqls7742\", \"base\" : \"master\" }";
			try (OutputStream os = urlConnection.getOutputStream()){
				byte request_data[] = ParamData.getBytes("utf-8");
				os.write(request_data);
				os.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			//http 요청 실시
			urlConnection.connect();
			System.out.println("http 요청 방식 : "+"POST BODY JSON");
			System.out.println("http 요청 타입 : "+"application/json");
			System.out.println("http 요청 주소 : "+apiUrl);
			System.out.println("http 요청 데이터 : "+ParamData);
			System.out.println("=============================");
			
			
			buffer = new StringBuilder();
			if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) 
			{
				bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
				while((readLine = bufferedReader.readLine()) != null) 
				{
					buffer.append(readLine).append("\n");
				}
			}
			else 
			{
				buffer.append("code : ");
				buffer.append(urlConnection.getResponseCode()).append("\n");
				buffer.append("message : ");
				buffer.append(urlConnection.getResponseMessage()).append("\n");
			}
		}
		catch(Exception ex) 
		{
			ex.printStackTrace();
		}
		finally 
		{
			try 
			{
				if (bufferedWriter != null) { bufferedWriter.close(); }
				if (bufferedReader != null) { bufferedReader.close(); }
			}
			catch(Exception ex) 
			{ 
				ex.printStackTrace();
			}
		}
		
		System.out.println(buffer.toString());
		
	}

	
}






