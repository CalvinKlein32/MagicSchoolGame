package Obstacle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import uc.ac.aston.game.Launcher;

public class Spikes extends InteractiveObstacle{

	public Spikes(World world, TiledMap map, Rectangle bounds) {
		super(world, map, bounds);
		fixture.setUserData(this);
		setCategoryFilter(Launcher.spikeBit);
	}

	@Override
	public void onFeetHit() {
		Gdx.app.log("Spikes","Collision");
		
		
	}
	
	public void onSidehit() {
		Gdx.app.log("Spikes","Collision");
	}
	
}
