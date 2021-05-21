package Deliverable1;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.errors.TransportException;


public class GithubHandler {
	

	private GithubHandler() {}
	
	
	public static Git cloneProjectFromGitHub(String path, String projectName) throws GitAPIException, IOException {
		//get the project from github and copy it locally
		
		Git git;
		
		try {
			git = Git.cloneRepository()
				       .setURI("https://github.com/apache/" + projectName)
				       .setDirectory(new File(path)) 
				       .call();
		} catch (JGitInternalException e) {
			git = Git.open(new File(path));
			
		}
		
		return git;
		
		
	}
	
	
	public static List<RevCommit> getAllCommits(Git git) throws GitAPIException, IOException{
		
		List<RevCommit> commits = new ArrayList<>();
		Iterable<RevCommit> log = git.log().all().call();

        for (RevCommit commit : log) {
        		
                commits.add(commit);
        }
		return commits;
        
        //System.out.print(commits.toString() + "\n");
		
	}


	public static LocalDateTime findFixDate(List<RevCommit> commits, String key) {
		
		Collections.reverse(commits);
		LocalDateTime dateTime = null;
		
		for (int i=0; i<commits.size(); i++) {
			RevCommit commit = commits.get(i);
			
			if (commit.getFullMessage().contains(key+"\n")) {
				
				dateTime = Instant.ofEpochSecond(commit.getCommitTime()).atZone(ZoneId.of("UTC")).toLocalDateTime();
				
				//System.err.print("\nKey: " + ticket.getTicketKey() + "\n" + commit.getFullMessage() +"\n\n");
				//System.out.println("ticket: " + ticket.getTicketKey() + "------>commit: " + commit.getFullMessage());
				
			}
			
		}
		return dateTime;
		
		
	}
	
	

}
