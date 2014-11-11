<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="resources/css/jquery.datetimepicker.css"/>
    <script src="resources/js/jquery.js"></script>
    <script src="resources/js/jquery.datetimepicker.js"></script>
</head>
<body>
<style>
    table {
        background-color: #aaa;
    }
    tbody {
        background-color: #ddd;
        height: 200px;
        overflow: auto;
    }
    td {
        padding: 3px 10px;
    }

    thead > tr, tbody{
        display:block;}
</style>
    <form>
        Date time from: &nbsp;<input type="text" value="" id="startdatetime"> <br/><br/>
        Date time to: &nbsp;<input type="text" value="" id="enddatetime">
    </form>

    <input type="button" value="PDF" id="pdfButton"/>
    <input type="button" value="CSV" id="csvButton"/>
    <input type="button" value="Preview" id="previewButton"/>
    <table>
        <thead>
            <tr>
                <td> EmployeeId</td>
                <td> Working Hours</td>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${reportEntries}" var="reportEntry">
                <tr>
                    <td><c:out value="${reportEntry.employeeId}"/></td>
                    <td><c:out value="${reportEntry.workingHours}"/></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
<script>
    $(document).ready(function () {
        jQuery('#startdatetime').datetimepicker({format: 'unixtime'});
        jQuery('#enddatetime').datetimepicker({format: 'unixtime'});

        jQuery('#previewButton').click(function() {
            var start = jQuery('#startdatetime').val();
            var end = jQuery('#enddatetime').val();

            window.location.replace("planner-servlet?start=" + start +"&end=" + end);
        });

        jQuery('#pdfButton').click(function() {
            var start = jQuery('#startdatetime').val();
            var end = jQuery('#enddatetime').val();
            window.location.replace("export-servlet?start="+ start +"&end=" + end + "&reportType=pdf");
        });

        jQuery('#csvButton').click(function() {
            var start = jQuery('#startdatetime').val();
            var end = jQuery('#enddatetime').val();
            window.location.replace("export-servlet?start="+ start +"&end=" + end + "&reportType=csv");
        })

    });
</script>
</body>
</html>
