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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.full.java.network.coordination.spacemanager;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import otsopack.full.java.network.coordination.ISpaceManager;
import otsopack.full.java.network.coordination.Node;

public class FileSpaceManager implements ISpaceManager {

	private final File file;
	
	public FileSpaceManager(File file){
		if(!file.exists())
			throw new IllegalArgumentException("Provided file: " + file.getAbsolutePath() + " not found in disk");
		
		this.file = file;
	}
	
	@Override
	public Node[] getNodes() throws SpaceManagerException {
		final Node[] nodes;
		try {
			final FileInputStream fis = new FileInputStream(this.file);
			final String fileContent = IOUtils.toString(fis);
			final JSONArray arr = new JSONArray(fileContent);
			nodes = new Node[arr.length()];
			for(int i = 0; i < arr.length(); ++i){
				final JSONObject obj = arr.getJSONObject(i);
				final String uuid = obj.getString("uuid");
				final String url = obj.getString("url");
				final boolean reachable = obj.optBoolean("reachable", true);
				final boolean mustPoll  = obj.optBoolean("mustPoll", false);
				final Node node = new Node(url, uuid, reachable, mustPoll);
				nodes[i] = node;
			}
		} catch (Exception e) {
			throw new SpaceManagerException("Error retrieving nodes from file " + this.file.getAbsolutePath() + ":" + e.getMessage(), e);
		}
		return nodes;
	}

}
