package game.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import game.engine.dataloader.DataLoader;
import game.engine.cards.Card;
import game.engine.cells.CardCell;
import game.engine.cells.Cell;
import game.engine.cells.ContaminationSock;
import game.engine.cells.ConveyorBelt;
import game.engine.cells.DoorCell;
import game.engine.cells.MonsterCell;
import game.engine.exceptions.InvalidMoveException;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.monsters.*;

public class Game {
	private Board board;
	private ArrayList<Monster> allMonsters;
	private Monster player;
	private Monster opponent;
	private Monster current;
	private boolean gameOver;
	private Monster winner;
	private Card lastDrawnCard;
	private String lastCardDrawer;
	private ArrayList<String> eventLog;
	private ArrayList<String> playerEventLog;
	private ArrayList<String> opponentEventLog;
	private int playerPreviousPosition;
	private int opponentPreviousPosition;
	private int lastRolledValue;

	public Game(Role playerRole) throws IOException {
		this.board = new Board(DataLoader.readCards());

		this.allMonsters = DataLoader.readMonsters();

		this.player = selectRandomMonsterByRole(playerRole);
		this.opponent = selectRandomMonsterByRole(playerRole == Role.SCARER ? Role.LAUGHER : Role.SCARER);
		this.current = player;
		this.gameOver = false;
		this.winner = null;
		this.lastDrawnCard = null;
		this.lastCardDrawer = null;
		this.eventLog = new ArrayList<>();
		this.playerEventLog = new ArrayList<>();
		this.opponentEventLog = new ArrayList<>();
		this.playerPreviousPosition = player.getPosition();
		this.opponentPreviousPosition = opponent.getPosition();
		this.lastRolledValue = 0;

		allMonsters.remove(player);
		allMonsters.remove(opponent);

		Board.setStationedMonsters(allMonsters);
		board.initializeBoard(DataLoader.readCells());
	}

	public Board getBoard() {
		return board;
	}

	public ArrayList<Monster> getAllMonsters() {
		return allMonsters;
	}

	public Monster getPlayer() {
		return player;
	}

	public Monster getOpponent() {
		return opponent;
	}

	public Monster getCurrent() {
		return current;
	}

	public void setCurrent(Monster current) {
		this.current = current;
	}

	private Monster selectRandomMonsterByRole(Role role) {
		Collections.shuffle(allMonsters);
		return allMonsters.stream()
				.filter(m -> m.getRole() == role)
				.findFirst()
				.orElse(null);
	}

	private Monster getCurrentOpponent() {
		return current == player ? opponent : player;
	}

	public int rollDice() {
		Random rand = new Random();
		return rand.nextInt(6) + 1;
	}

	public void usePowerup() throws OutOfEnergyException {
		if (gameOver)
			return;

		if (current.getEnergy() < Constants.POWERUP_COST) {
			addEvent(current, current.getName() + " tried to use power-up but needs "
					+ Constants.POWERUP_COST + " energy.");
			throw new OutOfEnergyException("Not enough energy to use powerup");
		}

		current.executePowerupEffect(getCurrentOpponent());
		current.setEnergy(current.getEnergy() - Constants.POWERUP_COST);
	}

	public int playTurn() throws InvalidMoveException {
		if (gameOver)
			return 0;

		lastDrawnCard = null;
		lastCardDrawer = null;
		Board.clearLastDrawnCard();

		if (current.isFrozen()) {
			System.out.println(current.getName() + " is frozen! Turn skipped.");
			addEvent(current, current.getName() + " was frozen and skipped the turn.");
			current.setFrozen(false);
			switchTurn();
			return 1;
		}

		int roll = rollDice();
		lastRolledValue = roll;
		int startPosition = current.getPosition();
		int startEnergy = current.getEnergy();
		Monster actingMonster = current;
		Monster actingOpponent = getCurrentOpponent();
		boolean actingWasShielded = actingMonster.isShielded();
		boolean opponentWasShielded = actingOpponent.isShielded();
		int opponentStartEnergy = actingOpponent.getEnergy();
		setPreviousPosition(actingMonster, startPosition);

		try {
			board.moveMonster(actingMonster, roll, actingOpponent);
		} catch (InvalidMoveException exception) {
			addEvent(actingMonster, actingMonster.getName() + " tried to land on "
					+ actingOpponent.getName() + ". Roll again.");
			throw exception;
		}
		lastDrawnCard = Board.getLastDrawnCard();
		if (lastDrawnCard != null) {
			lastCardDrawer = describePlayer(actingMonster);
		}
		addTurnEvents(actingMonster, roll, startPosition, startEnergy);
		addShieldBlockEventIfNeeded(actingMonster, actingWasShielded, startEnergy);
		addShieldBlockEventIfNeeded(actingOpponent, opponentWasShielded, opponentStartEnergy);

		if (updateWinState()) {
			addEvent(actingMonster, actingMonster.getName() + " reached Boo's Door and won!");
			return roll;
		}

		switchTurn();
		return roll;
	}

