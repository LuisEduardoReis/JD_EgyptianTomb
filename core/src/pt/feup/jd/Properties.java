package pt.feup.jd;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public class Properties {

	HashMap<String,String> container;
	
	public Properties() {
		container = new HashMap<String, String>();
	}

	public void load(InputStream read) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(read));
		String line;
		while((line = reader.readLine()) != null) {
			if (line.startsWith("#")) continue;
			
			int index = line.indexOf("=");
			container.put(line.substring(0,index), line.substring(index+1, line.length()));
		}
		reader.close();
	}
	
	public void store(OutputStream write) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(write));
		for(String key : container.keySet()) {
			writer.write(key+"="+container.get(key)+"\n");
		}
		writer.close();
	}	

	public String getProperty(String name) {
		return container.get(name);
	}

	public boolean containsKey(String name) {
		return container.containsKey(name);
	}

	public void put(String key, String value) {
		container.put(key, value);		
	}
}
