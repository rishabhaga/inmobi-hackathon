(function (module) {
    'use strict';

    var express = require('express');
    var fs = require('fs');
    var morgan = require('morgan');
    var appUtil = require('./helpers/util');
    var bodyParser = require('body-parser');
    var domainMiddleware = require('express-domain-middleware');
    var routeBootstrap = require('./routes/bootstrap');

    var log = appUtil.log;

    var app = function () {
    };

    app.prototype.init = function () {
        this._app = express();
        this._initMiddleWares();
        this._initRoutes();
        this._initErrorHandlers();
        this._listen();
    };

    app.prototype._initMiddleWares = function () {

        //  1.  Access log writer - Morgan
        //      1.1 creating log stream for morgan
        var accessLogStream = fs.createWriteStream(appUtil.appConfig.log_path + 'access.log', {flags: 'a'});
        //      1.2 setting up in app.
        this._app.use(morgan('combined', {stream: accessLogStream}));

        //  2.  body parser.
        this._app.use(bodyParser.json({type: 'application/json'}));

        //  3.  Express domain middleware
        //      If any error is caught, then it invokes the normal/common express error handler.
        this._app.use(domainMiddleware);

    };

    app.prototype._initRoutes = function () {
        routeBootstrap.init(this._app);
        this._handle404();
    };

    app.prototype._handle404 = function () {
        this._app.use(function (req, res, next) {
            res.status(404);
            res.end();
        });
    };


    app.prototype._initErrorHandlers = function () {
        this._app.use(function (err, req, res, next) {

            log.error(err, {
                stacktrace: appUtil.codeHelper.parsetrace(err)
            });

            var jsonView = new (appUtil.jsonView)();
            jsonView.setErrorCode(1001);
            jsonView.setMsg('Something went wrong. Please try again later.');

            res.status(500);
            res.setHeader('Content-Type', 'application/json');
            res.end(jsonView.render());
        });
    };

    app.prototype._listen = function(){
        this._app.listen(4000);
    };

    module.exports = app;
})(module);