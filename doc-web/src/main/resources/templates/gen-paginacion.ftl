<#--
    2017-10-19 edison.gonzalez@controltechcg.com Issue #132 (SICDI-Controltech) 
    feature-132: Macro para imprimir la barra de ayuda de cada tabla paginada.
-->

<#macro printBar url ori>
    <#local parametros = "">
    
    ${concatenar(ori)} dssdsj
    <center>
        <div class="row">
            <div class="col-sm-5">
                <div class="dataTables_info">${labelInformacion}</div>
            </div>
            <div class="col-sm-3">
                <ul class="dataTables_paginate">

                    <#if pageIndex gt 1>
                        <li class="page-item"><a href="${url}?pageIndex=1&pageSize=${pageSize}${parametros}" class="page-link"><<</a></li>
                        <li class="page-item"><a href="${url}?pageIndex=${pageIndex - 1}&pageSize=${pageSize}${parametros}" class="page-link"><</a></li>
                    <#else>
                        <li class="page-item disabled"><a class="page-link"><<</a></li>
                        <li class="page-item disabled"><a class="page-link"><</a></li>
                    </#if>

                    <li class="page-item"><a href="${url}?pageIndex=${pageIndex}&pageSize=${pageSize}${parametros}" class="page-link">${pageIndex}</a></li>

                    <#if pageIndex lt (totalPages)>
                        <li class="page-item"><a href="${url}?pageIndex=${pageIndex + 1}&pageSize=${pageSize}${parametros}" class="page-link">></a></li>
                        <li class="page-item"><a href="${url}?pageIndex=${totalPages}&pageSize=${pageSize}${parametros}" class="page-link">>></a></li>
                    <#else>
                        <li class="page-item disabled"><a class="page-link">></a></li>
                        <li class="page-item disabled"><a class="page-link">>></a></li>
                    </#if>
                </ul>        
            </div>
            <#if pageSizes??>
                
                <div class="col-sm-3">
                    <div class="form-inline" id="example_length">
                        <label>
                            Mostrar
                            <form method="POST">
                                <input class="form-control datepicker" id="fechaInicial" name="fechaInicial" value="${fechaInicialValor}" />
                            <select class="form-control input-sm" id="pageSize" name="pageSize">
                                <#list pageSizes as cla>
                                    <#if (cla = pageSize) >
                                        <option value="${cla}" selected="selected">${cla}</option>
                                    <#else>
                                        <option value="${cla}">${cla}</option>
                                    </#if>
                                </#list>
                            </select>
                            </form>
                            registros
                        </label>
                    </div>
                </div>
                
            </#if>
        </div>
    </center>
    <script type="text/javascript">
        $("#pageSize").change(function() {
            $(this).parents("form").submit();
        });
    </script>
</#macro>
    
<#function concatenar ori>
    <#local str = "">
    <#list ori?keys as key>
		${key}:${ori[key]}  <br/>
        str += ${key}:${ori[key]}
    </#list>
	<#return str>
</#function>
