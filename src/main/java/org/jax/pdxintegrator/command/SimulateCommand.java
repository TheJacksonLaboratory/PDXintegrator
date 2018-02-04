package org.jax.pdxintegrator.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jax.pdxintegrator.simulate.ModelSimulator;

public class SimulateCommand extends Command{
    private static final Logger logger = LogManager.getLogger();


    public SimulateCommand() {
        logger.trace("Simulating PDF net data...");
    }


    @Override
    public void execute() {
        ModelSimulator simulator = new ModelSimulator(1);
    }
}
