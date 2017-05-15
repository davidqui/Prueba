<#include "header.ftl">
        <div class="col-md-4 col-lg-3">
          <div class="card">
            <div class="card-header">
              Temas de capacitacion
            </div>
            <div class="card-block">
              <ul class="nav nav-pills nav-stacked">
              	<#if juegos?? >
                <#list juegos as j>
                <li class="nav-item">
                  <a href="/capacitacion-juego/juego?jid=${j.id}" class="nav-link <#if j.id == juego.id>active</#if>">${j.nombre}</a>
                </li>
                </#list>
				</#if>
              </ul>
            </div>
          </div>
        </div>
        <div class="col-md-8 col-lg-9">