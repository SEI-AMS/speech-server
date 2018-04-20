/*
KVM-based Discoverable Cloudlet (KD-Cloudlet) 
Copyright (c) 2015 Carnegie Mellon University.
All Rights Reserved.

THIS SOFTWARE IS PROVIDED "AS IS," WITH NO WARRANTIES WHATSOEVER. CARNEGIE MELLON UNIVERSITY EXPRESSLY DISCLAIMS TO THE FULLEST EXTENT PERMITTEDBY LAW ALL EXPRESS, IMPLIED, AND STATUTORY WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND NON-INFRINGEMENT OF PROPRIETARY RIGHTS.

Released under a modified BSD license, please see license.txt for full terms.
DM-0002138

KD-Cloudlet includes and/or makes use of the following Third-Party Software subject to their own licenses:
MiniMongo
Copyright (c) 2010-2014, Steve Lacy 
All rights reserved. Released under BSD license.
https://github.com/MiniMongo/minimongo/blob/master/LICENSE

Bootstrap
Copyright (c) 2011-2015 Twitter, Inc.
Released under the MIT License
https://github.com/twbs/bootstrap/blob/master/LICENSE

jQuery JavaScript Library v1.11.0
http://jquery.com/
Includes Sizzle.js
http://sizzlejs.com/
Copyright 2005, 2014 jQuery Foundation, Inc. and other contributors
Released under the MIT license
http://jquery.org/license
*/

/** Last changed: $LastChangedDate:$
 * Last changed by: $Author:$
 * @version $Revision:$* 
 */

package edu.cmu.sei.ams.clouldet.speechrec.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import edu.cmu.sphinx.frontend.util.AudioFileDataSource;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Lattice;
import edu.cmu.sphinx.result.LatticeOptimizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;

/**
 * 
 * @author ssimanta
 * 
 */
public class SpeechRecognitionServerHandler extends SimpleChannelHandler {

	private Logger logger = Logger.getLogger(SpeechRecognitionServer.class
			.getName());

	private URL audioURL;
	private URL configURL;

	private File audioFile;

	public static final String FILE_PREFIX = "audio";
	public static final String FILE_SUFFIX = ".wav";
	public static final String CONFIG_FILE_PATH = "./config/config.xml";

	private ConfigurationManager configurationManager;
	private Recognizer recognizer;
	private AudioFileDataSource dataSource;
	private FileOutputStream fileOutputStream;

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelConnected(ctx, e);
		logger.info("Client channel connected ... ");
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {

		ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
		logger.info("Client message received ... ");

		// we know the buffer contains only the contents of the audio file
		try {
			// First store the audio file in a temp file.
			audioFile = File.createTempFile(FILE_PREFIX, FILE_SUFFIX);
			long start = System.currentTimeMillis();
			fileOutputStream = new FileOutputStream(audioFile);
			int payloadSize = buffer.array().length;

			// while (buffer.readable()) {
			fileOutputStream.write(buffer.readBytes(payloadSize).array());
			// }
			fileOutputStream.flush();
			long end = System.currentTimeMillis();
			long tempFileTime = (end - start);

			logger.log(Level.INFO, "Created a temp audio file of size "
					+ audioFile.length() + " at " + audioFile.getPath()
					+ " in " + tempFileTime + " ms.");
			
			// Now process the file and get the speech.
			start = System.currentTimeMillis();
			String output = getSpeechOutput(audioFile);
			end = System.currentTimeMillis();
			long time = (end - start);
			logger.log(Level.INFO, "Time required to process speech file "
					+ time + " ms.");
			
			// Clean up the files.
			fileOutputStream.close();
			audioFile.delete();
			
			// Send response.
			Channel channel = e.getChannel();			
			channel.write("Speech output: " + output + " [file_create: " + tempFileTime
					+ "ms , processing: " + time + " ms.]"); 

		} catch (IOException e1) {
			e1.printStackTrace();
			logger.info("IO Exception: " + e1.toString());
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();

		Channel ch = e.getChannel();
		ch.close();
	}

	private String getSpeechOutput(final File tempAudioFile) throws IOException {

		audioURL = new File(tempAudioFile.getAbsolutePath()).toURI().toURL();
		logger.log(Level.INFO, "audio url:" + audioURL);

		/** IMP: configurationManager, recognizer and dataSource can be used across multiple 
		 *  Netty Handler calls because they are class variables of the Handler class. */
		if (configurationManager == null) {
			configURL = new URL("file:" + CONFIG_FILE_PATH);
			logger.log(Level.INFO, "config url: " + configURL);			
			configurationManager = new ConfigurationManager(configURL);
		}
		
		/** allocate() is a very expensive operation. Do it only if and when required. 
		 *  IMP: I'm not sure that the same recognizer can be reused for multiple interactions. */
		if (recognizer == null) {
			recognizer = (Recognizer) configurationManager.lookup("recognizer");
			recognizer.allocate();
		}

		if (dataSource == null) {
			dataSource = (AudioFileDataSource) configurationManager
					.lookup("audioFileDataSource");
		}

		dataSource.setAudioFile(audioURL, null);

		boolean done = false;

		StringBuffer outputBuffer = new StringBuffer();
		while (!done) {
			Result result = recognizer.recognize();
			if (result != null) {
				//Lattice lattice = new Lattice(result);
				//LatticeOptimizer optimizer = new LatticeOptimizer(lattice);
				//optimizer.optimize();
				String resultText = result.getBestResultNoFiller();
				//logger.log(Level.INFO, "[ Best Match ]: " + resultText);
				outputBuffer.append(resultText).append(" ");
			} else {
				done = true;
			}
		}

		logger.log(Level.INFO, "[ Best Matches ]: " + outputBuffer.toString());
		return outputBuffer.toString();
	}
}
