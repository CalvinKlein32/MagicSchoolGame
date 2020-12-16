package uc.ac.aston.game;


import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

//import io.socket.emitter.Emitter;
//import io.socket.client.IO;
import io.socket.client.*;
import io.socket.emitter.Emitter;
import Screens.GameOverScreen;
import Screens.PlayScreen;

public class Launcher extends Game {
	public static final int width =600;
	public static final int height =600;
//	public static final height;
	public SpriteBatch batch;
	public static final float PPM=100;
	public static final short DefaultBit=1;
	public static final short playerBit=2;
	public static final short spikeBit=4;
	public static final short seaBit=8;
	public static final short doorBit = 10;
	public static final short destroyedBit=16;
	private Socket socket;
	private boolean lobbyFull=false;
	private PlayScreen screen;
	private final float UPDATE_TIME = 1/60f;
	private float timer;
	private int lobbyNum=1;
	private boolean hasPlyerFiniehed=false;
	private boolean hasTheOtrerPlyerFiniehed=false;
	private int currentLevel=0;
	
	private int finalPoints=0;
	private int opponentFinalPoints=0;
	
	
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		screen = new PlayScreen(this,0,0);
		setScreen(screen);
		connectSocket();
		configSocketEvents();
		

		
	}

	@Override
	public void render () {
		super.render();

		
	}
	
	public void showGameOverScreen() {
		int finalPosition;
		if (finalPoints>opponentFinalPoints) {
			setScreen(new GameOverScreen(this,1,finalPoints, opponentFinalPoints));
		}else {
			setScreen(new GameOverScreen(this,2,finalPoints, opponentFinalPoints));
		}
	}
	
	public void nextLevel(int position) {
//		screen.nextLevel();
		
		int score=0;
		if (position==1) {
			score=screen.getCurrentSore()+100;
		}else {
			score=screen.getCurrentSore()+50;
		}
		if (currentLevel<3) {
			screen = new PlayScreen(this,currentLevel,score);
			setScreen(screen);
			screen.createPlayer2();
			System.out.println("Congratulations you finished the level in position: "+position);
		}else {
			finalPoints=score;
			JSONObject data = new JSONObject();
			try{
				data.put("playerScore", score);
				data.put("lobbyNum", lobbyNum);
				socket.emit("gameEnded", data);
			}catch(JSONException e) {
				Gdx.app.log("SocketIO", "Error in sending has player finished ");
			}
			System.out.println("Congratulations you finished the Game");
		}
		hasPlyerFiniehed=false;
		hasTheOtrerPlyerFiniehed=false;

	}
	
	public void updateServer(float dt) {
		timer+=dt;
		if (timer>=UPDATE_TIME) {
			JSONObject data = new JSONObject();
			try{
				data.put("x", screen.getPlayerPosition().x);
				data.put("y", screen.getPlayerPosition().y);
				data.put("lobbyNum", lobbyNum);
				socket.emit("playerMoved", data);
			}catch(JSONException e) {
				Gdx.app.log("SocketIO", "Error in sending update position ");
			}
		}
	}
	
	public void updateHasPlayerFinished() {
		hasPlyerFiniehed=true;
		if (hasTheOtrerPlyerFiniehed) {
//			System.out.println("ready to move to the next level, you came in 2nd position");
			currentLevel++;
			nextLevel(2);
		}
		JSONObject data = new JSONObject();
		try{
			data.put("playerFinished", true);
			data.put("lobbyNum", lobbyNum);
			socket.emit("playerCompletedLevel", data);
		}catch(JSONException e) {
			Gdx.app.log("SocketIO", "Error in sending has player finished ");
		}
		
	}
	
	
	public void connectSocket() {
		try{
			socket =IO.socket("http://localhost:8080");
			socket.connect();
			//socket.connect();
			//System.out.print();
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void configSocketEvents() {
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				Gdx.app.log("SocketIO", "Connected");
				
			}
		}).on("socketID", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try{
					String id = data.getString("id");
					Gdx.app.log("SocketIO", "My ID: "+id);
				}catch(JSONException e) {
					Gdx.app.log("SocketIO", "Error in fetching id ");
				}
				
				
			}
		}).on("newPlayer", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try{
					int count=data.getInt("playersInLobby");
					lobbyNum=data.getInt("lobby");
					System.out.println("this is player number "+count+" in lobby number "+lobbyNum);
					if (count==1) {
						screen.createPlayer2();
					}
					//String id = data.getString("id");
					//Gdx.app.log("SocketIO", "New Player connected ID: "+id);
					//lobbyFull=true;
				}catch(JSONException e) {
					Gdx.app.log("SocketIO", "Error in fetching new player");
				}
				
			}
		}).on("playerDisconeccted", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try{
					String id = data.getString("id");
					Gdx.app.log("SocketIO", " Player disconnected ID: "+id);
					//lobbyFull=true;
				}catch(JSONException e) {
					Gdx.app.log("SocketIO", "Error in fetching discoeccted player");
				}
				
			}
		}).on("getPlayers", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				JSONArray objects = (JSONArray)args[0];
				//System.out.println(objects.length());
/*				try {
					//String id=objects.getJSONObject(0).getString("id");
					System.out.println(objects.getJSONObject(2).getDouble("y"));
				} catch (JSONException e) {
					System.out.println(e);
					//e.printStackTrace();
				}
				*/
				for (int i=0; i<objects.length();i++) {
					try {
						int playerLobby=objects.getJSONObject(i).getInt("lobby");
						if (lobbyNum==playerLobby) {
							screen.createPlayer2();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//System.out.println(lobbyNum);	
				}
				
				
			}
		}).on("playerMoved", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try{
					
					int lobby = data.getInt("lobbyNum");
					if (lobbyNum==lobby) {
						String id = data.getString("id");
						Double x = data.getDouble("x");
						Double y = data.getDouble("y");
						Vector2 position = new Vector2(x.floatValue(),y.floatValue());
						screen.setPlayer2Position(position);
					}
					
					
				}catch(JSONException e) {
					Gdx.app.log("SocketIO", "Error in getting players movement");
				}
				
				
			}
		}).on("playerCompletedLevel", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try{
					int lobby = data.getInt("lobbyNum");
					if (lobbyNum==lobby) {
						hasTheOtrerPlyerFiniehed=data.getBoolean("playerFinished");
						if (hasPlyerFiniehed) {
							System.out.println("Ready to move to the next level. came in 1st position");
							
							currentLevel++;
							screen.setSignalToMoveLevelReceived(true);
						}
					}		
				}catch(JSONException e) {
					Gdx.app.log("SocketIO", "Error in getting players movement");
				}
				
				
			}
		}).on("gameEnded", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try{
					int lobby = data.getInt("lobbyNum");
					if (lobbyNum==lobby) {
						opponentFinalPoints=data.getInt("playerScore");
						screen.setHasGameEnded();
					}		
				}catch(JSONException e) {
					Gdx.app.log("SocketIO", "Error in getting players movement");
				}
				
				
			}
		});
	}
	
	
	
	
	

}
	
