package br.com.codeminer.parser.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Game implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@OneToMany(fetch = FetchType.EAGER)
	private List<Player> playersList;
	
	@OneToMany
	private List<Death> deathList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Player> getPlayersList() {
		return playersList;
	}

	public void setPlayersList(List<Player> playersList) {
		this.playersList = playersList;
	}

	public List<Death> getDeathList() {
		return deathList;
	}

	public void setDeathList(List<Death> deathList) {
		this.deathList = deathList;
	}

	public Game(List<Player> playersList, List<Death> deathList) {
		super();
		this.playersList = playersList;
		this.deathList = deathList;
	}

	public Game() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deathList == null) ? 0 : deathList.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((playersList == null) ? 0 : playersList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (deathList == null) {
			if (other.deathList != null)
				return false;
		} else if (!deathList.equals(other.deathList))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (playersList == null) {
			if (other.playersList != null)
				return false;
		} else if (!playersList.equals(other.playersList))
			return false;
		return true;
	}
	
	
}
