/*
 * Copyright (C) 2008 onwards University of Deusto
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
package otsopack.commons.network.coordination.spacemanager;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;

import otsopack.commons.network.coordination.Node;

public class FileSpaceManager extends SpaceManager {

	private final File file;
	
	public FileSpaceManager(File file){
		if(!file.exists())
			throw new IllegalArgumentException("Provided file: " + file.getAbsolutePath() + " not found in disk");
		
		this.file = file;
	}
	
	protected boolean optBoolean(LinkedHashMap<String, Object> object, String key, boolean defaultValue) {
		if (object.containsKey(key)) {
			return ((Boolean)object.get(key));
		}
		return defaultValue;
	}
	
	@Override
	public Node[] getRegisteredNodes() throws SpaceManagerException {
		final Node[] nodes;
		try {
			final FileInputStream fis = new FileInputStream(this.file);
			final String fileContent = IOUtils.toString(fis);
			ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
			LinkedList<Object> arr = mapper.readValue(fileContent, LinkedList.class);
			
			nodes = new Node[arr.size()];
			int i=0;
			for(Object object: arr){
				final LinkedHashMap<String, Object> obj = (LinkedHashMap<String, Object>) object;
				final String uuid = (String) obj.get("uuid");
				final String url = (String) obj.get("url");
				final boolean reachable = optBoolean(obj, "reachable", true);
				final boolean isBulletinBoard = optBoolean(obj, "bulletinBoard", true);
				final boolean mustPoll = optBoolean(obj, "mustPoll", false);
				final Node node = new Node(url, uuid, reachable, isBulletinBoard, mustPoll);
				nodes[i] = node;
				i++;
			}
		} catch (ClassCastException e) {
			throw new SpaceManagerException("Error retrieving nodes from file " + this.file.getAbsolutePath() + ":" + e.getMessage(), e);
		} catch (Exception e) {
			throw new SpaceManagerException("Error retrieving nodes from file " + this.file.getAbsolutePath() + ":" + e.getMessage(), e);
		}
		return nodes;
	}

	private static final String [] references = new String[]{};
	
	@Override
	public String [] getExternalReferences() {
		return references;
	}
}