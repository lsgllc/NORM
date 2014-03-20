package com.lsgllc;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Goal which touches a timestamp file.
 *
 * @goal touch
 * 
 * @execute phase generate-resources
 */

public class GenAsmMojo
    extends AbstractMojo
{
    /**
     * Location of the file.
     * @parameter expression="${norm.asm.java.model.src.location}"
     * @required
     */
    private File javaModel;
    /**
     * Location of the file.
     * @parameter expression="${${basedir}/src/main/resources}"
     * @required
     */
    private String resourceDirectory;
    /**
     * Location of the file.
     * @parameter expression="${norm.asm.propertyFilename}"
     * @required
     */
    private String propertyFilename;

    public void execute()
        throws MojoExecutionException
    {

        File f = javaModel;
        String canonicalPropertyFileName = resourceDirectory + "/" + propertyFilename ;

        if ( !f.exists() )
        {
            throw new MojoExecutionException("Model file missing...");
        }

        FileWriter w = null;
        try
        {
            getLog().info( "Creating property file: " + canonicalPropertyFileName);
            getLog().info("Generating Byte-Code instructions using " + f.getCanonicalPath() + f.getName() );
        }
        catch ( IOException e )
        {
            throw new MojoExecutionException( "Error Creating Byte Code properties file" , e );
        }
        finally
        {
            if ( w != null )
            {
                try
                {
                    w.close();
                }
                catch ( IOException e )
                {
                    // ignore
                }
            }
        }
    }
}
