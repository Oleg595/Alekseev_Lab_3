package Writer;

import ru.spbstu.pipeline.BaseGrammar;

public class WriterGrammar extends BaseGrammar {

    private static final String[] data;

    static{
        WriterGrammar.TokenWriter[] token = WriterGrammar.TokenWriter.values();

        data = new String[token.length];

        for(int i = 0; i < data.length; i++){
            data[i] = token[i].toString();
        }
    }

    public WriterGrammar(){
        super(data);
    }

    public enum TokenWriter{
        MODE("MODE ");

        private final String name;

        TokenWriter(String str){
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

