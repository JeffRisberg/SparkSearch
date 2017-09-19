<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<style>
    .error {
        color: #ff0000;
    }
</style>

<script src="<c:url value="/resources/javascript/jquery.Jcrop.js" />"></script>

<c:if test="${flashMessage != null}">
    <div class="message">${flashMessage}</div>
</c:if>

<c:url var="saveUrl" value="/site/save"/>
<form:form method="post" action="${saveUrl}" enctype="multipart/form-data">
    <form:hidden path="id"/>

    <table>
        <tr>
            <td>Name:</td>
            <td><form:input path="name" size="40"/></td>
            <td><form:errors path="name" cssClass="error"/></td>
        </tr>
        <tr>
            <td>Custom CSS:</td>
            <td><form:input path="customCSS" size="40"/></td>
            <td><form:errors path="customCSS" cssClass="error"/></td>
        </tr>
        <tr>
            <td>File Upload:</td>
            <td><input type="file" name="imageFile"></td>
        </tr>
        <tr>
            <td>Image:</td>
            <td>
                <img id="jcrop_target" src="<c:url value="/resources/images/earth.jpg" />">
                <br/>
                <input type="hidden" size="4" id="x" name="x"/>
                <input type="hidden" size="4" id="y" name="y"/>
                <input type="hidden" size="4" id="x2" name="x2"/>
                <input type="hidden" size="4" id="y2" name="y2"/>
                <input type="hidden" size="4" id="w" name="w"/>
                <input type="hidden" size="4" id="h" name="h"/>
            </td>
        </tr>
    </table>

    <div class="botButtons">
        <input class="save" type="submit" value="Submit"/>
    </div>

    <form:hidden path="dateCreated"/>
</form:form>
<script type="text/javascript">
    $(function () {
        $('#jcrop_target').Jcrop({
            onChange: showCoords,
            onSelect: showCoords
        });
    });

    function showCoords(c) {
        $('#x').val(c.x);
        $('#y').val(c.y);
        $('#x2').val(c.x2);
        $('#y2').val(c.y2);
        $('#w').val(c.w);
        $('#h').val(c.h);
    }
</script>
