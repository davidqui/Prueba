<#include "util-macros.ftl" />
<#include "archivo-header.ftl">
        <div class="col-md-4 col-lg-3">
          <div class="card">
            <div class="card-header">
              Bandejas
            </div>
            <div class="card-block">
              <ul class="nav nav-pills nav-stacked">
                <li class="nav-item">
                  <a href="/archivo/solicitud/list-solicitud" class="nav-link <#if activePill == 'solicitud' >active</#if>">Solicitudes prestamo</a>
                </li>
                <li class="nav-item">
                  <a href="#" class="nav-link  <#if activePill == 'transito' >active</#if>">En transito</a>
                </li>
              </ul>
            </div>
          </div>
        </div>
        <div class="col-md-8 col-lg-9">
         <h1 class="cus-h1-page-title">${pageTitle}</h1>
          <@flash/>
          