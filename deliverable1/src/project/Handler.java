package project;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Handler {

	private Handler() {
	}

	private static String readAll(Reader rd) throws IOException {
		var sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try (var rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){

			
			String jsonText = readAll(rd);
			return new JSONArray(jsonText);

		} finally {
			is.close();
		}
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try(var rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
			
			String jsonText = readAll(rd);
			return new JSONObject(jsonText);
		} finally {
			is.close();
		}
	}

	public static void writeCsv(Map<String, LocalDateTime> resolutions, List<String> tickets, double percentage) throws IOException {

		Integer numTickets;
		Integer i;
		
		try(FileWriter fileWriter = new FileWriter("ProcessControlChart.csv")){ // Name of CSV for output) 
			
			
			
			fileWriter.append("Ticket; Resolution Date; Percentage of tickets without fixDate");
			
			fileWriter.append("\n");
			numTickets = resolutions.size();
			for (i = 0; i < numTickets; i++) {
				
				fileWriter.append(tickets.get(i));
				fileWriter.append(";");
				fileWriter.append(resolutions.get(tickets.get(i)).toString().subSequence(0, 10));
				
				if (i == 0) {
					fileWriter.append(";");
					fileWriter.append(percentage+ "%");
				}
				fileWriter.append("\n");
			}
			
			fileWriter.flush();
		

		} catch (Exception e) {
			MyLogger.getSingletonInstance().saveMess("[*] Error in csv writing!!!");
			e.printStackTrace();
		} 
		

	}
}
