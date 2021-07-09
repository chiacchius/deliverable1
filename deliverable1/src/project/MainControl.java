package project;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.json.JSONException;
import org.json.JSONObject;




public class MainControl {

	static Map<String, LocalDateTime> resolutions = new HashMap<>();
	static List<String> tickets = new ArrayList<>();
	static List<RevCommit> commits = new ArrayList<>();
	static MyLogger logCTR;

	public static void main(String[] args) throws IOException, JSONException, GitAPIException {

		
		
		var projName = "STDCXX";
		var path = "./";
		Integer j = 0;
		Integer i = 0;
		Integer total = 1;
		var git = GithubHandler.cloneProjectFromGitHub(path + projName, projName);
		logCTR = MyLogger.getSingletonInstance();
		logCTR.saveMess("[*] Starting retrieve data for proj " + projName);

		commits = GithubHandler.getAllCommits(git);

		do {
			// Only gets a max of 1000 at a time, so must do this multiple times if bugs
			// >1000
			j = i + 1000;
			String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22" + projName
					+ "%22AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
					+ i.toString() + "&maxResults=" + j.toString();
			JSONObject json = Handler.readJsonFromUrl(url);
			var issues = json.getJSONArray("issues");
			total = json.getInt("total");

			for (; i < total && i < j; i++) {
				// Iterate through each bug
				var key = issues.getJSONObject(i % 1000).get("key").toString();

				LocalDateTime dateTime = GithubHandler.findFixDate(commits, key);
				if (dateTime != null) {
					resolutions.put(key, dateTime);
					tickets.add(key);
				} else {
					logCTR.saveMess(key);
				}

			}
		} while (i < total);

		Handler.writeCsv(resolutions, tickets);

		logCTR.saveMess("[*] Exiting for proj " + projName);
	}

}
