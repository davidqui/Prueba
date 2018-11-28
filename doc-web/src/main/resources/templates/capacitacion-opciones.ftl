<#include "header.ftl">
        <div class="col-md-4 col-lg-3">
          <div class="card">
            <div class="card-header">
              Temas de capacitacion
            </div>
            <div class="card-block">
              <ul class="nav nav-pills nav-stacked">
              	<#if temas?? >
                <#list temas as tema>

                <li class="nav-item" >
                  <a href="/capacitacion/juego/" style="white-space: initial; justific" class="nav-link"><b>${tema.tema?capitalize} &nbsp;&nbsp;<#if !tema.clasificacion??><em>&nbsp;&nbsp; SIN CLASIFICACION </em><#else><em>${tema.clasificacion}</em></b></#if></a>

                </li>

                </#list>
				</#if>
              </ul>
            </div>
          </div>
        </div>
        <div class="col-md-8 col-lg-9">