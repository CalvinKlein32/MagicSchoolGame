package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uc.ac.aston.game.Launcher;

public class GameOverScreen implements Screen{
	
	private Viewport view;
	private Stage stage;
	private Launcher game;
	
	public GameOverScreen(Launcher game, int position, int points, int otherPlayerPoints) {
		this.game=game;
		view = new FitViewport(Launcher.width,Launcher.height, new OrthographicCamera());
		stage = new Stage(view,game.batch);
		
		Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(),Color.WHITE);
		
		String results;
		if (points==otherPlayerPoints) {
			results= "Congratulations magician you finished the game :), You ended the game with a Tie, you and your opponent both got "+
					+points + " points.";
		}else {
			results= "Congratulations magician you finished the game :), Overall you finished in";
			if (position==1) {
				results+=" 1st ";
			}else {
				results+=" 2nd ";
			}
			results+= " position with "+points+" points, and your opponent finished with "+otherPlayerPoints + " points.";

		}
				
		Label gameResults= new Label(results,font);
		gameResults.setWrap(true);
		gameResults.setWidth(350);
		Label gameOverText= new Label("Game Over !!!",font);
		
		Table table = new Table();
		table.setFillParent(true);
		
		table.add(gameOverText).width(100).expandX();
		table.row();
		table.add(gameResults).width(350).expandX().padTop(20f);
		
		stage.addActor(table);
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
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
		stage.dispose();
		// TODO Auto-generated method stub
		
	}

}
