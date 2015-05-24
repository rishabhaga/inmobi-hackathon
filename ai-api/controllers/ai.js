/**
 * Created by suraj on 23/05/15.
 */

(function (module) {
    'use strict';

    var async = require('async');
    var moment = require('moment');
    var mysql = require('mysql');
    var appUtil = require('../helpers/util');
    var log = appUtil.log;

    var connection = mysql.createConnection({
        host    : '10.14.120.177',
        user    : 'root',
        password: '',
        database: 'inmobi'
    });

    module.exports = {
        learn  : function (data, callback) {
            var startTime = data.start_time;
            var burst = data.burst;
            var dayStart = moment.utc().startOf('day')/1000;
            var secTillDayStart = startTime - dayStart;
            var slotId = Math.floor(secTillDayStart / (5 * 60));
            var arrIndex = slotId - 1;
            var newStream = {};
            var j = 0;

            connection.query('select * from user_activity where user_id = ?', [data.user_id], function (queryErr, rows) {
                if (queryErr) {
                    return callback(queryErr);
                }

                if (rows.length === 0) {
                    // add a new entry. fire update.
                    for (; j <= burst.length; j++) {
                        newStream[arrIndex + j] = {
                            count: 1,
                            value: Number(burst.charAt(j))
                        };
                    }

                }
                else {
                    newStream = JSON.parse(rows[0].slots);
                    for (; j <= burst.length; j++) {
                        var pivot = newStream[arrIndex + j];
                        newStream[arrIndex + j] = {
                            count: (pivot) ? ++pivot.count : 1,
                            value: (pivot) ?
                                (pivot.value + Number(burst.charAt(j))) :
                                Number(burst.charAt(j))
                        };
                    }
                }
                connection.query('insert into user_activity values (?,?) on duplicate key update slots = ?',
                    [data.user_id, JSON.stringify(newStream), JSON.stringify(newStream)], function (insertErr) {
                        return callback(insertErr);
                    });
            });
        },
        predict: function (userId, callback) {
            connection.query('select * from user_activity where user_id = ?', [userId], function queryCB(queryErr, rows) {
                if (queryErr) {
                    return callback(queryErr);
                }

                if (rows.length === 0) {
                    return callback(null, {});
                }

                var slots = JSON.parse(rows[0].slots);
                var returnD = {};

                for (var prop in slots) {
                    if (slots.hasOwnProperty(prop)) {
                        var avg  = (slots[prop].value / slots[prop].count);
                        returnD[prop] = (avg >= 0.6) ? 1 : 0;
                    }
                }

                return callback(null, returnD);
            });
        }
    };

})(module);