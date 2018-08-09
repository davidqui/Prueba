<#assign objectConstructor = "freemarker.template.utility.ObjectConstructor"?new()>
<#assign yyyymmdd = objectConstructor("java.text.SimpleDateFormat","yyyy-MM-dd")>

<#macro input propd>
    <#switch propd.widget>
        <#case "text">
            <input class="form-control" type="text" name="${propd.name}" id="${propd.name}" />
            <#break>
        <#case "checkbox">
            <input class="form-control" type="checkbox" name="${propd.name}" id="${propd.name}" />
            <#break>
        <#case "textarea">
            <textarea class="form-control" name="${propd.name}" id="${propd.name}"></textarea>
            <#break>
        <#case "calendar">
            <input class="form-control datepicker" type="text" name="${propd.name}" id="${propd.name}" />
            <#break>
        <#case "ofsfile">
            <input class="form-control" type="file" name="${propd.name}" id="${propd.name}" />
            <#break>
        <#case "editor">
            <textarea class="form-control" name="${propd.name}" id="${propd.name}"></textarea>
            <script type="text/javascript">
                CKEDITOR.replace( '${propd.name}' );
            </script>
       	<#case "upload">
       		<input type="file" class="form-control" name="archivo" id="archivo"/>
            <#break>
        <#case "select">
            <select class="form-control" name="${propd.name}" id="${propd.name}">
                <#assign vlist = lists.get(propd.widgetList) >
                <option value=""></option>
                <#list vlist as vitem>
                	<#assign vvalue = propd.widgetValueProp.value(vitem)?c />
                    <option value="${vvalue}">${vitem}</option>
                </#list>
            </select>
            <#break>
        <#case "localizacion">
        	<a href="#" onclick='showModalLoc();' class="btn btn-link btn-sm">Localizacion</a>
            <#break>
    </#switch>
</#macro>

<#macro inputedit propd obj>
    <#switch propd.widget>
        <#case "text">
            <input class="form-control" type="text" name="${propd.name}" id="${propd.name}" value="${propd.valueQuote(obj)!""}" escape="false" />
            <#break>
        <#case "checkbox">
            <input class="form-control" type="checkbox" name="${propd.name}" id="${propd.name}" <#if propd.value(obj)?? && propd.value(obj)>checked="checked"</#if> />
            <#break>
        <#case "textarea">
            <textarea class="form-control" name="${propd.name}" id="${propd.name}">${propd.value(obj)!""}</textarea>
            <#break>
        <#case "calendar">
            <#if propd.value(obj)?? >
                <input class="form-control datepicker" type="date" name="${propd.name}" id="${propd.name}" value="${yyyymmdd.format(propd.value(obj))}" />
            <#else>
                <input class="form-control datepicker" type="date" name="${propd.name}" id="${propd.name}" value="" />
            </#if>
            <#break>
        <#case "ofsfile">
            <input class="form-control" type="file" name="${propd.name}" id="${propd.name}" />
            <#break>
        <#case "editor">
            <textarea class="form-control" name="${propd.name}" id="${propd.name}">${propd.value(obj)!""}</textarea>
            <script type="text/javascript">
                CKEDITOR.replace( '${propd.name}' );
            </script>
            <#break>
        <#case "upload">
       		<input type="file" class="form-control" name="archivo" id="archivo"/>
            <#break>
        <#case "select">
            <select class="form-control" name="${propd.name}" id="${propd.name}">
                <#assign vlist = lists.get(propd.widgetList) >
                <#assign vvalue = (propd.id(obj)!"")?string >
                <option value=""></option>
                <#list vlist as vitem>
                    <#assign vovalue = propd.widgetValueProp.value(vitem)?c />
                    <#if vovalue == vvalue >
                        <option value="${vovalue}" selected="selected">${vitem}</option>
                    <#else>
                        <option value="${vovalue}">${vitem}</option>
                    </#if>
                </#list>
            </select>
            <#break>
        <#case "localizacion">
        	<a href="#" onclick='showModalLoc();' class="btn btn-link btn-sm">Localizacion</a>
            <#break>
    </#switch>
</#macro>

<#macro hidden propd obj>
    <input type="hidden" name="${propd.name}" id="${propd.name}" value="${propd.value(obj)}" />
</#macro>

<#macro hiddenids descriptor obj>
    <#list descriptor.idProperties() as p>
        <@hidden p entity />
    </#list>
</#macro>

<#macro actionpath action obj="NIL">
    <#assign epath = path + "/" + action + "?" />
    <#if queryString??>
        <#assign epath = epath + queryString />
    </#if>
    <#if queryString?? && obj?? && obj != "NIL" && descriptor.idProperties()?size != 0 >
        <#assign epath = epath + "&" />
    </#if>
    <#if obj?? && obj != "NIL">
        <#assign first = 1 />
        <#list descriptor.idProperties() as p>
            <#if first == 0><#assign epath = epath + "&" /></#if>
            <#assign epath = epath + p.name + "=" + p.value(obj) />
            <#assign first = 0 />
        </#list>
    </#if>
    ${epath}
</#macro>

<#macro createpath >
    <@actionpath "create" />
</#macro>

<#macro savepath >
    <@actionpath "save" />
</#macro>

<#macro listpath >
    <@actionpath "" />
</#macro>

<#macro editpath obj>
    <@actionpath "edit" obj />
</#macro>

<#macro deletepath obj>
    <@actionpath "delete" obj />
</#macro>

<#macro historypath obj>
    <@actionpath "history" obj />
</#macro>

<#macro accion accion obj >
    <#assign epath = accion.baseUrl + "?" />
    <#assign first = 1 />
    <#list accion.parameters as p>
        <#if first == 0><#assign epath = epath + "&" /></#if>
        <#assign epath = epath + p.name + "=" + p.prop.valueSelect(obj) />
        <#assign first = 0 />
    </#list>
    <!--#181 se agrega loader --> 
    <a href="${epath}" class="btn btn-link btn-sm" onclick="loading();">${accion.label}</a>
</#macro>

