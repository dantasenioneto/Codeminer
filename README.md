# Codeminer
Quake log parser

instructions to Run the Parser;
Required
1º Mysql Server 5.5 - with max_connections >= 200
	(how to do: execute the command "SET GLOBAL max_connections = 200;");
2º create a Database in mysql with name "parser" the command 
	(how to do: "CREATE DATABASE parser;");
3º at method getBufferReaderLog put the location from games.log
4º execute the class PopulateDb.java at package br.com.codeminer.parser.controller; 
OBS: case to occur: "ERROR: Data source rejected establishment of connection,  message from server: "Too many connections",
follow de first step.
