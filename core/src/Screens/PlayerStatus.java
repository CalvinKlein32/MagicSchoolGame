package Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import uc.ac.aston.game.Launcher;


public class PlayerStatus implements Disposable{
	public Stage stage;
	private Viewport view;
	
	private Integer level;
	private Integer score;
	private String user;
	
	Label scoreLabel;
	Label levelLabel;
	Label theScore;
	Label theLevel;
	
	public PlayerStatus(SpriteBatch batch, int level, int score){
		this.score=score;
		this.level=level;
		
		view = new FitViewport(Launcher.width,Launcher.height,new OrthographicCamera());
		stage = new Stage (view, batch);
		
		Table table = new Table();
		table.top();
		table.setFillParent(true);
		
		scoreLabel = new Label (String.format("%03d", score), new Label.LabelStyle(new BitmapFont(),Color.WHITE));
		levelLabel = new Label (String.format("%02d", level), new Label.LabelStyle(new BitmapFont(),Color.WHITE));
		theScore = new Label ("SCORE", new Label.LabelStyle(new BitmapFont(),Color.WHITE));
		theLevel = new Label ("LEVEL", new Label.LabelStyle(new BitmapFont(),Color.WHITE));
		
		table.add(theScore).expandX().padTop(10);
		table.add(theLevel).expandX().padTop(10);
		table.row();
		table.add(scoreLabel).expandX();
		table.add(levelLabel).expandX();
		
		stage.addActor(table);
		
		
		
		
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		
	}
	

}
