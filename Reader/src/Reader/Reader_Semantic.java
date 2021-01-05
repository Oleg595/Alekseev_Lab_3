package Reader;

import ru.spbstu.pipeline.RC;

import java.util.TreeMap;
import java.util.logging.Logger;

public class Reader_Semantic {
    private int buffer_size;
    private Logger log;

    public RC Semantic(TreeMap<String, String> map, Logger logger){
        log = logger;
        RC rc = Check(map);
        if(rc != RC.CODE_SUCCESS){
            return rc;
        }
        for (String str : map.keySet()) {
            if(str.equals(ReaderGrammar.TokenReader.BUFFER_SIZE.toString())){
                buffer_size = Integer.parseInt(map.get(str));
            }
        }
        return RC.CODE_SUCCESS;
    }

    private RC Check(TreeMap<String, String> map){
        if(map.get(ReaderGrammar.TokenReader.BUFFER_SIZE.toString()) == null){
            log.info("Not buffer size");
            return RC.CODE_INVALID_ARGUMENT;
        }
        return RC.CODE_SUCCESS;
    }

    public int Buffer_Size() {
        return buffer_size;
    }

}
