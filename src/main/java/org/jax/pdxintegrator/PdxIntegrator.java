package org.jax.pdxintegrator;

import org.jax.pdxintegrator.command.Command;
import org.jax.pdxintegrator.io.CommandParser;

/**
 * An app for importing data from PDX centers and converting it to our standard dataformat.
 * ToDo still a prototype!
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 * @version 0.1.0
 */
public class PdxIntegrator {



    public static void main(String args[]) {
        CommandParser cmdline= new CommandParser(args);
        Command command = cmdline.getCommand();
        command.execute();

    }




}
