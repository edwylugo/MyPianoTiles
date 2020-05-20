package br.com.edwylugo.mypianotiles;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import static br.com.edwylugo.mypianotiles.Cons.*;


public class MainClass extends ApplicationAdapter {

	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	private Array<Fileira> fileiras;
	private float tempoTotal;
	private int indexInf;
	private int pontos;
	private Random random;
	private int estado;
	private Texture texIniciar;
	private Piano piano;
	private BitmapFont fonte;
	private GlyphLayout glyphLayout;
	
	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		batch = new SpriteBatch();

		fileiras = new Array<Fileira>();

		random = new Random();

		texIniciar = new Texture("iniciar.png");

		piano = new Piano("natal");

		glyphLayout = new GlyphLayout();

		FreeTypeFontGenerator.setMaxTextureSize(2048);
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = (int)(0.06f*screenY);
		parameter.color = Color.BLACK;
		fonte = generator.generateFont(parameter);
		generator.dispose();

		iniciar();

	}

	@Override
	public void render () {

		input();

		update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shapeRenderer.begin();

		for(Fileira f:fileiras) {
			f.draw(shapeRenderer);
		}

		shapeRenderer.end();


			batch.begin();
			if(estado == 0) batch.draw(texIniciar,0, tileHeight/4, screenX, tileHeight/2);
			fonte.draw(batch, String.valueOf(pontos), 0, screenY);
			fonte.draw(batch, String.format("%.3f",velAtual/tileHeight), screenX-getWidth(fonte,String.format("%.3f",velAtual/tileHeight)), screenY);

			batch.end();


	}

	private void update(float deltaTime) {
		if(estado == 1) {
			tempoTotal += deltaTime;

			velAtual = velIni + tileHeight*tempoTotal/8f;

			for (int i=0; i<fileiras.size; i++) {
				int retorno = fileiras.get(i).update(deltaTime);
				fileiras.get(i).anim(deltaTime);
				if (retorno != 0){
					if (retorno == 1){
						fileiras.removeIndex(i);
						i--; //nao pular item
						indexInf--;
						adicionar();
					} else if (retorno == 2){
						finalizar(1);
					}
				}
			}
		} else if (estado == 2) {
			for (Fileira f:fileiras) {
				f.anim(deltaTime);
			}
		}
	}

	private void input() {
		if (Gdx.input.justTouched()) {
			int x = Gdx.input.getX();
			int y = screenY - Gdx.input.getY();
			if (estado == 0) {
				estado = 1;
			}

			if (estado == 1) {
				for (int i=0; i<fileiras.size; i++) {
					int retorno = fileiras.get(i).toque(x,y);

					if (retorno != 0) {
						if (retorno == 1 && i == indexInf) {
							//continua..
							pontos++;
							indexInf++;
							piano.tocar();
						} else if (retorno == 1){
							//finalizar na forma 1
							fileiras.get(indexInf).erro();
							finalizar(0);
						} else {
							//finalizar na forma 2
							finalizar(0);
						}
						break;
					}
				}
			} else if (estado == 2) iniciar();
		}
	}

	private void adicionar() {
		float y = fileiras.get(fileiras.size-1).y + tileHeight;
		fileiras.add(new Fileira(y, random.nextInt(4)));
	}

	private void iniciar() {
		tempoTotal = 0;
		indexInf = 0;
		pontos = 0;

		fileiras.clear();
		fileiras.add(new Fileira(tileHeight,random.nextInt(4)));
		adicionar();
		adicionar();
		adicionar();
		adicionar();

		estado = 0;
		velAtual = 0;
		piano.reset();
	}

	private void finalizar(int opt) {
		Gdx.input.vibrate(200);
		estado = 2;
		if (opt == 1){
			for (Fileira f:fileiras) {
				f.y += tileHeight;
			}
		}
	}

	private float getWidth(BitmapFont font, String texto){
		glyphLayout.reset();
		glyphLayout.setText(font, texto);
		return glyphLayout.width;
	}


	@Override
	public void dispose () {
		shapeRenderer.dispose();
		batch.dispose();
		texIniciar.dispose();
		piano.dispose();
	}
}
