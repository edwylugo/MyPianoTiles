package br.com.edwylugo.mypianotiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import static br.com.edwylugo.mypianotiles.Cons.*;

public class Fileira {

    public float y;
    private int correta; // 0 a 3
    private int pos;
    private boolean ok;
    private boolean dest;
    private float anim;

    public Fileira(float y, int correta) {
        this.y = y;
        this.correta = correta;
        this.ok = false;
        this.dest = false;
        this.anim = 0;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(verde);

        shapeRenderer.rect(correta*tileWidth,y,tileWidth,tileHeight);


        if (dest) {
            if (ok){
                shapeRenderer.setColor(certo);
            } else {
                shapeRenderer.setColor(errado);
            }

            shapeRenderer.rect(pos*tileWidth + (tileWidth - anim*tileWidth)/2f, y+(tileHeight-anim*tileHeight)/2, anim*tileWidth, anim*tileHeight);
        }

        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GRAY);

        for (int i=0; i<=3; i++) {
            shapeRenderer.rect(i*tileWidth,y,tileWidth,tileHeight);
        }
    }

    public void  anim(float time) {
        if (dest && anim < 1) {
            anim += 5*time;
            if (anim >= 1) {
                anim = 1;
            }
        }
    }


    //Controlando a fileira
    public int update(float time) {
        y -= time*velAtual;

        if (y < 0 - tileHeight) {
            if (ok) {
                return 1;
            } else {
                erro();
                return 2;
            }
        }
        return 0;
    }

    //Controlador e regra do toque
    public int toque(int tx, int ty) {
        if (ty > y && ty <= y + tileHeight){
            pos = tx/tileWidth;

            if (pos == correta) {
                ok = true;
                dest = true;
              return 1;
            } else {
                ok = false;
                dest = true;
                return 2;
            }
        }
        return 0;
    }

    public void erro() {
        dest = true;
        pos = correta;
    }


}
