<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<tr>
    <th>
        <label for="teamcity.build.workingDir">Working Directory: </label>
    </th>
    <td>
        <props:textProperty name="teamcity.build.workingDir" style="width:32em;"/>
        <span class="error" id="error_teamcity.build.workingDir"></span>
        <span class="smallNote">
             Optional. Specify when different from the checkout directory.
        </span>
    </td>
</tr>

<tr>
    <th>
        <label for="argument.build_output_directory">Build output directory: </label>
    </th>
    <td>
        <props:textProperty name="argument.build_output_directory" style="width:32em;"/>
        <span class="error" id="error_argument.build_output_directory"></span>
        <span class="smallNote">
             Optional. Specify build output directory relative to the checkout directory. Defaults to 'dist'.
        </span>
    </td>
</tr>

<tr>
    <th>
        <label for="argument.compiler_name">Compiler: </label>
    </th>
    <td>
        <props:textProperty name="argument.compiler_name" style="width:32em;"/>
        <span class="error" id="error_argument.compiler_name"></span>
        <span class="smallNote">
             Optional. Specify compiler name (e.g. ghc, uhc, yhc). Defaults to 'ghc'.
        </span>
    </td>
</tr>

<tr>
    <th>
        <label for="argument.compiler_path">Compiler path: </label>
    </th>
    <td>
        <props:textProperty name="argument.compiler_path" style="width:32em;"/>
        <span class="error" id="error_argument.compiler_path"></span>
        <span class="smallNote">
             Optional. Specify compiler path. Defaults to the compiler name, assuming it can be found within the PATH environment variable.
        </span>
    </td>
</tr>

<props:selectSectionProperty name="argument.build_log_verbosity" title="Verbosity level:">
    <props:selectSectionPropertyContent value="verbosity-0" caption="0"/>
    <props:selectSectionPropertyContent value="verbosity-1" caption="1"/>
    <props:selectSectionPropertyContent value="verbosity-2" caption="2"/>
    <props:selectSectionPropertyContent value="verbosity-3" caption="3" />
</props:selectSectionProperty>

<tr>
    <th>
        <label for="argument.run_tests">Run tests: </label>
    </th>
    <td>
         <props:checkboxProperty name="argument.run_tests"/>
         <span class="error" id="error_argument.run_tests"></span>
         <span class="smallNote">
             Run tests.
         </span>
    </td>
</tr>

<props:selectSectionProperty name="argument.test_reporting" title="Test reporting:">
    <props:selectSectionPropertyContent value="user" caption="user"/>
    <props:selectSectionPropertyContent value="test-framework" caption="test-framework"/>
</props:selectSectionProperty>

<tr>
    <th>
        <label for="argument.clean_before_build">Clear output before: </label>
    </th>
    <td>
         <props:checkboxProperty name="argument.clean_before_build"/>
         <span class="error" id="error_argument.clean_before_build"></span>
         <span class="smallNote">
             Clear output directory before running build.
         </span>
    </td>
</tr>

<tr>
    <th>
        <label for="argument.compiler_flags">Compiler flags: </label>
    </th>
    <td>
        <props:textProperty name="argument.compiler_flags" style="width:32em;"/>
        <span class="error" id="error_argument.compiler_flags"></span>
        <span class="smallNote">
             Give extra options to the compiler.
        </span>
    </td>
</tr>
