package Manager;


import ru.spbstu.pipeline.BaseGrammar;

public class MyGrammar extends BaseGrammar {
    private static final String[] data;

    static{
        Token[] token = Token.values();

        data = new String[token.length];

        for(int i = 0; i < data.length; i++){
            data[i] = token[i].toString();
        }
    }

    public MyGrammar(){
        super(data);
    }

    public enum Token{
        READER_INPUT("READER_INPUT "),
        CONFIGURATION("CONFIGURATION "),
        WRITER_OUTPUT("WRITER_OUTPUT "),
        NUM_EXECUTORS("NUM_EXECUTORS "),
        EXECUTOR("EXECUTOR ");

        private final String name;

        Token(String str){
            name = str;
        }

        public String toString(){
            return name;
        }

    }

}