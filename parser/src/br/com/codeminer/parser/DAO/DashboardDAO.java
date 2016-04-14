package br.com.codeminer.parser.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.codeminer.parser.modelo.Death;
import br.com.codeminer.parser.modelo.Game;
import br.com.codeminer.parser.modelo.Player;
import br.com.codeminer.parser.util.JPAUtil;

public class DashboardDAO {

	/***
	 * Return the total of Deads in game.
	 * 
	 * @param game
	 * @return
	 */
	public Long totalDeadsInGame(Game game) {
		EntityManager em = new JPAUtil().getEntityManager();
		Session session = em.unwrap(Session.class);
		Long total = (Long) session.createCriteria(Death.class).add(Restrictions.eq("game", game))
				.setProjection(Projections.rowCount()).uniqueResult();
		return total;
	}

	/***
	 * return dead's names from game ordered by name ASC
	 * 
	 * @param game
	 * @return List Player
	 */
	@SuppressWarnings("unchecked")
	public List<Player> namesFromDeadsInGameAsc(Game game) {
		EntityManager em = new JPAUtil().getEntityManager();
		List<Player> resultsName = (List<Player>) em
				.createQuery("SELECT DISTINCT(m.player) FROM Death AS m where m.game=:idGame ORDER BY m.player ASC")
				.setParameter("idGame", game).getResultList();

		return resultsName;
	}

	/***
	 * return dead's Means from game ordered by cause ASC
	 * 
	 * @param game
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> meansOfDeadsInGameByCauseAsc(Game game) {
		EntityManager em = new JPAUtil().getEntityManager();
		List<String> resultsMeansDeath = (List<String>) em
				.createQuery("SELECT DISTINCT(m.cause) FROM Death AS m where m.game=:idGame ORDER BY m.cause ASC")
				.setParameter("idGame", game).getResultList();

		return resultsMeansDeath;
	}

	/***
	 * return List String with Player's name and total Dead's count concatenated
	 * for each game.
	 * 
	 * @param game
	 * @return
	 */
	public List<String> getNameDeadsListWithCountDeads(Game game) {

		List<Player> resultsName = namesFromDeadsInGameAsc(game);
		List<Number> resultsCount = countDeadsFromGameBynameAsc(game);
		return unifyListNameDeadsWithListCountDeadsByname(resultsName, resultsCount);
	}

	/***
	 * this method concatenate Player's name and total Dead's count.
	 * 
	 * @param resultsName
	 * @param resultsCount
	 * @return
	 */
	public List<String> unifyListNameDeadsWithListCountDeadsByname(List<Player> resultsName,
			List<Number> resultsCount) {
		List<String> namesAndCountsDeath = new ArrayList<>();
		for (int i = 0; i <= resultsName.size() - 1; i++) {
			String aux = resultsName.get(i).getName() + ": " + resultsCount.get(i).intValue();
			namesAndCountsDeath.add(aux);
		}
		return namesAndCountsDeath;
	}

	/***
	 * return List String with Player's name and total Dead's count concatenated
	 * for each game.
	 * 
	 * @param resultsMeansDead
	 * @param resultsCount
	 * @return
	 */
	public List<String> unifyListMeansDeadsWithListCountMeansDeadsByCause(List<String> resultsMeansDead,
			List<Number> resultsCount) {
		List<String> namesAndCountsDeath = new ArrayList<>();
		for (int i = 0; i <= resultsMeansDead.size() - 1; i++) {
			String aux = resultsMeansDead.get(i).toString() + ": " + resultsCount.get(i).intValue();
			namesAndCountsDeath.add(aux);
		}
		return namesAndCountsDeath;
	}

	/***
	 * this method return a dead's list Counted by name Player ordered by name
	 * ASC
	 * 
	 * @param game
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Number> countDeadsFromGameBynameAsc(Game game) {
		EntityManager em = new JPAUtil().getEntityManager();
		List<Number> resultsCount = (List<Number>) em
				.createQuery(
						"SELECT COUNT(player) FROM Death AS m where m.game=:idGame GROUP BY m.player ORDER BY m.player ASC")
				.setParameter("idGame", game).getResultList();
		return resultsCount;

	}

	/***
	 * this method return a dead's Means list Counted by Death ordered by cause
	 * 
	 * @param game
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Number> countMeansDeadFromGameByCauseAsc(Game game) {
		EntityManager em = new JPAUtil().getEntityManager();
		List<Number> resultsCount = (List<Number>) em
				.createQuery(
						"SELECT COUNT(cause) FROM Death AS m where m.game=:idGame GROUP BY m.cause ORDER BY m.cause ASC")
				.setParameter("idGame", game).getResultList();
		return resultsCount;

	}

	/***
	 * return List String with Death's Means and total Dead's Means count
	 * concatenated for each game.
	 * 
	 * @param game
	 * @return
	 */
	public List<String> getDeathMeansListWithCountDeads(Game game) {
		List<String> resultsMeansDead = meansOfDeadsInGameByCauseAsc(game);
		List<Number> resultsCount = countMeansDeadFromGameByCauseAsc(game);
		return unifyListMeansDeadsWithListCountMeansDeadsByCause(resultsMeansDead, resultsCount);
	}
}
