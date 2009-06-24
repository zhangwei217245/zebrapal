/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
    
    public void loadContext() throws ContextLoadException{
        File propFile = new File(propFileName);

        Properties props = new Properties();

        InputStream in = null;

        try {
            if(propFile.exists()){
                in = new BufferedInputStream(new FileInputStream(propFileName));
                props.load(in);
            }
        } catch (IOException e) {
            throw new ContextLoadException("Property File "+propFileName+
                    " cannot be read, please check your property file", e);
        }
        
    }

    public void unloadContext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
