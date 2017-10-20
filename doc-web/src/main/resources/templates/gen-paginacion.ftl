<#--
    2017-10-19 edison.gonzalez@controltechcg.com Issue #132 (SICDI-Controltech) 
    feature-132: Macro para imprimir la barra de ayuda de cada tabla paginada.
-->

<#macro printBar url params={}>
    
    <#assign parametros = "">
    <#if params?size gt 0>
        <#list params?keys as key>
            <#assign parametros = parametros+"&"+key+"="+params[key]>
        </#list>
    </#if>

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
                <form action="${url}">
                    <#list params?keys as key>
                        <input type="hidden" id="${key}" name="${key}" value="${params[key]}" />
                    </#list>
                    <div class="col-sm-3">
                        <div class="form-inline" id="example_length">
                            <label>
                                Mostrar
                                <select class="form-control input-sm" id="pageSize" name="pageSize">
                                    <#list pageSizes as cla>
                                        <#if (cla = pageSize) >
                                            <option value="${cla}" selected="selected">${cla}</option>
                                        <#else>
                                            <option value="${cla}">${cla}</option>
                                        </#if>
                                    </#list>
                                </select>
                                registros
                            </label>
                        </div>
                    </div>
                </form>
            </#if>
        </div>
    </center>
    <script type="text/javascript">
        $("#pageSize").change(function() {
            $(this).parents("form").submit();
        });
    </script>
</#macro>
