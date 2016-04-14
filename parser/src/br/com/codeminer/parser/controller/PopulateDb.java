package br.com.codeminer.parser.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import br.com.codeminer.parser.DAO.DAO;
import br.com.codeminer.parser.modelo.Death;
import br.com.codeminer.parser.modelo.Game;
import br.com.codeminer.parser.modelo.Player;
import br.com.codeminer.parser.view.ResultLogParser;

public class PopulateDb {

	public static void main(String[] args) throws IOException {
		System.out.println("populating DATABASE");
		BufferedReader br = getBufferReaderLog("/home/brainiac/workspace/codeminer/Codeminer/games.log");
		String firstLine = getFirstLineLog(br);
		
		ArrayList<List<String>> listOfStringSeparatedPerLine = new ArrayList<List<String>>();
		listOfStringSeparatedPerLine = toSeparateMatchesPerLine(firstLine, br);
		
		defineGamesFromListOfMatches(listOfStringSeparatedPerLine);
		System.out.println("DATABASE populated;");
		
		ResultLogParser.main(null);
		
	}
	/**
	 * get the first line's log.
	 * @param BufferedReader br
	 * @return
	 * @throws IOException
	 */
	public static String getFirstLineLog(BufferedReader br) throws IOException{
		String line = br.readLine();
		return line;
	}
	/**
	 * locate the log file and return the; 
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public static BufferedReader getBufferReaderLog(String fileName) throws FileNotFoundException{
		FileInputStream stream = new FileInputStream(fileName);
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader br = new BufferedReader(reader);
		return br;
	}
	/***
	 * locate in log's file all mathches played.
	 * @param line
	 * @param BufferedReader
	 * @return list of list of line by log's file separated by game.
	 * @throws IOException
	 */
	public static ArrayList<List<String>> toSeparateMatchesPerLine(String linha, BufferedReader br) throws IOException {		
		ArrayList<List<String>> listOfGames = new ArrayList<List<String>>();
		while (linha != null) {
			if (linha.indexOf("InitGame:") == 7) {
				List<String> game = new ArrayList<String>();
				game.add(linha);
				linha = br.readLine();
				while (linha != null && linha.indexOf("ShutdownGame:") != 7) {
					game.add(linha);
					linha = br.readLine();
				}
				game.add(linha);
				listOfGames.add(game);
			}

			linha = br.readLine();

		}
		return listOfGames;

	}
	
	/***
	 * this method receive a list of lines and separe for each game,calling methods to populate a List of the POJOS for each Entity.
	 * @param listOfMatchesPlayedInStringList
	 */
	
	public static void defineGamesFromListOfMatches(ArrayList<List<String>> listOfStringSeparatedPerLine) {
		for (List<String> gameSeparated : listOfStringSeparatedPerLine) {

			List<Player> players = new ArrayList<>();
			List<Death> deads = new ArrayList<>();

			for (String lineOfeachGame : gameSeparated) {
				List<Player> Listplayers = setPlayerForEachGame(lineOfeachGame, players);
				   List<Death> Listdeads = setDeadPlayerForEachGame(lineOfeachGame, deads);
				setGamePlayed(Listplayers, Listdeads, lineOfeachGame);

			}
		}

	}
	/***
	 * this method set at POJO's Player the players's game;
	 * @param lines of eachGamePlayed
	 * @param players
	 * @return
	 */
	public static List<Player> setPlayerForEachGame(String ClientUserinfoChanged, List<Player> players) {
		Player playerOfGame = new Player();
		if (ClientUserinfoChanged.contains("ClientUserinfoChanged:")) {
			String namePlayer = ClientUserinfoChanged.substring(34, ClientUserinfoChanged.indexOf("\\t\\")).toString();
			playerOfGame.setName(namePlayer);
			players.add(new DAO<Player>(Player.class).addNewPlayer(playerOfGame));
		}
		return players;
	}
	/***
	 * this method set at POJO's Death the players's wich were Deads;
	 * @param listOfDeathPlayers
	 * @param lines of eachGamePlayed
	 * @return
	 */
	public static List<Death> setDeadPlayerForEachGame(String lineOfRound, List<Death> deads) {
		Death death = new Death();
		Player player = new Player();
		if (lineOfRound != null && lineOfRound.contains("<world>")) {
			
			int endIndex = lineOfRound.indexOf(" MOD_") - 3;
			int initMod = lineOfRound.indexOf(" by ");
			String namePlayer = lineOfRound.substring(39, endIndex).toString();
			String causeDeath = lineOfRound.substring(initMod + 3);
			
			player = new DAO<Player>(Player.class).getOriginalPlayerByName(namePlayer);
			death.setPlayer(player);
			death.setCause(causeDeath);
			deads.add(new DAO<Death>(Death.class).addNewDeath(death));
		}
		return deads;
	}
	/***
	 * this method set at POJO's Game the game's wich were Playeds;
	 * * @param players
	 * @param listOfDeathPlayers
	 * @param lines of eachGamePlayed
	 * @return
	 */
	public static void setGamePlayed(List<Player> players, List<Death> deads, String lineOfRound) {
		if (lineOfRound.contains("ShutdownGame:")) {
			new DAO<Game>(Game.class).addNewGame(new Game(players, deads));
		}
	}

}
