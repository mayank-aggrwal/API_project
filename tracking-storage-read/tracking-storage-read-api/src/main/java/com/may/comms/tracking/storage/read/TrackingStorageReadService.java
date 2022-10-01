package com.may.comms.tracking.storage.read;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TrackingStorageReadService extends AbstractVerticle {

    private static Logger LOGGER = LogManager.getLogger(TrackingStorageReadService.class);
    private TrackingStorageReadBootstrap trackingStorageReadBootstrap;

    @Override
    public void start(final Future<Void> startFuture) {
        LOGGER.info("Deploying Tracking storage read service");
        trackingStorageReadBootstrap = new TrackingStorageReadBootstrap(this.config(), this.vertx);
        final Future<Void> bootstrapFuture = Future.future();
        trackingStorageReadBootstrap.start(bootstrapFuture);
        bootstrapFuture.setHandler(result -> {
            if(result.succeeded()) {
                LOGGER.info("Succesfully deployed tracking storge servicce");
            } else {
                LOGGER.error("Deployment of tracking storage read service failed with cause: {}",
                        result.cause().getMessage());
                startFuture.fail(result.cause());
            }
        });
        bootstrapFuture.setHandler(result -> {
                LOGGER.info("Succesfully deployed tracking storge servicce");
            
        });
    }
    
    // Test route
    public void stop(final Future<Void> stopFuture) {
        System.out.println("Stop");
    }

}
