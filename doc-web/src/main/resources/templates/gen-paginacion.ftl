<#--
    2017-10-19 edison.gonzalez@controltechcg.com Issue #132 (SICDI-Controltech) 
    feature-132: Macro para imprimir la barra de ayuda de cada tabla paginada.
-->

<#macro printBar url params={} metodo="">
    
    <#assign parametros = "">
    <#if params?size gt 0>
        <#list params?keys as key>
            <#assign parametros = parametros+"&"+key+"="+params[key]>
        </#list>
    </#if>

    <center>
        <form action="${url}" name="form" id="form" method="${metodo}">
            <#list params?keys as key>
                <input type="hidden" id="${key}" name="${key}" value="${params[key]}" />
            </#list>
            <input type="hidden" id="pageIndex" name="pageIndex" value="${pageIndex}" />
        <div class="row">
            <div class="col-sm-5">
                <div class="dataTables_info"><h5><span class = "label label-default">${labelInformacion}</span></h5></div>
            </div>
            <div class="col-sm-3">
                <ul class="dataTables_paginate">

                    <#if pageIndex gt 1>
                        <li class="page-item"><a href="javascript: myFunction(1);" class="page-link"><<</a></li>
                        <li class="page-item"><a href="javascript: myFunction(${pageIndex - 1});" class="page-link"><</a></li>
                    <#else>
                        <li class="page-item disabled"><a class="page-link"><<</a></li>
                        <li class="page-item disabled"><a class="page-link"><</a></li>
                    </#if>
                    
                    <#list 1..totalPages as i>
                        <#if ((pageIndex - 2 == i && pageIndex - 2 gt 0 ) || (pageIndex - 1 == i && pageIndex - 1 gt 0))>
                            <li class="page-item" ><a href="javascript: myFunction(${i});" class="page-link">${i}</a></li>
                        </#if>
                        <#if pageIndex == i>
                            <li class="page-item" ><a href="javascript: myFunction(${i});" class="page-link" style="background-color: gray; color:white; font-weight: bold;">${i}</a></li>
                        </#if>
                        <#if ((pageIndex + 2 == i && pageIndex + 2 lt totalPages+1 ) || (pageIndex + 1 == i && pageIndex + 1 lt totalPages+1))>
                            <li class="page-item" ><a href="javascript: myFunction(${i});" class="page-link">${i}</a></li>
                        </#if>
                        <#if pageIndex + 2 lt totalPages && i == totalPages>
                            <li class="page-item" ><a href="javascript: myFunction(${i});" class="page-link">${i}</a></li>
                        </#if>
                    </#list>

                    <#if pageIndex lt (totalPages)>
                        <li class="page-item"><a href="javascript: myFunction(${pageIndex + 1});" class="page-link">></a></li>
                        <li class="page-item"><a href="javascript: myFunction(${totalPages});" class="page-link">>></a></li>
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
                
            </#if>
        </div>
                        </form>
    </center>
    <script type="text/javascript">
        $("#pageSize").change(function() {
            $(this).parents("form").submit();
        });
            
        function myFunction(x) {
            $("#pageIndex").val(x);
            $('#form').submit();
        }
    </script>
</#macro>
