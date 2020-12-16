package Player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import Screens.PlayScreen;
import uc.ac.aston.game.Launcher;

public class Player extends Sprite {
	
	public World world;
	public Body b2body;
	private FixtureDef fdef;
	private Fixture f;
	private boolean isDead;
	private boolean canOpenDoor;
	private float respawnTimer;
	private TextureRegion playerStand;
	private enum State{JUMPING,STANDING,RUNNING};
	private State currentState;
	private State previousState;
	private Animation<TextureRegion> playerRuns;
	private Animation<TextureRegion> playerJumps;
	private boolean runningRight;
	private float stateTimer;
	private boolean hasFinishedLevel=false;
	
	
	public Player(World world, PlayScreen screen) {
		super(screen.getAtlass().findRegion("Woodcutter_walk"));
		this.world=world;
		defineCharacter();
		playerStand=new TextureRegion(getTexture(),1,1,32,65);
		setBounds(0,20,32/Launcher.PPM,64/Launcher.PPM);
		setRegion(playerStand);
		isDead=false;
		respawnTimer=50;
		canOpenDoor=false;
		currentState=State.STANDING;
		previousState=State.STANDING;
		runningRight=true;
		stateTimer=0;
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for (int i=581;i<701;i+=40) {
			frames.add(new TextureRegion(getTexture(),i,151,40,65));
		}
		playerRuns = new Animation<TextureRegion>(0.3f,frames);
		frames.clear();
		
		for (int i=1;i<=201;i+=40) {
			frames.add(new TextureRegion(getTexture(),i,51,40,65));
		}
		playerJumps = new Animation<TextureRegion>(0.02f,frames);
	}
	
	public void update(float dt) {
		setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
		setRegion(getFrame(dt));

	}
	
	public TextureRegion getFrame(float dt){
		currentState=getState();
		TextureRegion region;
		if (currentState==State.JUMPING) {
			region=playerJumps.getKeyFrame(stateTimer);
		}else if(currentState==State.RUNNING) {
			region=playerRuns.getKeyFrame(stateTimer,true);
		}else {
			region=playerStand;
		}
		
		if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
			region.flip(true, false);
			runningRight=false;
		}else if ((b2body.getLinearVelocity().x>0 || runningRight) && region.isFlipX()) {
			region.flip(true, false);
			runningRight=true;
		}
		
		if (currentState==previousState) {
			stateTimer+=dt;
		}else {
			stateTimer=0;
		}
		previousState=currentState;
		
		
		return region;
	}
	
	public State getState() {
		if (b2body.getLinearVelocity().y>0) {
			return State.JUMPING;
		}else if(b2body.getLinearVelocity().x!=0) {
			return State.RUNNING;
		}else {
			return State.STANDING;
		}
	}
	
	public void defineCharacter() {
		BodyDef character = new BodyDef();
		character.position.set(80/Launcher.PPM,150/Launcher.PPM);
		System.out.print("ok");
		character.type=BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(character);
		
		fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(10/Launcher.PPM);
		fdef.filter.categoryBits = Launcher.playerBit;
		fdef.filter.maskBits = Launcher.DefaultBit| Launcher.spikeBit| Launcher.seaBit;
		
		fdef.shape=shape;
		f=b2body.createFixture(fdef);
		
		EdgeShape feet= new EdgeShape();
		feet.set(new Vector2(-5/Launcher.PPM,-10/Launcher.PPM),new Vector2(+5/Launcher.PPM,-10/Launcher.PPM));
		fdef.shape=feet;
		fdef.isSensor = true;
		
		b2body.createFixture(fdef).setUserData("feet");
		
		
		EdgeShape front= new EdgeShape();
		front.set(new Vector2(10/Launcher.PPM,-8/Launcher.PPM),new Vector2(10/Launcher.PPM,8/Launcher.PPM));
		fdef.shape=front;
		fdef.isSensor = true;
		
		b2body.createFixture(fdef).setUserData("side");
		
		EdgeShape back= new EdgeShape();
		back.set(new Vector2(-10/Launcher.PPM,-8/Launcher.PPM),new Vector2(-10/Launcher.PPM,8/Launcher.PPM));
		fdef.shape=back;
		fdef.isSensor = true;
		
		b2body.createFixture(fdef).setUserData("side");
		
		EdgeShape head= new EdgeShape();
		head.set(new Vector2(-5/Launcher.PPM,10/Launcher.PPM),new Vector2(+5/Launcher.PPM,10/Launcher.PPM));
		fdef.shape=head;
		fdef.isSensor = true;
		
		b2body.createFixture(fdef).setUserData("head");
	}
	
	
	public void destroyPlayer() {
		Filter filter= new Filter();
		filter.maskBits= Launcher.destroyedBit;
		isDead=true;
		for (Fixture fixture: b2body.getFixtureList()) {
			fixture.setFilterData(filter);
			//System.out.println(fixture.toString());
		}

	}
	
	public void revivePlayer() {
		Filter filter= new Filter();
		filter.maskBits= Launcher.DefaultBit| Launcher.spikeBit| Launcher.seaBit;;
		isDead=false;
		respawnTimer=50;
		for (Fixture fixture: b2body.getFixtureList()) {
			fixture.setFilterData(filter);
			//System.out.println(fixture.toString());
		}

	}
	
	public void makeGhost() {
		Filter filter= new Filter();
		filter.maskBits= Launcher.destroyedBit;
		//isDead=true;
		for (Fixture fixture: b2body.getFixtureList()) {
			fixture.setFilterData(filter);
			//System.out.println(fixture.toString());
		}

	}
	
	public void decrementRespawnTimer() {
		respawnTimer--;
	}
	
	public float getTimer() {
		return respawnTimer;
	}
	
	
	public boolean getIsDead() {
		return isDead;
	}
	
	public void setCanOpenDoor(boolean value) {
		canOpenDoor=value;
	}
	public boolean getCanOpenDoor() {
		return canOpenDoor;
	}

	public boolean getHasFinishedLevel() {
		return hasFinishedLevel;
	}
	
	public void updateWordl(World newWorld) {
		this.world=newWorld;
	}

	public void setHasFinishedLevel(boolean isLevelOver) {
		hasFinishedLevel = isLevelOver;
	}
	


	

	
}
