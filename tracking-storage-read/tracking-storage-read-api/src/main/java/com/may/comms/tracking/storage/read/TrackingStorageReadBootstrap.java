package com.may.comms.tracking.storage.read;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class TrackingStorageReadBootstrap {
	private final JsonObject config;
	private final Vertx vertx;
	private final TrackingHandler trackingHandler;
	private final Router trackingStorageReadRouter;

	public TrackingStorageReadBootstrap (final JsonObject config, final Vertx vertx) {
		this.config = config;
		this.vertx = vertx;
		this.trackingStorageReadRouter = Router.router(this.vertx);
		this.vertx.eventBus().registerCOdec(new DefaultObjectCodec());
		this.trackingHandler = new TrackingHandlerImpl(this.vertx.eventBus());
	}

	public void start(final Future<Void> bootstrapFuture) {
		LOGGER.info("Deploying tracking storage read service");
		final Future<Void> verticleDeploymentFuture = Future.future();
		this.deployWorkerVerticles(verticleDeploymentFuture);

		verticleDeploymentFuture.compose(futureResult -> {
			this.initialiseRouter();
			this.mountSubRoutes();
		}).setHandler(handler -> {
			if(handler.succeeded()) {
				bootstrapFuture.complete();
			}
			else {
				bootstrapFuture.fail(handler.cause());
			}
		})
	}

	public mountSubRoutes() {
		this.trackingStorageReadRouter.post('events').handler(this.trackingHandler::createEvent);
		this.trackingStorageReadRouter.post('events/update').handler(this.trackingHandler::modifyEvent);
		this.trackingStorageReadRouter.get('health').handler(req -> {
			req.response().end("Service is up and running.");
		});
		this.trackingStorageReadRouter.post('test').handler(req -> {
			req.response().end("Test route.");
		});
	}

}
