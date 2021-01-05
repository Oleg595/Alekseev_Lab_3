package Coding;



import ru.spbstu.pipeline.RC;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

public class Symbols {

    private Map<Byte, Double> hmap;

    public Symbols(){
        hmap = new TreeMap<Byte, Double>();
    }

    public Symbols(Byte[] sym, Double[] prob){
        hmap = new TreeMap<Byte, Double>();
        for(int i = 0; i < sym.length; i++){
            hmap.put(sym[i], prob[i]);
        }
    }

    public Symbols(byte[] str){
        hmap = new TreeMap<Byte, Double>();
        for (int i = 0; i < str.length; i++)
        {
            Double c = 0.;//hmap.get(str[i])
            if (hmap.get(str[i]) == null)
                hmap.put((byte)str[i], 1.0 / str.length);
            else
                c = hmap.get(str[i]);
            hmap.put((byte)str[i], c + (1.0 / str.length));
        }
        double t, cur = 1.0;
        for(int i = this.length() - 1; i >= 0; i--){
            t = this.Value(i);
            hmap.put(this.Key(i), cur);
            cur -= t;
        }
    }

    public int length(){
        return hmap.size();
    }

    public void Add(Byte ch, Double num){
        hmap.put(ch, num);
    }

    public Byte[] Element(){//метод Element совместим с jdk 8 версии
        Byte[] arr = new Byte[length()];
        int i = 0;
        for (Byte key: hmap.keySet()) {
            arr[i] = key;
            i++;
        }
        return arr;
    }

    public Double[] Elem_Prob(){//метод Elem_Prob совместим с jdk 8 версии
        Double[] arr = new Double[length()];
        int i = 0;
        for (Byte key: hmap.keySet()) {
            arr[i] = hmap.get(key);
            i++;
        }
        return arr;
    }

    public Byte Key(int i){
        return this.Element()[i];
    }

    public Double Value(int i){
        return this.Elem_Prob()[i];
    }

    public Double Val_Key(Byte ch){
        for(int i = 0; i < this.length(); i++){
            if(ch == this.Key(i)){
                return this.Value(i);
            }
        }
        return 0.0;
    }

    public Double Prev_Val_Key(Byte ch){
        for(int i = 1; i < this.length(); i++){
            if(ch == this.Key(i)){
                return this.Value(i - 1);
            }
        }
        return 0.0;
    }

    public byte Symbol(Double num){
        for(int i = 1; i < this.length(); i++){
            if((Value(i) >= num) && (Value(i - 1) <= num)){
                return Key(i);
            }
        }
        return this.Key(0);
    }
}