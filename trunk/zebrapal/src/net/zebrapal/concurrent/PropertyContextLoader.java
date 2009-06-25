package net.zebrapal.concurrent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.zebrapal.concurrent.persist.ITaskPersistenceManager;

/**
 *
 * @author X-Spirit
 */
public class PropertyContextLoader implements ZebrapalContextLoader{

    private String propFileName="zebrapal.properties";

    public PropertyContextLoader() {
        
    }

    public PropertyContextLoader(String propFileName){
        this.propFileName=propFileName;
    }
    
    public TaskContext loadContext() throws ContextLoadException{
        File propFile = new File(propFileName);

        Properties props = new Properties();

        InputStream in = null;

        try {
            if(propFile.exists()){
                System.out.println("Loading properties file: "+propFileName);
                try {
                    in = new BufferedInputStream(new FileInputStream(propFileName));
                    props.load(in);
                } catch (IOException ioe) {
                    throw new ContextLoadException("Property File "+propFileName+
                    " cannot be read, please check your property file", ioe);
                }
                
            }else if(propFileName!=null){
                System.out.println("Loading properties file: "+propFileName);
                in = Thread.currentThread().getContextClassLoader().getResourceAsStream(propFileName);
                if(in == null) {
                    throw new ContextLoadException("Properties file: '"
                        + propFileName + "' could not be found.");
                }

                in = new BufferedInputStream(in);
                try {
                    props.load(in);
                } catch (IOException ioe) {
                    throw  new ContextLoadException("Properties file: '"
                            + propFileName + "' could not be read.", ioe);
                }
            }
        } finally {
            if(in != null) {
                try { in.close(); } catch(IOException ignore) { /* ignore */ }
            }
        }
        try {
            return initialize(props);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        
        
    }

    private TaskContext initialize(Properties props) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        TaskContext taskContext = new TaskContext();
        taskContext.initialize(props);
        return taskContext;
    }

    public void unloadContext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
