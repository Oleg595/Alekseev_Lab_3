package Reader;


import ru.spbstu.pipeline.BaseGrammar;

public class ReaderGrammar extends BaseGrammar {

    private static final String[] data;

    static{
        ReaderGrammar.TokenReader[] token = ReaderGrammar.TokenReader.values();

        data = new String[token.length];

        for(int i = 0; i < data.length; i++){
            data[i] = token[i].toString();
        }
    }

    public ReaderGrammar(){
        super(data);
    }

    public enum TokenReader{
        MODE("MODE "),
        BUFFER_SIZE("BUFFER_SIZE ");

        private final String name;

        TokenReader(String str){
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