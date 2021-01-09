package Reader;

import ru.spbstu.pipeline.*;
import ru.spbstu.pipeline.RC;

import java.io.*;
import java.util.logging.Logger;

public class Reader implements IReader {
    private FileInputStream reader;
    private String config;
    private Logger logger;
    private IConsumer consumer;
    private ReaderGrammar grammar;
    private int buffer_size;
    private ReaderGrammar.MODE mode;
    private TYPE[] types;
    private byte[] data;

    public Reader(Logger log){
        logger = log;
        grammar = new ReaderGrammar();
        types = new TYPE[]{TYPE.CHAR, TYPE.BYTE, TYPE.SHORT};
    }

    @Override
    public RC setConsumer(IConsumer iConsumer) {
        if(iConsumer == null){
            logger.warning("Not consumer in reader!");
            return RC.CODE_FAILED_PIPELINE_CONSTRUCTION;
        }
        consumer = iConsumer;
        return RC.CODE_SUCCESS;
    }

    public RC setProducer(IProducer iProducer){
        return RC.CODE_SUCCESS;
    }

    public RC setInputStream(FileInputStream fis){
        if(fis == null){
            logger.warning("Not input file");
            return RC.CODE_INVALID_ARGUMENT;
        }
        reader = fis;
        return RC.CODE_SUCCESS;
    }
    public RC setConfig(String configFileName){
        config = configFileName;
        ReaderGrammar grammar = new ReaderGrammar();
        try {
            Syntaksis synt = new Syntaksis();
            RC rc = synt.Synt(new BufferedReader(new FileReader(config)), logger, grammar.delimiter());
            if(rc != RC.CODE_SUCCESS){
                return rc;
            }
            Reader_Semantic seman = new Reader_Semantic();
            rc = seman.Semantic(synt.Result(), logger);
            if(rc != RC.CODE_SUCCESS){
                return rc;
            }
            buffer_size = seman.Buffer_Size();
        }
        catch(FileNotFoundException e){
            logger.warning(RC.CODE_INVALID_ARGUMENT.toString());
            return RC.CODE_INVALID_ARGUMENT;
        }
        return RC.CODE_SUCCESS;
    }

    public TYPE[] getOutputTypes(){
        return types;
    }



    public IMediator getMediator(TYPE var1){
        if(var1 == TYPE.BYTE){
            return new Byte_Mediator();
        }
        if(var1 == TYPE.CHAR){
            return new Char_Mediator();
        }
        if(var1 == TYPE.SHORT){
            return new Short_Mediator();
        }
        return null;
    }

    class Byte_Mediator implements IMediator {

        public Object getData(){
            if(data == null){
                return null;
            }
            return Cast.Copy(data);
        }

    }

    class Char_Mediator implements IMediator {

        public Object getData(){
            if(data == null){
                return null;
            }
            return Cast.ByteToChar(Cast.Copy(data));
        }

    }

    class Short_Mediator implements IMediator {

        public Object getData(){
            if(data == null){
                return null;
            }
            return Cast.ByteToShort(Cast.Copy(data));
        }

    }

    public RC execute(){
        byte[] arr = new byte[buffer_size];
        try {
            int end = buffer_size;
            while(end == buffer_size){
                end = reader.read(arr);
                if(end != -1) {
                    data = new byte[end];
                }
                else{
                    data = null;
                }
                for(int i = 0; i < end; i++){
                    data[i] = arr[i];
                }
                RC rc = consumer.execute();
                if(rc != RC.CODE_SUCCESS){
                    return rc;
                }
                if(arr == null){
                    break;
                }
                arr = new byte[buffer_size];
            }
        }
        catch(IOException e){
            logger.warning(RC.CODE_FAILED_TO_READ.toString());
            return RC.CODE_FAILED_TO_READ;
        }
        return RC.CODE_SUCCESS;
    }

}
