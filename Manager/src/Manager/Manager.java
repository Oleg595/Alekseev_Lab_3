package Manager;

import ru.spbstu.pipeline.*;

import java.io.*;
import java.util.TreeMap;
import java.util.logging.Logger;



public class Manager {
    private TreeMap<Integer, IExecutor> classes;
    private IReader read;
    private IWriter write;
    private static final Logger logger = Logger.getLogger(Manager.class.getName());

    public Manager(String str){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(str));
            BaseGrammar b_g = new MyGrammar();
            Syntaksis synt = new Syntaksis();
            RC rc = synt.Synt(reader, logger, b_g.delimiter());
            if(rc != RC.CODE_SUCCESS){
                logger.warning("Error" + rc.toString());
                return;
            }
            Manager_Semantic seman = new Manager_Semantic(logger);
            rc = seman.Semantic(synt.Result());
            if(rc != RC.CODE_SUCCESS){
                logger.warning("Error" + rc.toString());
                return;
            }
            classes = seman.Classes();
            rc = Conveyor(seman.Configs(), seman.Reader(), seman.Writer(), seman.Num_Exec(), seman.Reader_Input(), seman.Writer_Output());
            if(rc != RC.CODE_SUCCESS){
                logger.warning("Error" + rc.toString());
                return;
            }
            rc = read.execute();
            if(rc != RC.CODE_SUCCESS){
                logger.warning("Error" + rc.toString());
            }
            else{
                logger.info("SUCCESS");
            }
        }
        catch(FileNotFoundException e){
            logger.warning(RC.CODE_INVALID_ARGUMENT.toString());
        }
    }

    RC Conveyor(TreeMap<Integer, String> conf, IReader reader, IWriter writer, int num_exec, FileInputStream input, FileOutputStream output){
        RC rc;
        read = reader;
        rc = read.setConsumer(classes.get(2));
        if(rc != RC.CODE_SUCCESS){
            return rc;
        }
        rc = read.setConfig(conf.get(1));
        if(rc != RC.CODE_SUCCESS){
            return rc;
        }
        rc = read.setInputStream(input);
        if(rc != RC.CODE_SUCCESS){
            return rc;
        }
        write = writer;
        rc = write.setProducer(classes.get(num_exec - 1));
        if(rc != RC.CODE_SUCCESS){
            return rc;
        }
        rc = write.setConfig(conf.get(num_exec));
        if(rc != RC.CODE_SUCCESS){
            return rc;
        }
        rc = write.setOutputStream(output);
        if(rc != RC.CODE_SUCCESS){
            return rc;
        }
        IExecutor exec;
        for (Integer i: classes.keySet()) {
            exec = classes.get(i);
            if(classes.get(i + 1) != null){
                rc = exec.setConsumer(classes.get(i + 1));
                if(rc != RC.CODE_SUCCESS){
                    return rc;
                }
            }
            else{
                rc = exec.setConsumer(write);
                if(rc != RC.CODE_SUCCESS){
                    return rc;
                }
                rc = write.setProducer(exec);
                if(rc != RC.CODE_SUCCESS){
                    return rc;
                }
            }
            if(classes.get(i - 1) != null){
                rc = exec.setProducer(classes.get(i - 1));
                if(rc != RC.CODE_SUCCESS){
                    return rc;
                }
            }
            else{
                rc = exec.setProducer(read);
                if(rc != RC.CODE_SUCCESS){
                    return rc;
                }
                rc = write.setConsumer(exec);
                if(rc != RC.CODE_SUCCESS){
                    return rc;
                }
            }
            rc = exec.setConfig(conf.get(i));
            if(rc != RC.CODE_SUCCESS){
                return rc;
            }
        }
        return RC.CODE_SUCCESS;
    }
}
