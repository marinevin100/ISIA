<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Gestion d'un emprunt</title>
  </head>
  <body>
    <h1>Modification de l'état d'un emprunt</h1>
    <form action="Controleur" method="POST">
      Email :<input type="email" name="email" required placeholder="email" /><br />
      Code emprunt : <input type="number" name="codeEmprunt" required placeholder="code de l'emprunt" />
      Nouvel état :
      <select name="newEtat">
        <c:forEach items="${listeEtats}" var="etat">
          <option value="${etat.name()}">${etat.toString()}</option>
        </c:forEach>
      </select>
      <input type ="submit" value="Go" />
    </form>
  </body>
</html>