<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Modification de l'état d'un emprunt d'équipement</title>

</head>

<body>
    <h1>Demande de passage d'état à ${etatVise} initié par ${emailUtilisateur}</h1>

    <p>Emprunt n° ${numEmprunt}</p>

    <p><strong>Résultat : ${messageResultat}</strong></p>

    <hr>
    <p><a href="Controleur">Retour au formulaire</a></p>
</body>
</html>