package Collisions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;


import Obstacle.Door;
import Obstacle.FinishLine;
import Obstacle.InteractiveObstacle;
import Obstacle.Spikes;
import Player.Player;
import uc.ac.aston.game.Launcher;

public class WorldContactListener implements ContactListener{
	private Player player;
	private boolean doGenerateQuestion;
	
	public WorldContactListener(Player player) {
		this.player=player;
		doGenerateQuestion= false;
	}
	


	@Override
	public void beginContact(Contact contact) {
		
		Fixture fixA= contact.getFixtureA();
		Fixture fixB= contact.getFixtureB();
		if (fixA.getUserData()=="feet" || fixB.getUserData()=="feet") {
			Fixture feet;
			Fixture obstacle;
			if (fixA.getUserData()=="feet") {
				feet= fixA;
				obstacle = fixB;
			}else {
				feet=fixB;
				obstacle=fixA;
			}
			if (obstacle.getUserData()!=null && (obstacle.getUserData() instanceof InteractiveObstacle)) {
				((InteractiveObstacle) obstacle.getUserData()).onFeetHit();
				if ((player.b2body.getFixtureList().contains(feet, true))){
					player.destroyPlayer();
				};
			}
		
		}else if (fixA.getUserData()=="side" || fixB.getUserData()=="side") {
			Fixture side;
			Fixture obstacle;
			if (fixA.getUserData()=="side") {
				side= fixA;
				obstacle = fixB;
			}else {
				side=fixB;
				obstacle=fixA;
			}
			if (player.b2body.getFixtureList().contains(side, true)) {
				if (obstacle.getUserData()!=null && (obstacle.getUserData() instanceof Spikes)) {
					((Spikes) obstacle.getUserData()).onSidehit();
					player.destroyPlayer();
					
					//System.out.println("been hit"+side.toString());
//					if ((player.b2body.getFixtureList().contains(side, true))){
//						player.destroyPlayer();
//					};
					
				}else if (obstacle.getUserData()!=null && (obstacle.getUserData() instanceof Door)) {
					if (!player.getCanOpenDoor()) {
						doGenerateQuestion=true;
					}else {
						((Door) obstacle.getUserData()).destroyDoorCells();
						player.setCanOpenDoor(false);
					}
					
					
				}else if (obstacle.getUserData()!=null && (obstacle.getUserData() instanceof FinishLine)) {
					((FinishLine) obstacle.getUserData()).onSideHit();
					player.setHasFinishedLevel(true);
				}
			}

		}
		
	}
	
	public void resetGenerateQuestion() {
		doGenerateQuestion=false;
	}
	
	public boolean getDoGenerateQuestion() {
		return doGenerateQuestion;
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
