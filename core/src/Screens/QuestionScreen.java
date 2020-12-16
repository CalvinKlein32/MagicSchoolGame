package Screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import Questions.Question;
import uc.ac.aston.game.Launcher;

public class QuestionScreen implements Screen{
	private Viewport view;
	private Stage stage;
	private Launcher game;
	private PlayScreen playScreen;
	private boolean isAnswerCorrect;
	private boolean hasButtonBeenPressed;
	//private Question listOfQuestions;
	ArrayList<String> question;
	
	public QuestionScreen(Launcher game, PlayScreen playScreen, Question listOfQuestions) {
		this.game=game;
		view = new FitViewport(Launcher.width,Launcher.height, new OrthographicCamera());
		this.playScreen = playScreen;
		isAnswerCorrect=false;
		stage = new Stage(view,game.batch);
		Gdx.input.setInputProcessor(stage);
		Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(),Color.WHITE);
		question = listOfQuestions.randomQuestionGenerator();
		
		Table table = new Table();
		table.setFillParent(true);
		
		Label questionLabel = new Label(question.get(0),font);
		//questionLabel.setBounds(0, 0, 10, 40);
		//questionLabel.setWidth(20);
		//questionLabel.setHeight(40);
		questionLabel.setWrap(true);
		questionLabel.setWidth(200);
		
		//questionLabel.setHeight(20);
		//Label answer1 = new Label("A:38",font);
		//Label answer2 = new Label("B:48",font);
		//Label answer3 = new Label("C:47",font);
		//Label answer4= new Label("D:37", font);
		Label info = new Label("Press Q to go back to the playing screen",font);
		
		TextButtonStyle style= new TextButtonStyle();
		style.pressedOffsetX= 1;
		style.pressedOffsetY= -1;
		style.font=new BitmapFont();
		//ClickListener event=new ClickListener(String value) {
			
		
		TextButton answer1= new TextButton("A: "+question.get(1),style);
		answer1.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				isAnswerCorrect=question.get(1).equals(question.get(5));
				hasButtonBeenPressed=true;
			}
		});
		
		TextButton answer2= new TextButton("B: "+question.get(2),style);
		answer2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				isAnswerCorrect=question.get(2).equals(question.get(5));
				hasButtonBeenPressed=true;
			}
		});
		

		TextButton answer3= new TextButton("C: "+question.get(3),style);
		answer3.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				isAnswerCorrect=question.get(3).equals(question.get(5));;
				hasButtonBeenPressed=true;
			}
		});
		
		TextButton answer4= new TextButton("D: "+question.get(4),style);
		answer4.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				isAnswerCorrect=question.get(4).equals(question.get(5));;
				hasButtonBeenPressed=true;
			}
		});
		
		
		table.add(questionLabel).width(200).expandX();
		//table.center();
		table.row();
		table.add(answer1).width(10).expandX();
		table.row();
		table.add(answer2).width(10).expandX();
		table.row();
		table.add(answer3).width(10).expandX();
		table.row();
		table.add(answer4).width(10).expandX();
		table.row();
		table.add(info).expandX().padTop(60f);
		
		stage.addActor(table);
		
	}

	private void clicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyPressed(Input.Keys.Q)|(hasButtonBeenPressed)){
			if (isAnswerCorrect) {
				playScreen.unlockDoor();
			}
			playScreen.returnToPlayScreen();
			game.setScreen(playScreen);
			
//			playScreen.pl
			dispose();
			
			
		}
		
		

		//System.out.print(isAnswerCorrect);
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
		// TODO Auto-generated method stub
		
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
