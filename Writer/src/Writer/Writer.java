package Writer;

import ru.spbstu.pipeline.*;
import ru.spbstu.pipeline.IWriter;
import ru.spbstu.pipeline.RC;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class Writer implements IWriter {

    private FileOutputStream write;
    private Logger logger;
    private IProducer producer;
    private IConsumer consumer;
    private WriterGrammar grammar;
    private final TYPE[] types;
    private IMediator mediator;

    public Writer(Logger log){
        logger = log;
        grammar = new WriterGrammar();
        types = new TYPE[]{TYPE.BYTE, TYPE.CHAR, TYPE.SHORT};
    }
    public RC setOutputStream(FileOutputStream fos){
        if(fos == null){
            logger.warning("Not output file");
            return RC.CODE_INVALID_OUTPUT_STREAM;
        }
        write = fos;
        return RC.CODE_SUCCESS;
    }
    public RC setConsumer(IConsumer var1){
        consumer = null;
        return RC.CODE_SUCCESS;
    }
    public RC setProducer(IProducer var1){
        if(var1 == null){
            logger.warning("Not producer in Writer");
            return RC.CODE_FAILED_PIPELINE_CONSTRUCTION;
        }
        producer = var1;
        mediator = var1.getMediator(Cast.Input_Type(types, producer.getOutputTypes()));
        return RC.CODE_SUCCESS;
    }

    private byte[] Input_Data(){
        TYPE t = Cast.Input_Type(types, producer.getOutputTypes());
        if(mediator.getData() == null){
            return null;
        }
        if(t == TYPE.SHORT) {
            return Cast.ShortToByte((short[])mediator.getData());
        }
        if(t == TYPE.CHAR){
            return Cast.CharToByte((char[])mediator.getData());
        }
        return (byte[])mediator.getData();
    }

    public RC setConfig(String configFileName){
        return RC.CODE_SUCCESS;
    }
    public RC execute(){
        try {
            byte[] data = Input_Data();
            if(data == null){
                return RC.CODE_SUCCESS;
            }
            write.write(data);
        }
        catch(IOException e) {
            logger.warning(RC.CODE_FAILED_TO_READ.toString());
            return RC.CODE_FAILED_TO_WRITE;
        }
        return RC.CODE_SUCCESS;
    }
}