	public void forceCurrentWinForTesting() {
		if (gameOver)
			return;

		current.setEnergy(Constants.WINNING_ENERGY + 1);
		current.setPosition(Constants.WINNING_POSITION);
		board.syncMonsterPositions(player, opponent);
		updateWinState();
		addEvent(current, current.getName() + " was moved to Cell 99 with 1001 energy for testing.");
	}

	public void forceCurrentPositionForTesting(int position) {
		if (gameOver)
			return;

		current.setPosition(position);
		board.syncMonsterPositions(player, opponent);
		updateWinState();
		addEvent(current, current.getName() + " was moved to Cell " + current.getPosition() + " for testing.");
	}

	public void forceCurrentEnergyForTesting(int energy) {
		if (gameOver)
			return;

		current.setEnergy(energy);
		updateWinState();
		addEvent(current, current.getName() + " energy was set to " + current.getEnergy() + " for testing.");
	}

	public void switchTurn() {
		this.setCurrent(getCurrentOpponent());
	}

	public boolean checkWinCondition(Monster monster) {
		return monster.getPosition() == Constants.WINNING_POSITION &&
				monster.getEnergy() >= Constants.WINNING_ENERGY;
	}

	private boolean updateWinState() {
		winner = findWinner();
		gameOver = winner != null;

		return gameOver;
	}

	private Monster findWinner() {
		if (checkWinCondition(player))
			return player;

		if (checkWinCondition(opponent))
			return opponent;

		return null;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public Monster getWinner() {
		return winner;
	}

	public int getPlayerPreviousPosition() {
		return playerPreviousPosition;
	}

	public int getOpponentPreviousPosition() {
		return opponentPreviousPosition;
	}

	public int getLastRolledValue() {
		return lastRolledValue;
	}

	public Card getLastDrawnCard() {
		return lastDrawnCard;
	}

	public String getLastCardDrawer() {
		return lastCardDrawer;
	}

	private String describePlayer(Monster monster) {
		return (monster == player ? "You" : "Opponent") + " - " + monster.getName();
	}

	public ArrayList<String> getEventLog() {
		return new ArrayList<>(eventLog);
	}

	public ArrayList<String> getPlayerEventLog() {
		return new ArrayList<>(playerEventLog);
	}

	public ArrayList<String> getOpponentEventLog() {
		return new ArrayList<>(opponentEventLog);
	}

	private void addTurnEvents(Monster monster, int roll, int startPosition, int startEnergy) {
		int landedPosition = board.getLastLandedPosition();
		int finalPosition = monster.getPosition();
		int finalEnergy = monster.getEnergy();
		Cell landedCell = board.getLastLandedCell();

		addEvent(monster, monster.getName() + " rolled " + roll + ".");
		addEvent(monster, monster.getName() + " moved from Cell " + startPosition + " to Cell " + landedPosition + ".");

		if (landedCell instanceof ConveyorBelt) {
			addEvent(monster,
					monster.getName() + " used a conveyor belt: Cell " + landedPosition + " -> Cell " + finalPosition + ".");
		} else if (landedCell instanceof ContaminationSock) {
			addEvent(monster, monster.getName() + " slipped on a contamination sock: Cell " + landedPosition + " -> Cell "
					+ finalPosition + ".");
		} else if (landedCell instanceof CardCell && lastDrawnCard != null) {
			addEvent(monster, monster.getName() + " drew " + lastDrawnCard.getName() + ".");
		} else if (landedCell instanceof DoorCell) {
			DoorCell doorCell = (DoorCell) landedCell;
			addEvent(monster, monster.getName() + " landed on a " + doorCell.getRole() + " door.");
		} else if (landedCell instanceof MonsterCell) {
			addEvent(monster, monster.getName() + " encountered a monster cell.");
		}

		if (finalEnergy != startEnergy) {
			String change = finalEnergy > startEnergy ? "+" + (finalEnergy - startEnergy)
					: String.valueOf(finalEnergy - startEnergy);
			addEvent(monster, monster.getName() + " energy: " + startEnergy + " -> " + finalEnergy + " (" + change + ").");
		}
	}

	private void addEvent(Monster monster, String message) {
		eventLog.add(message);
		getMonsterLog(monster).add(message);

		while (eventLog.size() > 12) {
			eventLog.remove(0);
		}

		while (getMonsterLog(monster).size() > 8) {
			getMonsterLog(monster).remove(0);
		}
	}

	private ArrayList<String> getMonsterLog(Monster monster) {
		return monster == player ? playerEventLog : opponentEventLog;
	}

	private void addShieldBlockEventIfNeeded(Monster monster, boolean wasShielded, int energyBefore) {
		if (wasShielded && !monster.isShielded() && monster.getEnergy() == energyBefore) {
			addEvent(monster, monster.getName() + " did not take damage because the shield blocked it.");
		}
	}

	private void setPreviousPosition(Monster monster, int position) {
		if (monster == player) {
			playerPreviousPosition = position;
		} else {
			opponentPreviousPosition = position;
		}
	}

}
