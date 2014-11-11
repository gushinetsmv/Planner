<!doctype html>
<html lang="en" xmlns="http://www.w3.org/1999/html" ng-app="plannerApp">
<head>
    <meta charset="UTF-8">
    <title>Planner: report generation page</title>

    <link rel="stylesheet" type="text/css" href="css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="css/bootstrap-datetimepicker.min.css"/>

    <script type="text/javascript" src="js/jquery-1.11.1.js"></script>
    <script type="text/javascript" src="js/moment.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.js"></script>
    <script type="text/javascript" src="js/angular.js"></script>
    <script type="text/javascript" src="js/bootstrap-datetimepicker-utc.js"></script>

    <script>
        var plannerApp = angular.module('plannerApp', []);

        function getInterval() {
            return {
                'start': $('.start-report-date').data("DateTimePicker").getDate(),
                'end'  : $('.end-report-date').data("DateTimePicker").getDate()
            };
        }

        plannerApp.controller('ReportCtrl', ['$scope', '$http', function($scope, $http) {
            $scope.reportEntries = [
            ];

            $scope.pdf = function() {
                console.debug("pdf generation");
                var interval = getInterval();
                console.debug("\t[" + interval.start + "," +interval.end + "]");
                window.location.replace("export-report?start="+ interval.start +"&end="
                        + interval.end + "&reportType=pdf");
            }

            $scope.csv = function() {
                console.debug("csv generation");
                var interval = getInterval();
                console.debug("\t[" + interval.start + "," +interval.end + "]");
                window.location.replace("export-report?start="+ interval.start +"&end="
                        + interval.end + "&reportType=csv");
            }

            $scope.preview = function() {
                console.debug("preview generation");
                var interval = getInterval();
                $http.get('planner-servlet?start=' + interval.start + "&end=" + interval.end)
                        .success (function(data){
                        $scope.reportEntries = data;
                })

                console.debug("\t[" + interval.start + "," +interval.end + "]");
            }

        }]);

    </script>
    <style>
        .form-ctrl-label {
            font-size: 20px;
        }

        .css-form input.ng-invalid.ng-dirty {
            background-color: #FA787E;
        }
    </style>
</head>
<body>
    <div class="container" ng-controller="ReportCtrl">
        <h1>Capacity per Employee</h1>
        <form role="form" style=" margin-top: 2em;">
            <div class="form-group">
                <label for="startDateId" class="form-ctrl-label control-label">Date time from</label>
                <div class='input-group date start-report-date'>
                    <input type='text' id="startDateId" class="form-control" data-date-format="YYYY-MM-DD HH:mm"
                           placeholder="Select start date.."/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
                        </span>
                </div>
            </div>
            <div class="form-group">
                <label for="endDateId" class="form-ctrl-label control-label">Date time to</label>
                <div class='input-group date end-report-date'>
                    <input type='text' id="endDateId" class="form-control" data-date-format="YYYY-MM-DD HH:mm"
                           placeholder="Select end date.."/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
                        </span>
                </div>
            </div>
            <div class="form-group">
                <button type="button" class="btn btn-primary" ng-click="pdf()">PDF</button>
                <button type="button" class="btn btn-primary" ng-click="csv()">CSV</button>
                <button type="button" class="btn btn-primary" ng-click="preview()">Preview</button>
            </div>
        </form>

        <div style="overflow: auto; height: 200px; margin-top: 4em">
            <table class="table table-striped table-bordered table-hover">
                <thead>
                    <tr>
                        <th>Employee Key</th>
                        <th>Capacity</th>
                    </tr>
                </thead>
                <tbody>
                    <tr ng-repeat="reportEntry in reportEntries">
                        <td>{{reportEntry.employeeId}}</td>
                        <td>{{reportEntry.workingHours}}</td>
                    </tr>
                </tbody>
            </table>

        </div>
    </div>

    <script>
        $(function(){

            $('.start-report-date').datetimepicker(
                    {
                        showToday: true,
                        sideBySide: true
                    }
            );

            $('.end-report-date').datetimepicker(
                    {
                        showToday: true,
                        sideBySide: true
                    }
            );
        });

    </script>
</body>
</html>