/*
 * Copyright (C) 2008-2011 University of Deusto
 * 
 * All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */
package otsopack.otsoME.log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import otsopack.commons.log.IPrintOut;

public class FileWriter implements IPrintOut {
	static final private String SEPARATOR = System.getProperty("file.separator");
	String filepath; 
	FileConnection fc;
	OutputStream out;
    PrintStream output;
	
    /**
     * See in the path
     * 		/home/twolf/j2mewtk/2.5.2/appdb/MediaControlSkin/filesystem/root1/logs/filename
     * @param filename
     * 		The name of the file in which the log lines will be written.
     * @throws IOException
     */
	public FileWriter(String filename) throws IOException {
		if( System.getProperty("microedition.io.file.FileConnection.version") != null ) {
			// /home/twolf/j2mewtk/2.5.2/appdb/MediaControlSkin/filesystem/root1/
			filepath = System.getProperty("fileconn.dir.photos") + "logs" + SEPARATOR;
			createFolder(filepath);
			filepath += filename;
		}
	}
	
		private void createFolder(String foldername) throws IOException {
			FileConnection folder = (FileConnection) Connector.open(foldername);
			if( !folder.exists() ) {
				folder.mkdir();
			}
			folder.close();
		}

	public void startup() throws IOException {
		fc = (FileConnection) Connector.open(filepath,Connector.READ_WRITE);
		if( !fc.exists() ) {
			fc.create();
		} else {
			fc.delete();
			fc.create();
		}
		    
		out = fc.openOutputStream();
		output = new PrintStream( out );
	}
	
	public void print(String msg) {
		output.println(msg);
        output.flush();
        try {
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void shutdown() throws IOException {
		output.close();
		out.close();
		fc.close();
	}
}
