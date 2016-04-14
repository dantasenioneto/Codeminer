package br.com.codeminer.parser.view;

import java.util.List;

import br.com.codeminer.parser.DAO.DAO;
import br.com.codeminer.parser.DAO.DashboardDAO;
import br.com.codeminer.parser.json.JSONArray;
import br.com.codeminer.parser.json.JSONObject;
import br.com.codeminer.parser.modelo.Game;

public class ResultLogParser {

	public static void main(String[] args) {

		List<Game> games = new DAO<Game>(Game.class).listAll();
		for (Game game : games) {
			displayReportFromGame(game);
		}
	}

	/***
	 * this method show to display the Reports by Game
	 * 
	 * @param game
	 */
	public static void displayReportFromGame(Game game) {
		System.out.println("game_" + game.getId() + ":");
		System.out.println("total_kills: " + getTotalDeadListJS(game));
		System.out.println(getPlayerListJS(game));
		System.out.println(getNameDeadListJS(game));
		System.out.println(getTotalDeathByMeans(game));
		 
	}

	private static String getTotalDeathByMeans(Game game) {
		List<String> namesAndCounts = new DashboardDAO().getDeathMeansListWithCountDeads(game);
		JSONObject my_obj = new JSONObject();
		JSONArray DeadsJS = new JSONArray();
		
		for (String nameNCount : namesAndCounts) {
			DeadsJS.put(nameNCount);
		}
		my_obj.put("kills_by_means: ", DeadsJS);
		return my_obj.toString();
	}

	/***
	 * this method configure to String JSON the Players in game.
	 * 
	 * @param game
	 * @return
	 */
	public static String getPlayerListJS(Game game) {
		JSONObject my_obj = new JSONObject();
		JSONArray playersJS = new JSONArray();

		for (int i = 0; i <= game.getPlayersList().size() - 1; i++) {
			playersJS.put(game.getPlayersList().get(i).getName());
		}
		my_obj.put("Players", playersJS);
		return my_obj.toString();
	}

	/***
	 * this method configure to String JSON the Dead's name in game.
	 * 
	 * @param game
	 * @return
	 */
	public static String getNameDeadListJS(Game game) {
		List<String> namesAndCounts = new DashboardDAO().getNameDeadsListWithCountDeads(game);
		JSONObject my_obj = new JSONObject();
		JSONArray DeadsJS = new JSONArray();

		for (String nameNCount : namesAndCounts) {
			DeadsJS.put(nameNCount);
		}
		my_obj.put("Kills", DeadsJS);
		return my_obj.toString();
	}

	/***
	 * this method configure to String JSON the total's Dead in game.
	 * 
	 * @param game
	 * @return
	 */
	public static Long getTotalDeadListJS(Game game) {
		Long total = new DashboardDAO().totalDeadsInGame(game);
		return total;
	}

}
