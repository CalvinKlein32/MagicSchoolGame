package Screens;

import java.io.BufferedReader;
import java.io.FileReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import Collisions.WorldContactListener;
import Levels.LevelCreator;
import Player.Player;
import Questions.Question;
import uc.ac.aston.game.Launcher;

public class PlayScreen implements Screen{
	
	private Launcher theGame;
	private Texture img;
	private Viewport gamePort;
	private OrthographicCamera cam;
	protected PlayerStatus status;
	private TiledMap map;
	private TmxMapLoader mapLoader;
	private OrthogonalTiledMapRenderer mapRender;
	private World world;
	private Box2DDebugRenderer b2;
	private Player player;
	private TextureAtlas atlas;
	LevelCreator levels;
	WorldContactListener detectCollision;
	private Player player2;
	private float startX= (float) 0.8;
	private float startY=(float) 1.078326;
	private Vector2 player2Position;
	private String[] levelsMaps = {"level1.tmx","level2.tmx","level3.tmx"};
	private int currentLevel;
	private int currentScore;
	private Question listOfQuestions;
	private boolean waitingToMoveLevel=false;
	private boolean signalToMoveLevelReceived=false;
	private boolean hasGameEnded=false;
	
	
	public PlayScreen(Launcher theGame,int level,int score) {
		currentScore=score;
		atlas=new TextureAtlas("the_player.pack");
		currentLevel=level;
		this.theGame= theGame;
		//this.img = new Texture("badlogic.jpg");
		cam = new OrthographicCamera();
		gamePort = new FitViewport(Launcher.width/Launcher.PPM,Launcher.height/Launcher.PPM,cam);
		status = new PlayerStatus(theGame.batch,currentLevel+1,currentScore);
		mapLoader= new TmxMapLoader();
		map = mapLoader.load(levelsMaps[currentLevel]);
		mapRender = new OrthogonalTiledMapRenderer(map,1/Launcher.PPM);
		cam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2,0);
		world = new World(new Vector2(0,-10), true);
		b2 = new Box2DDebugRenderer();
		b2.setDrawBodies(false);
//		b2.SHAPE_STATIC.set(1,1,1,1);
		player = new Player(world,this);
		player.b2body.setTransform(new Vector2(1,3),0);
		player2= null;
		levels= new LevelCreator(map,world);
		detectCollision= new WorldContactListener(player);
		world.setContactListener(detectCollision);
		listOfQuestions= new Question();
		

		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		//System.out.println(player.b2body.getWorldCenter());
		if (player.getTimer()==0) {
			//theGame.setScreen(new PlayScreen((Launcher) theGame));
			//player = new Player(world,this);
			player.revivePlayer();
			player.b2body.setTransform(new Vector2(1,3),0);
			//detectCollision= new WorldContactListener(player);
			//world.setContactListener(detectCollision);
			//status.dispose();
		}
		update(delta);
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		
		
		theGame.batch.setProjectionMatrix(status.stage.getCamera().combined);
		status.stage.draw();
		
		mapRender.render();
		b2.render(world, cam.combined);
		if (detectCollision.getDoGenerateQuestion()) {
			theGame.setScreen(new QuestionScreen(theGame,this,listOfQuestions));
			//status.dispose();
		}
		
		
		
