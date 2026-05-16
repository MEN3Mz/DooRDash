package game.engine.cells;

import game.engine.Board;
import game.engine.Role;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class DoorCell extends Cell implements CanisterModifier {
	private Role role;
	private int energy;
	private boolean activated;
	private boolean lastLandingEnergyChangeActivation;
	
	public DoorCell(String name, Role role, int energy) {
		super(name);
		this.role = role;
		this.energy = energy;
		this.activated = false;
		this.lastLandingEnergyChangeActivation = false;
	}
	
	public Role getRole() {
		return role;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean isActivated) {
		this.activated = isActivated;
	}

	public boolean wasLastLandingEnergyChangeActivation() {
		return lastLandingEnergyChangeActivation;
	}

	@Override
	public void onLand(Monster landingMonster, Monster opponentMonster) {
		super.onLand(landingMonster, opponentMonster);
		lastLandingEnergyChangeActivation = false;
		
		if(isActivated())
			return; 
		
		System.out.println(landingMonster.getName() + " landed on " + role + " door!");
		
		boolean wasShielded = landingMonster.isShielded();
		int landingEnergyBefore = landingMonster.getEnergy();
	     
		modifyCanisterEnergy(landingMonster, this.energy);
		boolean energyChanged = landingMonster.getEnergy() != landingEnergyBefore;

		// Only block if the monster took damage (opposing team) and was shielded
		if (wasShielded && landingMonster.getRole() != this.role) 
			return;

	    
		for (Monster monster : Board.getStationedMonsters()) {
			//Only affect team members
			if (monster.getRole() == landingMonster.getRole()) {
				int stationedEnergyBefore = monster.getEnergy();
				modifyCanisterEnergy(monster, this.energy);
				energyChanged = energyChanged || monster.getEnergy() != stationedEnergyBefore;
				System.out.println("  -> " + monster.getName() + " got " + this.energy + " energy!");
			}
		}
		
		lastLandingEnergyChangeActivation = energyChanged;
		setActivated(true);
	}

	@Override
	public void modifyCanisterEnergy(Monster monster, int canisterValue) {
		//Affect on team members vary according to role
		monster.alterEnergy(this.role == monster.getRole() ? canisterValue : -canisterValue);
	}
}
