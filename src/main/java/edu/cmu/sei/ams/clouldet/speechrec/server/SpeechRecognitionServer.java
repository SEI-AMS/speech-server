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

import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.HeapChannelBufferFactory;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * 
 * @author ssimanta
 * 
 */
public class SpeechRecognitionServer {

	public static final int DEFAULT_SERVER_PORT = 10191;

	public static void main(String[] args) throws Exception {

		int serverPort = DEFAULT_SERVER_PORT;
		if (args == null || args.length == 0) {
			System.out.println("Using default server port "
					+ DEFAULT_SERVER_PORT);
		} else if (args.length == 1)
			try {
				serverPort = Integer.parseInt(args[0]);
			} catch (NumberFormatException nfe) {
				System.err.println("Input port " + serverPort
						+ "is invalid. Using default port "
						+ DEFAULT_SERVER_PORT);

			}

		SpeechRecognitionServer server = new SpeechRecognitionServer();
		server.run(serverPort);
	}

	public void run(final int port) {
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ServerBootstrap bootstrap = new ServerBootstrap(factory);

		bootstrap.setPipelineFactory(new SpeechServerPipelineFactory());

		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.setOption("child.bufferFactory",
				new HeapChannelBufferFactory(ByteOrder.BIG_ENDIAN));

		bootstrap.bind(new InetSocketAddress(port));
		System.out.println("SpeechRecognitionServer V5.0 listening on " + port
				+ " ...");
	}

}
