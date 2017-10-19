<#--
    2017-10-19 edison.gonzalez@controltechcg.com Issue #132 (SICDI-Controltech) 
    feature-132: Macro para imprimir la barra de ayuda de cada tabla paginada.
-->
<#macro printBar url parametros="">
    <center>
        <div class="row">
            <div class="col-sm-5">
                <div class="dataTables_info">${labelInformacion}</div>
            </div>
            <div class="col-sm-3">
                <ul class="dataTables_paginate">

                    <#if pageIndex gt 1>
                        <li class="page-item"><a href="${url}?pageIndex=1${parametros}" class="page-link"><<</a></li>
                        <li class="page-item"><a href="${url}?pageIndex=${pageIndex - 1}${parametros}" class="page-link"><</a></li>
                    <#else>
                        <li class="page-item disabled"><a class="page-link"><<</a></li>
                        <li class="page-item disabled"><a class="page-link"><</a></li>
                    </#if>

                    <li class="page-item"><a href="${url}?pageIndex=${pageIndex}${parametros}" class="page-link">${pageIndex}</a></li>

                    <#if pageIndex lt (totalPages)>
                        <li class="page-item"><a href="${url}?pageIndex=${pageIndex + 1}${parametros}" class="page-link">></a></li>
                        <li class="page-item"><a href="${url}?pageIndex=${totalPages}${parametros}" class="page-link">>></a></li>
                    <#else>
                        <li class="page-item disabled"><a class="page-link">></a></li>
                        <li class="page-item disabled"><a class="page-link">>></a></li>
                    </#if>
                </ul>        
            </div>
        </div>
    </center>
</#macro>
