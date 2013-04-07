/**
 * Created with IntelliJ IDEA.
 * User: steve
 * Date: 4/7/13
 * Time: 11:29 AM
 * To change this template use File | Settings | File Templates.
 */

$(function () {
    $.get('/tasks/list', function (result) {

    }, 'json');
});