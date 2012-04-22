<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<div class="parameter">
  <c:choose>
    <c:when test="${not empty propertiesBean.properties['teamcity.build.workingDir']}">
      Working Directory: <strong><props:displayValue name="teamcity.build.workingDir" /></strong>
    </c:when>
  </c:choose>
</div>

<div class="parameter">
  Compiler path: <strong><props:displayValue name="argument.compiler_path" /></strong>
</div>

<div class="parameter">
  Build verbosity level: <strong><props:displayValue name="argument.build_verbosity_level" /></strong>
</div>

<div class="parameter">
  Build output directory: <strong><props:displayValue name="argument.build_output_directory" /></strong>
</div>

<div class="parameter">
  <c:choose>
    <c:when test="${not empty propertiesBean.properties['argument.compiler_flags']}">
      Compiler flags: <strong><props:displayValue name="argument.compiler_flags" /></strong>
    </c:when>
  </c:choose>
</div>

<div class="parameter">
  Run tests: <strong><props:displayValue name="argument.run_tests" /></strong>
</div>

<div class="parameter">
  Test reporting: <strong><props:displayValue name="argument.test_reporting" /></strong>
</div>

<div class="parameter">
  Clean before build: <strong><props:displayValue name="argument.clean_before_build" /></strong>
</div>
