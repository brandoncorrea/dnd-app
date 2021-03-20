const path = require("path"),
  express = require("express"),
  exampleRouter = require("../routes/example.route");

const staticFiles = express.static(path.join(__dirname, "../../client/build"));

function errorHandler(err, req, res, next) {
  res.status(err.status || 500);
  res.json({ error: err });
}

function notFoundHandler(req, res, next) {
  var err = new Error("Not Found");
  err.status = 404;
  next(err);
}

reactRoutingHandler = (req, res) =>
    res.sendFile(path.join(__dirname, "../../client/build", "index.html"));

module.exports.init = () => {
  // initialize app
  const app = express();

  // add a router
  app.use("/api/example", exampleRouter);
  app.use(errorHandler);
  app.use(notFoundHandler);
  if (process.env.NODE_ENV === "production") {
    app.use(staticFiles);
    app.get("*", reactRoutingHandler);
  }
  return app;
};