		theGame.batch.setProjectionMatrix(cam.combined);
		theGame.batch.begin();
		player.draw(theGame.batch);
		if (player2!=null) {
			player2.draw(theGame.batch);
		}
		theGame.batch.end();
		
	}
	
	public void unlockDoor() {
		player.setCanOpenDoor(true);
	}
	
	public void createPlayer2() {
		player2=  new Player(world,this);
//		System.out.println("Player 2 created ");
		player2Position = new Vector2(1,1);
		//player2.makeGhost();
	}
	
	
	public Vector2 getPlayerPosition() {
		return player.b2body.getWorldCenter();
	}
	
	public void setPlayer2Position(Vector2 pos) {
		//player2.setPosition(pos.x, pos.y);
		player2Position=pos;
		//player2.b2body.setTransform(pos, 0);
		//player2.b2body.applyLinearImpulse(pos, player2.b2body.getWorldCenter(), true);
	}
	
	public boolean handleInput(float dx) {
		if (!player.getIsDead()) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.b2body.getLinearVelocity().y==0) {
				player.b2body.applyLinearImpulse(new Vector2(0,4f), player.b2body.getWorldCenter(),true);
				return true;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)&& player.b2body.getLinearVelocity().x<=2) {
				player.b2body.applyLinearImpulse(new Vector2(0.1f,0),player.b2body.getWorldCenter(),true);
				return true;
			}if (Gdx.input.isKeyPressed(Input.Keys.LEFT)&& player.b2body.getLinearVelocity().x>=-2) {
				player.b2body.applyLinearImpulse(new Vector2(-0.1f,0),player.b2body.getWorldCenter(),true);
				return true;
			}
			
		}
		return false;
		
/**		if (!player2.getIsDead()) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.W) && player2.b2body.getLinearVelocity().y==0) {
				player2.b2body.applyLinearImpulse(new Vector2(0,4f), player2.b2body.getWorldCenter(),true);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.D)&& player2.b2body.getLinearVelocity().x<=2) {
				player2.b2body.applyLinearImpulse(new Vector2(0.1f,0),player2.b2body.getWorldCenter(),true);
			}if (Gdx.input.isKeyPressed(Input.Keys.A)&& player.b2body.getLinearVelocity().x>=-2) {
				player2.b2body.applyLinearImpulse(new Vector2(-0.1f,0),player2.b2body.getWorldCenter(),true);
			}
		}
		
**/		
	}
	public void update (float delta) {
		world.step(1/60f, 6, 2);
		if(handleInput(delta)) {
			//theGame.updateServer(delta);
		}
		theGame.updateServer(delta);
		if (player2!=null) {
			player2.b2body.setTransform(player2Position, 0);
		}
		
		
		player.update(delta);
		if (player2!=null) {
			player2.update(delta);
		}
		if (!player.getIsDead()) {
			cam.position.x=player.b2body.getPosition().x;
			
		}else {
			player.decrementRespawnTimer();
		}
		if (!waitingToMoveLevel) {
			if (player.getHasFinishedLevel()) {
				theGame.updateHasPlayerFinished();
				waitingToMoveLevel=true;
//				nextLevel();
//				player.setHasFinishedLevel(false);
			}
		}else if (signalToMoveLevelReceived) {
			signalToMoveLevelReceived=false;
			theGame.nextLevel(1);
			
		}
		
		if (hasGameEnded & currentLevel==2) {
			theGame.showGameOverScreen();
		}

		
		cam.update();
		mapRender.setView(cam);
		
	}
	
	public void returnToPlayScreen() {
		//status.stage.draw();
		detectCollision.resetGenerateQuestion();
	}
	
/**	public void nextLevel() {
		currentLevel++;
		if (currentLevel<3) {
			theGame.setScreen(new PlayScreen(theGame,currentLevel));
		}else {
			System.out.println("Congratulations you finished the Game");
		}
		//world.dispose();
		//world = new World(new Vector2(0,-10), true);
		//player.updateWordl(world);
		map = mapLoader.load(levelsMaps[currentLevel]);
		mapRender = new OrthogonalTiledMapRenderer(map,1/Launcher.PPM);
		levels= new LevelCreator(map,world);
		player.b2body.setTransform(new Vector2(1,3),0);
		System.out.println("next");
		
		
		//theGame.setScreen(new PlayScreen((Launcher) theGame));
	}
	
**/
	public void setSignalToMoveLevelReceived(boolean signal) {
		this.signalToMoveLevelReceived = signal;
	}

	public int getCurrentSore() {
		return currentScore;
	}
	
	public void setHasGameEnded() {
		hasGameEnded=true;
	}

	public TextureAtlas getAtlass() {
		return atlas;
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		map.dispose();
		mapRender.dispose();
		world.dispose();
		b2.dispose();
		status.dispose();
		
		
		// TODO Auto-generated method stub
		
	}

}
