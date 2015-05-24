/**
 * Created by suraj on 23/05/15.
 */

(function (module) {
    'use strict';

    var appUtil = require('../../helpers/util');
    var log = appUtil.log;
    var ai = require('../../controllers/ai');
    var jsonView = appUtil.jsonView;
    module.exports = {
        init: function (app) {
            app.post('/learn', function (req, res, next) {
                var data = req.body;
                var json = new jsonView();
                // some basic validation
                if (!(data && data.user_id && data.start_time && data.burst)) {
                    json.setMsg('Invalid Input');
                    res.status(400);
                    res.setHeader('Content-Type', 'application/json');
                    res.end(json.render());
                    return;
                }

                ai.learn(data, function (error, status) {
                    if (error) {
                        next(error);
                        return;
                    }

                    json.setMsg('success');
                    res.status(200);
                    res.setHeader('Content-Type', 'application/json');
                    res.end(json.render());
                });
            });

            app.get('/predict/:user_id', function (req, res) {

                if (!req.params.user_id) {
                    var json = new jsonView();
                    json.setMsg('Invalid Input');
                    res.status(400);
                    res.setHeader('Content-Type', 'application/json');
                    res.end(json.render());
                    return;
                }

                ai.predict(req.params.user_id, function (error, data) {
                    var json = new jsonView();
                    if (error) {
                        log.error(error);
                        json.setMsg('Something went wrong. Please try again later.');
                        res.status(500);
                        res.setHeader('Content-Type', 'application/json');
                        res.end(json.render());
                        return;
                    }

                    res.status(200);
                    res.setHeader('Content-Type', 'application/json');
                    json.data(data);
                    res.end(json.render());
                });
            });
        }
    };
})(module);