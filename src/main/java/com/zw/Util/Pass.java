package com.zw.Util;


import java.io.*;

public class Pass {
    private static Pass pass = new Pass();
    private static String c;
    private Pass (){}
    public static Pass getPass() {
        return pass;
    }
    public boolean getPassWord(String pass){
        try {
            FileInputStream in = new FileInputStream("pass.txt");
            InputStreamReader inReader = new InputStreamReader(in,"utf-8");
            BufferedReader bufReader = new BufferedReader(inReader);
            String line = bufReader.readLine().toString().trim();
            bufReader.close();
            inReader.close();
            in.close();
            if(line.equals(pass)){
                return true;
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return false;
    }

}
