/**
 * Created by suraj on 23/05/15.
 */

(function (module) {
    'use strict';

    module.exports = {
        init: function (app) {
            app.post('/v1/learn', function(req, res){
                throw new Error('shit');
            });
        }
    };
})(module);