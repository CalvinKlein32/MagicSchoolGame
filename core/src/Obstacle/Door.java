package Obstacle;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import uc.ac.aston.game.Launcher;

public class Door {

	private World world;
	private TiledMap map;
	private TiledMapTile tile;
	private Rectangle bounds;
	private Body body;
	private Fixture fixture;
	
	public Door(World world, TiledMap map, Rectangle bounds) {
		this.world = world;
		this.map=map;
		this.bounds=bounds;
		BodyDef bdef = new BodyDef();
		PolygonShape actor = new PolygonShape();
		FixtureDef f = new  FixtureDef();
		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set((bounds.getX()+bounds.getWidth()/2)/Launcher.PPM, (bounds.getY() + bounds.getHeight()/2)/Launcher.PPM);
		body = world.createBody(bdef);
		actor.setAsBox((bounds.getWidth()/2)/Launcher.PPM, (bounds.getHeight()/2)/Launcher.PPM);
		f.shape = actor;
		fixture = body.createFixture(f);
		fixture.setUserData(this);
		setCategoryFilter(Launcher.doorBit);
		
	}
	
	public void onSidehit() {
		Gdx.app.log("Door","Collision");


		

		
	}
	
	public void setCategoryFilter(short filterBit) {
		Filter filter = new Filter();
		filter.categoryBits=filterBit;
		fixture.setFilterData(filter);
	}
	
	public void destroyDoorCells(){
		setCategoryFilter(Launcher.destroyedBit);

		TiledMapTileLayer layer= (TiledMapTileLayer) map.getLayers().get(3);
		int dx=Math.round(bounds.getX()/16);
		int dy=((int)(bounds.getY()+48)/16);
		
		for (int j=0;j<= Math.round(bounds.getHeight())/16;j++) {
			for (int i=0; i<Math.round(bounds.getWidth()/16);i++) {
				//System.out.println((dx+i)+"and "+(dy-j));
				
				if (layer.getCell(dx+i, dy-j)!=null){
					layer.getCell(dx+i, dy-j).setTile(null);
				}
			}
		}
	
	}
	

}
