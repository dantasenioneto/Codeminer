package br.com.codeminer.parser.DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.codeminer.parser.modelo.Death;
import br.com.codeminer.parser.modelo.Game;
import br.com.codeminer.parser.modelo.Player;
import br.com.codeminer.parser.util.JPAUtil;

public class DAO<T> {

	private final Class<T> classe;

	public DAO(Class<T> classe) {
		this.classe = classe;
	}

	/***
	 * this method insert case player in new or return the existent player;
	 * 
	 * @param player
	 * @return new Player or Existent Player
	 */
	public Player addNewPlayer(Player player) {
		EntityManager em = new JPAUtil().getEntityManager();
		Player existentPlayer = getOriginalPlayerByName(player.getName());
		if (existentPlayer == null || !existentPlayer.getName().equals(player.getName())) {
			em.getTransaction().begin();
			em.persist(player);
			em.getTransaction().commit();
			em.close();
			return player;
		}
		return existentPlayer;
	}

	/***
	 * this method select a Entity
	 * 
	 * @return listResult
	 */
	public List<T> listAll() {
		EntityManager em = new JPAUtil().getEntityManager();
		CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(classe);
		query.select(query.from(classe));
		List<T> lista = em.createQuery(query).getResultList();

		em.close();
		return lista;
	}

	/***
	 * this method return a original Player selected by Name
	 * 
	 * @param namePlayer
	 * @return Player Object
	 */
	public Player getOriginalPlayerByName(String namePlayer) {
		EntityManager em = new JPAUtil().getEntityManager();
		Session session = em.unwrap(Session.class);
		Player player = new Player();
		player = (Player) session.createCriteria(Player.class).add(Restrictions.eq("name", namePlayer)).uniqueResult();
		return player;
	}

	/***
	 * Add a new Death in Entity and return the Death Add.
	 * 
	 * @param death
	 * @return death;
	 */
	public Death addNewDeath(Death death) {
		if (death != null && death.getPlayer() != null) {
			EntityManager em = new JPAUtil().getEntityManager();
			em.getTransaction().begin();
			em.persist(death);
			em.getTransaction().commit();
			em.close();
		}
		return death;

	}

	/***
	 * Add a new Game in Entity;
	 * 
	 * @param game
	 */
	public void addNewGame(Game game) {
		if (game != null) {
			EntityManager em = new JPAUtil().getEntityManager();
			em.getTransaction().begin();
			em.persist(game);
			em.getTransaction().commit();
			em.close();
			setGametoDeathList(game);
		}
	}

	/***
	 * this method set for All Death entity a Game wich was played.
	 * 
	 * @param game
	 */
	public void setGametoDeathList(Game game) {
		if (!game.getDeathList().isEmpty()) {
			for (Death death : game.getDeathList()) {
				EntityManager em = new JPAUtil().getEntityManager();
				em.getTransaction().begin();
				death.setGame(game);
				em.merge(death);
				em.getTransaction().commit();
				em.close();
			}
		}
	}
}
