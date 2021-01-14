package Manager;

import ru.spbstu.pipeline.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.TreeMap;
import java.util.logging.Logger;

public class Manager_Semantic {
    private FileInputStream input;
    private FileOutputStream output;
    private IReader reader;
    private IWriter writer;
    private TreeMap<Integer, IExecutor> classes;
    private TreeMap<Integer, String> conf;
    private int num_exec;
    Logger logger;

    public Manager_Semantic(Logger log){
        logger = log;
    }

    public RC Semantic(TreeMap<String, String> map){
        conf = new TreeMap<Integer, String>();
        classes = new TreeMap<Integer, IExecutor>();
        try {
            if(map.get(MyGrammar.Token.NUM_EXECUTORS.toString()) != null){
                num_exec = Integer.parseInt(map.get(MyGrammar.Token.NUM_EXECUTORS.toString()));
            }
            else{
                num_exec = 1;
            }
            RC rc = Check(map, num_exec);
            if(rc != RC.CODE_SUCCESS){
                return rc;
            }
            for (String str : map.keySet()) {
                if (str.equals(MyGrammar.Token.READER_INPUT.toString())) {
                    input = new FileInputStream(map.get(str));
                }
                if (str.contains(MyGrammar.Token.CONFIGURATION.toString())) {
                    String number = str.substring(str.indexOf(" ") + 1, str.lastIndexOf(" "));
                    conf.put(Integer.parseInt(number), map.get(str));
                }
                if (str.equals(MyGrammar.Token.WRITER_OUTPUT.toString())) {
                    output = new FileOutputStream(map.get(str));
                }
                if (str.contains(MyGrammar.Token.EXECUTOR.toString())) {
                    Object obj = New_Exec(map.get(str));
                    String number = str.substring(str.indexOf(" ") + 1, str.lastIndexOf(" "));
                    if(Integer.parseInt(number) == 1){
                        reader = (IReader)obj;
                        continue;
                    }
                    if(Integer.parseInt(number) == num_exec){
                        writer = (IWriter)obj;
                        continue;
                    }
                    classes.put(Integer.parseInt(number), (IExecutor) obj);
                }
            }
        }
        catch (FileNotFoundException e){
            logger.warning("Not file to read");
            return RC.CODE_FAILED_TO_READ;
        }
        return RC.CODE_SUCCESS;
    }

    private RC Check(TreeMap<String, String> map, int num){
        if(map.get(MyGrammar.Token.READER_INPUT.toString()) == null){
            logger.warning("Not input file");
            return RC.CODE_CONFIG_GRAMMAR_ERROR;
        }
        if(map.get(MyGrammar.Token.WRITER_OUTPUT.toString()) == null){
            logger.warning("Not output file");
            return RC.CODE_CONFIG_GRAMMAR_ERROR;
        }
        for(Integer i = 1; i <= num; i++){
            if(map.get(MyGrammar.Token.CONFIGURATION.toString() + i.toString() + " ") == null){
                logger.warning("Not configuration file");
                return RC.CODE_CONFIG_GRAMMAR_ERROR;
            }
            if(map.get(MyGrammar.Token.EXECUTOR.toString() + i.toString() + " ") == null){
                logger.warning("Not executor");
                return RC.CODE_CONFIG_GRAMMAR_ERROR;
            }
        }
        return RC.CODE_SUCCESS;
    }

    private Object New_Exec(String str){
        try{
            return Class.forName(str).getConstructor(Logger.class).newInstance(logger);
        }
        catch(ClassNotFoundException  | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e){
            return null;
        }
    }

    public int Num_Exec(){
        return num_exec;
    }

    public TreeMap<Integer, IExecutor> Classes(){
        return classes;
    }

    public TreeMap<Integer, String> Configs(){
        return conf;
    }

    public FileInputStream Reader_Input(){
        return input;
    }

    public FileOutputStream Writer_Output(){
        return output;
    }

    public IReader Reader(){
        return reader;
    }

    public IWriter Writer(){
        return writer;
    }
}
