package br.com.edwylugo.mypianotiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;


import java.util.HashMap;
import java.util.Map;

public class Piano {

    private Map<String, Sound> sounds;
    private Array<String> notas;
    private int indice = 0;

    public Piano(String musica) {
        FileHandle file = Gdx.files.internal(musica + ".txt");
        String texto = file.readString();
        notas = new Array<String>(texto.split(" "));
        Gdx.app.log("Log", notas.toString());

        sounds = new HashMap<String, Sound>();

        for (String s:notas) {
            if (!sounds.containsKey(s)) {
                sounds.put(s, Gdx.audio.newSound(Gdx.files.internal("sons/" + s + ".wav")));
            }
        }

    }

    public void tocar() {
        sounds.get(notas.get(indice)).play();
        indice++;
        if (indice == notas.size) {
            indice = 0;
        }
    }

    public void reset() {
        indice = 0;
    }

    public void dispose() {
        for (String key:sounds.keySet()) {
            sounds.get(key).dispose();
        }
    }

}
