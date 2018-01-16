package com.codex.shootingstars;

import android.util.Log;
import com.filip.androidgames.framework.FileIO;

import java.io.*;

public class Settings {
    public static boolean soundEnabled = true;
    public static String PlayerShip = "PlayerShip.png";
    public static int[] highscores = new int[] {0, 0, 0, 0, 0};

    public static void loadFiles(FileIO files)
    {
        BufferedReader in = null;
        try{
            in = new BufferedReader(new InputStreamReader(files.readFile(".shootingstars")));
            soundEnabled = Boolean.parseBoolean(in.readLine());
            PlayerShip = in.readLine();
            for (int i = 0; i < highscores.length; i++){
                highscores[i] = Integer.parseInt(in.readLine());
            }
        }
        catch (IOException e){
        }
        catch (NumberFormatException e){}
        finally{
            try{
                if (in != null)
                    in.close();
            }catch (IOException e){}
        }
    }

    public static void saveFiles(FileIO files){
        BufferedWriter out = null;
        try{
            out = new BufferedWriter(new OutputStreamWriter(files.writeFile(".shootingstars")));
            out.write(Boolean.toString(soundEnabled));
            out.write('\n');
            out.write(PlayerShip);
            out.write('\n');
            for (int i = 0; i < highscores.length; i++) {
                out.write(Integer.toString(highscores[i]));
                out.write('\n');
            }
        }
        catch (IOException e) {
            Log.e("Cake", "Exception: " + e);
        }finally{
            try{
                if (out !=null)
                    out.close();
                Log.d("Test","???");
            }catch(IOException e){

            }
        }
    }

    public static void addScore(int score){
        for (int i = 0; i < highscores.length; i++){
            if (highscores[i] < score){
                for (int j = highscores.length - 1; j > i; j--)
                    highscores[j] = highscores[j-1];
                highscores[i] = score;
                break;
            }
        }
    }
}
