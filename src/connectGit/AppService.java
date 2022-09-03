package connectGit;


import java.util.ArrayList;
import java.util.HashMap;


import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Scanner;


public class AppService {
	
	public static void main(String[] args) throws Exception {
		
	System.out.println("ignore test");
	System.out.println("started");

	Scanner scan = new Scanner(System.in);
	
	//GitService 생성
	GitService gitService = new GitService();
	
	//조직 repository list 생성
	JSONArray repoList = gitService.getOrgRepository();
	
	//pullrequest 목록 담을 arrayList
	ArrayList<HashMap<String, String>> arr = new ArrayList<>();
	
	
	for(int i = 0; i < repoList.length(); i++) {
		//각 repository 객체 생성
		JSONObject repoObj = (JSONObject) repoList.get(i);
		
		//해당 repository에 pullRequest있는지 검사
		HashMap<String, String> repoMap = gitService.getPullRequestList(repoObj.optString("full_name"));
		
		//pullRequest 목록 존재 할 때 arrayList에 저장
		if(!repoMap.isEmpty()) {
			arr.add(repoMap);
		}
	}
	
//	arr.clear();
	
	if(arr.size() > 0) {
		System.out.println("");
		System.out.println("머지를 원하시면 1을 입력하세요. 이외에는 종료됩니다.");
		
		if(scan.nextInt() == 1) {
			System.out.println("");
			System.out.println("merge started...");
			for(HashMap<String, String> pr : arr) {

				gitService.mergeRequest(pr.get("url"));
			}
			System.out.println("merge finished...");
		} else {
			System.exit(0);
		}
		
		
	} else {
		System.out.println("");
		System.out.println("======================풀리퀘스트 목록이 존재하지 않습니다.================================");
	}
	
			
		
		System.out.println("finished");
		System.out.println("종료를 원하시면 아무 키나 입력하세요.");
		scan.hasNext();
		System.exit(0);
		
	}

}
