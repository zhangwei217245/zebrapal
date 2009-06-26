package net.zebrapal.concurrent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

    public TaskContext loadContext(String propFileName) throws Exception{
        this.propFileName=propFileName;
        return loadContext();
    }
    
    public TaskContext loadContext() throws Exception{
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
            }else {
                System.out.println("default resource file in Zebrapal package: 'zebrapal.properties'");

                in = getClass().getClassLoader().getResourceAsStream(
                        "zebrapal.properties");

                if (in == null) {
                    in = getClass().getClassLoader().getResourceAsStream(
                            "/zebrapal.properties");
                }
                if (in == null) {
                    in = getClass().getClassLoader().getResourceAsStream(
                            "net/zebrapal/zebrapal.properties");
                }
                if (in == null) {
                    throw new ContextLoadException("Default zebrapal.properties not found in class path");
                }
                try {
                    props.load(in);
                } catch (IOException ioe) {
                    throw new ContextLoadException("Resource properties file: 'net/zebrapal/zebrapal.properties' "
                                    + "could not be read from the classpath.", ioe);
                }
            }
        } finally {
            if(in != null) {
                try { in.close(); } catch(IOException ignore) { /* ignore */ }
            }
        }
        return initialize(props);
       
    }

    public void unloadContext(TaskContext taskContext) {
        taskContext.destroy();
    }
    private TaskContext initialize(Properties props) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        TaskContext taskContext = new TaskContext();
        taskContext.initialize(props);
        return taskContext;
    }
}
