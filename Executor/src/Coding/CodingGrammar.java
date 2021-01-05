package Coding;


import ru.spbstu.pipeline.BaseGrammar;

public class CodingGrammar extends BaseGrammar {

    private static final String[] data;

    static{
        CodingGrammar.TokenCoding[] token = CodingGrammar.TokenCoding.values();

        data = new String[token.length];

        for(int i = 0; i < data.length; i++){
            data[i] = token[i].toString();
        }
    }

    public CodingGrammar(){
        super(data);
    }

    public enum TokenCoding{
        MODE("MODE "),
        FILE("FILE ");

        private final String name;

        TokenCoding(String str){
            name = str;
        }

        public String toString(){
            return name;
        }

    }

    public enum MODE{
        ENCODING,
        DECODING
    }

}

