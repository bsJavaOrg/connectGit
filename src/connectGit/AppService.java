package connectGit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.json.JSONArray;
import org.json.JSONObject;


public class AppService {
	
	public static void main(String[] args) throws Exception {
		
		System.out.println("started");
		
		GitService gitService = new GitService();
		
//		gitService.callGitService();
		
//		gitService.apiTestGet();
//		gitService.apiTestPost();
//		gitService.apiTestPut();
		
		JSONArray repoList = gitService.getOrgRepository();
		
		ArrayList<HashMap<String, String>> arr = new ArrayList<>();

		for(int i = 0; i < repoList.length(); i++) {
			JSONObject repoObj = (JSONObject) repoList.get(i);
			
			arr.add(gitService.getPullRequestList(repoObj.optString("full_name")));
//			gitService.apiTestPut(jsonObj.optString("number"));
		}
		
		System.out.println("");
		System.out.println("======================PullRequestList================================");
		for(HashMap<String, String> pr : arr) {
			System.out.println(pr.toString());
//			System.out.println(pr.get("url"));
			gitService.apiTestPut(pr.get("url"));
		}

		
			
		
		System.out.println("finished");
		
	}

}
